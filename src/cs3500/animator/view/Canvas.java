package cs3500.animator.view;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import cs3500.animator.model.IShape;

/**
 * Represents the panel for which
 * the shapes are to be drawn.
 */
public class Canvas extends JPanel {
  private List<IShape> shapes;

  /**
   * The constructor for Canvas. Builds a canvas
   * based off the given width and height.
   * @param width the width of the canvas.
   * @param height the height of the canvas.
   */
  public Canvas(int width, int height) {
    super();
    this.setPreferredSize(new Dimension(width, height));
    this.setBackground(Color.WHITE);
    this.shapes = new LinkedList<IShape>();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D)g;

    for (IShape shape : shapes) {
      AffineTransform old = g2d.getTransform();
      switch (shape.getCode()) {
        case "rectangle":
          g2d.rotate(Math.toRadians(shape.getAngle()),
                  shape.getX() + shape.getWidth()/ 2,
                  shape.getY() + shape.getHeight()/2);
          g2d.setColor(new Color(shape.getRed() % 256,
                  shape.getGreen() % 256,
                  shape.getBlue() % 256));
          g2d.fillRect(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
          g2d.setTransform(old);
          break;
        case "ellipse":
          g2d.rotate(Math.toRadians(shape.getAngle()),
                  shape.getX() + shape.getWidth()/2,
                  shape.getY() + shape.getHeight()/2);
          g2d.setColor(new Color(shape.getRed(), shape.getGreen(), shape.getBlue()));
          g2d.fillOval(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
          g2d.setTransform(old);
          break;
        default:
          throw new IllegalStateException("undrawable shape");
      }
    }
  }

  /**
   * Sets the shapes of this canvas to the given List.  These Shapes are to be painted on this.
   * @param shapes the list of shapes to paint.
   */
  public void setShapes(List<IShape> shapes) {
    this.shapes = shapes;
  }
}
