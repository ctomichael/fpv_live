package lecho.lib.hellocharts.animation;

import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import lecho.lib.hellocharts.view.PieChartView;

public class PieChartRotationAnimatorV8 implements PieChartRotationAnimator {
    /* access modifiers changed from: private */
    public ChartAnimationListener animationListener;
    final PieChartView chart;
    final long duration;
    final Handler handler;
    final Interpolator interpolator;
    boolean isAnimationStarted;
    /* access modifiers changed from: private */
    public final Runnable runnable;
    long start;
    /* access modifiers changed from: private */
    public float startRotation;
    /* access modifiers changed from: private */
    public float targetRotation;

    public PieChartRotationAnimatorV8(PieChartView chart2) {
        this(chart2, 200);
    }

    public PieChartRotationAnimatorV8(PieChartView chart2, long duration2) {
        this.interpolator = new AccelerateDecelerateInterpolator();
        this.isAnimationStarted = false;
        this.startRotation = 0.0f;
        this.targetRotation = 0.0f;
        this.animationListener = new DummyChartAnimationListener();
        this.runnable = new Runnable() {
            /* class lecho.lib.hellocharts.animation.PieChartRotationAnimatorV8.AnonymousClass1 */

            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
             method: ClspMth{java.lang.Math.min(float, float):float}
             arg types: [float, int]
             candidates:
              ClspMth{java.lang.Math.min(double, double):double}
              ClspMth{java.lang.Math.min(long, long):long}
              ClspMth{java.lang.Math.min(int, int):int}
              ClspMth{java.lang.Math.min(float, float):float} */
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - PieChartRotationAnimatorV8.this.start;
                if (elapsed > PieChartRotationAnimatorV8.this.duration) {
                    PieChartRotationAnimatorV8.this.isAnimationStarted = false;
                    PieChartRotationAnimatorV8.this.handler.removeCallbacks(PieChartRotationAnimatorV8.this.runnable);
                    PieChartRotationAnimatorV8.this.chart.setChartRotation((int) PieChartRotationAnimatorV8.this.targetRotation, false);
                    PieChartRotationAnimatorV8.this.animationListener.onAnimationFinished();
                    return;
                }
                PieChartRotationAnimatorV8.this.chart.setChartRotation((int) ((((PieChartRotationAnimatorV8.this.startRotation + ((PieChartRotationAnimatorV8.this.targetRotation - PieChartRotationAnimatorV8.this.startRotation) * Math.min(PieChartRotationAnimatorV8.this.interpolator.getInterpolation(((float) elapsed) / ((float) PieChartRotationAnimatorV8.this.duration)), 1.0f))) % 360.0f) + 360.0f) % 360.0f), false);
                PieChartRotationAnimatorV8.this.handler.postDelayed(this, 16);
            }
        };
        this.chart = chart2;
        this.duration = duration2;
        this.handler = new Handler();
    }

    public void startAnimation(float startRotation2, float targetRotation2) {
        this.startRotation = ((startRotation2 % 360.0f) + 360.0f) % 360.0f;
        this.targetRotation = ((targetRotation2 % 360.0f) + 360.0f) % 360.0f;
        this.isAnimationStarted = true;
        this.animationListener.onAnimationStarted();
        this.start = SystemClock.uptimeMillis();
        this.handler.post(this.runnable);
    }

    public void cancelAnimation() {
        this.isAnimationStarted = false;
        this.handler.removeCallbacks(this.runnable);
        this.chart.setChartRotation((int) this.targetRotation, false);
        this.animationListener.onAnimationFinished();
    }

    public boolean isAnimationStarted() {
        return this.isAnimationStarted;
    }

    public void setChartAnimationListener(ChartAnimationListener animationListener2) {
        if (animationListener2 == null) {
            this.animationListener = new DummyChartAnimationListener();
        } else {
            this.animationListener = animationListener2;
        }
    }
}
