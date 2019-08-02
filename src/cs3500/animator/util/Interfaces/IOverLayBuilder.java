package cs3500.animator.util.Interfaces;

import cs3500.animator.util.AnimationBuilder;

public interface IOverLayBuilder<Doc> {

    AnimationBuilder<Doc> declareShape(String name, String type, int layer);

    AnimationBuilder<Doc> declareLayer(Integer layer);
}
