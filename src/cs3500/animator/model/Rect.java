package cs3500.animator.model;


/**
 * Represents a rectangle shape.
 * Use a BuildShape for configuration input.
 */
public class Rect extends AShape<Rect> {

  /**
   * The rectangle builder for constructing a rectangle.
   */
  public static class BuildRect extends BuildShape<Rect, BuildRect> {

    /**
     * Constructs a rectangle builder.
     */
    public BuildRect() {
      super();
      this.setCode("rectangle");
    }

    @Override
    public Rect build() {
      return new Rect(this);
    }

    @Override
    BuildRect self() {
      return this;
    }
  }

  /**
   * Construct a rectangle object from an existing builder.
   * @param builder the builder containing all configuration information
   */
  private Rect(BuildShape<Rect, BuildRect> builder) {
    super(builder);
  }
}
