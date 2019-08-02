package cs3500.animator.util.Interfaces;

import cs3500.animator.util.AnimationBuilder;
import java.util.Scanner;

public interface ReadCanvas<Doc> {

    void readCanvas(Scanner s, AnimationBuilder<Doc> builder);
}
