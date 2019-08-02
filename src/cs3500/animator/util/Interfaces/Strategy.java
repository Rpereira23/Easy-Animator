package cs3500.animator.util.Interfaces;

import cs3500.animator.util.AnimationBuilder;

public interface Strategy<Doc> {

    Doc parseFile(Readable readable, AnimationBuilder<Doc> builder);
}
