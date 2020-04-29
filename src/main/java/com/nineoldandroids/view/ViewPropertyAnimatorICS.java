package com.nineoldandroids.view;

import android.animation.Animator;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;
import com.nineoldandroids.animation.Animator;
import java.lang.ref.WeakReference;

class ViewPropertyAnimatorICS extends ViewPropertyAnimator {
    private static final long RETURN_WHEN_NULL = -1;
    private final WeakReference<ViewPropertyAnimator> mNative;

    ViewPropertyAnimatorICS(View view) {
        this.mNative = new WeakReference<>(view.animate());
    }

    public ViewPropertyAnimator setDuration(long duration) {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            n.setDuration(duration);
        }
        return this;
    }

    public long getDuration() {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            return n.getDuration();
        }
        return -1;
    }

    public ViewPropertyAnimator setStartDelay(long startDelay) {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            n.setStartDelay(startDelay);
        }
        return this;
    }

    public long getStartDelay() {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            return n.getStartDelay();
        }
        return -1;
    }

    public ViewPropertyAnimator setInterpolator(Interpolator interpolator) {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            n.setInterpolator(interpolator);
        }
        return this;
    }

    public ViewPropertyAnimator setListener(final Animator.AnimatorListener listener) {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            if (listener == null) {
                n.setListener(null);
            } else {
                n.setListener(new Animator.AnimatorListener() {
                    /* class com.nineoldandroids.view.ViewPropertyAnimatorICS.AnonymousClass1 */

                    public void onAnimationStart(android.animation.Animator animation) {
                        listener.onAnimationStart(null);
                    }

                    public void onAnimationRepeat(android.animation.Animator animation) {
                        listener.onAnimationRepeat(null);
                    }

                    public void onAnimationEnd(android.animation.Animator animation) {
                        listener.onAnimationEnd(null);
                    }

                    public void onAnimationCancel(android.animation.Animator animation) {
                        listener.onAnimationCancel(null);
                    }
                });
            }
        }
        return this;
    }

    public void start() {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            n.start();
        }
    }

    public void cancel() {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            n.cancel();
        }
    }

    public ViewPropertyAnimator x(float value) {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            n.x(value);
        }
        return this;
    }

    public ViewPropertyAnimator xBy(float value) {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            n.xBy(value);
        }
        return this;
    }

    public ViewPropertyAnimator y(float value) {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            n.y(value);
        }
        return this;
    }

    public ViewPropertyAnimator yBy(float value) {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            n.yBy(value);
        }
        return this;
    }

    public ViewPropertyAnimator rotation(float value) {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            n.rotation(value);
        }
        return this;
    }

    public ViewPropertyAnimator rotationBy(float value) {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            n.rotationBy(value);
        }
        return this;
    }

    public ViewPropertyAnimator rotationX(float value) {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            n.rotationX(value);
        }
        return this;
    }

    public ViewPropertyAnimator rotationXBy(float value) {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            n.rotationXBy(value);
        }
        return this;
    }

    public ViewPropertyAnimator rotationY(float value) {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            n.rotationY(value);
        }
        return this;
    }

    public ViewPropertyAnimator rotationYBy(float value) {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            n.rotationYBy(value);
        }
        return this;
    }

    public ViewPropertyAnimator translationX(float value) {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            n.translationX(value);
        }
        return this;
    }

    public ViewPropertyAnimator translationXBy(float value) {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            n.translationXBy(value);
        }
        return this;
    }

    public ViewPropertyAnimator translationY(float value) {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            n.translationY(value);
        }
        return this;
    }

    public ViewPropertyAnimator translationYBy(float value) {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            n.translationYBy(value);
        }
        return this;
    }

    public ViewPropertyAnimator scaleX(float value) {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            n.scaleX(value);
        }
        return this;
    }

    public ViewPropertyAnimator scaleXBy(float value) {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            n.scaleXBy(value);
        }
        return this;
    }

    public ViewPropertyAnimator scaleY(float value) {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            n.scaleY(value);
        }
        return this;
    }

    public ViewPropertyAnimator scaleYBy(float value) {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            n.scaleYBy(value);
        }
        return this;
    }

    public ViewPropertyAnimator alpha(float value) {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            n.alpha(value);
        }
        return this;
    }

    public ViewPropertyAnimator alphaBy(float value) {
        ViewPropertyAnimator n = this.mNative.get();
        if (n != null) {
            n.alphaBy(value);
        }
        return this;
    }
}
