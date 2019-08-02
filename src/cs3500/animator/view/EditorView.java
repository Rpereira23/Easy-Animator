package cs3500.animator.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import javax.swing.*;

import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import cs3500.animator.model.IKeyFrame;
import cs3500.animator.model.IShape;
import cs3500.animator.model.ImmutableModel;

import cs3500.animator.view.IEditableView;

/**
 * EditorView is a view that allows its user's to create and / or modify
 * animations. Provides an editor panel that is interactive and a visual frame
 * view for viewing the edited animation.
 */
public class EditorView extends VisualFrameView implements IEditableView {
  //Private Fields
  private final Map<String, JButton> toolbarMap;
  public JList shapeList;
  public JList keyFrames;
  public JList layers;
  private IShape focusedShape;
  private IKeyFrame focusedKeyFrame;
  private JLabel frameCount;
  private int focusedLayer;
  private JTextField nameField;
  private JTextField shapeTypeField;
  private JTextField layerField;
  private JTextField timeField;
  private JTextField widthField;
  private JTextField heightField;
  private JTextField xField;
  private JTextField yField;
  private JTextField redField;
  private JTextField blueField;
  private JTextField greenField;
  private JTextField angleField;
  private JButton addShape;
  private JButton deleteShape;
  private JButton addFrame;
  private JButton deleteFrame;
  private JButton alterFrame;
  private JButton addLayer;
  private JButton deleteLayer;
  private JTextField speedField;
  private JSlider frameSlider;
  private int maxSliderFrame;
  private final int MIN_SLIDER_Frame;

  /**
   * Construct an editable view with access to the given observable model.
   * @param model the observable model
   */
  public EditorView(ImmutableModel model) {
    super(model, 1);
    this.maxSliderFrame = model.getEndTime();
    this.MIN_SLIDER_Frame = 0;
    this.model = model;
    this.buildLayers();
    this.buildShapes();
    this.keyFrames = new JList();
    this.toolbarMap = new HashMap<>();
    this.init();
  }

  /**
   * Adds an action listener to all the JButtons in the toolbar,
   * the speed field, and the various editor buttons.
   * @param a : the action listener to be added.
   */
  @Override
  public void addListener(ActionListener a) {
    for (JButton b : this.toolbarMap.values()) {
      b.addActionListener(a);
    }

    speedField.addActionListener(a);
    addShape.addActionListener(a);
    deleteShape.addActionListener(a);
    addFrame.addActionListener(a);
    alterFrame.addActionListener(a);
    deleteFrame.addActionListener(a);
    addLayer.addActionListener(a);
    deleteLayer.addActionListener(a);
  }

  /**
   * Adds the ListSelectionListener to the JList of shapes.
   * @param s the ListSelectionListener
   */
  @Override
  public void addListSelectionListenerToShapes(ListSelectionListener s) {
    this.shapeList.addListSelectionListener(s);
  }

  /**
   * Add the ListSelecionListener to the JList of Layers.
   * @param s the ListSelectionListener.
   */
  @Override
  public void addListSelectionListenerToLayers(ListSelectionListener s) {
    this.layers.addListSelectionListener(s);
  }


  /**
   * Adds a change listener to the speed slider.
   * @param l the change listener.
   */
  @Override
  public void addChangeListenerToSpeedSlider(ChangeListener l) {
    this.frameSlider.addChangeListener(l);
  }
  /**
   * Adds the ListSelectionListener to the JList of KeyFrames.
   * @param s the ListSelectionListener
   */
  @Override
  public void addListSelectionListenerToKeyFrames(ListSelectionListener s) {
    this.keyFrames.addListSelectionListener(s);
  }

  /**
   * Pauses the animation being displayed.
   */
  @Override
  public void pause() {
    this.timer.stop();
  }

  /**
   * Resumes the paused animation being displayed.
   */
  @Override
  public void resume() {
    this.timer.start();
  }

  /**
   * Restarts the animation being displayed.
   */
  @Override
  public void restart() {
    this.count = 0;
    this.timer.restart();
  }

  /**
   * Toggles the looping for an animation.
   */
  @Override
  public void toggleLooping() {
    this.loop = !this.loop;
  }

  /**
   * Gets the next key frame.
   */
  @Override
  public void advanceFrame() {
    this.nextScene();
  }

  @Override
  public void nextScene() {
    super.nextScene();
    this.frameCount.setText(Integer.toString(this.count));
    this.frameSlider.setValue(this.count);
  }

  /**
   * Gets the currently focused shape.
   * @return the currently focused shape.
   */
  @Override
  public IShape getFocusedShape() {
    return this.focusedShape;
  }

  /**
   * Sets the focused shape to the provided one.
   * @param shape the new focused shape.
   */
  @Override
  public void setFocusedShape(IShape shape) {
    this.focusedShape = shape;
    this.focusedKeyFrame = null;
    this.updateShapeNames();
    this.buildKeyFrames();
  }

  /**
   * Gets the current shape index within the current JList.
   * @return the focused shape index.
   */
  @Override
  public int getFocusedShapeIndex() {
    return this.shapeList.getSelectedIndex();
  }

  /**
   * Gets the Focused frame index with the current JList.
   * @return the focused frame index.
   */
  @Override
  public int getFocusedFrameIndex() {
    return this.keyFrames.getSelectedIndex();
  }

  /**
   * Gets the currently focused key frame.
   * @return the currently key framed
   */
  @Override
  public IKeyFrame getFocusedKeyFrame() {
    return this.focusedKeyFrame;
  }

  /**
   * Sets the currently focused key frame to the provided one.
   * @param keyFrame the new key frame.
   */
  @Override
  public void setFocusedKeyFrame(IKeyFrame keyFrame) {
    this.focusedKeyFrame = keyFrame;
    if (keyFrame != null) {
      this.updateFieldBoxes();
    }
  }

  /**
   * Gets the current layer's index within the current JList
   * @return the focused
   */
  @Override
  public int getFocusedLayerIndex() {
    return this.layers.getSelectedIndex();
  }

  /**
   * Returns the current focused layer.
   * @return the currently focused layer.
   */
  @Override
  public int getFocusedLayer() {
    return this.focusedLayer;
  }

  /**
   * Sets the focused shaped the provided layer.
   * @param focusedLayer the new focused layer.
   */
  @Override
  public void setFocusedLayer(int focusedLayer) {
    this.focusedLayer = focusedLayer;
    this.focusedShape = null;
    this.focusedKeyFrame = null;
    this.updateLayers();
    this.updateShapeNames();
    this.buildLayers();
    this.buildShapes();
    this.buildKeyFrames();
  }

  /**
   * A list of strings providing the editable fields (time, width, height,
   * x, y, red, blue, and green).
   * @return a list of strings providing the editor panel information.
   */
  @Override
  public int[] getTextFields() {
    int[] fields = new int[9];

    try {
      fields[0] = (Integer.parseInt(timeField.getText()));
      fields[1] = (Integer.parseInt(this.widthField.getText()));
      fields[2] = (Integer.parseInt(this.heightField.getText()));
      fields[3] = (Integer.parseInt(this.xField.getText()));
      fields[4] = (Integer.parseInt(this.yField.getText()));
      fields[5] = (Integer.parseInt(this.redField.getText()));
      fields[6] = (Integer.parseInt(this.greenField.getText()));
      fields[7] = (Integer.parseInt(this.blueField.getText()));
      fields[8] = (Integer.parseInt(this.angleField.getText()));
    } catch (NumberFormatException e) {
      return null;
    }

    return fields;
  }

  /**
   * Loads the text file into a readable.
   * @return a readable form of the input file.
   * @throws IOException if the file cannot be read.
   */
  @Override
  public Readable loadFile() throws IOException {
    JFileChooser chooser = new JFileChooser();
    chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Files ending in .txt", "txt");
    chooser.setFileFilter(filter);
    int returnVal = chooser.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      Readable fileReader = new FileReader(chooser.getSelectedFile().getPath());
      return fileReader;
    }
    throw new IOException();
  }

  /**
   * Saves the file as either a text file or an svg file.
   * @throws IOException If the file cannot be written.
   */
  @Override
  public void saveFile() throws IOException {
    JFileChooser fileChooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "svg or txt", "svg", "txt");
    fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
    fileChooser.setMultiSelectionEnabled(true);
    fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(".svg Format", "svg"));
    fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(".txt Format", "txt"));
    if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
      File file = fileChooser.getSelectedFile();
      if (file != null) {
        FileFilter fileFilter = fileChooser.getFileFilter();
        if (fileFilter instanceof FileNameExtensionFilter && !fileFilter.accept(file)) {
          FileNameExtensionFilter fileNameExtensionFilter = (FileNameExtensionFilter) fileFilter;
          String extension = fileNameExtensionFilter.getExtensions()[0];
          String newName = file.getName() + "." + extension;
          file = new File(file.getParent(), newName);
          file.createNewFile();
        }
        else {
          file.createNewFile();
        }
        this.writeToFile(file);
      } else {
        throw new IOException();
      }
    }
  }

  /**
   * Updates the model based on the user's provided speed.
   */
  @Override
  public void updateSpeed() {
    try {
      Integer newSpeed = Integer.parseInt(speedField.getText().trim());
      if (newSpeed <= 0) {
        this.sendPopup(speedField.getText() + " is not a valid integer.");
      } else if (newSpeed != this.speed) {
        this.speed = newSpeed;
        Boolean playing = this.timer.isRunning();
        this.timer.stop();
        this.timer = new Timer(this.speed / 1000, this);
        if (playing) {
          this.timer.start();
        }
      }
    } catch (NumberFormatException nfe) {
      this.sendPopup(speedField.getText() + " is not a valid integer.");
    }
  }

  public void updateTime() {
    boolean playing = this.timer.isRunning();
    this.pause();
    this.count = this.frameSlider.getValue();
    this.frameCount.setText(Integer.toString(count));
    this.curScene();
    if (playing) {
      this.resume();
    }
  }

  /**
   * Rewinds to the previous frame.
   */
  @Override
  public void rewindFrame() {
    this.prevScene();
  }

  @Override
  public void prevScene() {
    super.prevScene();
    this.frameCount.setText(Integer.toString(this.count));
    this.frameSlider.setValue(this.count);
  }

  /**
   * Renders the Frame.
   */
  @Override
  public void render() {
    super.render();
    this.timer.stop();
  }

  /**
   * Gets the name of the current focused shape.
   * @return the name of the current focused shape.
   */
  @Override
  public String getTextName() {
    return this.nameField.getText();
  }

  /**
   * Provides the type of the current shape.
   * @return the type of the current shape.
   */
  @Override
  public String getTextCode() {
    return this.shapeTypeField.getText();
  }


  @Override
  public String getLayerField() { return this.layerField.getText(); }

  /**
   * Sets the current model to the provided immutable one.
   * @param model the new model.
   */
  @Override
  public void setModel(ImmutableModel model) {
    if (model == null) {
      throw new IllegalArgumentException("cannot set a null model");
    }
    this.model = model;
    this.maxSliderFrame = this.model.getEndTime();
    this.frameSlider.setMaximum(this.maxSliderFrame);
  }

  /**
   * Displays a popup indicating a message (typically error).
   * @param message the message to be displayed.
   */
  @Override
  public void sendPopup(String message) {
    JOptionPane.showMessageDialog(null, message);
  }

  /**
   * Builds a list of layers based on the current
   * selected animation file.
   */
  @Override
  public void buildLayers() {
    DefaultListModel<String> defMod = new DefaultListModel<>();
    Iterator<Integer> layerIterator = model.getLayers();
    while(layerIterator.hasNext()) {
      defMod.addElement(layerIterator.next().toString());
    }

    if(this.layers != null) {
      this.layers.setModel(defMod);
    } else {
      this.layers = new JList();
      this.layers.setModel(defMod);
    }
  }

  /**
   * Builds shapes based on the currently selected animation file.
   */
  public void buildShapes() {
    //Shape List
    DefaultListModel<String> defMod = new DefaultListModel<>();
    if(this.focusedLayer != 0) {
      Iterator<IShape> shapeIterator = model.getShapesAt(this.focusedLayer);
      while (shapeIterator.hasNext()) {
        defMod.addElement(shapeIterator.next().getName());
      }
    }

    if (this.shapeList != null) {
      this.shapeList.setModel(defMod);
    } else {
      this.shapeList = new JList();
      this.shapeList.setModel(defMod);
    }
  }

  /**
   * Builds the key frames based on the currently selected shape.
   */
  public void buildKeyFrames() {
    //KeyFrame List
    DefaultListModel<String> defMod = new DefaultListModel<>();
    if (this.focusedShape != null) {
      Iterator<IKeyFrame> keyFrameIterator = model.getAnimationsFrom(this.focusedShape);
      while (keyFrameIterator.hasNext()) {
        defMod.addElement(Integer.toString(keyFrameIterator.next().getTime()));
      }
    }
    this.keyFrames.setModel(defMod);
  }

  //Private Fields.
  /**
   * Initializes the editor panel, the visual panel, and the toolbar.
   */
  private void init() {
    //Creating Editor Panel.
    JPanel editorPanel = new JPanel();
    editorPanel.setMinimumSize(new Dimension(300, model.getHeight()));
    this.initEditorPanel(editorPanel);

    //Visual Panel
    Container visual = this.getContentPane();

    //Creating Split
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, editorPanel, visual);

    JPanel content = new JPanel();
    content.setLayout(new BorderLayout());
    content.setSize(
            model.getWidth() + editorPanel.getSize().width,
            model.getHeight());
    content.add(splitPane, BorderLayout.CENTER);

    //Creating Toolbar
    JToolBar toolBar = new JToolBar();
    this.initToolBar(toolBar);
    content.add(toolBar, BorderLayout.PAGE_START);

    //Finishing
    this.setContentPane(content);
    this.pack();
  }

  /**
   * Writes the model to the provided valid file (determined by its extension).
   * @param file the file to be written to.
   * @throws IOException if provided file cannot be written to.
   */
  private void writeToFile(File file) throws IOException {
    Objects.requireNonNull(file);
    String fileName = file.getName();
    int index = fileName.lastIndexOf('.');
    if (index == 0) {
      throw new IllegalArgumentException("no extension on file");
    }
    String extension = (fileName.substring(index + 1));
    FileWriter fileWriter = new FileWriter(file);
    switch (extension) {
      case "txt":
        IView txtView = new TextView(this.model, fileWriter);
        txtView.render();
        break;
      case "svg":
        IView svgView = new SVGView(this.model, fileWriter, this.speed);
        svgView.render();
        break;
      default:
        throw new IllegalArgumentException("invalid extension");
    }
    fileWriter.close();
  }

  /**
   * Updates the focused key frame based on the user's input.
   */
  private void updateFieldBoxes() {
    if (this.focusedKeyFrame != null) {
      this.timeField.setText(Integer.toString(this.focusedKeyFrame.getTime()));
      this.widthField.setText(Integer.toString(this.focusedKeyFrame.getWidth()));
      this.heightField.setText(Integer.toString(this.focusedKeyFrame.getHeight()));
      this.xField.setText(Integer.toString(this.focusedKeyFrame.getX()));
      this.yField.setText(Integer.toString(this.focusedKeyFrame.getY()));
      this.redField.setText(Integer.toString(this.focusedKeyFrame.getRed()));
      this.greenField.setText(Integer.toString(this.focusedKeyFrame.getGreen()));
      this.blueField.setText(Integer.toString(this.focusedKeyFrame.getBlue()));
      this.angleField.setText(Integer.toString(this.focusedKeyFrame.getAngle()));
      this.layerField.setText(Integer.toString(this.focusedLayer));
    } else {
      this.timeField.setText("");
      this.widthField.setText("");
      this.heightField.setText("");
      this.xField.setText("");
      this.yField.setText("");
      this.redField.setText("");
      this.greenField.setText("");
      this.blueField.setText("");
      this.angleField.setText("");
      this.layerField.setText("");
    }
  }

  /**
   * Updates the focused layer based on the user's input.
   */
  private void updateLayers() {
    if (this.focusedLayer != 0) {
      this.layerField.setText(Integer.toString(this.focusedLayer));
    } else {
      this.layerField.setText("");
    }
  }

  /**
   * Updates the name and code field based on the user's input.
   */
  private void updateShapeNames() {
    if (this.focusedShape != null) {
      this.nameField.setText(this.focusedShape.getName());
      this.shapeTypeField.setText(this.focusedShape.getCode());
    } else {
      this.nameField.setText("");
      this.shapeTypeField.setText("");
    }
  }

  /**
   * initializes the editor panel.
   * @param editor the editor panel to be built.
   */
  private void initEditorPanel(JPanel editor) {
    editor.setLayout(new GridLayout(2, 1));

    JScrollPane listScroller = new JScrollPane(this.shapeList);
    listScroller.setPreferredSize(new Dimension(125, 450));
    listScroller.setAlignmentX(LEFT_ALIGNMENT);

    //Shape Panel
    JPanel shapePanel = new JPanel();
    shapePanel.add(listScroller);

    JScrollPane keyFrameScroller = new JScrollPane(this.keyFrames);
    keyFrameScroller.setPreferredSize(new Dimension(125, 450));
    keyFrameScroller.setAlignmentX(LEFT_ALIGNMENT);

    //KeyFrame Panel
    JPanel keyPanel = new JPanel();
    keyPanel.add(keyFrameScroller);


    JScrollPane layerScroller = new JScrollPane(this.layers);
    layerScroller.setPreferredSize(new Dimension(125, 450));
    layerScroller.setAlignmentX(LEFT_ALIGNMENT);

    //Layering Panel
    JPanel layerPanel = new JPanel();
    layerPanel.add(layerScroller);

    //Splitting
    JSplitPane splitPaneFirst = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, shapePanel, keyPanel);
    splitPaneFirst.setDividerSize(3);

    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, layerPanel, splitPaneFirst);
    splitPane.setDividerSize(3);

    //Editable fields components
    this.nameField = new JTextField();
    this.shapeTypeField = new JTextField();
    this.layerField = new JTextField();
    this.timeField = new JTextField();
    this.widthField = new JTextField();
    this.heightField = new JTextField();
    this.xField = new JTextField();
    this.yField = new JTextField();
    this.redField = new JTextField();
    this.blueField = new JTextField();
    this.greenField = new JTextField();
    this.angleField = new JTextField();
    this.speedField = new JTextField();

    this.nameField.setPreferredSize(new Dimension(10, 10));
    this.shapeTypeField.setPreferredSize(new Dimension(10, 10));
    this.layerField.setPreferredSize(new Dimension(10,10));
    this.timeField.setPreferredSize(new Dimension(10, 10));
    this.widthField.setPreferredSize(new Dimension(10, 10));
    this.heightField.setPreferredSize(new Dimension(10, 10));
    this.xField.setPreferredSize(new Dimension(10, 10));
    this.yField.setPreferredSize(new Dimension(10, 10));
    this.redField.setPreferredSize(new Dimension(10, 10));
    this.blueField.setPreferredSize(new Dimension(10, 10));
    this.greenField.setPreferredSize(new Dimension(10, 10));
    this.angleField.setPreferredSize(new Dimension(10, 10));
    this.speedField.setPreferredSize(new Dimension(10, 10));

    //Setting Labels:
    JLabel nameLabel = new JLabel("Shape Name: ");
    nameLabel.setLabelFor(nameField);

    JLabel shapeTypeLabel = new JLabel("Shape Type: ");
    shapeTypeLabel.setLabelFor(shapeTypeField);

    JLabel layerLabel = new JLabel("Layer");
    layerLabel.setLabelFor(layerField);

    JLabel timeLabel = new JLabel("t");
    timeLabel.setLabelFor(timeField);

    JLabel widthLabel = new JLabel("width");
    widthLabel.setLabelFor(widthField);

    JLabel heightLabel = new JLabel("height");
    heightLabel.setLabelFor(heightField);

    JLabel xLabel = new JLabel("X");
    xLabel.setLabelFor(xField);

    JLabel yLabel = new JLabel("Y");
    yLabel.setLabelFor(yField);

    JLabel redLabel = new JLabel("red");
    redLabel.setLabelFor(redField);

    JLabel blueLabel = new JLabel("blue");
    blueLabel.setLabelFor(blueField);

    JLabel greenLabel = new JLabel("green");
    greenLabel.setLabelFor(greenField);

    JLabel angleLabel = new JLabel("angle");
    angleLabel.setLabelFor(angleField);

    //Setting Labels:
    JLabel speedLabel = new JLabel("Speed: ");
    speedLabel.setLabelFor(speedField);
    this.speedField.setActionCommand("speed");
    //Adding Speed's text.
    this.speedField.setText(Integer.toString(this.speed));


    //Editable field panel
    JPanel editingPanel = new JPanel();
    editingPanel.setLayout(new GridLayout(13, 8, 10, 10));

    editingPanel.add(nameLabel);
    editingPanel.add(nameField);

    editingPanel.add(shapeTypeLabel);
    editingPanel.add(shapeTypeField);

    editingPanel.add(layerLabel);
    editingPanel.add(layerField);

    editingPanel.add(timeLabel);
    editingPanel.add(timeField);

    editingPanel.add(widthLabel);
    editingPanel.add(widthField);

    editingPanel.add(heightLabel);
    editingPanel.add(heightField);

    editingPanel.add(xLabel);
    editingPanel.add(xField);

    editingPanel.add(yLabel);
    editingPanel.add(yField);

    editingPanel.add(redLabel);
    editingPanel.add(redField);

    editingPanel.add(greenLabel);
    editingPanel.add(greenField);

    editingPanel.add(blueLabel);
    editingPanel.add(blueField);

    editingPanel.add(angleLabel);
    editingPanel.add(angleField);

    editingPanel.add(speedLabel);
    editingPanel.add(speedField);

    JPanel commandPanel = new JPanel();
    commandPanel.setLayout(new GridLayout(7, 1, 6, 6));

    addShape = new JButton("Add Shape");
    deleteShape = new JButton("Delete Shape");
    addFrame = new JButton("Add Frame");
    alterFrame = new JButton("Alter Frame");
    deleteFrame = new JButton("Delete Frame");
    addLayer = new JButton("Add Layer");
    deleteLayer = new JButton("Delete Layer");

    commandPanel.add(addShape);
    commandPanel.add(deleteShape);
    commandPanel.add(addFrame);
    commandPanel.add(alterFrame);
    commandPanel.add(deleteFrame);
    commandPanel.add(addLayer);
    commandPanel.add(deleteLayer);

    JSplitPane splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, editingPanel, commandPanel);
    splitPane.setDividerSize(5);

    //Completed
    editor.add(splitPane);
    editor.add(splitPane2);
  }

  /**
   * initializes the toolbar.
   * @param toolBar the toolbar to be built.
   */
  private void initToolBar(JToolBar toolBar) {
    toolBar.setLayout(new GridLayout(1, 10));

    JButton resume = new JButton("Resume");
    this.toolbarMap.put("resume", resume);
    toolBar.add(resume);

    JButton pause = new JButton("pause");
    this.toolbarMap.put("pause", pause);
    toolBar.add(pause);

    JButton restart = new JButton("restart");
    this.toolbarMap.put("restart", restart);
    toolBar.add(restart);

    JButton previousFrame = new JButton("<<");
    this.toolbarMap.put("previousFrame", previousFrame);
    toolBar.add(previousFrame);

    JButton nextFrame = new JButton(">>");
    this.toolbarMap.put("nextFrame", nextFrame);
    toolBar.add(nextFrame);

    JButton loop = new JButton("Toggle Loop");
    this.toolbarMap.put("loop", loop);
    toolBar.add(loop);

    JButton save = new JButton("save");
    this.toolbarMap.put("save", save);
    toolBar.add(save);

    JButton load = new JButton("load");
    this.toolbarMap.put("load", load);
    toolBar.add(load);

    JLabel frameLabel = new JLabel("Frame: ");
    this.frameCount = new JLabel(Integer.toString(this.count));
    JSplitPane frameInfo = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, frameLabel, frameCount);

    this.frameSlider = new JSlider(JSlider.HORIZONTAL,
            this.MIN_SLIDER_Frame, this.maxSliderFrame, this.count);
    frameSlider.setPaintTicks(true);
    frameSlider.setPaintLabels(true);
    frameSlider.setMajorTickSpacing(50);
    frameSlider.setMinorTickSpacing(5);

    JSplitPane framePane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, frameInfo, frameSlider);
    toolBar.add(framePane);
  }
}
