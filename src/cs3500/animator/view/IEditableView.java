package cs3500.animator.view;

import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionListener;

import cs3500.animator.model.IKeyFrame;
import cs3500.animator.model.IShape;
import cs3500.animator.model.ImmutableModel;

/**
 * An Interface that promises the ability to
 * interact with an IView.
 */
public interface IEditableView extends IView {

  /**
   * Provides the ability to pause the animation view.
   */
  void pause();

  /**
   * Provides the ability to resume a paused animation view.
   */
  void resume();

  /**
   * Allows an action listener to be added to appropriate JButtons and
   * TextFields.
   */
  void addListener(ActionListener a);

  /**
   * Allows a ListSelectionListener to be added to appropriate Shape ListSelections.
   * @param s the ListSelectionListener
   */
  void addListSelectionListenerToShapes(ListSelectionListener s);

  /**
   * Allows a ListSelectionListener to be added to appropriate KeyFrame ListSelections.
   * @param s the ListSelectionListener
   */
  void addListSelectionListenerToKeyFrames(ListSelectionListener s);

  /**
   * Allows a listSelectionListener to be added to the appropriate layer list selections.
   * @param s the ListSelectionListener.
   */
  void addListSelectionListenerToLayers(ListSelectionListener s);


  /**
   * Allows a ChangeListener to listen to the speed slider.
   * @param l the change listener.
   */
  void addChangeListenerToSpeedSlider(ChangeListener l);

  /**
   * Restarts the animation view.
   */
  void restart();

  /**
   * Toggles with looping.
   */
  void toggleLooping();

  /**
   * Rewinds the key frame.
   */
  void rewindFrame();

  /**
   * advances the key frame.
   */
  void advanceFrame();

  /**
   * Opens directory dialogue to allow user to select a TextFile to load as an animation .
   * @return a readable of the file selected by the user
   * @throws IOException if unable to access the file or convert to a readable
   */
  Readable loadFile() throws IOException;

  /**
   * Allow the user to save the current animation as a text or svg file.
   * @throws IOException if unable to save
   */
  void saveFile() throws IOException;

  /**
   * Update the speed field display from user input.
   */
  void updateSpeed();

  /**
   * Return the current shape clicked by the user.  If no shape selected, return null.
   * @return the current shape clicked by the user
   */
  IShape getFocusedShape();

  /**
   * Set the focused shape of the view.  The view should display characteristics for this shape.
   * @param shape the shape to display characteristics for
   */
  void setFocusedShape(IShape shape);

  /**
   * Return the index of the clicked KeyFrame.
   * @return the index of the selected frame
   */
  int getFocusedFrameIndex();

  /**
   * Return the index of the clicked Shape.
   * @return the index of the selected IShape
   */
  int getFocusedShapeIndex();

  /**
   * Return the current KeyFrame clicked by the user. If no KeyFrame selected, return null.
   * @return the current KeyFrame selected
   */
  IKeyFrame getFocusedKeyFrame();

  /**
   * Set the focused KeyFrame to the given KeyFrame.  This KeyFrame's characteristics should be
   * displayed.
   * @param keyFrame the KeyFrame to focus on
   */
  void setFocusedKeyFrame(IKeyFrame keyFrame);

  /**
   * Returns the current focused layer index in the context of its JList
   * @return the index of the current focused layer
   */
  int getFocusedLayerIndex();


  /**
   * Returns the integer corresponding to the focused layer.
   * @return the current focused layer.
   */
  int getFocusedLayer();

  /**
   * Sets the current focused Index to the given one.
   * @param focusedLayer the new focused index.
   */
  void setFocusedLayer(int focusedLayer);

  /**
   * Return an array of the field values entered by the user.  They will be returned in the order
   * time, x, y, width, height, red, green, blue
   * @return the text box entries as an array
   */
  int[] getTextFields();

  /**
   * Update the display of KeyFrames.
   */
  void buildKeyFrames();

  /**
   * Updates the display of shapes.
   */
  void buildShapes();

  /**
   * Updates the display of layers.
   */
  void buildLayers();

  /**
   * Returns the content of the shape name text box.
   * @return the string content of the shape name text box.
   */
  String getTextName();

  /**
   * Return the content of the shape code text box.
   * @return the string content of the shape code text box representing a shape type
   */
  String getTextCode();

  /**
   * Gets the current value in the layers field.
   * @return the current value in the layers field.
   */
  String getLayerField();

  /**
   * Update the model that the view displays from.
   * @param model the new model being loaded to the view
   */
  void setModel(ImmutableModel model);

  /**
   * Send a popup message through the UI with the given descriptive error message.
   * @param message the message to display to the user
   */
  void sendPopup(String message);

  void updateTime();
}
