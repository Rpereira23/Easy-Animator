package cs3500.animator.model;

import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import cs3500.animator.util.AnimationBuilder;
import cs3500.animator.util.AnimationReader;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExcellenceModelTest {
  ExcellenceModel smallDemoModel;
  ExcellenceModel buildingsModel;
  ExcellenceModel simpleModel;
  IShape r1;
  IShape o1;
  KeyFrame k1;
  KeyFrame k2;
  KeyFrame k3;
  KeyFrame k4;

  private ExcellenceModel readFile(String filePath) throws IOException {
    Readable r = new FileReader(filePath);
    AnimationBuilder<ExcellenceModel> builder = new ExcellenceModelImpl.Builder();
    AnimationReader<ExcellenceModel> reader = new AnimationReader();
    return reader.parseFile(r, builder);
  }

  @Before
  public void setUp() {
    try {
      this.smallDemoModel = readFile("smalldemo.txt");
      // this.bigBangModel = readFile("big-bang-big-crunch.txt");
      this.buildingsModel = readFile("buildings.txt");
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    AnimationBuilder<ExcellenceModel> builder = new ExcellenceModelImpl.Builder();
    builder.setBounds(200, 200, 500, 500);
    builder.declareShape("R0", "rectangle");
    builder.declareShape("E0", "ellipse");

    k1 = new KeyFrame.BuildKeyFrame().setTime(1).setX(5).setY(5).setWidth(50).setHeight(50)
            .setRed(155).setGreen(155).setBlue(155).build();
    k2 = new KeyFrame.BuildKeyFrame().setTime(100).setX(300).setY(300).setWidth(26)
            .setHeight(38).setRed(100).setGreen(200).setBlue(255).build();

    k3 = new KeyFrame.BuildKeyFrame().setTime(1).setX(40).setY(40).setWidth(50).setHeight(50)
            .setRed(155).setGreen(155).setBlue(155).build();
    k4 = new KeyFrame.BuildKeyFrame().setTime(200).setX(40).setY(40).setWidth(50).setHeight(50)
            .setRed(155).setGreen(155).setBlue(155).build();

    builder.addMotion("R0", k1.getTime(), k1.getX(), k1.getY(), k1.getWidth(), k1.getHeight(),
            k1.getRed(), k1.getGreen(), k1.getBlue(), k2.getTime(), k2.getX(), k2.getY(),
            k2.getWidth(), k2.getHeight(), k2.getRed(), k2.getGreen(), k2.getBlue());

    builder.addMotion("E0", k3.getTime(), k3.getX(), k3.getY(), k3.getWidth(), k3.getHeight(),
            k3.getRed(), k3.getGreen(), k3.getBlue(), k4.getTime(), k4.getX(), k4.getY(),
            k4.getWidth(), k4.getHeight(), k4.getRed(), k4.getGreen(), k4.getBlue());

    this.simpleModel = new ExcellenceModelImpl.Builder().build();

    this.o1 = new Ellipse.BuildEllipse().setName("o1").setHeight(1).setGreen(99).setY(9).build();
    this.r1 = new Rect.BuildRect().setName("r1").setWidth(1).setHeight(2).setRed(1).build();
  }

  @Test
  public void declareShape() {
    this.simpleModel = simpleModel.declareShape("r1", "rectangle");
    Iterator<IShape> shapes = this.simpleModel.getShapes();
    assertEquals("r1", shapes.next().getName());

    this.simpleModel = simpleModel.declareShape("o1", "ellipse");
    shapes = this.simpleModel.getShapes();
    shapes.next();
    assertEquals("o1", shapes.next().getName());

    try {
      this.simpleModel = this.simpleModel.declareShape(null, "ellipse");
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("cannot set a null shape name", e.getMessage());
    }

    try {
      this.simpleModel = this.simpleModel.declareShape("hi", null);
      fail("no error thrown");
    } catch (IllegalStateException i) {
      assertEquals("invalid shape type", i.getMessage());
    }

    try {
      this.simpleModel = this.simpleModel.declareShape("hi", "sauce");
      fail("no error thrown");
    } catch (IllegalStateException i) {
      assertEquals("invalid shape type", i.getMessage());
    }

    try {
      this.simpleModel = this.simpleModel.declareShape("r1", "rectangle");
      fail("no error thrown");
    } catch (IllegalArgumentException i) {
      assertEquals("there is already a shape with that name", i.getMessage());
    }
  }

  @Test
  public void remove() {
    this.simpleModel = this.simpleModel.declareShape("hi", "rectangle");
    assertEquals("hi", this.simpleModel.getShapes().next().getName());
    this.simpleModel.remove("hi");
    assertFalse(this.simpleModel.getShapes().hasNext());

    try {
      this.simpleModel.remove(null);
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("attempting to delete null shape", e.getMessage());
    }

    try {
      this.simpleModel.remove("bye");
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("cannot find a shape with that name", e.getMessage());
    }
  }

  @Test
  public void addAnimation() {
    this.simpleModel = this.simpleModel.declareShape("o1", "ellipse");
    IShape ellipse = this.simpleModel.getShapes().next();
    this.simpleModel = this.simpleModel.addMotion("o1", 1, 1, 1,
            1, 1, 1, 100, 1, 2, 2, 2, 2, 2, 2,
            2, 2);
    Iterator<IKeyFrame> kfs = this.simpleModel.getAnimationsFrom(ellipse);
    assertEquals(1, kfs.next().getTime());
    assertEquals(2, kfs.next().getTime());

    this.simpleModel.addMotion("o1", 2, 2, 2, 2, 2, 2, 2, 2, 5,
            5, 5, 5, 5, 5, 5, 5);

    kfs = this.simpleModel.getAnimationsFrom(ellipse);
    kfs.next();
    kfs.next();
    assertEquals(5, kfs.next().getTime());

    try {
      this.simpleModel.addMotion(null, 4, 4, 4, 4, 4, 4, 4, 4,
              7, 7, 7, 7, 7, 7, 7, 7);
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("cannot add a motion to a null shape", e.getMessage());
    }

    try {
      this.simpleModel.addMotion("o1", 7, 7, 7, 7, 7, 7, 7, 7,
              9, 9, 9, 9, 9, 9, 9, 9);
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("attempting to add an animation that will form a gap in timeline\n" +
              " or will overlap a frame", e.getMessage());
    }

    try {
      this.simpleModel.addMotion("o1", 0, 0, 0, 0, 0, 0, 0,
              0, 10, 0, 0, 0, 0, 0, 0, 0);
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("attempting to add an animation that will form a gap in timeline\n" +
              " or will overlap a frame", e.getMessage());
    }
  }

  @Test
  public void deleteKeyFrame() {
    this.simpleModel = this.simpleModel.declareShape("o1", "ellipse");
    IShape ellipse = this.simpleModel.getShapes().next();
    this.simpleModel = this.simpleModel.addKeyFrame("o1", 1, 1, 1,
            1, 1, 1, 100, 1, 1);

    this.simpleModel = this.simpleModel.deleteKeyFrame("o1", 1);
    assertFalse(this.simpleModel.getAnimationsFrom(ellipse).hasNext());

    this.simpleModel = this.simpleModel.addKeyFrame("o1", 1, 1, 1,
            1, 1, 1, 100, 1, 1);
    this.simpleModel = this.simpleModel.addKeyFrame("o1", 2, 1, 2, 2, 2,
            2, 2, 2, 1);

    Iterator<IKeyFrame> kfs = this.simpleModel.getAnimationsFrom(ellipse);
    kfs.next();
    assertTrue(kfs.hasNext());

    this.simpleModel = this.simpleModel.deleteKeyFrame("o1", 2);
    kfs = this.simpleModel.getAnimationsFrom(ellipse);
    kfs.next();
    assertFalse(kfs.hasNext());

    try {
      this.simpleModel = this.simpleModel.deleteKeyFrame(null, 1);
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("cannot delete a keyframe from a null shape", e.getMessage());
    }

    try {
      this.simpleModel = this.simpleModel.deleteKeyFrame("o1", 8);
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("did not remove successfully", e.getMessage());
    }
  }

  @Test
  public void addKeyFrame() {
    this.simpleModel = this.simpleModel.declareShape("o1", "ellipse");
    IShape ellipse = this.simpleModel.getShapes().next();
    this.simpleModel = this.simpleModel.addKeyFrame("o1", 1, 1, 1,
            1, 1, 1, 100, 1, 1);
    assertEquals(1, this.simpleModel.getAnimationsFrom(ellipse).next().getTime());
    assertEquals(100, this.simpleModel.getAnimationsFrom(ellipse).next().getGreen());

    this.simpleModel = this.simpleModel.addKeyFrame("o1", 2, 1, 2, 2, 2,
            2, 2, 2, 1);
    Iterator<IKeyFrame> kfs = this.simpleModel.getAnimationsFrom(ellipse);
    kfs.next();
    assertEquals(2, kfs.next().getTime());

    try {
      this.simpleModel = this.simpleModel.addKeyFrame("o1", 1, 1, 1, 1,
              1, 1, 1, 1, 1);
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("cannot override the frame at 1 \n" +
              " either alter or delete", e.getMessage());
    }

    try {
      this.simpleModel = this.simpleModel.addKeyFrame(null, 1, 1, 1, 1,
              1, 1, 1, 1, 1);
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("cannot add a keyframe to a null shape", e.getMessage());
    }

    try {
      this.simpleModel = this.simpleModel.addKeyFrame("Mississippi", 1, 1, 1, 1,
              1, 1, 1, 1, 1);
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("did not add successfully", e.getMessage());
    }
  }
}