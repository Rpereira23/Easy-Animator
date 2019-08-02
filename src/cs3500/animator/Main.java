package cs3500.animator;

import cs3500.animator.controller.Controller;
import cs3500.animator.controller.IController;
import cs3500.animator.model.ExcellenceModel;
import cs3500.animator.model.ExcellenceModelImpl;
import cs3500.animator.model.ViewModel;
import cs3500.animator.provider.adapters.AdapterController;
import cs3500.animator.provider.view.AnimatorEditorView;
import cs3500.animator.util.AnimationBuilder;
import cs3500.animator.util.AnimationReader;
import cs3500.animator.util.ArgParser;
import cs3500.animator.util.OverlayingReadShape;
import cs3500.animator.util.RotationReadMotion;
import cs3500.animator.util.StrategyAnimationReader;
import cs3500.animator.util.StrategyBuilder;
import cs3500.animator.view.EditorView;
import cs3500.animator.view.IView;
import cs3500.animator.view.SVGView;
import cs3500.animator.view.TextView;
import cs3500.animator.view.VisualFrameView;

/**
 * Main class for running ExCellence Animator.
 */
public class Main {

  /**
   * Method for running ExCellence Animator.
   * @param args arguments to configure the Animator
   */
  public static void main(String[] args) {
    //Allow a parser to interpret the arguments
    ArgParser init = new ArgParser.Parser(args).parse();

    //Construct a model through the AnimationBuilder and Animation Reader
    ExcellenceModel model;
    AnimationBuilder<ExcellenceModel> builder = new ExcellenceModelImpl.Builder();

    //Set up of various Readers to parse input file
    AnimationReader<ExcellenceModel> rotationAndOverlayReader =
            new StrategyBuilder<ExcellenceModel>()
                    .setToMotions(new RotationReadMotion<>())
                    .setShapes(new OverlayingReadShape<>())
                    .build();

    AnimationReader<ExcellenceModel> rotationReader =
            new StrategyBuilder<ExcellenceModel>()
                    .setToMotions(new RotationReadMotion<>())
                    .build();

    AnimationReader<ExcellenceModel> overlayReader =
            new StrategyBuilder<ExcellenceModel>()
                    .setShapes(new OverlayingReadShape<>())
                    .build();

    AnimationReader<ExcellenceModel> defaultReader =
            new StrategyBuilder<ExcellenceModel>()
                    .build();

    //Construct strategy to attempt to parse file various ways
    StrategyAnimationReader reader
            = new StrategyAnimationReader(
                    new StrategyAnimationReader(rotationAndOverlayReader, rotationReader, init),
                     new StrategyAnimationReader(overlayReader, defaultReader, init), init);

    //Retrieve input, output, and ViewType parameters from the parser
    Readable r = init.getIn();
    Appendable a = init.getOut();
    ArgParser.ViewOptions viewType = init.getViewType();

    //Execute the parsing through our strategy reader
    model = reader.parseFile(r, builder);

    //Construct a ViewModel that is immutable and may be passed into the view.
    ViewModel vm = new ViewModel(model);

    //Properly construct the view according to designated configuration
    IView view;
    switch (viewType) {
      case VISUAL:
        view = new VisualFrameView(vm, init.getSpeed());
        view.render();
        break;
      case TEXT:
        view = new TextView(vm, a);
        view.render();
        break;
      case SVG:
        view = new SVGView(vm, a, init.getSpeed());
        view.render();
        break;
      case EDIT:
        view = new EditorView(vm);
        IController controller = new Controller((EditorView) view, model);
        view.render();
        break;
      case PROVIDER:
        AdapterController adapterController =
                new AdapterController(model, new AnimatorEditorView());
        adapterController.start();
        break;
      default:
        throw new IllegalArgumentException("Unknown View");
    }
  }
}
