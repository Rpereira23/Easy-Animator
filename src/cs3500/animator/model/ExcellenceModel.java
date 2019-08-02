package cs3500.animator.model;

/**
 * Model For ExCellence--Easy-Animator representing the current animations on shapes, that allows
 * mutability.
 */
public interface ExcellenceModel extends ImmutableModel {

  /**
   * Adds a shape of the given name and type to the animation.
   * @param name a string representing the name of the new shape
   * @param type a string representing the type of the new shape
   * @return the new model with the new shape added to this
   */
  ExcellenceModel declareShape(String name, String type);

  /**
   * Adds a shape of the given name and type to the animation.
   * @param name a string representing the name of the new shape
   * @param type a string representing the type of the new shape
   * @param layer the layer the shaped will be declared under.
   * @return the new model with the new shape added to this
   */
  ExcellenceModel declareShape(String name, String type, Integer layer);


  /**
   * Add a layer of the given priority to the animation.
   * @param layer the new layer
   * @return the new model with the layer added.
   */
  ExcellenceModel declareLayer(Integer layer);

  /**
   * Removes the shape of given name from the animation.
   * @param shapeName the name of the shape to remove from the animation
   */
  void remove(String shapeName);


  /**
   * Removes the layer from the given layer number
   * @param layer the layer
   */
  void removeLayer(Integer layer);

  /**
   * Add a motion to the excellence model.
   * @param name the name of the shape to add it to
   * @param t1 the time
   * @param x1 x pos
   * @param y1 y pox
   * @param w1 width starting
   * @param h1 height starting
   * @param r1 red starting
   * @param g1 green starting
   * @param b1 blue starting
   * @param t2 end time
   * @param x2 end x
   * @param y2 end y
   * @param w2 end width
   * @param h2 end height
   * @param r2 end red
   * @param g2 end green
   * @param b2 end blue
   * @return
   */
  ExcellenceModel addMotion(String name,
                                   int t1, int x1,
                                   int y1, int w1,
                                   int h1, int r1,
                                   int g1, int b1,
                                   int t2, int x2,
                                   int y2, int w2,
                                   int h2, int r2,
                                   int g2, int b2);

  /**
   * Removes the keyframe found at time t for the shape with the given name.
   * @param name the name of the shape that has the keyframe we wish to delete
   * @param t the time at which the keyframe occurs in the animation
   * @return
   */
  ExcellenceModel deleteKeyFrame(String name, int t);

  /**
   * Adds a keyframe to the given shape name with the given characteristics.
   * @param name the name of the shape to add the keyframe to
   * @param time the time at which the keyframe will occur
   * @param x the x position for the keyframe
   * @param y the y position for the keyframe
   * @param w the width dimension of the keyframe
   * @param h the height dimension of the keyframe
   * @param r the red color val of the keyframe
   * @param g the green color val of the keyframe
   * @param b the blue color val of the keyframe
   * @param a the angle value of the keyframe
   * @return
   */
  ExcellenceModel addKeyFrame(String name, int time, int x, int y, int w, int h, int r, int g,
                              int b, int a);
}

