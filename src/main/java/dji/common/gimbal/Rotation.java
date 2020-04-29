package dji.common.gimbal;

import dji.fieldAnnotation.EXClassNullAway;
import java.util.Objects;

@EXClassNullAway
public class Rotation {
    public static final float NO_ROTATION = Float.MAX_VALUE;
    private final boolean ignore;
    private final RotationMode mode;
    private final float pitch;
    private final float roll;
    private final double time;
    private final float yaw;

    private Rotation(Builder builder) {
        this.mode = builder.mode;
        this.pitch = builder.pitch;
        this.roll = builder.roll;
        this.yaw = builder.yaw;
        this.time = builder.time;
        this.ignore = builder.ignore;
    }

    public RotationMode getMode() {
        return this.mode;
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

    public double getTime() {
        return this.time;
    }

    public boolean getIgnore() {
        return this.ignore;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rotation)) {
            return false;
        }
        Rotation rotation = (Rotation) o;
        if (Float.compare(rotation.getPitch(), getPitch()) == 0 && Float.compare(rotation.getRoll(), getRoll()) == 0 && Float.compare(rotation.getYaw(), getYaw()) == 0 && Double.compare(rotation.getTime(), getTime()) == 0 && getIgnore() == rotation.getIgnore() && getMode() == rotation.getMode()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(getMode(), Float.valueOf(getPitch()), Float.valueOf(getRoll()), Float.valueOf(getYaw()), Double.valueOf(getTime()), Boolean.valueOf(getIgnore()));
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public boolean ignore;
        /* access modifiers changed from: private */
        public RotationMode mode;
        /* access modifiers changed from: private */
        public float pitch = Float.MAX_VALUE;
        /* access modifiers changed from: private */
        public float roll = Float.MAX_VALUE;
        /* access modifiers changed from: private */
        public double time;
        /* access modifiers changed from: private */
        public float yaw = Float.MAX_VALUE;

        public Builder mode(RotationMode mode2) {
            this.mode = mode2;
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

        public Builder time(double time2) {
            this.time = time2;
            return this;
        }

        public Builder ignoreGimbalStick(boolean ignore2) {
            this.ignore = ignore2;
            return this;
        }

        public Builder() {
        }

        public Builder(Rotation rotation) {
            this.mode = rotation.getMode();
            this.pitch = rotation.getPitch();
            this.roll = rotation.getRoll();
            this.yaw = rotation.getYaw();
            this.time = rotation.getTime();
            this.ignore = rotation.getIgnore();
        }

        public Rotation build() {
            return new Rotation(this);
        }
    }
}
