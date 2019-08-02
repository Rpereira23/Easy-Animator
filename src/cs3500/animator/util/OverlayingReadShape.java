package cs3500.animator.util;

import cs3500.animator.util.Interfaces.ReadShapes;

import java.util.Scanner;

/**
 * Parser for reading in shapes with a notion of layering.
 * @param <Doc> the type of model to be created
 */
public class OverlayingReadShape<Doc> implements ReadShapes<Doc> {

    @Override
    public void readShape(Scanner s, AnimationBuilder<Doc> builder) {
        String name;
        String type;
        int layer;
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
        if (s.hasNext()) {
            layer = Util.getInt(s, "Overlay", "Overlay");
        } else {
            throw new IllegalStateException("Shape: Expected a layer, but no more input available");
        }
        builder.declareShape(name, type, layer);
    }
}
