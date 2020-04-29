package lecho.lib.hellocharts.gesture;

import android.content.Context;
import android.os.SystemClock;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

public class ZoomerCompat {
    private static final int DEFAULT_SHORT_ANIMATION_DURATION = 200;
    private long mAnimationDurationMillis = 200;
    private float mCurrentZoom;
    private float mEndZoom;
    private boolean mFinished = true;
    private Interpolator mInterpolator = new DecelerateInterpolator();
    private long mStartRTC;

    public ZoomerCompat(Context context) {
    }

    public void forceFinished(boolean finished) {
        this.mFinished = finished;
    }

    public void abortAnimation() {
        this.mFinished = true;
        this.mCurrentZoom = this.mEndZoom;
    }

    public void startZoom(float endZoom) {
        this.mStartRTC = SystemClock.elapsedRealtime();
        this.mEndZoom = endZoom;
        this.mFinished = false;
        this.mCurrentZoom = 1.0f;
    }

    public boolean computeZoom() {
        if (this.mFinished) {
            return false;
        }
        long tRTC = SystemClock.elapsedRealtime() - this.mStartRTC;
        if (tRTC >= this.mAnimationDurationMillis) {
            this.mFinished = true;
            this.mCurrentZoom = this.mEndZoom;
            return false;
        }
        this.mCurrentZoom = this.mEndZoom * this.mInterpolator.getInterpolation((((float) tRTC) * 1.0f) / ((float) this.mAnimationDurationMillis));
        return true;
    }

    public float getCurrZoom() {
        return this.mCurrentZoom;
    }
}
