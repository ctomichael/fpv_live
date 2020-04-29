package dji.common.flightcontroller.flightassistant;

import android.graphics.RectF;

public class SmartCaptureState {
    private final SmartCaptureAction action;
    private final SmartCaptureFollowingMode followingMode;
    private final float photoCountdown;
    private final SmartCaptureSystemStatus systemStatus;
    private final RectF targetRect;

    public interface Callback {
        void onUpdate(SmartCaptureState smartCaptureState);
    }

    private SmartCaptureState(Builder builder) {
        this.targetRect = builder.targetRect;
        this.systemStatus = builder.systemStatus;
        this.action = builder.action;
        this.followingMode = builder.followingMode;
        this.photoCountdown = builder.photoCountdown;
    }

    public RectF getTargetRect() {
        return this.targetRect;
    }

    public SmartCaptureSystemStatus getSystemStatus() {
        return this.systemStatus;
    }

    public SmartCaptureAction getAction() {
        return this.action;
    }

    public SmartCaptureFollowingMode getFollowingMode() {
        return this.followingMode;
    }

    public float getPhotoCountdown() {
        return this.photoCountdown;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (!(o instanceof SmartCaptureState)) {
            return false;
        }
        SmartCaptureState that = (SmartCaptureState) o;
        if (Float.compare(that.getPhotoCountdown(), getPhotoCountdown()) != 0) {
            return false;
        }
        if (getTargetRect() != null) {
            if (!getTargetRect().equals(that.getTargetRect())) {
                return false;
            }
        } else if (that.getTargetRect() != null) {
            return false;
        }
        if (getSystemStatus() != that.getSystemStatus() || getAction() != that.getAction()) {
            return false;
        }
        if (getFollowingMode() != that.getFollowingMode()) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3;
        int i4 = 0;
        if (getTargetRect() != null) {
            result = getTargetRect().hashCode();
        } else {
            result = 0;
        }
        int i5 = result * 31;
        if (getSystemStatus() != null) {
            i = getSystemStatus().hashCode();
        } else {
            i = 0;
        }
        int i6 = (i5 + i) * 31;
        if (getAction() != null) {
            i2 = getAction().hashCode();
        } else {
            i2 = 0;
        }
        int i7 = (i6 + i2) * 31;
        if (getFollowingMode() != null) {
            i3 = getFollowingMode().hashCode();
        } else {
            i3 = 0;
        }
        int i8 = (i7 + i3) * 31;
        if (getPhotoCountdown() != 0.0f) {
            i4 = Float.floatToIntBits(getPhotoCountdown());
        }
        return i8 + i4;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public SmartCaptureAction action;
        /* access modifiers changed from: private */
        public SmartCaptureFollowingMode followingMode;
        /* access modifiers changed from: private */
        public float photoCountdown;
        /* access modifiers changed from: private */
        public SmartCaptureSystemStatus systemStatus;
        /* access modifiers changed from: private */
        public RectF targetRect;

        public Builder targetRect(RectF targetRect2) {
            this.targetRect = targetRect2;
            return this;
        }

        public Builder systemStatus(SmartCaptureSystemStatus systemStatus2) {
            this.systemStatus = systemStatus2;
            return this;
        }

        public Builder action(SmartCaptureAction action2) {
            this.action = action2;
            return this;
        }

        public Builder followingMode(SmartCaptureFollowingMode followingMode2) {
            this.followingMode = followingMode2;
            return this;
        }

        public Builder photoCountdown(float photoCountdown2) {
            this.photoCountdown = photoCountdown2;
            return this;
        }

        public SmartCaptureState build() {
            return new SmartCaptureState(this);
        }
    }
}
