package dji.common.mission.waypoint;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.common.error.DJIError;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public final class WaypointMissionExecutionEvent extends WaypointMissionEvent {
    @NonNull
    private final WaypointMissionState currentState;
    @Nullable
    private final WaypointMissionState previousState;
    @Nullable
    private final WaypointExecutionProgress progress;

    public WaypointMissionExecutionEvent(@NonNull Builder builder) {
        super(builder.error);
        this.previousState = builder.previousState;
        this.currentState = builder.currentState;
        this.progress = builder.progress;
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
        int i4 = (i3 + i) * 31;
        if (this.progress != null) {
            i2 = this.progress.hashCode();
        }
        return i4 + i2;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof WaypointMissionExecutionEvent)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (this.previousState == null || ((WaypointMissionExecutionEvent) o).previousState == null) {
            if (this.previousState == null || ((WaypointMissionExecutionEvent) o).previousState == null) {
                return false;
            }
        } else if (!this.previousState.equals(((WaypointMissionExecutionEvent) o).previousState)) {
            return false;
        }
        if (this.currentState == null || ((WaypointMissionExecutionEvent) o).currentState == null) {
            if (this.currentState == null || ((WaypointMissionExecutionEvent) o).currentState == null) {
                return false;
            }
        } else if (!this.currentState.equals(((WaypointMissionExecutionEvent) o).currentState)) {
            return false;
        }
        if (this.progress == null || ((WaypointMissionExecutionEvent) o).progress == null) {
            if (this.progress == null || ((WaypointMissionExecutionEvent) o).progress == null) {
                return false;
            }
        } else if (!this.progress.equals(((WaypointMissionExecutionEvent) o).progress)) {
            return false;
        }
        return true;
    }

    @Nullable
    public WaypointMissionState getPreviousState() {
        return this.previousState;
    }

    @NonNull
    public WaypointMissionState getCurrentState() {
        return this.currentState;
    }

    @Nullable
    public WaypointExecutionProgress getProgress() {
        return this.progress;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        @NonNull
        public WaypointMissionState currentState;
        /* access modifiers changed from: private */
        @Nullable
        public DJIError error;
        /* access modifiers changed from: private */
        @Nullable
        public WaypointMissionState previousState;
        /* access modifiers changed from: private */
        @Nullable
        public WaypointExecutionProgress progress;

        public Builder previousState(WaypointMissionState previousState2) {
            this.previousState = previousState2;
            return this;
        }

        public Builder currentState(WaypointMissionState currentState2) {
            this.currentState = currentState2;
            return this;
        }

        public Builder error(DJIError error2) {
            this.error = error2;
            return this;
        }

        public Builder progress(WaypointExecutionProgress progress2) {
            this.progress = progress2;
            return this;
        }

        public WaypointMissionExecutionEvent build() {
            return new WaypointMissionExecutionEvent(this);
        }
    }

    public String toString() {
        return "cur state=" + this.currentState + " pre state=" + this.previousState + " progress=" + this.progress + " error=" + getError();
    }
}
