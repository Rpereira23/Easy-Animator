package cs3500.animator.util.Interfaces;

import cs3500.animator.util.AnimationBuilder;

import java.util.Scanner;

public interface ReadMotion<Doc> {

    void readMotion(Scanner s, AnimationBuilder<Doc> builder);


}
