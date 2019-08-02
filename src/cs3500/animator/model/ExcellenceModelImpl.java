package cs3500.animator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import cs3500.animator.util.AnimationBuilder;

/**
 * An excellence model implementation for maintaining Shapes and Animations. INVARIANT: a single
 * Shape cannot simultaneously be acted on by two animations.
 */
public class ExcellenceModelImpl implements ExcellenceModel {
  //Mapping of shapes to all animations sorted by name.
  //Animations for each shape will be sorted by starting time.
  private final LinkedHashMap<IShape, TreeSet<IKeyFrame>> map;
  private final TreeMap<Integer, List<IShape>> layers;

  //The position of the model
  int x;
  int y;
  //The dimension of the model
  int width;
  int height;

  private int endTime;

  @Override
  public ExcellenceModel declareShape(String name, String type) {
    return this.declareShape(name, type, 1);
  }

  @Override
  public ExcellenceModel declareShape(String name, String type, Integer layer) {
    if (layer < 0 || layer == null) {
      throw new IllegalArgumentException("Invalid Layer");
    }
    ExcellenceModelImpl.Builder builder = new ExcellenceModelImpl.Builder(this);
    builder.declareShape(name, type, layer);
    return builder.build();
  }

  @Override
  public ExcellenceModel declareLayer(Integer layer) {
    ExcellenceModelImpl.Builder builder = new ExcellenceModelImpl.Builder(this);
    builder.declareLayer(layer);
    return builder.build();
  }

  @Override
  public void remove(String shapeName) {
    if (shapeName == null) {
      throw new IllegalArgumentException("attempting to delete null shape");
    }
    for (IShape shape: this.map.keySet()) {
      if (shape.getName().compareTo(shapeName) == 0) {
        this.map.remove(shape);
        return;
      }
    }
    throw new IllegalArgumentException("cannot find a shape with that name");
  }

  @Override
  public void removeLayer(Integer layer) {
    if (isValidLayer(layer)) {
      List<IShape> shapes = this.layers.get(layer);
      this.layers.remove(layer);

      for (IShape shape : shapes) {
        this.remove(shape.getName());
      }
    } else {
      throw new IllegalArgumentException("Invalid layer provided");
    }
  }

  @Override
  public ExcellenceModel addKeyFrame(String name, int time, int x, int y, int w, int h, int r,
                                     int g, int b, int a) {
    ExcellenceModelImpl.Builder builder = new ExcellenceModelImpl.Builder(this);
    builder.addKeyFrame(name, time, x, y, w, h, r, g, b, a);
    return builder.build();
  }

  @Override
  public ExcellenceModel addMotion(String name,
                                   int t1, int x1,
                                   int y1, int w1,
                                   int h1, int r1,
                                   int g1, int b1,
                                   int t2, int x2,
                                   int y2, int w2,
                                   int h2, int r2,
                                   int g2, int b2) {
    ExcellenceModelImpl.Builder builder = new ExcellenceModelImpl.Builder(this);
    builder.addMotion(name,
            t1, x1,
            y1, w1,
            h1, r1,
            g1, b1,
            t2, x2,
            y2, w2,
            h2, r2,
            g2, b2);
    return builder.build();
  }

  @Override
  public ExcellenceModel deleteKeyFrame(String name, int t) {
    if (name == null) {
      throw new IllegalArgumentException("cannot delete a keyframe from a null shape");
    }
    Boolean removed = false;
    for (IShape s : this.map.keySet()) {
      if (s.getName().compareTo(name) == 0) {
        removed = this.map.get(s).removeIf((x) -> x.getTime() == t);
      }
    }
    if (!removed) {
      throw new IllegalArgumentException("did not remove successfully");
    }
    return this;
  }

  /**
   * Builder used to construct model from file.
   */
  public static final class Builder implements AnimationBuilder<ExcellenceModel> {
    private final LinkedHashMap<IShape, TreeSet<IKeyFrame>> map;
    int x;
    int y;
    int width;
    int height;
    int endTime;
    private Map<String, AShape.BuildShape> shapeBuilders;
    private TreeMap<Integer, List<IShape>> layerMap;

    /**
     * Construct a new builder with initial configurations.
     */
    public Builder() {
      this.map = new LinkedHashMap<>();
      this.layerMap = new TreeMap<>();
      this.shapeBuilders = new HashMap<String, AShape.BuildShape>();
      this.shapeBuilders.put("rectangle", new Rect.BuildRect());
      this.shapeBuilders.put("ellipse", new Ellipse.BuildEllipse());
      this.endTime = 0;
    }

    private Builder(ExcellenceModelImpl excellenceModel) {
      this.map = excellenceModel.map;
      this.layerMap = excellenceModel.layers;
      this.shapeBuilders = new HashMap<String, AShape.BuildShape>();
      this.shapeBuilders.put("rectangle", new Rect.BuildRect());
      this.shapeBuilders.put("ellipse", new Ellipse.BuildEllipse());
      this.x = excellenceModel.x;
      this.y = excellenceModel.y;
      this.width = excellenceModel.width;
      this.height = excellenceModel.height;
      this.endTime = excellenceModel.endTime;
    }

    @Override
    public ExcellenceModel build() {
      return new ExcellenceModelImpl(this);
    }

    @Override
    public AnimationBuilder<ExcellenceModel> setBounds(int x, int y, int width, int height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      return this;
    }


    @Override
    public TreeMap<Integer, List<IShape>> getLayers() {
      return this.layerMap;
    }

    @Override
    public AnimationBuilder declareLayer(Integer layer) {
      if (isValidLayer(layer)) {
        if (this.layerMap.containsKey(layer)) {
          throw new IllegalArgumentException("Layer Already Exists");
        }

        this.layerMap.put(layer, new ArrayList<>());
        return this;

      }
      throw new IllegalArgumentException("Invalid layer provided");
    }

    @Override
    public AnimationBuilder declareShape(String name, String type, int layer) {
      if (isValidLayer(layer)) {
        AShape.BuildShape builder = this.shapeBuilders.get(type);
        if (builder == null) {
          throw new IllegalStateException("invalid shape type");
        }
        builder.setName(name);
        builder.setLayer(layer);
        IShape toAdd = builder.build();
        if (this.map.containsKey(toAdd)) {
          throw new IllegalArgumentException("there is already a shape with that name");
        }
        this.map.put(toAdd, new TreeSet<>(new KeyFrame.KeyFrameComparator()));

        if(layerMap.containsKey(layer)) {
          layerMap.get(layer).add(toAdd);
        } else {
          List<IShape> newList = new ArrayList<>();
          newList.add(toAdd);
          layerMap.put(layer, newList);
        }
        return this;

      }
        throw new IllegalArgumentException("Attempting to declare shape on invalid layer.");
    }

    @Override
    public AnimationBuilder<ExcellenceModel> declareShape(String name, String type) {
      return this.declareShape(name, type, 1);
    }

    @Override
    public AnimationBuilder<ExcellenceModel> addMotion(String name,
                                                       int t1, int x1,
                                                       int y1, int w1,
                                                       int h1, int r1,
                                                       int g1, int b1,
                                                       int t2, int x2,
                                                       int y2, int w2,
                                                       int h2, int r2,
                                                       int g2, int b2) {
      return this.addMotion(name, t1, x1, y1, w1, h1, r1, g1, b1, 0,
              t2, x2, y2, w2, h2, r2, g2, b2, 0);

    }

    @Override
    public AnimationBuilder addMotion(String name, int t1, int x1,
                                      int y1, int w1,
                                      int h1, int r1,
                                      int g1, int b1,
                                      int a1, int t2,
                                      int x2, int y2,
                                      int w2, int h2,
                                      int r2, int g2,
                                      int b2, int a2) {

      if (name == null) {
        throw new IllegalArgumentException("cannot add a motion to a null shape");
      }
      KeyFrame.BuildKeyFrame keyFrameIt = new KeyFrame.BuildKeyFrame();
      keyFrameIt.setTime(t1)
              .setWidth(w1).setHeight(h1).setX(x1).setY(y1).setRed(r1).setGreen(g1)
              .setBlue(b1).setAngle(a1);
      KeyFrame frame1 = keyFrameIt.build();

      keyFrameIt = new KeyFrame.BuildKeyFrame();
      keyFrameIt.setTime(t2).setWidth(w2).setHeight(h2).setX(x2).setY(y2).setRed(r2).setGreen(g2)
              .setBlue(b2).setAngle(a2);
      KeyFrame frame2 = keyFrameIt.build();

      TreeSet<IKeyFrame> curSet;
      for (IShape s : this.map.keySet()) {
        curSet = this.map.get(s);
        if (s.getName().compareTo(name) == 0) {
          if (!(curSet.isEmpty() || (curSet.contains(frame1) ^ curSet.contains(frame2)))) {
            throw new IllegalArgumentException("attempting to add an animation that will form a " +
                    "gap in timeline\n or will overlap a frame");
          }
          if ((curSet.contains(frame1) && curSet.floor(frame2).getTime() > frame1.getTime())
                  || (curSet.contains(frame2)
                  && curSet.ceiling(frame2).getTime() < frame2.getTime())) {
            throw new IllegalArgumentException("attempting to add an animation that will overlap");
          }
        }
      }
      this.addKeyFrame(name, frame1);
      return this.addKeyFrame(name, frame2);
    }

    @Override
    public AnimationBuilder addKeyFrame(String name, int t, int x, int y, int w, int h, int r, int g, int b, int a) {
      KeyFrame.BuildKeyFrame keyFrameIt = new KeyFrame.BuildKeyFrame();
      keyFrameIt.setTime(t).setWidth(w).setHeight(h).setX(x).setY(y).setRed(r).setGreen(g)
              .setBlue(b).setAngle(a);
      KeyFrame frame1 = keyFrameIt.build();

      this.addKeyFrame(name, frame1);

      return this;
    }

    @Override
    public AnimationBuilder<ExcellenceModel> addKeyFrame(String name, int t, int x,
                                                         int y, int w, int h,
                                                         int r, int g, int b) {
      return this.addKeyFrame(name, t , x, w, h, r, g, b, 0);
    }

    private AnimationBuilder<ExcellenceModel> addKeyFrame(String name, KeyFrame k) {
      if (name == null) {
        throw new IllegalArgumentException("cannot add a keyframe to a null shape");
      }
      for (IShape s : this.map.keySet()) {
        if (s.getName().compareTo(name) == 0) {
          IKeyFrame other = this.map.get(s).ceiling(k);
          if (other != null && other.getTime() == k.getTime() && !other.equalsComplete(k)) {
            throw new IllegalArgumentException(String.format("cannot override the frame at %d " +
                    "\n either alter or delete", other.getTime()));
          }
          if (this.endTime < k.getTime()) {
            this.endTime = k.getTime();
          }
          this.map.get(s).add(k);
          return this;
        }
      }
      throw new IllegalArgumentException("did not add successfully");
    }

    @Override
    public LinkedHashMap<IShape, TreeSet<IKeyFrame>> getMap() {
      return this.map;
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
    public int getHeight() {
      return this.height;
    }

    @Override
    public int getWidth() {
      return this.width;
    }

    @Override
    public Set<IShape> getShapes() {
      return this.map.keySet();
    }

    public int getEndTime() {
      return this.endTime;
    }

  }

  /**
   * Initial constructor to set an empty mapping of animations.
   */
  public ExcellenceModelImpl() {
    this.layers = new TreeMap<>();
    this.map = new LinkedHashMap<>();
  }

  /**
   * Constructor to configure model from builder.
   *
   * @param builder the builder with proper configurations
   */
  public ExcellenceModelImpl(AnimationBuilder<ExcellenceModel> builder) {
    this.layers = builder.getLayers();
    this.map = builder.getMap();
    this.x = builder.getX();
    this.y = builder.getY();
    this.height = builder.getHeight();
    this.width = builder.getWidth();
    this.endTime = builder.getEndTime();
  }

  /**
   * Gets a shape animated to the tick amount. The shape must have an animation or null is
   * returned.
   *
   * @param tick  represents a tick in time.
   * @param shape represents the shape to get.
   * @return the shape at that tick.
   */
  private IShape getShapeAt(int tick, IShape shape) {
    TreeSet<IKeyFrame> animations = this.map.get(shape);
    IShape curShape = shape;
    IKeyFrame prev = null;
    for (IKeyFrame kf : animations) {
      if (prev != null && kf.getTime() >= tick) {
        return curShape.getEnd(prev, kf, tick);
      }
      prev = kf;
    }
    return null;
  }

  /**
   * provides the list of shapes animated at that given tick.
   *
   * @param tick the tick.
   * @return a list of shapes animated to the given tick.
   */
  @Override
  public List<IShape> getFrame(int tick) {
    List<IShape> frame = new LinkedList<IShape>();
    IShape toAdd;
    for (IShape shape : this.map.keySet()) {
      toAdd = this.getShapeAt(tick, shape);
      if (toAdd != null) {
        frame.add(toAdd);
      }
    }
    return frame;
  }

  /**
   * gets an iterable form of the models shapes.
   *
   * @return an iterator of shapes.
   */
  @Override
  public Iterator<IShape> getShapes() {
    return this.map.keySet().iterator();
  }

  /**
   * Gets an iterable form of animations based on the provided shape.
   *
   * @param shape the shape.
   * @return a iterator of animations.
   */
  @Override
  public Iterator<IKeyFrame> getAnimationsFrom(IShape shape) {
    if (this.map.get(shape) != null) {
      return this.map.get(shape).iterator();
    }
    else {
      return null;
    }
  }

  @Override
  public Iterator<Integer> getLayers() {
    return this.layers.keySet().iterator();
  }

  @Override
  public Iterator<IShape> getShapesAt(Integer layer) {
      if(isValidLayer(layer)) {
          if(this.layers.containsKey(layer)) {
              return this.layers.get(layer).iterator();
          }
      }
    return null;
  }

  /**
   * Provides the width of the canvas.
   *
   * @return the width of the canvas.
   */
  @Override
  public int getWidth() {
    return this.width;
  }

  /**
   * Provides the height of the canvas.
   *
   * @return the height of the canvas.
   */
  @Override
  public int getHeight() {
    return this.height;
  }

  /**
   * Provides the x coordinate location of the canvas on the screen.
   *
   * @return the x coordinate.
   */
  @Override
  public int getX() {
    return this.x;
  }

  /**
   * Provides the y coordinate location of the canvas on the screen.
   *
   * @return the y coordinate.
   */
  @Override
  public int getY() {
    return this.y;
  }

  /**
   * Gets the end time of the animation
   * @return the end time of the animation.
   */
  @Override
  public int getEndTime() {
    return this.endTime;
  }


  /**
   * Wether the provided layer is valid
   * @param layer the layer
   * @return wether it is valid.
   */
  private static boolean isValidLayer(Integer layer) {
    return !(layer < 1 || layer == null);
  }

}
