package cs3500.animator.util;

import java.util.Scanner;

/**
 * Utility class for accessing static method commonly used.
 */
public class Util {

    /**
     * Gets the next integer value in the scanner or throws an error.
     * @param s the scanner.
     * @param label the label this under..
     * @param fieldName the field name is under..
     * @return the integer
     * @throws IllegalStateException if the scanner's next value is not an integer.
     */
    static int getInt(Scanner s, String label, String fieldName) {
        if (s.hasNextInt()) {
            return s.nextInt();
        } else if (s.hasNext()) {
            throw new IllegalStateException(
                    String.format("%s: expected integer for %s, got: %s", label, fieldName, s.next()));
        } else {
            throw new IllegalStateException(
                    String.format("%s: expected integer for %s, but no more input available",
                            label, fieldName));
        }
    }
}
