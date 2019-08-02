package cs3500.animator.model;

import java.util.Comparator;

/**
 * A representation of the state of an IShape at a single point in time.
 */
public class KeyFrame implements IKeyFrame {
  private final int time;
  private final int width;
  private final int height;
  private final int x;
  private final int y;
  private final int red;
  private final int green;
  private final int blue;
  private final int angle;

  /**
   * Comparator of KeyFrames used for comparison by time value.
   */
  public static class KeyFrameComparator implements Comparator<IKeyFrame> {
    @Override
    public int compare(IKeyFrame o1, IKeyFrame o2) {
      return o1.getTime() - o2.getTime();
    }
  }

  /**
   * Builder used to customize field configuration of KeyFrame.
   */
  public static class BuildKeyFrame {
    private int time;
    private int width;
    private int height;
    private int x;
    private int y;
    private int red;
    private int green;
    private int blue;
    private int angle;

    /**
     * Set the time field of the builder.
     * @param time the time value to set
     * @return the updated builder
     */
    public BuildKeyFrame setTime(int time) {
      if (time < 0) {
        throw new IllegalArgumentException("cannot have a negative time");
      }
      this.time = time;
      return this;
    }

    public BuildKeyFrame setAngle(int angle) {
      this.angle = (angle % 360);

      if (angle < 0) {
        this.angle = 360 + this.angle;
      }

      return this;
    }

    /**
     * Set the widht field of the builder.
     * @param width the width value to set
     * @return the updated builder
     */
    public BuildKeyFrame setWidth(int width) {
      if (width < 0) {
        throw new IllegalArgumentException("cannot have a negative width");
      }
      this.width = width;
      return this;
    }

    /**
     * Set the height field of the builder.
     * @param height the height value to set
     * @return the updated builder
     */
    public BuildKeyFrame setHeight(int height) {
      if (height < 0) {
        throw new IllegalArgumentException("cannot have a negative height");
      }
      this.height = height;
      return this;
    }

    /**
     * Set the x-coord field of the builder.
     * @param xcoord the x value to set
     * @return the updated builder
     */
    public BuildKeyFrame setX(int xcoord) {
      this.x = xcoord;
      return this;
    }

    /**
     * Set the y-coord field of the builder.
     * @param ycoord the y value to set
     * @return the updated builder
     */
    public BuildKeyFrame setY(int ycoord) {
      this.y = ycoord;
      return this;
    }

    /**
     * Set the red color value of the builder.
     * @param red the color value to set
     * @return the updated builder
     */
    public BuildKeyFrame setRed(int red) {
      if (red < 0 || red > 255) {
        throw new IllegalArgumentException("red color out of range");
      }
      this.red = red;
      return this;
    }

    /**
     * Set the green color value of the builder.
     * @param green the color value to set
     * @return the updated builder
     */
    public BuildKeyFrame setGreen(int green) {
      if (green < 0 || green > 255) {
        throw new IllegalArgumentException("green color out of range");
      }
      this.green = green;
      return this;
    }

    /**
     * Set the blue color value of the builder.
     * @param blue the color value to set
     * @return the updated builder
     */
    public BuildKeyFrame setBlue(int blue) {
      if (blue < 0 || blue > 255) {
        throw new IllegalArgumentException("blue color out of range");
      }
      this.blue = blue;
      return this;
    }

    public KeyFrame build() {
      return new KeyFrame(this);
    }
  }

  /**
   * Constructs KeyFrame by accessing fields of given builder.
   * @param builder the builder containing proper configuration fields
   */
  KeyFrame(BuildKeyFrame builder) {
    this.time = builder.time;
    this.x = builder.x;
    this.y = builder.y;
    this.width = builder.width;
    this.height = builder.height;
    this.red = builder.red;
    this.green = builder.green;
    this.blue = builder.blue;
    this.angle = builder.angle;
  }

  /**
   * Returns a string description of the KeyFrame.
   * @return the string representing the characteristics of this
   */
  public String getDescription() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.time).append(" ").append(this.x).append(" ").append(this.y).append(" ")
            .append(this.width).append(" ").append(this.height).append(" ").append(this.red)
            .append(" ").append(this.green).append(" ").append(this.blue).append(" ")
            .append(this.getAngle());
    return sb.toString();
  }

  /**
   * Determines if two KeyFrames are completely equal through all characteristics.
   * @param other a KeyFrame to compare this to
   * @return whether or not the KeyFrames share equality through all fields
   */
  public boolean equalsComplete(IKeyFrame other) {
    return this.getTime() == other.getTime() &&
            this.getWidth() == other.getWidth() &&
            this.getHeight() == other.getHeight() &&
            this.getX() == other.getX() &&
            this.getY() == other.getY() &&
            this.getRed() == other.getRed() &&
            this.getGreen() == other.getGreen() &&
            this.getBlue() == other.getBlue();
  }

  /**
   * Access the time field of this KeyFrame.
   * @return the time field of this KeyFrame
   */
  public int getTime() {
    return this.time;
  }

  /**
   * Access the width field of this KeyFrame.
   * @return the width of this KeyFrame
   */
  public int getWidth() {
    return this.width;
  }

  /**
   * Access the height field of this KeyFrame.
   * @return the height of this KeyFrame
   */
  public int getHeight() {
    return this.height;
  }

  /**
   * Access the x-coordinate field of this KeyFrame.
   * @return the x-coordinate field of this KeyFrame
   */
  public int getX() {
    return this.x;
  }

  /**
   * Access the y-coordinate field of this KeyFrame.
   * @return the y-coordinate field of this KeyFrame
   */
  public int getY() {
    return this.y;
  }

  /**
   * Access the red color value of this KeyFrame.
   * @return the red color value of this KeyFrame
   */
  public int getRed() {
    return this.red;
  }

  /**
   * Access the green color value of this KeyFrame.
   * @return the green color value of this KeyFrame
   */
  public int getGreen() {
    return this.green;
  }

  /**
   * Access the blue color value of this KeyFrame.
   * @return the blue color value of this KeyFrame
   */
  public int getBlue() {
    return this.blue;
  }

  /**
   * Access the angle of this KeyFrame.
   * @return the angle of this KeyFrame.
   */
  public int getAngle() {

    return this.angle;
  }
}
