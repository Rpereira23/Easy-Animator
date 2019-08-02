package cs3500.animator.view;

import java.io.IOException;
import java.util.Iterator;

import cs3500.animator.model.IShape;
import cs3500.animator.model.ImmutableModel;
import cs3500.animator.model.IKeyFrame;

/**
 * ExCellence View that renders as SVG.  The SCG text is output as a file that may be open and
 * displayed through a web browser.
 */
public class SVGView implements IView {
  private ImmutableModel model;
  private Appendable out;
  private int speed;

  /**
   * Construct the SVG view with an observable model, a designated output destination and a speed.
   *
   * @param model the observable model
   * @param out   an appendable destination source for the SVG text
   * @param speed the speed for the rendering
   */
  public SVGView(ImmutableModel model, Appendable out, int speed) {
    this.model = model;
    this.out = out;
    this.speed = speed;
  }

  /**
   * Render the model as an SVG.  Add shapes as elements and animate the attributes of those
   * shapes.
   */
  @Override
  public void render() {
    StringBuilder sb = new StringBuilder();

    Iterator<IShape> itShape = this.model.getShapes();

    IShape curShape;
    sb.append(String.format("<svg width=\"%s\" height=\"%s\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">\n",
            model.getWidth(), model.getHeight()));

    while (itShape.hasNext()) {
      curShape = itShape.next();
      switch (curShape.getCode()) {
        case "rectangle":
          this.svgRect(curShape, sb);
          break;
        case "ellipse":
          this.svgEllipse(curShape, sb);
          break;
      }
    }
    sb.append("</svg>");

    try {
      this.out.append(sb.toString());
    } catch (IOException e) {
      System.out.println("unable to append");
    }
  }

  private void svgRect(IShape shape, StringBuilder sb) {
    sb.append(String.format("<%s>\n", "rect"));
    Iterator<IKeyFrame> itKF = model.getAnimationsFrom(shape);
    IKeyFrame cur;
    IKeyFrame prev = null;
    while (itKF.hasNext()) {
      cur = itKF.next();
      if (prev != null) {
        this.displayRectAnimation(sb, prev, cur);
      }
      prev = cur;
    }
    sb.append(String.format("</%s>\n", "rect"));
  }

  private void svgEllipse(IShape shape, StringBuilder sb) {
    sb.append(String.format("<%s>\n", "ellipse"));
    Iterator<IKeyFrame> itKF = model.getAnimationsFrom(shape);
    IKeyFrame cur;
    IKeyFrame prev = null;
    while (itKF.hasNext()) {
      cur = itKF.next();
      if (prev != null) {
        this.displayEllipseAnimation(sb, prev, cur);
      }
      prev = cur;
    }
    sb.append(String.format("</%s>\n", "ellipse"));
  }

  /**
   * Construct SVG visitor with a StringBuilder to append to.
   *
   * @param sb the String Builder to append to
   *
   *           /** Helper to properly append an SVG animation for a rectangle.
   */
  private void displayRectAnimation(StringBuilder sb, IKeyFrame f1, IKeyFrame f2) {
    sb.append(String.format("<animate attributeType=\"xml\" begin=\"%d00ms\" dur=\"%d00ms\""
                    + " attributeName=\"%s\" from=\"%d\" to=\"%d\" fill=\"freeze\" />\n",
            f1.getTime(),
            (f2.getTime() - f1.getTime()) / speed, "x", f1.getX(), f2.getX()));
    sb.append(String.format("<animate attributeType=\"xml\" begin=\"%d00ms\" dur=\"%d00ms\""
                    + " attributeName=\"%s\" from=\"%d\" to=\"%d\" fill=\"freeze\" />\n",
            f1.getTime(),
            (f2.getTime() - f1.getTime()) / speed, "y", f1.getY(), f2.getY()));
    sb.append(String.format("<animate attributeType=\"xml\" begin=\"%d00ms\" dur=\"%d00ms\""
                    + " attributeName=\"%s\" from=\"%d\" to=\"%d\" fill=\"freeze\" />\n",
            f1.getTime(),
            (f2.getTime() - f1.getTime()) / speed, "width",
            f1.getWidth(), f2.getWidth()));
    sb.append(String.format("<animate attributeType=\"xml\" begin=\"%d00ms\" dur=\"%d00ms\""
                    + " attributeName=\"%s\" from=\"%d\" to=\"%d\" fill=\"freeze\" />\n",
            f1.getTime(),
            (f2.getTime() - f1.getTime()) / speed, "height",
            f1.getHeight(), f2.getHeight()));
    sb.append(String.format("<animate attributeType=\"xml\" begin=\"%d00ms\" dur=\"%d00ms\""
                    + " attributeName=\"%s\" from=\"rgb(%d,%d,%d)\" "
                    + "to=\"rgb(%d,%d,%d)\" fill=\"freeze\" />\n",
            f1.getTime(), (f2.getTime() - f1.getTime()) / speed, "fill",
            f1.getRed(), f1.getGreen(), f1.getBlue(), f2.getRed(), f2.getGreen(),
            f2.getBlue()));
    sb.append(String.format("<animateTransform attributeName=\"transform\" attributeType =\"XML\"" +
                    " type=\"rotate\" from=\"%d %d %d\" to=\"%d %d %d\" begin=\"%d00ms\" dur=\"%d00ms\" repeatCount=\"0\"" +
                    " fill=\"freeze\"/>", f1.getAngle(), f1.getX(), f1.getY(), f2.getAngle(),
            f2.getX(), f2.getY(), f1.getTime(), f2.getTime() - f1.getTime()));
  }

  /**
   * Helper to properly render the SVG animation as acting on an Oval.
   */
  private void displayEllipseAnimation(StringBuilder sb, IKeyFrame f1, IKeyFrame f2) {
    sb.append(String.format("<animate attributeType=\"xml\" begin=\"%d00ms\" dur=\"%d00ms\""
                    + " attributeName=\"%s\" from=\"%d\" to=\"%d\" fill=\"freeze\" />\n",
            f1.getTime(), (f2.getTime() - f1.getTime()) / speed, "cx",
            f1.getX(), f2.getX()));
    sb.append(String.format("<animate attributeType=\"xml\" begin=\"%d00ms\" dur=\"%d00ms\""
                    + " attributeName=\"%s\" from=\"%d\" to=\"%d\" fill=\"freeze\" />\n",
            f1.getTime(),
            (f2.getTime() - f1.getTime()) / speed, "cy", f1.getY(), f2.getY()));
    sb.append(String.format("<animate attributeType=\"xml\" begin=\"%d00ms\" dur=\"%d00ms\""
                    + " attributeName=\"%s\" from=\"%d\" to=\"%d\" fill=\"freeze\" />\n",
            f1.getTime(),
            (f2.getTime() - f1.getTime()) / speed, "rx",
            f1.getWidth() / 2, f2.getWidth() / 2));
    sb.append(String.format("<animate attributeType=\"xml\" begin=\"%d00ms\" dur=\"%d00ms\""
                    + " attributeName=\"%s\" from=\"%d\" to=\"%d\" fill=\"freeze\" />\n",
            f1.getTime(), ((f2.getTime() - f1.getTime()) / speed), "ry",
            f1.getHeight() / 2, f2.getHeight() / 2));
    sb.append(String.format("<animate attributeType=\"xml\" begin=\"%d00ms\" dur=\"%d00ms\""
                    + " attributeName=\"%s\" from=\"rgb(%d,%d,%d)\" "
                    + "to=\"rgb(%d,%d,%d)\" fill=\"freeze\" />\n",
            f1.getTime(), (f2.getTime() - f1.getTime()) / speed, "fill",
            f1.getRed(), f1.getGreen(), f1.getBlue(), f2.getRed(), f2.getGreen(),
            f2.getBlue()));
    sb.append(String.format("<animateTransform attributeName=\"transform\" attributeType =\"XML\"" +
                    " type=\"rotate\" from=\"%d %d %d\" to=\"%d %d %d\" begin=\"%d00ms\" dur=\"%d00ms\" repeatCount=\"0\"" +
                    " fill=\"freeze\"/>", f1.getAngle(), f1.getX(), f1.getY(), f2.getAngle(),
            f2.getX(), f2.getY(), f1.getTime(), f2.getTime() - f1.getTime()));
  }
}
