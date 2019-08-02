package cs3500.animator.util;

import cs3500.animator.util.Interfaces.*;

import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Reader for parsing text input and creating a model for animations.
 * This reader does not parse information for rotations or layering of shapes.
 * @param <Doc> the model type
 */
public class AnimationReader<Doc> implements IAnimationReader<Doc> {
    private final ReadCanvas<Doc> toCanvas;
    private final ReadMotion<Doc> toMotions;
    private final ReadShapes<Doc> toShapes;

    /**
     * Construct a default animation reader for reading in animations without overlay or rotation.
     */
    public AnimationReader() {
        this.toCanvas = new DefaultReadCanvas<>();
        this.toMotions = new DefaultReadMotion<>();
        this.toShapes = new DefaultReadShapes<>();
    }

    /**
     * Constructs a custom animation reader with specific readers for parts of the animation
     * document construction.
     * @param readMotion reader to parse motions
     * @param readCanvas reader to parse canvas info
     * @param readShapes reader to parse shapes
     */
    public AnimationReader(ReadMotion<Doc> readMotion,
                            ReadCanvas<Doc> readCanvas,
                            ReadShapes<Doc> readShapes) {
        this.toCanvas = readCanvas;
        this.toMotions = readMotion;
        this.toShapes = readShapes;
    }


    @Override
    public Doc parseFile(Readable readable, AnimationBuilder<Doc> builder) {
        Objects.requireNonNull(readable, "Must have non-null readable source");
        Objects.requireNonNull(builder, "Must provide a non-null AnimationBuilder");
        Scanner s = new Scanner(readable);
        // Split at whitespace, and ignore # comment lines
        s.useDelimiter(Pattern.compile("(\\p{Space}+|#.*)+"));
        while (s.hasNext()) {
            String word = s.next();
            switch (word) {
                case "canvas":
                    this.toCanvas.readCanvas(s, builder);
                    break;
                case "shape":
                    this.toShapes.readShape(s, builder);
                    break;
                case "motion":
                    this.toMotions.readMotion(s, builder);
                    break;
                default:
                    throw new IllegalStateException("Unexpected keyword: " + word + s.nextLine());
            }
        }
        return builder.build();
    }
}
