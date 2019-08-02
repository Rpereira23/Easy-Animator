package cs3500.animator.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionListener;

import cs3500.animator.model.ExcellenceModel;
import cs3500.animator.model.ExcellenceModelImpl;
import cs3500.animator.model.IKeyFrame;
import cs3500.animator.model.IShape;
import cs3500.animator.model.ImmutableModel;
import cs3500.animator.model.ViewModel;
import cs3500.animator.util.AnimationBuilder;
import cs3500.animator.util.AnimationReader;
import cs3500.animator.view.EditorView;
import cs3500.animator.view.IEditableView;

public class ControllerTest {
  ActionEvent resume;
  ActionEvent pause;
  ActionEvent restart;
  ActionEvent toggleLoop;
  ActionEvent rewindFrame;
  ActionEvent advanceFrame;
  ActionEvent addShape;
  ActionEvent deleteShape;
  ActionEvent addFrame;
  ActionEvent alterFrame;
  ActionEvent deleteFrame;
  ActionEvent save;
  ActionEvent load;
  ActionEvent speed;

  ExcellenceModel smallDemoModel;
  ExcellenceModel bigBangModel;
  ExcellenceModel buildingsModel;

  Map<ActionEvent, String> actionCommands;
  IEditableView editableView;
  Appendable appendable;

  //To Read Files
  protected  ExcellenceModel readFile(String filePath) throws IOException {
    Readable r = new FileReader(filePath);
    AnimationBuilder<ExcellenceModel> builder = new ExcellenceModelImpl.Builder();
    AnimationReader<ExcellenceModel> reader = new AnimationReader();
    return reader.parseFile(r, builder);
  }

  @Before
  public void setUp() {
    this.appendable = new StringBuilder();
    try {

      this.smallDemoModel = new MockEditableModel(this.readFile("smalldemo.txt"), this.appendable);
      this.bigBangModel = new MockEditableModel(this.readFile("big-bang-big-crunch.txt"),
              this.appendable);
      this.buildingsModel = new MockEditableModel(this.readFile("buildings.txt"),
              this.appendable);

    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  private void initWithModel(ViewModel viewModel) {

    this.editableView = new MockEditableView(this.appendable,viewModel);
    this.actionCommands = new HashMap<ActionEvent, String>();
    this.resume = new ActionEvent(editableView, 1, "Resume" );
    this.pause = new ActionEvent(editableView, 2, "pause" );
    this.restart = new ActionEvent(editableView, 3, "restart" );
    this.toggleLoop = new ActionEvent(editableView, 4, "Toggle Loop" );
    this.rewindFrame = new ActionEvent(editableView, 5, "<<" );
    this.advanceFrame = new ActionEvent(editableView, 6, ">>" );
    this.addShape = new ActionEvent(editableView, 7, "Add Shape" );
    this.deleteShape = new ActionEvent(editableView, 8, "Delete Shape" );
    this.addFrame = new ActionEvent(editableView, 9, "Add Frame" );
    this.alterFrame = new ActionEvent(editableView, 10, "Alter Frame" );
    this.deleteFrame = new ActionEvent(editableView, 11, "Delete Frame" );
    this.save = new ActionEvent(editableView, 12, "save" );
    this.load = new ActionEvent(editableView, 13, "load" );
    this.speed = new ActionEvent(editableView, 14, "speed" );

    this.actionCommands.put(resume, "Resume");
    this.actionCommands.put(pause, "Pause");
    this.actionCommands.put(restart, "Restart");
    this.actionCommands.put(toggleLoop, "ToggleLoop");
    this.actionCommands.put(advanceFrame, "AdvanceFrame");
    this.actionCommands.put(rewindFrame, "RewindFrame");
    this.actionCommands.put(addShape, "AddShape");
    this.actionCommands.put(deleteShape, "DeleteShape");
    this.actionCommands.put(addFrame, "AddKeyFrame");
    this.actionCommands.put(alterFrame, "AlterFrame");

    //Populating Fields.
    IShape focused = this.smallDemoModel.getShapes().next();
    this.editableView.setFocusedShape(focused);
    IKeyFrame focusedKeyFrame = this.smallDemoModel.getAnimationsFrom(focused).next();
    this.editableView.setFocusedKeyFrame(focusedKeyFrame);

  }

  @Test
  public void actionPause() {
    this.initWithModel(new ViewModel(this.smallDemoModel));
    Controller controller = new Controller(this.editableView, this.smallDemoModel);
    controller.actionPerformed(this.pause);
    Assert.assertTrue(appendable.toString().contains("Pause"));
  }

  @Test
  public void actionResume() {
    this.initWithModel(new ViewModel(this.smallDemoModel));
    Controller controller = new Controller(this.editableView, this.smallDemoModel);
    controller.actionPerformed(this.resume);
    Assert.assertTrue(appendable.toString().contains("Resume"));
  }

  @Test
  public void actionRestart() {
    this.initWithModel(new ViewModel(this.smallDemoModel));
    Controller controller = new Controller(this.editableView, this.smallDemoModel);
    controller.actionPerformed(this.restart);
    Assert.assertTrue(appendable.toString().contains("Restart"));
  }

  @Test
  public void actionAdvanceFrame() {
    this.initWithModel(new ViewModel(this.smallDemoModel));
    Controller controller = new Controller(this.editableView, this.smallDemoModel);
    controller.actionPerformed(this.advanceFrame);
    Assert.assertTrue(appendable.toString().contains("AdvanceFrame"));
  }

  @Test
  public void actionRewindFrame() {
    this.initWithModel(new ViewModel(this.smallDemoModel));
    Controller controller = new Controller(this.editableView, this.smallDemoModel);
    controller.actionPerformed(this.rewindFrame);
    Assert.assertTrue(appendable.toString().contains("RewindFrame"));
  }


  @Test
  public void actionToggleLoop() {
    this.initWithModel(new ViewModel(this.smallDemoModel));
    Controller controller = new Controller(this.editableView, this.smallDemoModel);
    controller.actionPerformed(this.toggleLoop);
    Assert.assertTrue(appendable.toString().contains("ToggleLoop"));
  }

  @Test
  public void actionLoad() {
    this.initWithModel(new ViewModel(this.smallDemoModel));
    Controller controller = new Controller(this.editableView, this.smallDemoModel);
    controller.actionPerformed(this.load);
    Assert.assertTrue(appendable.toString().contains("Load"));
  }

  @Test
  public void actionSave() {
    this.initWithModel(new ViewModel(this.smallDemoModel));
    Controller controller = new Controller(this.editableView, this.smallDemoModel);
    controller.actionPerformed(this.save);
    Assert.assertTrue(appendable.toString().contains("Save"));
  }

  @Test
  public void actionSpeed() {
    this.initWithModel(new ViewModel(this.smallDemoModel));
    Controller controller = new Controller(this.editableView, this.smallDemoModel);
    controller.actionPerformed(this.speed);
    Assert.assertTrue(appendable.toString().contains("Speed"));
  }

  //MODEL SIDE:
  @Test
  public void actionAddFrame() {
    this.initWithModel(new ViewModel(this.smallDemoModel));
    Controller controller = new Controller(this.editableView, this.smallDemoModel);
    controller.actionPerformed(this.addFrame);
    Assert.assertTrue(appendable.toString().contains("AddKeyFrame"));
  }

  @Test
  public void actionAddShape() {
    this.initWithModel(new ViewModel(this.smallDemoModel));
    Controller controller = new Controller(this.editableView, this.smallDemoModel);
    controller.actionPerformed(this.addShape);
    Assert.assertTrue(appendable.toString().contains("DeclareShape"));
  }

  @Test
  public void actionAlterFrame() {
    this.initWithModel(new ViewModel(this.smallDemoModel));
    Controller controller = new Controller(this.editableView, this.smallDemoModel);
    controller.actionPerformed(this.alterFrame);
    Assert.assertTrue(appendable.toString().contains("DeleteKeyFrame"));
    Assert.assertTrue(appendable.toString().contains("AddKeyFrame"));
  }

  @Test
  public void actionDeleteFrame() {
    this.initWithModel(new ViewModel(this.smallDemoModel));
    Controller controller = new Controller(this.editableView, this.smallDemoModel);
    controller.actionPerformed(this.deleteFrame);
    Assert.assertTrue(appendable.toString().contains("DeleteKeyFrame"));
  }

  @Test
  public void actionDeleteShape() {
    this.initWithModel(new ViewModel(this.smallDemoModel));
    Controller controller = new Controller(this.editableView, this.smallDemoModel);
    controller.actionPerformed(this.deleteShape);
    Assert.assertTrue(appendable.toString().contains("Remove"));
  }

  //Null Testing
  @Test
  public void nullFocusedShape() {
    //Delete Shape
    this.initWithModel(new ViewModel(this.smallDemoModel));
    this.editableView.setFocusedShape(null);
    Controller controller = new Controller(this.editableView, this.smallDemoModel);
    controller.actionPerformed(this.deleteShape);
    Assert.assertTrue(appendable.toString().contains("SendPopup"));
    Assert.assertFalse(appendable.toString().contains("Remove"));

    //AlterFrame
    this.initWithModel(new ViewModel(this.smallDemoModel));
    this.editableView.setFocusedShape(null);
    controller = new Controller(this.editableView, this.smallDemoModel);
    controller.actionPerformed(this.alterFrame);
    Assert.assertTrue(appendable.toString().contains("SendPopup"));
    Assert.assertFalse(appendable.toString().contains("DeleteKeyFrame"));
    Assert.assertFalse(appendable.toString().contains("AddKeyFrame"));

    //AddFrame
    this.initWithModel(new ViewModel(this.smallDemoModel));
    this.editableView.setFocusedShape(null);
    controller = new Controller(this.editableView, this.smallDemoModel);
    controller.actionPerformed(this.addFrame);
    Assert.assertTrue(appendable.toString().contains("SendPopup"));
    Assert.assertFalse(appendable.toString().contains("AddKeyFrame"));

    //DeleteFrame
    this.initWithModel(new ViewModel(this.smallDemoModel));
    this.editableView.setFocusedShape(null);
    controller = new Controller(this.editableView, this.smallDemoModel);
    controller.actionPerformed(this.deleteFrame);
    Assert.assertTrue(appendable.toString().contains("SendPopup"));
    Assert.assertFalse(appendable.toString().contains("DeleteKeyFrame"));
  }

  @Test
  public void nullFocusedFields() {
    //AlterFrame
    this.initWithModel(new ViewModel(this.smallDemoModel));
    this.editableView.setFocusedKeyFrame(null);
    Controller controller = new Controller(this.editableView, this.smallDemoModel);
    controller.actionPerformed(this.alterFrame);
    Assert.assertTrue(appendable.toString().contains("SendPopup"));
    Assert.assertFalse(appendable.toString().contains("DeleteKeyFrame"));
    Assert.assertFalse(appendable.toString().contains("AddKeyFrame"));

    //DeleteFrame
    this.initWithModel(new ViewModel(this.smallDemoModel));
    this.editableView.setFocusedKeyFrame(null);
    controller = new Controller(this.editableView, this.smallDemoModel);
    controller.actionPerformed(this.deleteFrame);
    Assert.assertTrue(appendable.toString().contains("SendPopup"));
    Assert.assertFalse(appendable.toString().contains("DeleteKeyFrame"));
  }

  private class MockEditableModel implements ExcellenceModel {
    ExcellenceModel model;
    Appendable appendable;

    public MockEditableModel(ExcellenceModel model, Appendable appendable) {
      this.model = model;
      this.appendable = appendable;

    }

    @Override
    public ExcellenceModel declareShape(String name, String type) {
      try {
        this.appendable.append("_DeclareShape_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }

      return model.declareShape(name, type);
    }

    @Override
    public ExcellenceModel declareShape(String name, String type, Integer layer) {
      try {
        this.appendable.append("_DeclareShape_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }

      return model.declareShape(name, type, layer);
    }

    @Override
    public ExcellenceModel declareLayer(Integer layer) {
      try {
        this.appendable.append("_DeclareLayer_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }

      return model.declareLayer(layer);
    }

    @Override
    public void remove(String shapeName) {
      try {
        this.appendable.append("_Remove_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }

      this.model.remove(shapeName);

    }

    @Override
    public void removeLayer(Integer layer) {
      try {
        this.appendable.append("_RemoveLayer_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }

      this.model.removeLayer(layer);


    }

    @Override
    public ExcellenceModel addMotion(String name, int t1, int x1, int y1, int w1,
                                     int h1, int r1, int g1, int b1,
                                     int t2, int x2, int y2, int w2,
                                     int h2, int r2, int g2, int b2) {
      try {
        this.appendable.append("_AddMotion_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }

      return this.model.addMotion(name, t1, x1, y1, w1, h1, r1, g1, b1,
              t2, x2, y2, w2, h2, r2, g2, b2);
    }

    @Override
    public ExcellenceModel deleteKeyFrame(String name, int t) {
      try {
        this.appendable.append("_DeleteKeyFrame_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }

      return this.model.deleteKeyFrame(name, t);
    }

    @Override
    public ExcellenceModel addKeyFrame(String name, int time, int x, int y, int w,
                                       int h, int r, int g, int b, int a) {
      try {
        this.appendable.append("_AddKeyFrame_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      return this.model.addKeyFrame(name,time,x, y, w, h, r, g, b, a);
    }

    @Override
    public List<IShape> getFrame(int time) {
      try {
        this.appendable.append("_GetFrame_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      return this.model.getFrame(time);
    }

    @Override
    public int getWidth() {
      try {
        this.appendable.append("_GetWidth_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      return this.model.getWidth();
    }

    @Override
    public int getHeight() {
      try {
        this.appendable.append("_GetHeight_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      return this.model.getHeight();
    }

    @Override
    public int getX() {
      try {
        this.appendable.append("_GetX_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      return this.model.getX();
    }

    @Override
    public int getY() {
      try {
        this.appendable.append("_GetY_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      return this.model.getY();
    }

    @Override
    public Iterator<IShape> getShapes() {
      try {
        this.appendable.append("_GetShapes_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      return this.model.getShapes();
    }

    @Override
    public Iterator<IKeyFrame> getAnimationsFrom(IShape shape) {
      try {
        this.appendable.append("_GetAnimationsFrom_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      return this.model.getAnimationsFrom(shape);
    }

    @Override
    public Iterator<Integer> getLayers() {
      try {
        this.appendable.append("_GetLayers_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }

      return this.model.getLayers();
    }

    @Override
    public Iterator<IShape> getShapesAt(Integer layer) {
      try {
        this.appendable.append("_GetShapesAt_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }

      return this.model.getShapesAt(layer);
    }

    @Override
    public int getEndTime() {
      try {
        this.appendable.append("_GetEndTime_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      return this.model.getEndTime();
    }
  }

  private class MockEditableView implements IEditableView {
    Appendable appendable;
    IEditableView editableView;

    public MockEditableView(Appendable appendable, ViewModel model) {
      this.appendable = appendable;
      this.editableView = new EditorView(model);
    }


    @Override
    public void pause() {
      try {
        this.appendable.append("_Pause_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }

    @Override
    public void resume() {
      try {
        this.appendable.append("_Resume_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }

    @Override
    public void addListener(ActionListener a) {
      try {
        this.appendable.append("_ActionListener_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }

    @Override
    public void addListSelectionListenerToShapes(ListSelectionListener s) {
      try {
        this.appendable.append("_ListSelectionListenerToShapes_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }

    @Override
    public void addListSelectionListenerToKeyFrames(ListSelectionListener s) {
      try {
        this.appendable.append("_ListSelectionListenerToKeyFrames_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }

    @Override
    public void addListSelectionListenerToLayers(ListSelectionListener s) {
      try {
        this.appendable.append("_ListSelectionListenerToLayers_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }

    }

    @Override
    public void addChangeListenerToSpeedSlider(ChangeListener l) {
      try {
        this.appendable.append("_ChangeListenerToSpeedSlider_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }

    @Override
    public void restart() {
      try {
        this.appendable.append("_Restart_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      this.editableView.restart();
    }

    @Override
    public void toggleLooping() {
      try {
        this.appendable.append("_ToggleLooping_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }

    @Override
    public void rewindFrame() {
      try {
        this.appendable.append("_RewindFrame_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }

    }

    @Override
    public void advanceFrame() {
      try {
        this.appendable.append("_AdvanceFrame_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }

    @Override
    public Readable loadFile() throws IOException {
      try {
        this.appendable.append("_LoadFile_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      return this.editableView.loadFile();
    }

    @Override
    public void saveFile() throws IOException {
      try {
        this.appendable.append("_SaveFile_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }

    @Override
    public void updateSpeed() {
      try {
        this.appendable.append("_UpdateSpeed_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }

    @Override
    public void updateTime() {
      //TODO
    }

    @Override
    public IShape getFocusedShape() {
      try {
        this.appendable.append("_GetFocusedShape_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      return this.editableView.getFocusedShape();
    }

    @Override
    public void setFocusedShape(IShape shape) {
      try {
        this.appendable.append("_SetFocusedShape_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      this.editableView.setFocusedShape(shape);

    }

    @Override
    public int getFocusedFrameIndex() {
      try {
        this.appendable.append("_GetFocusedFrameIndex_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      return this.editableView.getFocusedFrameIndex();
    }

    @Override
    public int getFocusedShapeIndex() {
      try {
        this.appendable.append("_GetFocusedShapeIndex_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      return this.editableView.getFocusedShapeIndex();
    }

    @Override
    public IKeyFrame getFocusedKeyFrame() {
      try {
        this.appendable.append("_GetFocusedKeyFrame_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      return this.editableView.getFocusedKeyFrame();
    }

    @Override
    public void setFocusedKeyFrame(IKeyFrame keyFrame) {
      try {
        this.appendable.append("_SetFocusedKeyFrame_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      this.editableView.setFocusedKeyFrame(keyFrame);
    }

    @Override
    public int getFocusedLayerIndex() {
      try {
        this.appendable.append("_GetFocusedLayerIndex_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      return this.editableView.getFocusedLayerIndex();
    }

    @Override
    public int getFocusedLayer() {
      try {
        this.appendable.append("_GetFocusedLayer_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      return this.editableView.getFocusedLayer();
    }

    @Override
    public void setFocusedLayer(int focusedLayer) {
      try {
        this.appendable.append("_SetFocusedLayer_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      this.editableView.setFocusedLayer(focusedLayer);

    }

    @Override
    public int[] getTextFields() {
      try {
        this.appendable.append("_GetTextFields_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      return this.editableView.getTextFields();
    }

    @Override
    public void buildKeyFrames() {
      try {
        this.appendable.append("_BuildKeyFrames_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      this.editableView.buildKeyFrames();
    }

    @Override
    public void buildShapes() {
      try {
        this.appendable.append("_BuildShapes_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }

      this.editableView.buildShapes();

    }

    @Override
    public void buildLayers() {
      try {
        this.appendable.append("_BuildLayers_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }

      this.editableView.buildLayers();

    }

    @Override
    public String getTextName() {
      try {
        this.appendable.append("_GetTextName_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      return this.editableView.getTextName();
    }

    @Override
    public String getTextCode() {
      try {
        this.appendable.append("_GetTextCode_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      return this.editableView.getTextCode();
    }

    @Override
    public String getLayerField() {
      try {
        this.appendable.append("_GetLayerField_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      return this.editableView.getLayerField();
    }

    @Override
    public void setModel(ImmutableModel model) {
      try {
        this.appendable.append("_SetModel_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      this.editableView.setModel(model);
    }

    @Override
    public void sendPopup(String message) {
      try {
        this.appendable.append("_SendPopup_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }

    @Override
    public void render() {
      try {
        this.appendable.append("_Render_");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }

    }
  }
}