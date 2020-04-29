package dji.common.flightcontroller.simulator;

import android.support.annotation.NonNull;
import dji.common.model.LocationCoordinate2D;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class SimulatorState {
    private final boolean areMotorsOn;
    private final boolean isFlying;
    private final LocationCoordinate2D location;
    private final float pitch;
    private final float positionX;
    private final float positionY;
    private final float positionZ;
    private final float roll;
    private final float yaw;

    public interface Callback {
        void onUpdate(@NonNull SimulatorState simulatorState);
    }

    private SimulatorState(Builder builder) {
        this.areMotorsOn = builder.areMotorsOn;
        this.isFlying = builder.isFlying;
        this.pitch = builder.pitch;
        this.roll = builder.roll;
        this.yaw = builder.yaw;
        this.positionX = builder.positionX;
        this.positionY = builder.positionY;
        this.positionZ = builder.positionZ;
        this.location = builder.location;
    }

    public boolean areMotorsOn() {
        return this.areMotorsOn;
    }

    public boolean isFlying() {
        return this.isFlying;
    }

    public float getPitch() {
        return this.pitch;
    }

    public float getRoll() {
        return this.roll;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPositionX() {
        return this.positionX;
    }

    public float getPositionY() {
        return this.positionY;
    }

    public float getPositionZ() {
        return this.positionZ;
    }

    public LocationCoordinate2D getLocation() {
        return this.location;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimulatorState simulatorState = (SimulatorState) o;
        if (areMotorsOn() == simulatorState.areMotorsOn() && isFlying() == simulatorState.isFlying() && getPitch() == simulatorState.getPitch() && getRoll() == simulatorState.getRoll() && getYaw() == simulatorState.getYaw() && getPositionX() == simulatorState.getPositionX() && getPositionY() == simulatorState.getPositionY() && getPositionZ() == simulatorState.getPositionZ()) {
            return getLocation().equals(simulatorState.getLocation());
        }
        return false;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8 = 0;
        if (getLocation() != null) {
            result = getLocation().hashCode();
        } else {
            result = 0;
        }
        int i9 = result * 31;
        if (getPitch() != 0.0f) {
            i = Float.floatToIntBits(getPitch());
        } else {
            i = 0;
        }
        int i10 = (i9 + i) * 31;
        if (getRoll() != 0.0f) {
            i2 = Float.floatToIntBits(getRoll());
        } else {
            i2 = 0;
        }
        int i11 = (i10 + i2) * 31;
        if (getYaw() != 0.0f) {
            i3 = Float.floatToIntBits(getYaw());
        } else {
            i3 = 0;
        }
        int i12 = (i11 + i3) * 31;
        if (getPositionX() != 0.0f) {
            i4 = Float.floatToIntBits(getPositionX());
        } else {
            i4 = 0;
        }
        int i13 = (i12 + i4) * 31;
        if (getPositionY() != 0.0f) {
            i5 = Float.floatToIntBits(getPositionY());
        } else {
            i5 = 0;
        }
        int i14 = (i13 + i5) * 31;
        if (getPositionZ() != 0.0f) {
            i6 = Float.floatToIntBits(getPositionZ());
        } else {
            i6 = 0;
        }
        int i15 = (i14 + i6) * 31;
        if (areMotorsOn()) {
            i7 = 0;
        } else {
            i7 = 1;
        }
        int i16 = (i15 + i7) * 31;
        if (!isFlying()) {
            i8 = 1;
        }
        return i16 + i8;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public boolean areMotorsOn;
        /* access modifiers changed from: private */
        public boolean isFlying;
        /* access modifiers changed from: private */
        public LocationCoordinate2D location;
        /* access modifiers changed from: private */
        public float pitch;
        /* access modifiers changed from: private */
        public float positionX;
        /* access modifiers changed from: private */
        public float positionY;
        /* access modifiers changed from: private */
        public float positionZ;
        /* access modifiers changed from: private */
        public float roll;
        /* access modifiers changed from: private */
        public float yaw;

        public Builder areMotorsOn(boolean areMotorsOn2) {
            this.areMotorsOn = areMotorsOn2;
            return this;
        }

        public Builder isFlying(boolean isFlying2) {
            this.isFlying = isFlying2;
            return this;
        }

        public Builder pitch(float pitch2) {
            this.pitch = pitch2;
            return this;
        }

        public Builder roll(float roll2) {
            this.roll = roll2;
            return this;
        }

        public Builder yaw(float yaw2) {
            this.yaw = yaw2;
            return this;
        }

        public Builder positionX(float positionX2) {
            this.positionX = positionX2;
            return this;
        }

        public Builder positionY(float positionY2) {
            this.positionY = positionY2;
            return this;
        }

        public Builder positionZ(float positionZ2) {
            this.positionZ = positionZ2;
            return this;
        }

        public Builder location(LocationCoordinate2D location2) {
            this.location = location2;
            return this;
        }

        public SimulatorState build() {
            return new SimulatorState(this);
        }
    }
}
