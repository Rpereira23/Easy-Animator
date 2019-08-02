package cs3500.animator.model;

import java.util.Iterator;
import java.util.List;

/**
 * A ExCellence Model interface that does not allow for any mutation.
 */
public interface ImmutableModel {

  /**
   * Access a list of IShapes that appear in the given time frame.
   * INVARIANT: the shapes are returned in the order they should be rendered in the frame.
   * @param time the time for which we would like to get the frame
   * @return the list of shapes in the specified frame
   */
  List<IShape> getFrame(int time);

  /**
   * Access the width of this model.
   * @return an int representing the width
   */
  int getWidth();

  /**
   * Access the height of this model.
   * @return an int representing the height
   */
  int getHeight();

  /**
   * Access the x coordinate location of this model.
   * @return an int representing the x coordinate
   */
  int getX();

  /**
   * Access the y coordinate of location of this model.
   * @return an int representing the y coordinate
   */
  int getY();

  /**
   * Return an iterator that will progress over all this model's shapes alphabetically.
   * @return the iterator
   */
  Iterator<IShape> getShapes();

  /**
   * Return an iterator that will progress over all the KeyFrames for the given shape in order
   * of starting time.
   * @param shape the shape to get the animations from.
   * @return the iterator
   */
  Iterator<IKeyFrame> getAnimationsFrom(IShape shape);

  /**
   * Return an iterator of all the layers within the animation.
   * @return the layers.
   */
  Iterator<Integer> getLayers();


  /**
   * Gets all the shapes based on the given layer.
   * @param layer the layer
   * @return the shapes in the given layer.
   */
  Iterator<IShape> getShapesAt(Integer layer);

  /**
   * Accesses the end time of the animation.  This is considered to be the last point in time with
   * an existing keyframe.
   * @return the end time of this model
   */
  int getEndTime();
}
