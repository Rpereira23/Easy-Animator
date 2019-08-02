package cs3500.animator.util.Interfaces;

import cs3500.animator.util.AnimationBuilder;

import java.util.Scanner;

public interface ReadShapes<Doc> {

    void readShape(Scanner s, AnimationBuilder<Doc> builder);
}
