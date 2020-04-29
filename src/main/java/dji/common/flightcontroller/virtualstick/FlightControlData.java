package dji.common.flightcontroller.virtualstick;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class FlightControlData {
    private float pitch;
    private float roll;
    private float verticalThrottle;
    private float yaw;

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch2) {
        this.pitch = pitch2;
    }

    public float getRoll() {
        return this.roll;
    }

    public void setRoll(float roll2) {
        this.roll = roll2;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw2) {
        this.yaw = yaw2;
    }

    public float getVerticalThrottle() {
        return this.verticalThrottle;
    }

    public void setVerticalThrottle(float verticalThrottle2) {
        this.verticalThrottle = verticalThrottle2;
    }

    public FlightControlData(float pitch2, float roll2, float yaw2, float verticalThrottle2) {
        this.pitch = pitch2;
        this.roll = roll2;
        this.yaw = yaw2;
        this.verticalThrottle = verticalThrottle2;
    }

    public int hashCode() {
        return (((((Float.floatToIntBits(this.pitch) * 31) + Float.floatToIntBits(this.roll)) * 31) + Float.floatToIntBits(this.yaw)) * 31) + Float.floatToIntBits(this.verticalThrottle);
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof FlightControlData)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        return this.pitch == ((FlightControlData) o).pitch && this.roll == ((FlightControlData) o).roll && this.yaw == ((FlightControlData) o).yaw && this.verticalThrottle == ((FlightControlData) o).verticalThrottle;
    }
}
