package cs3500.animator.view;

import org.junit.Before;

import java.io.FileReader;
import java.io.IOException;

import cs3500.animator.model.ExcellenceModel;
import cs3500.animator.model.ExcellenceModelImpl;
import cs3500.animator.model.KeyFrame;
import cs3500.animator.model.ViewModel;
import cs3500.animator.util.AnimationBuilder;
import cs3500.animator.util.AnimationReader;

/**
 * An Abstract class that provides starter code for view tests.
 */
public abstract class AViewTest {
  //Fields.
  ViewModel smallDemoModel;
  ViewModel bigBangModel;
  ViewModel buildingsModel;
  ViewModel simpleModel;
  KeyFrame k1;
  KeyFrame k2;
  KeyFrame k3;
  KeyFrame k4;


  //To Read Files
  protected  ExcellenceModel readFile(String filePath) throws IOException {
    Readable r = new FileReader(filePath);
    AnimationBuilder<ExcellenceModel> builder = new ExcellenceModelImpl.Builder();
    AnimationReader<ExcellenceModel> reader = new AnimationReader();
    return reader.parseFile(r, builder);
  }

  //The setup.
  @Before
  public void setUp() {
    try {
      this.smallDemoModel = new ViewModel(readFile("smalldemo.txt"));
      this.bigBangModel = new ViewModel(readFile("big-bang-big-crunch.txt"));
      this.buildingsModel = new ViewModel(readFile("buildings.txt"));

    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    AnimationBuilder<ExcellenceModel> builder = new ExcellenceModelImpl.Builder();
    builder.setBounds(200,200, 500, 500);
    builder.declareShape("R0" ,"rectangle");
    builder.declareShape("E0" ,"ellipse");
    //Changing Everything motion
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

    this.simpleModel = new ViewModel(builder.build());
  }


}
