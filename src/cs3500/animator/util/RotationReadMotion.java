package cs3500.animator.util;

import cs3500.animator.util.Interfaces.ReadMotion;

import java.util.Scanner;

/**
 * Parser for reading in motions that contain a rotation element.
 * @param <Doc> the model type to be created
 */
public class RotationReadMotion<Doc> implements ReadMotion<Doc> {

    @Override
    public void readMotion(Scanner s, AnimationBuilder<Doc> builder) {
        String[] fieldNames = new String[] {
                "initial time",
                "initial x-coordinate", "initial y-coordinate",
                "initial width", "initial height",
                "initial red value", "initial green value", "initial blue value",
                "initial angle",
                "final time",
                "final x-coordinate", "final y-coordinate",
                "final width", "final height",
                "final red value", "final green value", "final blue value",
                "final angle"
        };
        int[] vals = new int[18];
        String name;
        if (s.hasNext()) {
            name = s.next();
        } else {
            s.close();
            throw new IllegalStateException("Motion: Expected a shape name, but no more input available");
        }
        for (int i = 0; i < 18; i++) {
            vals[i] = Util.getInt(s, "Motion", fieldNames[i]);
        }
        builder.addMotion(name,
                vals[0], vals[1], vals[2 ], vals[3 ], vals[4 ], vals[5 ], vals[6 ], vals[7 ], vals[8 ],
                vals[9], vals[10], vals[11], vals[12], vals[13], vals[14], vals[15], vals[16 ], vals[ 17]);

    }
}
