package dji.common.mission.activetrack;

import android.support.annotation.Nullable;
import dji.common.error.DJIError;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class ActiveTrackMissionEvent {
    private ActiveTrackState currentState;
    private DJIError error;
    private ActiveTrackState previousState;
    private ActiveTrackTrackingState trackingState;

    @Nullable
    public ActiveTrackState getPreviousState() {
        return this.previousState;
    }

    @Nullable
    public ActiveTrackState getCurrentState() {
        return this.currentState;
    }

    @Nullable
    public DJIError getError() {
        return this.error;
    }

    @Nullable
    public ActiveTrackTrackingState getTrackingState() {
        return this.trackingState;
    }

    public ActiveTrackMissionEvent(ActiveTrackState previousState2, ActiveTrackState currentState2, DJIError error2, ActiveTrackTrackingState trackingState2) {
        this.previousState = previousState2;
        this.currentState = currentState2;
        this.error = error2;
        this.trackingState = trackingState2;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3 = 0;
        if (this.previousState != null) {
            result = this.previousState.hashCode();
        } else {
            result = 0;
        }
        int i4 = result * 31;
        if (this.currentState != null) {
            i = this.currentState.hashCode();
        } else {
            i = 0;
        }
        int i5 = (i4 + i) * 31;
        if (this.error != null) {
            i2 = this.error.hashCode();
        } else {
            i2 = 0;
        }
        int i6 = (i5 + i2) * 31;
        if (this.trackingState != null) {
            i3 = this.trackingState.hashCode();
        }
        return i6 + i3;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof ActiveTrackMissionEvent)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (this.previousState == null || ((ActiveTrackMissionEvent) o).previousState == null) {
            if (this.previousState == null || ((ActiveTrackMissionEvent) o).previousState == null) {
                return false;
            }
        } else if (!this.previousState.equals(((ActiveTrackMissionEvent) o).previousState)) {
            return false;
        }
        if (this.currentState == null || ((ActiveTrackMissionEvent) o).currentState == null) {
            if (this.currentState == null || ((ActiveTrackMissionEvent) o).currentState == null) {
                return false;
            }
        } else if (!this.currentState.equals(((ActiveTrackMissionEvent) o).currentState)) {
            return false;
        }
        if (this.error == null || ((ActiveTrackMissionEvent) o).error == null) {
            if (this.error == null || ((ActiveTrackMissionEvent) o).error == null) {
                return false;
            }
        } else if (!this.error.equals(((ActiveTrackMissionEvent) o).error)) {
            return false;
        }
        if (this.trackingState == null || ((ActiveTrackMissionEvent) o).trackingState == null) {
            if (this.trackingState == null || ((ActiveTrackMissionEvent) o).trackingState == null) {
                return false;
            }
        } else if (!this.trackingState.equals(((ActiveTrackMissionEvent) o).trackingState)) {
            return false;
        }
        return true;
    }
}
