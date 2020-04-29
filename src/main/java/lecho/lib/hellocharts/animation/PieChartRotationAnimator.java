package lecho.lib.hellocharts.animation;

public interface PieChartRotationAnimator {
    public static final int FAST_ANIMATION_DURATION = 200;

    void cancelAnimation();

    boolean isAnimationStarted();

    void setChartAnimationListener(ChartAnimationListener chartAnimationListener);

    void startAnimation(float f, float f2);
}
