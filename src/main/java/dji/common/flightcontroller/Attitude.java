package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class Attitude {
    public final double pitch;
    public final double roll;
    public final double yaw;

    public Attitude(double pitch2, double roll2, double yaw2) {
        this.pitch = pitch2;
        this.roll = roll2;
        this.yaw = yaw2;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.pitch);
        int result = (int) ((temp >>> 32) ^ temp);
        long temp2 = Double.doubleToLongBits(this.roll);
        int result2 = (result * 31) + ((int) ((temp2 >>> 32) ^ temp2));
        long temp3 = Double.doubleToLongBits(this.yaw);
        return (result2 * 31) + ((int) ((temp3 >>> 32) ^ temp3));
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof Attitude)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        return this.pitch == ((Attitude) o).pitch && this.roll == ((Attitude) o).roll && this.yaw == ((Attitude) o).yaw;
    }
}
