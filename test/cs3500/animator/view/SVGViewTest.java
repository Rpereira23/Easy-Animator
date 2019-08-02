package cs3500.animator.view;

import org.junit.Assert;
import org.junit.Test;

public class SVGViewTest extends AViewTest {

  @Test
  public void integrationTestSimpleModel() {
    Appendable appendable = new StringBuilder();
    IView svgViewOne = new SVGView(this.simpleModel,
            appendable, 1);
    svgViewOne.render();
    //General Output is correct:
    Assert.assertTrue(appendable.toString().contains("<rect>"));
    Assert.assertTrue(appendable.toString().contains("</rect>"));
    //Only One RECT
    Assert.assertTrue(appendable.toString().split("<rect>")
                    .length == 2);

    //Checking Ellipse
    Assert.assertTrue(appendable.toString().contains("<ellipse>"));
    Assert.assertTrue(appendable.toString().contains("</ellipse>"));
    //Only One Ellipse
    Assert.assertTrue(appendable.toString().split("<ellipse>")
            .length == 2);

    //11 Total Animations -> 5 for Rect, 5 Ellipse, 1 for the header.
    Assert.assertEquals(11, appendable.toString().split("<animate ").length);

    Assert.assertEquals("<svg width=\"500\" height=\"500\" "  + 
            "version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">\n"  + 
            "<rect>\n"  + 
            "<animate attributeType=\"xml\" begin=\"100ms\" "  + 
            "dur=\"9900ms\" attributeName=\"x\" from=\"5\" to=\"300\" fill=\"freeze\" />\n"  + 
            "<animate attributeType=\"xml\" begin=\"100ms\" "  + 
            "dur=\"9900ms\" attributeName=\"y\" from=\"5\" to=\"300\" fill=\"freeze\" />\n"  + 
            "<animate attributeType=\"xml\" begin=\"100ms\" "  + 
            "dur=\"9900ms\" attributeName=\"width\" from=\"50\" to=\"26\" fill=\"freeze\" />\n"  + 
            "<animate attributeType=\"xml\" begin=\"100ms\" "  + 
            "dur=\"9900ms\" attributeName=\"height\" from=\"50\" to=\"38\" fill=\"freeze\" />\n"  + 
            "<animate attributeType=\"xml\" begin=\"100ms\" "  + 
            "dur=\"9900ms\" attributeName=\"fill\" from=\"rgb(155,155,155)\" "  + 
            "to=\"rgb(100,200,255)\" fill=\"freeze\" />\n"  + 
            "</rect>\n"  + 
            "<ellipse>\n"  + 
            "<animate attributeType=\"xml\" begin=\"100ms\" "  + 
            "dur=\"19900ms\" attributeName=\"cx\" from=\"40\" to=\"40\" fill=\"freeze\" />\n"  + 
            "<animate attributeType=\"xml\" begin=\"100ms\" "  + 
            "dur=\"19900ms\" attributeName=\"cy\" from=\"40\" to=\"40\" fill=\"freeze\" />\n"  + 
            "<animate attributeType=\"xml\" begin=\"100ms\" "  + 
            "dur=\"19900ms\" attributeName=\"rx\" from=\"25\" to=\"25\" fill=\"freeze\" />\n"  + 
            "<animate attributeType=\"xml\" begin=\"100ms\" "  + 
            "dur=\"19900ms\" attributeName=\"ry\" from=\"25\" to=\"25\" fill=\"freeze\" />\n"  + 
            "<animate attributeType=\"xml\" begin=\"100ms\" "  + 
            "dur=\"19900ms\" attributeName=\"fill\" "  + 
            "from=\"rgb(155,155,155)\" to=\"rgb(155,155,155)\" fill=\"freeze\" />\n"  + 
            "</ellipse>\n"  + 
            "</svg>", appendable.toString());
  }

  @Test
  public void integrationTestBigBang() {
    Appendable appendable = new StringBuilder();
    IView svgViewOne = new SVGView(this.bigBangModel,
            appendable, 1);
    svgViewOne.render();
    //General Output is correct:
    //Zero Rects
    Assert.assertTrue(appendable.toString().split("<rect>")
            .length == 1);

    //Checking Ellipse
    Assert.assertTrue(appendable.toString().contains("<ellipse>"));
    Assert.assertTrue(appendable.toString().contains("</ellipse>"));
    //Only One Ellipse
    Assert.assertTrue(appendable.toString().split("<ellipse>")
            .length == 5001);

    //11 Total Animations -> 5 for Rect, 5 Ellipse, 1 for the header.
    Assert.assertEquals(124526, appendable.toString().split("<animate ")
            .length);
  }

  @Test
  public void integrationTestForAnimationValues() {
    Appendable appendable = new StringBuilder();
    IView svgViewOne = new SVGView(this.simpleModel,
            appendable, 1);
    svgViewOne.render();

    String[] animations = appendable.toString().split("<animate ");

    //Begin, From, To
    //dur is calculated by (endTime - startTime) / speed
    Assert.assertEquals("dur=\""  +  ((100 - 1) / 1)  +  "00ms\"", animations[1]
            .substring(animations[1].indexOf("dur="),
                    animations[1].indexOf("dur=")  +  12));

    Assert.assertEquals("from=\""  +  this.k1.getX() + "\"", animations[1]
            .substring(animations[1].indexOf("from="),
                    animations[1].indexOf("from=")  +  8));

    Assert.assertEquals("to=\"" +  this.k2.getX() + "\"", animations[1]
            .substring(animations[1].indexOf("to="),
                    animations[1].indexOf("to=")  +  8));

    //Y attribute
    Assert.assertEquals("dur=\""  +  ((100 - 1) / 1)  +  "00ms\"", animations[2]
            .substring(animations[2].indexOf("dur="),
                    animations[2].indexOf("dur=")  +  12));

    Assert.assertEquals("from=\""  +  this.k1.getY() + "\"", animations[2]
            .substring(animations[2].indexOf("from="),
                    animations[2].indexOf("from=")  +  8));

    Assert.assertEquals("to=\""  +  this.k2.getY() + "\"", animations[2]
            .substring(animations[2].indexOf("to="),
                    animations[2].indexOf("to=")  +  8));

    //Width attribute
    Assert.assertEquals("dur=\""  +  ((100 - 1) / 1)  +  "00ms\"", animations[3]
            .substring(animations[3].indexOf("dur="),
                    animations[3].indexOf("dur=")  +  12));

    Assert.assertEquals("from=\""  +  this.k1.getWidth() + "\"", animations[3]
            .substring(animations[3].indexOf("from="),
                    animations[3].indexOf("from=")  +  9));

    Assert.assertEquals("to=\""  +  this.k2.getWidth() + "\"", animations[3]
            .substring(animations[3].indexOf("to="),
                    animations[3].indexOf("to=")  +  7));

    //Height attribute
    Assert.assertEquals("dur=\""  +  ((100 - 1) / 1)  +  "00ms\"", animations[4]
            .substring(animations[4].indexOf("dur="),
                    animations[4].indexOf("dur=")  +  12));

    Assert.assertEquals("from=\""  +  this.k1.getHeight()
            +  "\"", animations[4]
            .substring(animations[4].indexOf("from="),
                    animations[4].indexOf("from=")  +  9));

    Assert.assertEquals("to=\""  +  this.k2.getHeight()  +  "\"", animations[4]
            .substring(animations[4].indexOf("to="),
                    animations[4].indexOf("to=")  +  7));
  }

  @Test
  public void integrationTestForAnimationsFormat() {
    Appendable appendable = new StringBuilder();
    IView svgViewOne = new SVGView(this.simpleModel,
            appendable, 1);
    svgViewOne.render();

    String[] animations = appendable.toString().split("<animate ");

    //5 Attribute names - x, y, w, h, fill
    //attribute x
    Assert.assertEquals("attributeName=\"x\"", animations[1]
            .substring(animations[1].indexOf("attributeName="),
                    animations[1].indexOf("attributeName=")  +  17));

    //attribute y
    Assert.assertEquals("attributeName=\"y\"", animations[2]
            .substring(animations[2].indexOf("attributeName="),
                    animations[2].indexOf("attributeName=")  +  17));

    //attribute width
    Assert.assertEquals("attributeName=\"width\"", animations[3]
            .substring(animations[3].indexOf("attributeName="),
                    animations[3].indexOf("attributeName=")  +  21));

    //attribute height
    Assert.assertEquals("attributeName=\"height\"", animations[4]
            .substring(animations[4].indexOf("attributeName="),
                    animations[4].indexOf("attributeName=")  +  22));

    //attribute fill
    Assert.assertEquals("attributeName=\"fill\"", animations[5]
            .substring(animations[5].indexOf("attributeName="),
                    animations[5].indexOf("attributeName=")  +  20));

    //Testing Ellipse

    //attribute cx
    Assert.assertEquals("attributeName=\"cx\"", animations[6]
            .substring(animations[6].indexOf("attributeName="),
                    animations[6].indexOf("attributeName=")  +  18));

    //attribute cy
    Assert.assertEquals("attributeName=\"cy\"", animations[7]
            .substring(animations[7].indexOf("attributeName="),
                    animations[7].indexOf("attributeName=")  +  18));

    //attribute width
    Assert.assertEquals("attributeName=\"rx\"", animations[8]
            .substring(animations[8].indexOf("attributeName="),
                    animations[8].indexOf("attributeName=")  +  18));

    //attribute height
    Assert.assertEquals("attributeName=\"ry\"", animations[9]
            .substring(animations[9].indexOf("attributeName="),
                    animations[9].indexOf("attributeName=")  +  18));

    //attribute fill
    Assert.assertEquals("attributeName=\"fill\"", animations[10]
            .substring(animations[10].indexOf("attributeName="),
                    animations[10].indexOf("attributeName=")  +  20));

  }
}
