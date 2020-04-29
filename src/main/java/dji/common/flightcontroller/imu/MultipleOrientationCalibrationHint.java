package dji.common.flightcontroller.imu;

import dji.common.flightcontroller.CalibrationOrientation;
import dji.fieldAnnotation.EXClassNullAway;
import java.util.HashSet;

@EXClassNullAway
public class MultipleOrientationCalibrationHint {
    private HashSet<CalibrationOrientation> orientationsCalibrated = new HashSet<>(6);
    private HashSet<CalibrationOrientation> orientationsToCalibrate = new HashSet<>(6);
    private OrientationCalibrationState state;

    public HashSet<CalibrationOrientation> getOrientationsCalibrated() {
        return this.orientationsCalibrated;
    }

    public void setOrientationsCalibrated(HashSet<CalibrationOrientation> orientationsCalibrated2) {
        this.orientationsCalibrated = orientationsCalibrated2;
    }

    public HashSet<CalibrationOrientation> getOrientationsToCalibrate() {
        return this.orientationsToCalibrate;
    }

    public void setOrientationsToCalibrate(HashSet<CalibrationOrientation> orientationsToCalibrate2) {
        this.orientationsToCalibrate = orientationsToCalibrate2;
    }

    public OrientationCalibrationState getState() {
        return this.state;
    }

    public void setState(OrientationCalibrationState state2) {
        this.state = state2;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MultipleOrientationCalibrationHint that = (MultipleOrientationCalibrationHint) o;
        if (getOrientationsCalibrated() != null) {
            if (!getOrientationsCalibrated().equals(that.getOrientationsCalibrated())) {
                return false;
            }
        } else if (that.getOrientationsCalibrated() != null) {
            return false;
        }
        if (getOrientationsToCalibrate() != null) {
            if (!getOrientationsToCalibrate().equals(that.getOrientationsToCalibrate())) {
                return false;
            }
        } else if (that.getOrientationsToCalibrate() != null) {
            return false;
        }
        if (getState() != that.getState()) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2 = 0;
        if (getOrientationsCalibrated() != null) {
            result = getOrientationsCalibrated().hashCode();
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (getOrientationsToCalibrate() != null) {
            i = getOrientationsToCalibrate().hashCode();
        } else {
            i = 0;
        }
        int i4 = (i3 + i) * 31;
        if (getState() != null) {
            i2 = getState().hashCode();
        }
        return i4 + i2;
    }
}
