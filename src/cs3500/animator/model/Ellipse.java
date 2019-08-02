package cs3500.animator.model;


/**
 * Represents an Ellipse shape.
 * Use a BuildShape for configuration input.
 */
public class Ellipse extends AShape<Ellipse> {

  /**
   * Builder for constructing an Ellipse shape.
   */
  public static class BuildEllipse extends BuildShape<Ellipse, BuildEllipse> {

    /**
     * Construct an Ellipse builder.
     */
    public BuildEllipse() {
      super();
      this.setCode("ellipse");
    }

    @Override
    public Ellipse build() {
      return new Ellipse(this);
    }

    @Override
    BuildEllipse self() {
      return this;
    }
  }

  /**
   *  Construct a customized ellipse object from an existing builder.
   * @param builder the builder containing configuration information
   */
  private Ellipse(BuildShape<Ellipse, BuildEllipse> builder) {
    super(builder);
  }
}