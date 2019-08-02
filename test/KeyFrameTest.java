import org.junit.Before;
import org.junit.Test;
import cs3500.animator.model.KeyFrame;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class KeyFrameTest {
  KeyFrame k1;
  KeyFrame k2;
  KeyFrame k3;
  KeyFrame k4;
  KeyFrame k5;

  @Before
  public void setUp() throws Exception {
    k1 = new KeyFrame.BuildKeyFrame().build();
    k2 = new KeyFrame.BuildKeyFrame().setTime(0).setHeight(5).setWidth(5).build();
    k3 = new KeyFrame.BuildKeyFrame().setTime(10).setHeight(10).setWidth(10).setX(10).setY(10)
            .build();
    k4 = new KeyFrame.BuildKeyFrame().setTime(20).setHeight(3).setWidth(7).setX(8).setY(4)
            .setRed(255).setGreen(7).setBlue(8).build();
    k5 = new KeyFrame.BuildKeyFrame().setTime(99).setHeight(9).setWidth(3).setX(-4).setY(-10)
            .setRed(3).setGreen(88).setBlue(9).build();
  }

  @Test
  public void getDescription() {
    assertEquals("0 0 0 0 0 0 0 0" ,k1.getDescription());
    assertEquals("0 0 0 5 5 0 0 0" ,k2.getDescription());
    assertEquals("10 10 10 10 10 0 0 0" ,k3.getDescription());
    assertEquals("20 8 4 7 3 255 7 8" ,k4.getDescription());
    assertEquals("99 -4 -10 3 9 3 88 9" ,k5.getDescription());
  }

  @Test
  public void equalsComplete() {
    assertFalse(k1.equalsComplete(k2));
    assertFalse(k2.equalsComplete(k3));
    assertFalse(k4.equalsComplete(k5));
    assertTrue(k3.equalsComplete(k3));
    KeyFrame k6 = new KeyFrame.BuildKeyFrame().setTime(10).setHeight(10).setWidth(10).setX(10)
            .setY(10).build();
    assertTrue(k3.equalsComplete(k6));
  }

  @Test
  public void testIncorrectConstruction() {
    KeyFrame error;
    try {
      error = new KeyFrame.BuildKeyFrame().setWidth(-5).build();
    } catch (IllegalArgumentException e) {
      assertEquals("cannot have a negative width", e.getMessage());
    }
    try {
      error = new KeyFrame.BuildKeyFrame().setHeight(-5).build();
    } catch (IllegalArgumentException e) {
      assertEquals("cannot have a negative height", e.getMessage());
    }
    try {
      error = new KeyFrame.BuildKeyFrame().setTime(-5).build();
    } catch (IllegalArgumentException e) {
      assertEquals("cannot have a negative time", e.getMessage());
    }
    try {
      error = new KeyFrame.BuildKeyFrame().setRed(-5).build();
    } catch (IllegalArgumentException e) {
      assertEquals("red color out of range", e.getMessage());
    }
    try {
      error = new KeyFrame.BuildKeyFrame().setRed(300).build();
    } catch (IllegalArgumentException e) {
      assertEquals("red color out of range", e.getMessage());
    }
    try {
      error = new KeyFrame.BuildKeyFrame().setGreen(-5).build();
    } catch (IllegalArgumentException e) {
      assertEquals("green color out of range", e.getMessage());
    }
    try {
      error = new KeyFrame.BuildKeyFrame().setGreen(300).build();
    } catch (IllegalArgumentException e) {
      assertEquals("green color out of range", e.getMessage());
    }
    try {
      error = new KeyFrame.BuildKeyFrame().setBlue(-5).build();
    } catch (IllegalArgumentException e) {
      assertEquals("blue color out of range", e.getMessage());
    }
    try {
      error = new KeyFrame.BuildKeyFrame().setBlue(300).build();
    } catch (IllegalArgumentException e) {
      assertEquals("blue color out of range", e.getMessage());
    }
  }

  @Test
  public void getTime() {
    assertEquals(0 ,k1.getTime());
    assertEquals(0 ,k2.getTime());
    assertEquals(10 ,k3.getTime());
    assertEquals(20 ,k4.getTime());
    assertEquals(99 ,k5.getTime());
  }

  @Test
  public void getWidth() {
    assertEquals(0 ,k1.getWidth());
    assertEquals(5 ,k2.getWidth());
    assertEquals(10 ,k3.getWidth());
    assertEquals(7 ,k4.getWidth());
    assertEquals(3 ,k5.getWidth());
  }

  @Test
  public void getHeight() {
    assertEquals(0 ,k1.getHeight());
    assertEquals(5 ,k2.getHeight());
    assertEquals(10 ,k3.getHeight());
    assertEquals(3 ,k4.getHeight());
    assertEquals(9 ,k5.getHeight());
  }

  @Test
  public void getX() {
    assertEquals(0 ,k1.getX());
    assertEquals(0 ,k2.getX());
    assertEquals(10 ,k3.getX());
    assertEquals(8 ,k4.getX());
    assertEquals(-4 ,k5.getX());
  }

  @Test
  public void getY() {
    assertEquals(0 ,k1.getY());
    assertEquals(0 ,k2.getY());
    assertEquals(10 ,k3.getY());
    assertEquals(4 ,k4.getY());
    assertEquals(-10 ,k5.getY());
  }

  @Test
  public void getRed() {
    assertEquals(0 ,k1.getRed());
    assertEquals(0 ,k2.getRed());
    assertEquals(0 ,k3.getRed());
    assertEquals(255 ,k4.getRed());
    assertEquals(3 ,k5.getRed());
  }

  @Test
  public void getGreen() {
    assertEquals(0 ,k1.getGreen());
    assertEquals(0 ,k2.getGreen());
    assertEquals(0 ,k3.getGreen());
    assertEquals(7 ,k4.getGreen());
    assertEquals(88 ,k5.getGreen());
  }

  @Test
  public void getBlue() {
    assertEquals(0 ,k1.getBlue());
    assertEquals(0 ,k2.getBlue());
    assertEquals(0 ,k3.getBlue());
    assertEquals(8 ,k4.getBlue());
    assertEquals(9 ,k5.getBlue());
  }
}