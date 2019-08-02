package cs3500.animator.model;

import java.util.Comparator;

/**
 * The abstract base class containing default implementations of Shapes.
 *
 * @param <T> the shape type that extends this class
 */
public abstract class AShape<T extends AShape<T>> implements IShape {
  private final int x;
  private final int y;
  protected final int width;
  protected final int height;
  private final String name;
  private final String code;
  private final int red;
  private final int green;
  private final int blue;
  private final int angle;
  private final int layer;

  /**
   * A Comparator for IShape objects.
   */
  public static class IShapeComparator implements Comparator<IShape> {
    @Override
    public int compare(IShape o1, IShape o2) {
      return o1.getName().compareTo(o2.getName());
    }
  }

  /**
   * A Default builder for Shapes.
   *
   * @param <T> the child shape type
   * @param <E> the builder type of the child shape
   */
  abstract static class BuildShape<T extends AShape<T>, E extends BuildShape<T, E>> {
    private String name;
    private String code;
    protected int width;
    protected int height;
    protected int y;
    protected int x;
    protected int red;
    protected int green;
    protected int blue;
    protected int angle;
    protected int layer;

    /**
     * Constructor for default builder that initialized default fields.
     */
    BuildShape() {
      this.width = 0;
      this.height = 0;
      this.name = "";
      this.code = "";
      this.x = 0;
      this.y = 0;
      this.red = 0;
      this.green = 0;
      this.blue = 0;
      this.layer = 1;
    }

    /**
     * Sets name field in builder.
     *
     * @param name the string to be set
     * @return the updated builder
     */
    public E setName(String name) {
      if (name == null) {
        throw new IllegalArgumentException("cannot set a null shape name");
      }
      this.name = name;
      return self();
    }

    /**
     * Sets the layer field of the builder.
     * @param layer the layer to set
     * @return the updated builder
     */
    public E setLayer(int layer) {
      this.layer = layer;
      return self();
    }

    /**
     * Sets the code field to the given String.
     *
     * @param code the string to be set
     * @return the updated builder
     */
    protected E setCode(String code) {
      if (code == null) {
        throw new IllegalArgumentException("cannot set a null shape code");
      }
      this.code = code;
      return self();
    }

    /**
     * Set the width of the builder.
     * @param width the width to be set
     * @return the updated builder
     */
    public E setWidth(int width) {
      if (width < 0) {
        throw new IllegalArgumentException("cannot have a negative width");
      }
      this.width = width;
      return self();
    }

    /**
     * Set the height of the builder.
     * @param height the height to be set
     * @return the updated builder
     */
    public E setHeight(int height) {
      if (height < 0) {
        throw new IllegalArgumentException("cannot have a negative height");
      }
      this.height = height;
      return self();
    }

    /**
     * Set the x value of the builder.
     * @param x the x val to be set
     * @return the updated builder
     */
    public E setX(int x) {
      this.x = x;
      return self();
    }

    /**
     * Set the y value of the builder.
     * @param y the y val to be set
     * @return the updated builder
     */
    public E setY(int y) {
      this.y = y;
      return self();
    }

    /**
     * Set the red color val of the builder.
     * @param r the color val
     * @return the updated builder
     */
    public E setRed(int r) {
      if (r < 0 || r > 255) {
        throw new IllegalArgumentException("invalid red color");
      }
      this.red = r;
      return self();
    }

    /**
     * Set the green color val of the builder.
     * @param g the color val
     * @return the updated builder
     */
    public E setGreen(int g) {
      if (g < 0 || g > 255) {
        throw new IllegalArgumentException("invalid green color");
      }
      this.green = g;
      return self();
    }

    /**
     * Set the blue color val of the builder.
     * @param b the color val
     * @return the updated builder
     */
    public E setBlue(int b) {
      if (b < 0 || b > 255) {
        throw new IllegalArgumentException("invalid blue color");
      }
      this.blue = b;
      return self();
    }

    /**
     * Sets the angle of the builder.
     * @param a the angle to be set
     * @return the updated builder
     */
    public E setAngle(int a) {
      this.angle = a % 360;
      if (a < 0) {
        this.angle = 360 + a;
      }
      return self();
    }

    /**
     * Build object of child type.
     *
     * @return the built object
     */
    abstract T build();

    /**
     * Return builder of child type.
     *
     * @return child's builder
     */
    abstract E self();
  }

  /**
   * Constructor for creating shape from builder.
   *
   * @param builder contains all data used to customize this shape
   */
  public AShape(BuildShape<T, ?> builder) {
    this.name = builder.name;
    this.code = builder.code;
    this.width = builder.width;
    this.height = builder.height;
    this.x = builder.x;
    this.y = builder.y;
    this.red = builder.red;
    this.green = builder.green;
    this.blue = builder.blue;
    this.angle = builder.angle;
    this.layer = builder.layer;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof IShape)) {
      return false;
    } else {
      IShape other = (IShape) o;
      return this.getName().compareTo(other.getName()) == 0;
    }
  }

  @Override
  public int hashCode() {
    return this.getName().hashCode();
  }

  @Override
  public String toString() {
    return this.getName();
  }

  @Override
  public int getWidth() {
    return this.width;
  }

  @Override
  public int getHeight() {
    return this.height;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public String getCode() {
    return this.code;
  }

  @Override
  public IShape getEnd(IKeyFrame start, IKeyFrame end, int t) {
    BuildShape builder;

    switch (this.getCode()) {
      case "rectangle":
        builder = new Rect.BuildRect();
        break;
      case "ellipse":
        builder = new Ellipse.BuildEllipse();
        break;
      default:
        throw new IllegalStateException("invalid shape type");
    }

    if (start == null || end == null || t < start.getTime() || t > end.getTime()) {
      return null;
    }

    if (t == start.getTime()) {
      return builder.setName(this.getName()).setWidth(start.getWidth())
              .setHeight(start.getHeight())
              .setX(start.getX()).setY(start.getY()).setRed(start.getRed())
              .setGreen(start.getGreen())
              .setBlue(start.getBlue())
              .setAngle(start.getAngle())
              .build();
    }

    if (t == end.getTime()) {
      return builder.setName(this.getName()).setWidth(end.getWidth()).setHeight(end.getHeight())
              .setX(end.getX()).setY(end.getY()).setRed(end.getRed()).setGreen(end.getGreen())
              .setBlue(end.getBlue())
              .setAngle(end.getAngle())
              .build();
    }

    IShape endShape = builder
            .setName(this.getName())
            .setWidth(this.tweenIt(start.getTime(), end.getTime(), start.getWidth(),
                    end.getWidth(), t))
            .setHeight(this.tweenIt(start.getTime(), end.getTime(),
                    start.getHeight(), end.getHeight(), t))
            .setX(this.tweenIt(start.getTime(),
                    end.getTime(), start.getX(), end.getX(), t))
            .setY(this.tweenIt(start.getTime(),
                    end.getTime(), start.getY(), end.getY(), t))
            .setRed(this.tweenIt(
                    start.getTime(), end.getTime(), start.getRed(), end.getRed(), t))
            .setGreen(this.tweenIt(start.getTime(), end.getTime(), start.getGreen(),
                    end.getGreen(), t))
            .setBlue(this.tweenIt(start.getTime(), end.getTime(), start.getBlue(),
                    end.getBlue(), t))
            .setAngle(this.tweenIt(start.getTime(), end.getTime(),
                    start.getAngle(), end.getAngle(), t))
            .build();
    return endShape;
  }

  /**
   * Determine the result of tweening between the given values on the given interval and the given
   * time.
   *
   * @param startTime the starting time for the interval
   * @param endTime   the ending time for the interval
   * @param startVal  the value at the start of the interval
   * @param endVal    the value at the end of the interval
   * @param curTime   the current time we are interested in
   */
  private int tweenIt(int startTime, int endTime, int startVal, int endVal, int curTime) {
    int x = (int) ((double) startVal * (((double) (endTime - curTime)) /
            ((double) (endTime - startTime))) + (double) endVal * (((double) (curTime - startTime))
            / ((double) (endTime - startTime))) + 0.5);
    if (x == 0) {
      return startVal + endVal;
    }
    return x;
  }

  @Override
  public int getX() {
    return this.x;
  }

  @Override
  public int getY() {
    return this.y;
  }

  @Override
  public int getRed() {
    return this.red;
  }

  @Override
  public int getGreen() {
    return this.green;
  }

  @Override
  public int getBlue() {
    return this.blue;
  }

  @Override
  public int getAngle() {
    return this.angle;
  }

  @Override
  public int getLayer() {
    return this.layer;
  }
}
