package dji.common.remotecontroller;

import android.support.annotation.IntRange;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class GimbalControlSpeedCoefficient {
    private int pitchSpeedCoefficient;
    private int rollSpeedCoefficient;
    private int yawSpeedCoefficient;

    public GimbalControlSpeedCoefficient(@IntRange(from = 0, to = 100) int pitchSpeedCoefficient2, @IntRange(from = 0, to = 100) int rollSpeedCoefficient2, @IntRange(from = 0, to = 100) int yawSpeedCoefficient2) {
        this.pitchSpeedCoefficient = pitchSpeedCoefficient2;
        this.rollSpeedCoefficient = rollSpeedCoefficient2;
        this.yawSpeedCoefficient = yawSpeedCoefficient2;
    }

    public int getPitchSpeedCoefficient() {
        return this.pitchSpeedCoefficient;
    }

    public int getRollSpeedCoefficient() {
        return this.rollSpeedCoefficient;
    }

    public int getYawSpeedCoefficient() {
        return this.yawSpeedCoefficient;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GimbalControlSpeedCoefficient that = (GimbalControlSpeedCoefficient) o;
        if (this.pitchSpeedCoefficient != that.pitchSpeedCoefficient || this.rollSpeedCoefficient != that.rollSpeedCoefficient) {
            return false;
        }
        if (this.yawSpeedCoefficient != that.yawSpeedCoefficient) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((this.pitchSpeedCoefficient * 31) + this.rollSpeedCoefficient) * 31) + this.yawSpeedCoefficient;
    }
}
