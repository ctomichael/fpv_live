package dji.common.mission.panorama;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.common.error.DJIError;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class PanoramaMissionEvent {
    @NonNull
    private final PanoramaMissionState currentState;
    @Nullable
    private final DJIError error;
    @Nullable
    private final PanoramaMissionExecutionState executionState;
    @Nullable
    private final PanoramaMissionState previousState;

    public PanoramaMissionEvent(@Nullable PanoramaMissionState previousState2, @NonNull PanoramaMissionState currentState2, @Nullable PanoramaMissionExecutionState executionState2, @Nullable DJIError error2) {
        this.previousState = previousState2;
        this.currentState = currentState2;
        this.executionState = executionState2;
        this.error = error2;
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
        if (this.executionState != null) {
            i2 = this.executionState.hashCode();
        } else {
            i2 = 0;
        }
        int i6 = (i5 + i2) * 31;
        if (this.error != null) {
            i3 = this.error.hashCode();
        }
        return i6 + i3;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof PanoramaMissionEvent)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (this.previousState == null || ((PanoramaMissionEvent) o).previousState == null) {
            if (this.previousState == null || ((PanoramaMissionEvent) o).previousState == null) {
                return false;
            }
        } else if (!this.previousState.equals(((PanoramaMissionEvent) o).previousState)) {
            return false;
        }
        if (this.currentState == null || ((PanoramaMissionEvent) o).currentState == null) {
            if (this.currentState == null || ((PanoramaMissionEvent) o).currentState == null) {
                return false;
            }
        } else if (!this.currentState.equals(((PanoramaMissionEvent) o).currentState)) {
            return false;
        }
        if (this.executionState == null || ((PanoramaMissionEvent) o).executionState == null) {
            if (this.executionState == null || ((PanoramaMissionEvent) o).executionState == null) {
                return false;
            }
        } else if (!this.executionState.equals(((PanoramaMissionEvent) o).executionState)) {
            return false;
        }
        if (this.error == null || ((PanoramaMissionEvent) o).error == null) {
            if (this.error == null || ((PanoramaMissionEvent) o).error == null) {
                return false;
            }
        } else if (!this.error.equals(((PanoramaMissionEvent) o).error)) {
            return false;
        }
        return true;
    }

    @Nullable
    public PanoramaMissionState getPreviousState() {
        return this.previousState;
    }

    @NonNull
    public PanoramaMissionState getCurrentState() {
        return this.currentState;
    }

    @Nullable
    public PanoramaMissionExecutionState getExecutionState() {
        return this.executionState;
    }

    @Nullable
    public DJIError getError() {
        return this.error;
    }
}
