package cs3500.animator.model;

public interface IKeyFrame {

  /**
   * Returns a string description of the KeyFrame.
   *
   * @return the string representing the characteristics of this
   */
   String getDescription();

  /**
   * Determines if two KeyFrames are completely equal through all characteristics.
   *
   * @param other a KeyFrame to compare this to
   * @return whether or not the KeyFrames share equality through all fields
   */
   boolean equalsComplete(IKeyFrame other);

  /**
   * Access the time field of this KeyFrame.
   *
   * @return the time field of this KeyFrame
   */
   int getTime();

  /**
   * Access the width field of this KeyFrame.
   *
   * @return the width of this KeyFrame
   */
   int getWidth();

  /**
   * Access the height field of this KeyFrame.
   *
   * @return the height of this KeyFrame
   */
   int getHeight();

  /**
   * Access the x-coordinate field of this KeyFrame.
   *
   * @return the x-coordinate field of this KeyFrame
   */
   int getX();

  /**
   * Access the y-coordinate field of this KeyFrame.
   *
   * @return the y-coordinate field of this KeyFrame
   */
   int getY();

  /**
   * Access the red color value of this KeyFrame.
   *
   * @return the red color value of this KeyFrame
   */
   int getRed();

  /**
   * Access the green color value of this KeyFrame.
   *
   * @return the green color value of this KeyFrame
   */
   int getGreen();

  /**
   * Access the blue color value of this KeyFrame.
   *
   * @return the blue color value of this KeyFrame
   */
   int getBlue();

   /**
    * Access the angle of this key frame.
    * @return the angle of this key frame.
    */
   int getAngle();
}
