package cs3500.animator.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.JScrollPane;


import cs3500.animator.model.IShape;
import cs3500.animator.model.ImmutableModel;

/**
 * ExCellence Animation view that renders visually frame by frame.  The Animation appears and plays
 * in a JFrame window.
 */
public class VisualFrameView extends JFrame implements IView, ActionListener {
  private Canvas canvas;
  protected Timer timer;
  protected ImmutableModel model;
  protected int count;
  protected boolean loop;
  protected int speed;

  /**
   * Construct a visual frame view with the given observable model and the given rendering speed.
   *
   * @param model the observable model for the view
   * @param speed the rendering speed of the view
   */
  public VisualFrameView(ImmutableModel model, int speed) {
    super();
    //Setup:
    this.model = model;
    this.count = 0;
    this.speed = speed;
    this.timer = new Timer(100 / speed, this);

    //Creating Frame
    this.setTitle("Visual Animation");
    this.setSize(model.getWidth(), model.getHeight());
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //Creating Scrollable Visual
    this.canvas = new Canvas(model.getWidth(), model.getHeight());
    JScrollPane scrollBar = new JScrollPane(canvas, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    scrollBar.setPreferredSize(new Dimension(model.getWidth(), model.getHeight()));
    this.setContentPane(scrollBar);
    this.loop = true;

  }

  /**
   * Wait for action, specifically timer, to advance scene.
   *
   * @param e the observed action
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    nextScene();
  }

  /**
   * Update the shapes to be rendered in this view.
   *
   * @param shapes the list of shapes found in the frame
   */
  private void setShapes(List<IShape> shapes) {
    this.canvas.setShapes(shapes);
  }

  /**
   * Begin the rendering of this view.
   */
  @Override
  public void render() {
    this.timer.start();
    setVisible(true);
  }

  /**
   * Advance this view to then next scene.
   */
  protected void nextScene() {
    //Retrieve new shapes, advance count, refresh the view
    this.setShapes(this.model.getFrame(count));
    this.refresh();

    if (model.getEndTime() == count && this.loop) {
      this.count = 0;
    }
    else if (count < model.getEndTime()) {
      count++;
    }
  }

  protected void curScene() {
    this.setShapes(this.model.getFrame(count));
    this.refresh();
  }

  protected void prevScene() {
    if (count > 0) {
      this.count -= 1;
    }
    this.setShapes(this.model.getFrame(count));
    this.refresh();
  }

  /**
   * Refresh the display of the JFrame.
   */
  protected void refresh() {
    this.repaint();
  }

}
