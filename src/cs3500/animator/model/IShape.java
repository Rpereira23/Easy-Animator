package cs3500.animator.model;

/**
 * Represents a shape.
 */
public interface IShape {
  /**
   * Computes the height of this shape.
   *
   * @return the height.
   */
  int getHeight();

  /**
   * Computes the width of this shape.
   *
   * @return the width.
   */
  int getWidth();

  /**
   * A string representing the name of this shape. (i.e "Oval")
   *
   * @return the name.
   */
  String getName();

  /**
   * A string representing the code of this shape (i.e "oval")
   *
   * @return the code.
   */
  String getCode();

  /**
   * Construct a new Shape that would be the result of animating this with the given animation to
   * the given time t.
   * @param t the time at which to apply the animation
   * @return the new shapes with the traits after animation
   */
  IShape getEnd(IKeyFrame start, IKeyFrame end, int t);

  /**
   * Access the x coordinate location of this.
   * @return the x coordinate
   */
  int getX();

  /**
   * Access the y coordinate location of this.
   * @return the y coordinate
   */
  int getY();

  /**
   * Access the red color value of this.
   * @return the red value
   */
  int getRed();

  /**
   * Access the blue color value of this.
   * @return the blue value
   */
  int getBlue();

  /**
   * Access the green color value of this.
   * @return the green value
   */
  int getGreen();

  /**
   * Access the angle of this shape.
   * @return the angle of this shape.
   */
  int getAngle();

  /**
   * Get the layer this shape occurs at.
   * @return the layer as an integer
   */
  int getLayer();
}
