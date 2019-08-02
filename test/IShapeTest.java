import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cs3500.animator.model.IShape;
import cs3500.animator.model.Ellipse;
import cs3500.animator.model.Rect;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

public class IShapeTest {
  IShape rect;
  IShape rect2;
  IShape ellipse;
  IShape ellipse2;

  @Before
  public void setUp() {
    this.rect = new Rect.BuildRect().setName("hi").setX(5).setY(3)
            .setWidth(10).setHeight(100).setRed(255).setBlue(0).setGreen(0).build();
    this.rect2 = new Rect.BuildRect().build();
    this.ellipse = new Ellipse.BuildEllipse().setName("sue").setHeight(10).setWidth(10)
            .setX(20).setY(22).setRed(30).setGreen(3).setBlue(100).build();
    this.ellipse2 = new Ellipse.BuildEllipse().build();
  }

  @Test
  public void invalidNegHeight() {
    IShape test;
    try {
      test = new Ellipse.BuildEllipse().setHeight(-1).build();
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("cannot have a negative height", e.getMessage());
    }
    try {
      test = new Rect.BuildRect().setHeight(-10).build();
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("cannot have a negative height", e.getMessage());
    }
    try {
      test = new Rect.BuildRect().setHeight(-1).build();
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("cannot have a negative height", e.getMessage());
    }
    try {
      test = new Ellipse.BuildEllipse().setHeight(-10).build();
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("cannot have a negative height", e.getMessage());
    }
  }

  @Test
  public void invalidNegWidth() {
    IShape test;
    try {
      test = new Ellipse.BuildEllipse().setWidth(-1).build();
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("cannot have a negative width", e.getMessage());
    }
    try {
      test = new Rect.BuildRect().setWidth(-10).build();
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("cannot have a negative width", e.getMessage());
    }
    try {
      test = new Rect.BuildRect().setWidth(-1).build();
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("cannot have a negative width", e.getMessage());
    }
    try {
      test = new Ellipse.BuildEllipse().setWidth(-10).build();
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("cannot have a negative width", e.getMessage());
    }
  }

  @Test
  public void invalidColorValue() {
    IShape test;
    try {
      test = new Ellipse.BuildEllipse().setRed(-2).build();
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("invalid red color", e.getMessage());
    }
    try {
      test = new Rect.BuildRect().setRed(-3).build();
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("invalid red color", e.getMessage());
    }
    try {
      test = new Rect.BuildRect().setRed(256).build();
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("invalid red color", e.getMessage());
    }
    try {
      test = new Ellipse.BuildEllipse().setRed(257).build();
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("invalid red color", e.getMessage());
    }
    try {
      test = new Ellipse.BuildEllipse().setBlue(-2).build();
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("invalid blue color", e.getMessage());
    }
    try {
      test = new Rect.BuildRect().setBlue(-3).build();
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("invalid blue color", e.getMessage());
    }
    try {
      test = new Rect.BuildRect().setBlue(256).build();
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("invalid blue color", e.getMessage());
    }
    try {
      test = new Ellipse.BuildEllipse().setBlue(257).build();
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("invalid blue color", e.getMessage());
    }
    try {
      test = new Ellipse.BuildEllipse().setGreen(-2).build();
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("invalid green color", e.getMessage());
    }
    try {
      test = new Rect.BuildRect().setGreen(-3).build();
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("invalid green color", e.getMessage());
    }
    try {
      test = new Rect.BuildRect().setGreen(256).build();
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("invalid green color", e.getMessage());
    }
    try {
      test = new Ellipse.BuildEllipse().setGreen(257).build();
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("invalid green color", e.getMessage());
    }
  }

  @Test
  public void invalidNullArgToBuilder() {
    IShape test;
    try {
      test = new Ellipse.BuildEllipse().setName(null).build();
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("cannot set a null shape name", e.getMessage());
    }
    try {
      test = new Rect.BuildRect().setName(null).build();
      fail("no error thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("cannot set a null shape name", e.getMessage());
    }
  }


  //Integration Testing
  @Test
  public void getValuesFromEllipse() {
    IShape ellipse = new Ellipse.BuildEllipse().build();
    assertForGetters(ellipse, "", "ellipse", 0, 0, 0, 0, 0, 0,
            0);

    ellipse = new Ellipse.BuildEllipse().setName("O").build();

    assertForGetters(ellipse, "O", "ellipse", 0, 0, 0, 0, 0,
            0, 0);
  }

  //Integration Testing
  @Test
  public void getValuesFromRect() {
    IShape rect = new Rect.BuildRect().build();
    assertForGetters(rect, "", "rectangle", 0, 0, 0, 0, 0,
            0, 0);

    rect = new Rect.BuildRect().setName("r").build();
    assertForGetters(rect, "r", "rectangle", 0, 0, 0, 0, 0,
            0, 0);

    assertForGetters(this.rect, "hi", "rectangle", 10, 100, 5, 3,
            255, 0, 0);
    assertForGetters(this.rect2, "", "rectangle", 0, 0, 0, 0, 0,
            0, 0);
    assertForGetters(this.ellipse, "sue", "ellipse", 10, 10, 20, 22,
            30, 3, 100);
    assertForGetters(this.ellipse2, "", "ellipse", 0, 0, 0, 0, 0,
            0, 0);
  }

  /**
   * Helper method asserting that the provided shape matches the expected data.
   *
   * @param shape  the expected shape.
   * @param name   the expected name.
   * @param code   the expected code.
   * @param width  the expected width.
   * @param height the expected height.
   * @param x      the expected x.
   * @param y      the expected y.
   * @param red    the expected red code.
   * @param green  the expected green code.
   * @param blue   the expected blue code.
   */
  public void assertForGetters(IShape shape,
                               String name, String code, int width, int height, int x,
                               int y, int red, int green, int blue) {
    Assert.assertEquals(name, shape.getName());
    Assert.assertEquals(code, shape.getCode());
    Assert.assertEquals(height, shape.getHeight());
    Assert.assertEquals(width, shape.getWidth());
    assertEquals(x, shape.getX());
    assertEquals(y, shape.getY());
    assertEquals(red, shape.getRed());
    assertEquals(green, shape.getGreen());
    assertEquals(blue, shape.getBlue());
  }
}