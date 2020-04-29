package lecho.lib.hellocharts.animation;

import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.Chart;

public class ChartViewportAnimatorV8 implements ChartViewportAnimator {
    /* access modifiers changed from: private */
    public ChartAnimationListener animationListener = new DummyChartAnimationListener();
    final Chart chart;
    /* access modifiers changed from: private */
    public long duration;
    final Handler handler;
    final Interpolator interpolator = new AccelerateDecelerateInterpolator();
    boolean isAnimationStarted = false;
    /* access modifiers changed from: private */
    public Viewport newViewport = new Viewport();
    /* access modifiers changed from: private */
    public final Runnable runnable = new Runnable() {
        /* class lecho.lib.hellocharts.animation.ChartViewportAnimatorV8.AnonymousClass1 */

        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: ClspMth{java.lang.Math.min(float, float):float}
         arg types: [float, int]
         candidates:
          ClspMth{java.lang.Math.min(double, double):double}
          ClspMth{java.lang.Math.min(long, long):long}
          ClspMth{java.lang.Math.min(int, int):int}
          ClspMth{java.lang.Math.min(float, float):float} */
        public void run() {
            long elapsed = SystemClock.uptimeMillis() - ChartViewportAnimatorV8.this.start;
            if (elapsed > ChartViewportAnimatorV8.this.duration) {
                ChartViewportAnimatorV8.this.isAnimationStarted = false;
                ChartViewportAnimatorV8.this.handler.removeCallbacks(ChartViewportAnimatorV8.this.runnable);
                ChartViewportAnimatorV8.this.chart.setCurrentViewport(ChartViewportAnimatorV8.this.targetViewport);
                ChartViewportAnimatorV8.this.animationListener.onAnimationFinished();
                return;
            }
            float scale = Math.min(ChartViewportAnimatorV8.this.interpolator.getInterpolation(((float) elapsed) / ((float) ChartViewportAnimatorV8.this.duration)), 1.0f);
            ChartViewportAnimatorV8.this.newViewport.set(ChartViewportAnimatorV8.this.startViewport.left + ((ChartViewportAnimatorV8.this.targetViewport.left - ChartViewportAnimatorV8.this.startViewport.left) * scale), ChartViewportAnimatorV8.this.startViewport.top + ((ChartViewportAnimatorV8.this.targetViewport.top - ChartViewportAnimatorV8.this.startViewport.top) * scale), ChartViewportAnimatorV8.this.startViewport.right + ((ChartViewportAnimatorV8.this.targetViewport.right - ChartViewportAnimatorV8.this.startViewport.right) * scale), ChartViewportAnimatorV8.this.startViewport.bottom + ((ChartViewportAnimatorV8.this.targetViewport.bottom - ChartViewportAnimatorV8.this.startViewport.bottom) * scale));
            ChartViewportAnimatorV8.this.chart.setCurrentViewport(ChartViewportAnimatorV8.this.newViewport);
            ChartViewportAnimatorV8.this.handler.postDelayed(this, 16);
        }
    };
    long start;
    /* access modifiers changed from: private */
    public Viewport startViewport = new Viewport();
    /* access modifiers changed from: private */
    public Viewport targetViewport = new Viewport();

    public ChartViewportAnimatorV8(Chart chart2) {
        this.chart = chart2;
        this.duration = 300;
        this.handler = new Handler();
    }

    public void startAnimation(Viewport startViewport2, Viewport targetViewport2) {
        this.startViewport.set(startViewport2);
        this.targetViewport.set(targetViewport2);
        this.duration = 300;
        this.isAnimationStarted = true;
        this.animationListener.onAnimationStarted();
        this.start = SystemClock.uptimeMillis();
        this.handler.post(this.runnable);
    }

    public void startAnimation(Viewport startViewport2, Viewport targetViewport2, long duration2) {
        this.startViewport.set(startViewport2);
        this.targetViewport.set(targetViewport2);
        this.duration = duration2;
        this.isAnimationStarted = true;
        this.animationListener.onAnimationStarted();
        this.start = SystemClock.uptimeMillis();
        this.handler.post(this.runnable);
    }

    public void cancelAnimation() {
        this.isAnimationStarted = false;
        this.handler.removeCallbacks(this.runnable);
        this.chart.setCurrentViewport(this.targetViewport);
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
