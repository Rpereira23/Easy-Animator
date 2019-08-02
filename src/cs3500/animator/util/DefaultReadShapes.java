package cs3500.animator.util;

import cs3500.animator.util.Interfaces.ReadShapes;

import java.util.Scanner;

/**
 * Parser for default shapes with no notion of layering.
 * @param <Doc> the model type to be created
 */
public class DefaultReadShapes<Doc> implements ReadShapes<Doc> {

    @Override
    public void readShape(Scanner s, AnimationBuilder<Doc> builder) {
        String name;
        String type;
        if (s.hasNext()) {
            name = s.next();
        } else {
            throw new IllegalStateException("Shape: Expected a name, but no more input available");
        }
        if (s.hasNext()) {
            type = s.next();
        } else {
            throw new IllegalStateException("Shape: Expected a type, but no more input available");
        }
        builder.declareShape(name, type);
    }
}
