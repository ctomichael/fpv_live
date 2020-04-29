package com.nineoldandroids.animation;

public class TimeAnimator extends ValueAnimator {
    private TimeListener mListener;
    private long mPreviousTime = -1;

    public interface TimeListener {
        void onTimeUpdate(TimeAnimator timeAnimator, long j, long j2);
    }

    /* access modifiers changed from: package-private */
    public boolean animationFrame(long currentTime) {
        long deltaTime = 0;
        if (this.mPlayingState == 0) {
            this.mPlayingState = 1;
            if (this.mSeekTime < 0) {
                this.mStartTime = currentTime;
            } else {
                this.mStartTime = currentTime - this.mSeekTime;
                this.mSeekTime = -1;
            }
        }
        if (this.mListener == null) {
            return false;
        }
        long totalTime = currentTime - this.mStartTime;
        if (this.mPreviousTime >= 0) {
            deltaTime = currentTime - this.mPreviousTime;
        }
        this.mPreviousTime = currentTime;
        this.mListener.onTimeUpdate(this, totalTime, deltaTime);
        return false;
    }

    public void setTimeListener(TimeListener listener) {
        this.mListener = listener;
    }

    /* access modifiers changed from: package-private */
    public void animateValue(float fraction) {
    }

    /* access modifiers changed from: package-private */
    public void initAnimation() {
    }
}
