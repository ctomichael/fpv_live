package dji.common.gimbal;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJIGimbalAttitude {
    private final float pitch;
    private final float roll;
    private final float yaw;

    public DJIGimbalAttitude(float pitch2, float roll2, float yaw2) {
        this.pitch = pitch2;
        this.roll = roll2;
        this.yaw = yaw2;
    }

    public boolean equals(Object o) {
        boolean ret = super.equals(o);
        if (ret || !(o instanceof DJIGimbalAttitude)) {
            return ret;
        }
        DJIGimbalAttitude tmp = (DJIGimbalAttitude) o;
        return ((double) Math.abs(tmp.pitch - this.pitch)) < 0.01d && ((double) Math.abs(tmp.roll - this.roll)) < 0.01d && ((double) Math.abs(tmp.yaw - this.yaw)) < 0.01d;
    }

    public int hashCode() {
        return ((((Float.floatToIntBits(this.pitch) + 527) * 31) + Float.floatToIntBits(this.roll)) * 31) + Float.floatToIntBits(this.yaw);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(32);
        sb.append("pitch:").append(this.pitch).append("; roll:").append(this.roll).append("; yaw:").append(this.yaw);
        return sb.toString();
    }
}
