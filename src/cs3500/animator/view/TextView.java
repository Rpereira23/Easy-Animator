package cs3500.animator.view;

import java.io.IOException;
import java.util.Iterator;

import cs3500.animator.model.IKeyFrame;
import cs3500.animator.model.IShape;
import cs3500.animator.model.ImmutableModel;

/**
 * ExCellence Animation view that renders model as plain text.
 */
public class TextView implements IView {
  private Appendable out;
  private ImmutableModel model;

  /**
   * Construct a text view with a designated observable model and a designated output destination.
   * @param model an observable model
   * @param out a designated appendable output destination
   */
  public TextView(ImmutableModel model, Appendable out) {
    this.model = model;
    this.out = out;
  }

  /**
   * Render the model in plain text.  Group by shape and order animations by start time.
   */
  @Override
  public void render() {
    StringBuilder sb = new StringBuilder();

    Iterator<IShape> itShape = this.model.getShapes();

    sb.append("canvas ").append(this.model.getX()).append(" ").append(this.model.getY()).append(" ")
            .append(this.model.getWidth()).append(" ").append(this.model.getHeight()).append("\n");

    IShape curShape;
    while (itShape.hasNext()) {
      curShape = itShape.next();
      sb.append(String.format("shape %s %s %d\n", curShape.getName(), curShape.getCode(),
              curShape.getLayer()));
      Iterator<IKeyFrame> itKF = this.model.getAnimationsFrom(curShape);
      IKeyFrame cur;
      IKeyFrame prev = null;
      while (itKF.hasNext()) {
        cur = itKF.next();
        if (prev != null) {
          sb.append("motion ");
          sb.append(curShape.getName());
          sb.append(" ");
          sb.append(prev.getDescription()).append(" ").append(cur.getDescription()).append("\n");
        }
        prev = cur;
      }
    }

    try {
      this.out.append(sb.toString().trim());
    } catch (IOException e) {
      throw new IllegalStateException("unable to append to destination");
    }
  }
}
