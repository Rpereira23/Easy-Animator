package cs3500.animator.util;

import cs3500.animator.util.Interfaces.ReadCanvas;
import java.util.Scanner;
import static cs3500.animator.util.Util.getInt;


/**
 * Parser for reading in Canvas dimensions and characteristics to form animation.
 * @param <Doc> model type
 */
public class DefaultReadCanvas<Doc> implements ReadCanvas<Doc> {

    @Override
    public void readCanvas(Scanner s, AnimationBuilder<Doc> builder) {
        int[] vals = new int[4];
        String[] fieldNames = {"left", "top", "width", "height"};
        for (int i = 0; i < 4; i++) {
            vals[i] = getInt(s, "Canvas", fieldNames[i]);
        }
        builder.setBounds(vals[0], vals[1], vals[2], vals[3]);
    }
}
