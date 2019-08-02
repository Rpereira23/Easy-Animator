package cs3500.animator.util;

import cs3500.animator.model.ExcellenceModel;
import cs3500.animator.model.ExcellenceModelImpl;
import cs3500.animator.util.Interfaces.IAnimationReader;

/**
 * Strategic parser of input file to obtain animation whether it has rotation, layer, or neither.
 */
public class StrategyAnimationReader implements IAnimationReader<ExcellenceModel> {
    private IAnimationReader<ExcellenceModel> first;
    private IAnimationReader<ExcellenceModel> second;
    private ArgParser argParser;


    public StrategyAnimationReader(IAnimationReader<ExcellenceModel> first,
                                   IAnimationReader<ExcellenceModel> second,
                                   ArgParser argsParser
    ) {
        this.first = first;
        this.second = second;
        this.argParser = argsParser;

    }

    @Override
    public ExcellenceModel parseFile(Readable readable,
                                     AnimationBuilder<ExcellenceModel> builder) {
        ExcellenceModel model;
        try {
            model = this.first.parseFile(readable, builder);
        } catch (IllegalStateException ise) {
            model = this.second.parseFile(
                    this.argParser.getNewIn(),
                    new ExcellenceModelImpl.Builder());
        }
        return model;
    }
}
