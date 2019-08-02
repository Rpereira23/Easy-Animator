package cs3500.animator.util;

import cs3500.animator.util.Interfaces.ReadCanvas;
import cs3500.animator.util.Interfaces.ReadMotion;
import cs3500.animator.util.Interfaces.ReadShapes;

/**
 * Provide construction of method to parse input file.
 * @param <Doc> the type of model to be created
 */
public class StrategyBuilder<Doc> {
    private ReadMotion<Doc> toMotions;
    private ReadCanvas<Doc> toCanvas;
    private ReadShapes<Doc> toShapes;

    public StrategyBuilder() {
        this.toMotions = new DefaultReadMotion<Doc>();
        this.toCanvas = new DefaultReadCanvas<Doc>();
        this.toShapes = new DefaultReadShapes<Doc>();
    }

    public StrategyBuilder<Doc> setToMotions(ReadMotion<Doc> toMotions) {
        this.toMotions = toMotions;
        return this;
    }

    public StrategyBuilder<Doc> setShapes(ReadShapes<Doc> toShapes) {
        this.toShapes = toShapes;
        return this;
    }

    public StrategyBuilder<Doc> setCanvas(ReadCanvas<Doc> toCanvas) {
        this.toCanvas = toCanvas;
        return this;
    }

    public AnimationReader<Doc> build() {
        return new AnimationReader<>(this.toMotions, this.toCanvas, this.toShapes);
    }
}

