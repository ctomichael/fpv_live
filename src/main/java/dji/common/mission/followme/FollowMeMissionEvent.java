package dji.common.mission.followme;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.common.error.DJIError;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class FollowMeMissionEvent {
    @NonNull
    private final FollowMeMissionState currentState;
    private final float distanceToTarget;
    @Nullable
    private final DJIError error;
    @Nullable
    private final FollowMeMissionState previousState;

    public FollowMeMissionEvent(@NonNull Builder builder) {
        this.error = builder.error;
        this.previousState = builder.previousState;
        this.currentState = builder.currentState;
        this.distanceToTarget = builder.distanceToTarget;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        @NonNull
        public FollowMeMissionState currentState;
        /* access modifiers changed from: private */
        public float distanceToTarget;
        /* access modifiers changed from: private */
        @Nullable
        public DJIError error;
        /* access modifiers changed from: private */
        @Nullable
        public FollowMeMissionState previousState;

        public Builder previousState(FollowMeMissionState previousState2) {
            this.previousState = previousState2;
            return this;
        }

        public Builder currentState(FollowMeMissionState currentState2) {
            this.currentState = currentState2;
            return this;
        }

        public Builder error(DJIError error2) {
            this.error = error2;
            return this;
        }

        public Builder distanceToTarget(float distanceToTarget2) {
            this.distanceToTarget = distanceToTarget2;
            return this;
        }

        public FollowMeMissionEvent build() {
            return new FollowMeMissionEvent(this);
        }
    }

    public int hashCode() {
        int result;
        int i;
        int i2 = 0;
        if (this.previousState != null) {
            result = this.previousState.hashCode();
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (this.currentState != null) {
            i = this.currentState.hashCode();
        } else {
            i = 0;
        }
        int floatToIntBits = (((i3 + i) * 31) + Float.floatToIntBits(this.distanceToTarget)) * 31;
        if (this.error != null) {
            i2 = this.error.hashCode();
        }
        return floatToIntBits + i2;
    }

    public boolean equals(Object o) {
        boolean z;
        if (o == null || !(o instanceof FollowMeMissionEvent)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (this.previousState == null || ((FollowMeMissionEvent) o).previousState == null) {
            if (this.previousState == null || ((FollowMeMissionEvent) o).previousState == null) {
                return false;
            }
        } else if (!this.previousState.equals(((FollowMeMissionEvent) o).previousState)) {
            return false;
        }
        if (this.currentState == null || ((FollowMeMissionEvent) o).currentState == null) {
            if (this.currentState == null || ((FollowMeMissionEvent) o).currentState == null) {
                return false;
            }
        } else if (!this.currentState.equals(((FollowMeMissionEvent) o).currentState)) {
            return false;
        }
        if (this.error == null || ((FollowMeMissionEvent) o).error == null) {
            if (this.error == null || ((FollowMeMissionEvent) o).error == null) {
                return false;
            }
        } else if (!this.error.equals(((FollowMeMissionEvent) o).error)) {
            return false;
        }
        if (this.distanceToTarget == ((FollowMeMissionEvent) o).distanceToTarget) {
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    @Nullable
    public FollowMeMissionState getPreviousState() {
        return this.previousState;
    }

    @NonNull
    public FollowMeMissionState getCurrentState() {
        return this.currentState;
    }

    public float getDistanceToTarget() {
        return this.distanceToTarget;
    }

    @Nullable
    public DJIError getError() {
        return this.error;
    }
}
