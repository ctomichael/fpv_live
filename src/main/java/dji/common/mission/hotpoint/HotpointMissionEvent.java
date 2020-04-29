package dji.common.mission.hotpoint;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.common.error.DJIError;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class HotpointMissionEvent {
    @NonNull
    private final HotpointMissionState currentState;
    @Nullable
    private final DJIError error;
    private final float maxAngularVelocity;
    @Nullable
    private final HotpointMissionState previousState;
    private final float radius;

    public HotpointMissionEvent(@NonNull Builder builder) {
        this.error = builder.error;
        this.previousState = builder.previousState;
        this.currentState = builder.currentState;
        this.radius = builder.radius;
        this.maxAngularVelocity = builder.maxAngularVelocity;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        @NonNull
        public HotpointMissionState currentState;
        /* access modifiers changed from: private */
        @Nullable
        public DJIError error;
        /* access modifiers changed from: private */
        public float maxAngularVelocity;
        /* access modifiers changed from: private */
        @Nullable
        public HotpointMissionState previousState;
        /* access modifiers changed from: private */
        public float radius;

        public Builder previousState(HotpointMissionState previousState2) {
            this.previousState = previousState2;
            return this;
        }

        public Builder currentState(HotpointMissionState currentState2) {
            this.currentState = currentState2;
            return this;
        }

        public Builder error(DJIError error2) {
            this.error = error2;
            return this;
        }

        public Builder radius(float radius2) {
            this.radius = radius2;
            return this;
        }

        public Builder maxAngularVelocity(float maxAngularVelocity2) {
            this.maxAngularVelocity = maxAngularVelocity2;
            return this;
        }

        public HotpointMissionEvent build() {
            return new HotpointMissionEvent(this);
        }
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (!(o instanceof HotpointMissionEvent)) {
            return false;
        }
        HotpointMissionEvent that = (HotpointMissionEvent) o;
        if (Float.compare(that.getRadius(), getRadius()) != 0 || Float.compare(that.getMaxAngularVelocity(), getMaxAngularVelocity()) != 0) {
            return false;
        }
        if (getPreviousState() != null) {
            if (!getPreviousState().equals(that.getPreviousState())) {
                return false;
            }
        } else if (that.getPreviousState() != null) {
            return false;
        }
        if (getCurrentState() != null) {
            if (!getCurrentState().equals(that.getCurrentState())) {
                return false;
            }
        } else if (that.getCurrentState() != null) {
            return false;
        }
        if (getError() != null) {
            z = getError().equals(that.getError());
        } else if (that.getError() != null) {
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
        if (getPreviousState() != null) {
            result = getPreviousState().hashCode();
        } else {
            result = 0;
        }
        int i5 = result * 31;
        if (getCurrentState() != null) {
            i = getCurrentState().hashCode();
        } else {
            i = 0;
        }
        int i6 = (i5 + i) * 31;
        if (getRadius() != 0.0f) {
            i2 = Float.floatToIntBits(getRadius());
        } else {
            i2 = 0;
        }
        int i7 = (i6 + i2) * 31;
        if (getMaxAngularVelocity() != 0.0f) {
            i3 = Float.floatToIntBits(getMaxAngularVelocity());
        } else {
            i3 = 0;
        }
        int i8 = (i7 + i3) * 31;
        if (getError() != null) {
            i4 = getError().hashCode();
        }
        return i8 + i4;
    }

    @Nullable
    public HotpointMissionState getPreviousState() {
        return this.previousState;
    }

    @NonNull
    public HotpointMissionState getCurrentState() {
        return this.currentState;
    }

    public float getRadius() {
        return this.radius;
    }

    public float getMaxAngularVelocity() {
        return this.maxAngularVelocity;
    }

    @Nullable
    public DJIError getError() {
        return this.error;
    }
}
