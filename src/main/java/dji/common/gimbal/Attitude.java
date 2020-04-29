package dji.common.gimbal;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class Attitude {
    public static final float NO_ROTATION = Float.MAX_VALUE;
    private float pitch;
    private float roll;
    private float yaw;

    public Attitude(float pitch2, float roll2, float yaw2) {
        this.pitch = pitch2;
        this.roll = roll2;
        this.yaw = yaw2;
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

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Attitude attitude = (Attitude) o;
        if (Float.compare(attitude.pitch, this.pitch) != 0 || Float.compare(attitude.roll, this.roll) != 0) {
            return false;
        }
        if (Float.compare(attitude.yaw, this.yaw) != 0) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2 = 0;
        if (this.pitch != 0.0f) {
            result = Float.floatToIntBits(this.pitch);
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (this.roll != 0.0f) {
            i = Float.floatToIntBits(this.roll);
        } else {
            i = 0;
        }
        int i4 = (i3 + i) * 31;
        if (this.yaw != 0.0f) {
            i2 = Float.floatToIntBits(this.yaw);
        }
        return i4 + i2;
    }

    public String toString() {
        return "Attitude{pitch=" + this.pitch + ", roll=" + this.roll + ", yaw=" + this.yaw + '}';
    }
}
