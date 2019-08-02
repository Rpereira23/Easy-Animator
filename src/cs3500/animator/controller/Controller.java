package cs3500.animator.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cs3500.animator.model.ExcellenceModel;
import cs3500.animator.model.ExcellenceModelImpl;
import cs3500.animator.model.IKeyFrame;
import cs3500.animator.model.IShape;
import cs3500.animator.model.ViewModel;
import cs3500.animator.util.AnimationBuilder;
import cs3500.animator.util.AnimationReader;
import cs3500.animator.view.IEditableView;

/**
 * Controller for editor view.  Has direct access to the view and well acting as a mediator between
 * the two.  Handles input from view and translates that into the model.
 */
public class Controller implements ActionListener, IController {
  //A mapping of possible actions that a view could have
  protected final Map<String, Runnable> actions;
  //Direct access to a mutable model
  private ExcellenceModel model;
  //Direct access to edit view
  protected IEditableView view;

  /**
   * Constructor for controller requiring an editor view and a mutable model.
   *
   * @param view  the editor view that will receive user input
   * @param model the mutable model
   */
  public Controller(IEditableView view, ExcellenceModel model) {
    actions = new HashMap<>();
    this.addDefaultActions();
    this.model = model;
    this.view = view;
    this.view.addListener(this);
    this.view.addListSelectionListenerToShapes(new SelectionShapeListener());
    this.view.addListSelectionListenerToKeyFrames(new SelectionKeyFrameListener());
    this.view.addListSelectionListenerToLayers(new SelectionLayerListener());
    this.view.addChangeListenerToSpeedSlider(new ChangeFrameSliderListener());
  }

  /**
   * Initializer method to set up our map of possible operations promised by the view UI.
   */
  protected void addDefaultActions() {
    this.actions.put("Resume", new ResumeAction());
    this.actions.put("pause", new PauseAction());
    this.actions.put("restart", new RestartAction());
    this.actions.put("Toggle Loop", new LoopAction());
    this.actions.put("<<", new RewindAction());
    this.actions.put(">>", new FFAction());
    this.actions.put("Add Shape", new AddShape());
    this.actions.put("Delete Shape", new DeleteShape());
    this.actions.put("Add Frame", new AddFrame());
    this.actions.put("Alter Frame", new AlterFrame());
    this.actions.put("Delete Frame", new DeleteFrame());
    this.actions.put("Add Layer", new AddLayer());
    this.actions.put("Delete Layer", new DeleteLayer());
    this.actions.put("load", new LoadFile());
    this.actions.put("save", new SaveFile());
    this.actions.put("speed", new SpeedAction());
    this.actions.put("refresh", new Refresh());
  }

  /**
   * Overridden method for handling events from the view and appropriately executing a function
   * object from our mapping.
   *
   * @param e The event performed in the UI interface
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    this.actions.get(e.getActionCommand()).run();
  }

  /**
   * Function object for resuming playback in the view.
   */
  private class ResumeAction implements Runnable {
    @Override
    public void run() {
      view.resume();
    }
  }

  /**
   * Function object for loading file in the view.
   */
  private class LoadFile implements Runnable {
    @Override
    public void run() {
      ExcellenceModel backupModel = model;
      try {
        Readable readable = view.loadFile();
        Objects.requireNonNull(readable);
        AnimationBuilder<ExcellenceModel> builder = new ExcellenceModelImpl.Builder();
        AnimationReader<ExcellenceModel> reader = new AnimationReader();
        model = reader.parseFile(readable, builder);
        Objects.requireNonNull(model);
      } catch (IOException io) {
        model = backupModel;
        view.sendPopup("The file provided is not a valid SVG or Txt file.");
        //Display Error Message
      } catch (IllegalStateException ise) {
        model = backupModel;
        view.sendPopup("The file provided is not a valid SVG or Txt file.");
      } catch (NullPointerException npe) {
        model = backupModel;
        view.sendPopup("The file provided is not a valid SVG or Txt file.");
      } finally {
        actionPerformed(new ActionEvent(this, this.hashCode(), "refresh"));
      }
    }
  }

  /**
   * Function object for saving current animation as a text or svg file.
   */
  private class SaveFile implements Runnable {
    @Override
    public void run() {
      try {
        view.saveFile();
      } catch (IOException ioe) {
        view.sendPopup("unable to save animation");
      }
    }
  }

  /**
   * Function object for pausing the animation.
   */
  private class PauseAction implements Runnable {
    @Override
    public void run() {
      view.pause();
    }
  }

  /**
   * Function object for restarting the animation.
   */
  private class RestartAction implements Runnable {
    @Override
    public void run() {
      view.restart();
      actions.get("Resume").run();
    }
  }

  /**
   * Function object for enabling looping in the animation.
   */
  private class LoopAction implements Runnable {
    @Override
    public void run() {
      view.toggleLooping();
    }
  }

  /**
   * Function object for moving the animation one frame in the reverse direction.
   */
  private class RewindAction implements Runnable {
    @Override
    public void run() {
      view.rewindFrame();
    }
  }

  /**
   * Function object for moving the animation one frame forward.
   */
  private class FFAction implements Runnable {
    @Override
    public void run() {
      view.advanceFrame();
    }
  }

  /**
   * Function object for adding a shape to the model, given input from the user.  Then signals the
   * view to adjust its display of shapes.  Failed operation results in a popup sent through the
   * view.
   */
  private class AddShape implements Runnable {
    @Override
    public void run() {
      try {
        model = model.declareShape(
                view.getTextName(), view.getTextCode(), view.getFocusedLayer());
      } catch (IllegalArgumentException i) {
        view.sendPopup(i.getMessage());
        return;
      } catch (IllegalStateException s) {
        view.sendPopup(s.getMessage());
        return;
      }
      view.setModel(new ViewModel(model));
      view.buildShapes();
      return;
    }
  }

  /**
   * Function object for safely deleting shape from the animation and then signaling the view to
   * redisplay affected areas.  Failed operation results in a popup sent through the view.
   */
  private class DeleteShape implements Runnable {

    @Override
    public void run() {
      if (view.getFocusedShape() == null) {
        view.sendPopup("click a shape from the list to delete");
        return;
      }
      try {
        model.remove(view.getFocusedShape().getName());
      } catch (IllegalArgumentException ex) {
        view.sendPopup("click a shape from the list to delete");
      }
      view.setFocusedShape(null);
      view.setFocusedKeyFrame(null);
      view.buildShapes();
      view.buildKeyFrames();
      return;
    }
  }

  /**
   * Function object for adding frame to the animation.  Then updates the display of frames in the
   * view if successful.  Failed operations result in popups sent through the view.
   */
  private class AddFrame implements Runnable {

    @Override
    public void run() {
      IShape shapeFocus = view.getFocusedShape();
      if (shapeFocus == null) {
        view.sendPopup("must add a frame to an existing shape");
        return;
      }
      int[] fields = view.getTextFields();
      if (fields == null) {
        view.sendPopup("invalid field entry");
        return;
      }
      try {
        model = model.addKeyFrame(shapeFocus.getName(), fields[0], fields[3], fields[4], fields[1],
                fields[2], fields[5], fields[6], fields[7], fields[8]);
      } catch (IllegalArgumentException a) {
        view.sendPopup(a.getMessage());
        return;
      }
      view.setModel(new ViewModel(model));
      view.buildKeyFrames();
      return;
    }
  }

  /**
   * Function object for altering frame in the view from user input present in the view.  Then calls
   * to update frame display within the view. Failed operation results in popup message sent through
   * the view.
   */
  private class AlterFrame implements Runnable {

    @Override
    public void run() {
      IShape shapeFocus = view.getFocusedShape();
      if (shapeFocus == null) {
        view.sendPopup("can only alter a frame for an existing shape");
        return;
      }
      IKeyFrame frameFocus = view.getFocusedKeyFrame();
      if (frameFocus == null) {
        view.sendPopup("must alter an existing frame");
        return;
      }
      int[] fields = view.getTextFields();
      if (fields == null) {
        view.sendPopup("invalid field entry");
        return;
      }
      if (fields[0] != frameFocus.getTime()) {
        view.sendPopup("must alter an existing frame");
        return;
      }
      try {
        model.deleteKeyFrame(shapeFocus.getName(), frameFocus.getTime());
      } catch (IllegalArgumentException x) {
        view.sendPopup("must alter an existing frame");
        return;
      }
      model = model.addKeyFrame(shapeFocus.getName(), fields[0], fields[3], fields[4], fields[1],
              fields[2], fields[5], fields[6], fields[7], fields[8]);

      view.setModel(new ViewModel(model));
    }
  }


  /**
   * Function object for deleting frame within the view.  Then calls to adjust display of frames.
   * Failed operation results in a popup being sent through the view.
   */
  private class DeleteFrame implements Runnable {
    @Override
    public void run() {
      IShape shapeFocus = view.getFocusedShape();
      IKeyFrame frameFocus = view.getFocusedKeyFrame();
      if (shapeFocus == null) {
        view.sendPopup("must delete a frame for a valid shape");
        return;
      }
      if (frameFocus == null) {
        view.sendPopup("must delete a valid frame");
        return;
      }
      try {
        System.out.println("here");
        model = model.deleteKeyFrame(shapeFocus.getName(), frameFocus.getTime());
      } catch (IllegalArgumentException ix) {
        view.sendPopup("must delete a valid frame");
        return;
      }
      view.setFocusedKeyFrame(null);
      view.setModel(new ViewModel(model));
      view.buildKeyFrames();
      return;
    }
  }

  /**
   * Function object for adding a layer.
   */
  private class AddLayer implements Runnable {

    @Override
    public void run() {
      if (view.getLayerField().compareTo("") == 0) {
        view.sendPopup("must enter a layer to be created");
        return;
      }
      try {
        model = model.declareLayer(Integer.parseInt(view.getLayerField()));
      } catch (IllegalArgumentException i) {
        view.sendPopup(i.getMessage());
        return;
      } catch (IllegalStateException s) {
        view.sendPopup(s.getMessage());
        return;
      }
      view.setModel(new ViewModel(model));
      view.buildLayers();
      return;

    }
  }

  /**
   * Function Object for deleting a layer.
   */
  private class DeleteLayer implements Runnable {

    @Override
    public void run() {
      if (view.getFocusedLayer() == 0) {
        view.sendPopup("click a layer from the list to delete");
        return;
      }
      try {
        model.removeLayer(view.getFocusedLayer());
      } catch (IllegalArgumentException ex) {
        view.sendPopup("must delete an existing layer");
      }
      view.setFocusedLayer(0);
      view.setFocusedShape(null);
      view.setFocusedKeyFrame(null);
      view.buildLayers();
      view.buildShapes();
      view.buildKeyFrames();
      return;
    }
  }

  /**
   * Function object for updating speed within the view.
   */
  private class SpeedAction implements Runnable {
    @Override
    public void run() {
      view.updateSpeed();
    }
  }

  /**
   * Function object for refreshing the view with new
   * information from the model.
   */
  private class Refresh implements Runnable {

    @Override
    public void run() {
      System.out.println("From my Controller: loop refresh");
      view.setModel(model);
      view.buildShapes();
      actions.get("restart").run();
      view.render();
    }
  }

  /**
   * Function object for handling a selection of a shape in the view.  Upon user click of a shape,
   * this function updates display data to show shape features and updates frames to display for
   * this shape.
   */
  private class SelectionShapeListener implements ListSelectionListener {
    @Override
    public void valueChanged(ListSelectionEvent e) {
      if (view.getFocusedLayer() > 0) {
        Iterator<IShape> it = model.getShapesAt(view.getFocusedLayer());
        int count = 0;
        IShape currentShape;
        int index = view.getFocusedShapeIndex();
        while (it.hasNext() && count <= index) {
          currentShape = it.next();
          if (count == index) {
            view.setFocusedShape(currentShape);
          }
          count += 1;
        }
      }
    }
  }

  /**
   * Function object for handling a selection of a keyframe by the user.  Upon user click, this
   * function updates display data to show the features of the clicked keyframe.
   */
  private class SelectionKeyFrameListener implements ListSelectionListener {
    @Override
    public void valueChanged(ListSelectionEvent e) {
      if (view.getFocusedShape() != null) {
        Iterator<IKeyFrame> it = model.getAnimationsFrom(view.getFocusedShape());
        if (it == null) {
          return;
        }

        int count = 0;
        IKeyFrame currentKeyFrame;

        int index = view.getFocusedFrameIndex();

        while (it.hasNext() && count <= index) {
          currentKeyFrame = it.next();
          if (count == index) {
            view.setFocusedKeyFrame(currentKeyFrame);
            break;
          }
          count += 1;
        }
      }
    }
  }

  /**
   * A function object for listening the layer selection.
   */
  private class SelectionLayerListener implements ListSelectionListener {

    @Override
    public void valueChanged(ListSelectionEvent e) {
      int index = view.getFocusedLayerIndex();
      int count = 0;

      Iterator<Integer> layers = model.getLayers();

      Integer currentLayer;

      while (layers.hasNext() && count <= index) {
        currentLayer = layers.next();
        if (count == index) {
          view.setFocusedLayer(currentLayer);
        }
        count += 1;
      }
    }
  }

  /**
   * A function object for listening the change state of the speed
   * slider.
   */
  private class ChangeFrameSliderListener implements ChangeListener {
    @Override
    public void stateChanged(ChangeEvent e) {
      view.updateTime();
    }
  }
}
