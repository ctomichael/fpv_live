package dji.common.remotecontroller;

import android.support.annotation.NonNull;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class FocusControllerState {
    private ControlType controlType;
    private Direction direction;
    private boolean isWorking;

    public enum Direction {
        CLOCKWISE,
        COUNTER_CLOCKWISE,
        UNKNOWN
    }

    public enum ControlType {
        APERTURE,
        FOCAL_LENGTH,
        FOCUS_DISTANCE,
        UNKNOWN
    }

    public interface FocusControllerStateCallback {
        void onUpdate(@NonNull FocusControllerState focusControllerState);
    }

    private FocusControllerState(Builder builder) {
        this.isWorking = builder.isWorking;
        this.controlType = builder.controlType;
        this.direction = builder.direction;
    }

    public boolean isWorking() {
        return this.isWorking;
    }

    public ControlType getControlType() {
        return this.controlType;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FocusControllerState that = (FocusControllerState) o;
        if (this.isWorking != that.isWorking || this.controlType != that.controlType) {
            return false;
        }
        if (this.direction != that.direction) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2 = 0;
        if (this.isWorking) {
            result = 1;
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (this.controlType != null) {
            i = this.controlType.hashCode();
        } else {
            i = 0;
        }
        int i4 = (i3 + i) * 31;
        if (this.direction != null) {
            i2 = this.direction.hashCode();
        }
        return i4 + i2;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public ControlType controlType;
        /* access modifiers changed from: private */
        public Direction direction;
        /* access modifiers changed from: private */
        public boolean isWorking;

        public Builder isWorking(boolean isWorking2) {
            this.isWorking = isWorking2;
            return this;
        }

        public Builder controlType(ControlType controlType2) {
            this.controlType = controlType2;
            return this;
        }

        public Builder direction(Direction direction2) {
            this.direction = direction2;
            return this;
        }

        public FocusControllerState build() {
            return new FocusControllerState(this);
        }
    }
}
