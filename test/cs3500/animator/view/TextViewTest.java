package cs3500.animator.view;

import org.junit.Assert;
import org.junit.Test;

public class TextViewTest extends AViewTest {

  @Test
  public void integrationTestSimpleModel() {
    Appendable appendable = new StringBuilder();
    TextView textOne = new TextView(this.simpleModel,
            appendable);
    textOne.render();
    //General Output is correct:
    Assert.assertTrue(appendable.toString().contains("rect"));
    //Only One RECT
    Assert.assertEquals(2, appendable.toString().split("rect")
            .length);

    //Checking Ellipse
    Assert.assertTrue(appendable.toString().contains("ellipse"));
    //Only One Ellipse
    Assert.assertEquals(2, appendable.toString().split("ellipse").length);

    //11 Total Animations -> 5 for Rect, 5 Ellipse, 1 for the header.
    Assert.assertEquals(5, appendable.toString().split("\n")
            .length);

    Assert.assertEquals("canvas 200 200 500 500\n" +
            "shape R0 rectangle\n" +
            "motion R0 1 5 5 50 50 155 155 155 100 300 300 26 38 100 200 255\n" +
            "shape E0 ellipse\n" +
            "motion E0 1 40 40 50 50 155 155 155 200 40 40 50 50 155 155 155",
            appendable.toString());
  }

  @Test
  public void integrationTestBigBang() {
    Appendable appendable = new StringBuilder();
    IView textOne = new TextView(this.bigBangModel,
            appendable);
    textOne.render();
    //General Output is correct:
    //Zero Rects
    Assert.assertTrue(appendable.toString().split("rect")
            .length == 1);

    //Checking Ellipse
    Assert.assertTrue(appendable.toString().contains("ellipse"));
    Assert.assertTrue(appendable.toString().contains("ellipse"));
    //Only One Ellipse
    Assert.assertTrue(appendable.toString().split("ellipse")
            .length == 5001);

    //11 Total Animations -> 5 for Rect, 5 Ellipse, 1 for the header.
    Assert.assertEquals(29906, appendable.toString().split("\n")
            .length);
  }

  @Test
  public void integrationTestForAnimationValues() {
    Appendable appendable = new StringBuilder();
    IView textOne = new TextView(this.buildingsModel,
            appendable);
    textOne.render();

    Assert.assertEquals(68, appendable.toString().split("rect")
            .length);

    //Checking Ellipse
    Assert.assertTrue(appendable.toString().contains("ellipse"));
    Assert.assertTrue(appendable.toString().contains("ellipse"));
    //Only One Ellipse
    Assert.assertEquals(43, appendable.toString().split("ellipse")
            .length);

    //11 Total Animations -> 5 for Rect, 5 Ellipse, 1 for the header.
    Assert.assertEquals( 387, appendable.toString().split("\n")
            .length);
  }
}
