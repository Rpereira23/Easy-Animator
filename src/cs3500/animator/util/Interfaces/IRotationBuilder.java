package cs3500.animator.util.Interfaces;


import cs3500.animator.util.AnimationBuilder;

public interface IRotationBuilder<Doc>  {

    AnimationBuilder<Doc> addMotion(String name,
                                    int t1, int x1, int y1, int w1, int h1, int r1, int g1, int b1, int a1,
                                    int t2, int x2, int y2, int w2, int h2, int r2, int g2, int b2, int a2);


    AnimationBuilder<Doc> addKeyFrame(String name, int t, int x, int y,
                                      int w, int h, int r, int g, int b, int a);
}
