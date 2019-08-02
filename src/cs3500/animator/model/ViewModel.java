package cs3500.animator.model;

import java.util.Iterator;
import java.util.List;

/**
 * A ExCellence Model implementation that provides only observers.
 */
public class ViewModel implements ImmutableModel {
  //A delegated Mutable Excellence model
  private final ExcellenceModel model;

  /**
   * Construct a ViewModel with the given mutable model.
   * @param model the mutable model
   */
  public ViewModel(ExcellenceModel model) {
    this.model = model;
  }

  @Override
  public List<IShape> getFrame(int time) {
    return this.model.getFrame(time);
  }

  @Override
  public int getWidth() {
    return this.model.getWidth();
  }

  @Override
  public int getHeight() {
    return this.model.getHeight();
  }

  @Override
  public int getX() {
    return this.model.getX();
  }

  @Override
  public int getY() {
    return this.model.getY();
  }

  @Override
  public Iterator<IShape> getShapes() {
    return this.model.getShapes();
  }

  @Override
  public Iterator<IKeyFrame> getAnimationsFrom(IShape shape) {
    return this.model.getAnimationsFrom(shape);
  }

  @Override
  public Iterator<Integer> getLayers() {
    return model.getLayers();
  }

  @Override
  public Iterator<IShape> getShapesAt(Integer layer) {
    return model.getShapesAt(layer);
  }

  @Override
  public int getEndTime() {
    return this.model.getEndTime();
  }
}
