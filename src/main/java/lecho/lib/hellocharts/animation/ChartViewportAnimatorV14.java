package lecho.lib.hellocharts.animation;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.Chart;

@SuppressLint({"NewApi"})
public class ChartViewportAnimatorV14 implements ChartViewportAnimator, Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
    private ChartAnimationListener animationListener = new DummyChartAnimationListener();
    private ValueAnimator animator;
    private final Chart chart;
    private Viewport newViewport = new Viewport();
    private Viewport startViewport = new Viewport();
    private Viewport targetViewport = new Viewport();

    public ChartViewportAnimatorV14(Chart chart2) {
        this.chart = chart2;
        this.animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.animator.addListener(this);
        this.animator.addUpdateListener(this);
        this.animator.setDuration(300L);
    }

    public void startAnimation(Viewport startViewport2, Viewport targetViewport2) {
        this.startViewport.set(startViewport2);
        this.targetViewport.set(targetViewport2);
        this.animator.setDuration(300L);
        this.animator.start();
    }

    public void startAnimation(Viewport startViewport2, Viewport targetViewport2, long duration) {
        this.startViewport.set(startViewport2);
        this.targetViewport.set(targetViewport2);
        this.animator.setDuration(duration);
        this.animator.start();
    }

    public void cancelAnimation() {
        this.animator.cancel();
    }

    public void onAnimationUpdate(ValueAnimator animation) {
        float scale = animation.getAnimatedFraction();
        this.newViewport.set(this.startViewport.left + ((this.targetViewport.left - this.startViewport.left) * scale), this.startViewport.top + ((this.targetViewport.top - this.startViewport.top) * scale), this.startViewport.right + ((this.targetViewport.right - this.startViewport.right) * scale), this.startViewport.bottom + ((this.targetViewport.bottom - this.startViewport.bottom) * scale));
        this.chart.setCurrentViewport(this.newViewport);
    }

    public void onAnimationCancel(Animator animation) {
    }

    public void onAnimationEnd(Animator animation) {
        this.chart.setCurrentViewport(this.targetViewport);
        this.animationListener.onAnimationFinished();
    }

    public void onAnimationRepeat(Animator animation) {
    }

    public void onAnimationStart(Animator animation) {
        this.animationListener.onAnimationStarted();
    }

    public boolean isAnimationStarted() {
        return this.animator.isStarted();
    }

    public void setChartAnimationListener(ChartAnimationListener animationListener2) {
        if (animationListener2 == null) {
            this.animationListener = new DummyChartAnimationListener();
        } else {
            this.animationListener = animationListener2;
        }
    }
}
