package dji.common.flightcontroller.imu;

import android.support.annotation.NonNull;
import dji.fieldAnnotation.EXClassNullAway;
import java.util.Arrays;

@EXClassNullAway
public class IMUState {
    private SensorState accelerometerState;
    private float accelerometerValue;
    private int calibrationProgress;
    private CalibrationState calibrationState;
    private boolean[] currentDownside = {false, false, false, false, false, false};
    private int currentSideStatus = 2;
    private boolean[] finishCalibrationSide = {true, true, true, true, true, true};
    private SensorState gyroscopeState;
    private float gyroscopeValue;
    private int index = -1;
    private boolean isConnected = false;
    private boolean isMultiSideCalibrationType = false;
    private MultipleOrientationCalibrationHint multiOrientationCalibrationHint = new MultipleOrientationCalibrationHint();
    private boolean[] needCalibrationSide = {true, true, true, true, true, true};
    private IMUState[] subIMUState;

    public interface Callback {
        void onUpdate(@NonNull IMUState iMUState);
    }

    public IMUState(int subImuCount) {
        if (subImuCount > 0) {
            this.subIMUState = new IMUState[subImuCount];
            for (int i = 0; i < subImuCount; i++) {
                IMUState state = new IMUState(0);
                state.setIndex(i);
                this.subIMUState[i] = state;
            }
        }
    }

    public IMUState() {
    }

    public int getCurrentSideStatus() {
        return this.currentSideStatus;
    }

    public void setCurrentSideStatus(int currentSideStatus2) {
        this.currentSideStatus = currentSideStatus2;
    }

    public MultipleOrientationCalibrationHint getMultipleOrientationCalibrationHint() {
        return this.multiOrientationCalibrationHint;
    }

    public void setMultiOrientationCalibrationHint(MultipleOrientationCalibrationHint multiOrientationCalibrationHint2) {
        this.multiOrientationCalibrationHint = multiOrientationCalibrationHint2;
    }

    public boolean isMultiSideCalibrationType() {
        return this.isMultiSideCalibrationType;
    }

    public void setMultiSideCalibrationType(boolean multiSideCalibrationType) {
        this.isMultiSideCalibrationType = multiSideCalibrationType;
    }

    public boolean[] getCurrentDownside() {
        return this.currentDownside;
    }

    public void setCurrentDownside(boolean[] currentDownside2) {
        this.currentDownside = currentDownside2;
    }

    public boolean[] getNeedCalibrationSide() {
        return this.needCalibrationSide;
    }

    public void setNeedCalibrationSide(boolean[] needCalibrationSide2) {
        this.needCalibrationSide = needCalibrationSide2;
    }

    public boolean[] getFinishCalibrationSide() {
        return this.finishCalibrationSide;
    }

    public void setFinishCalibrationSide(boolean[] finishCalibrationSide2) {
        this.finishCalibrationSide = finishCalibrationSide2;
    }

    public void setIsConnected(boolean isConnected2) {
        this.isConnected = isConnected2;
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    public void setIndex(int index2) {
        this.index = index2;
    }

    public int getIndex() {
        return this.index;
    }

    public float getGyroscopeValue() {
        return this.gyroscopeValue;
    }

    public void setGyroscopeValue(float gyroscopeValue2) {
        this.gyroscopeValue = gyroscopeValue2;
    }

    public float getAccelerometerValue() {
        return this.accelerometerValue;
    }

    public void setAccelerometerValue(float accelerometerValue2) {
        this.accelerometerValue = accelerometerValue2;
    }

    public void setGyroscopeState(SensorState state) {
        this.gyroscopeState = state;
    }

    public SensorState getGyroscopeState() {
        return this.gyroscopeState;
    }

    public void setAccelerometerState(SensorState accelerometerState2) {
        this.accelerometerState = accelerometerState2;
    }

    public SensorState getAccelerometerState() {
        return this.accelerometerState;
    }

    public void setCalibrationProgress(int i) {
        this.calibrationProgress = i;
    }

    public int getCalibrationProgress() {
        return this.calibrationProgress;
    }

    public void setCalibrationState(CalibrationState state) {
        this.calibrationState = state;
    }

    public CalibrationState getCalibrationState() {
        return this.calibrationState;
    }

    public IMUState[] getSubIMUState() {
        return this.subIMUState;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IMUState)) {
            return false;
        }
        IMUState that = (IMUState) o;
        if (this.currentSideStatus != that.getCurrentSideStatus()) {
            return false;
        }
        if (isConnected() != that.isConnected()) {
            return false;
        }
        if (this.index != that.index) {
            return false;
        }
        if (getCalibrationProgress() != that.getCalibrationProgress()) {
            return false;
        }
        if (!Arrays.equals(getNeedCalibrationSide(), that.getNeedCalibrationSide())) {
            return false;
        }
        if (!Arrays.equals(getFinishCalibrationSide(), that.getFinishCalibrationSide())) {
            return false;
        }
        if (!Arrays.equals(getCurrentDownside(), that.getCurrentDownside())) {
            return false;
        }
        if (getGyroscopeValue() != that.getGyroscopeValue()) {
            return false;
        }
        if (getAccelerometerValue() != that.getAccelerometerValue()) {
            return false;
        }
        if (getGyroscopeState() != that.getGyroscopeState()) {
            return false;
        }
        if (getAccelerometerState() != that.getAccelerometerState()) {
            return false;
        }
        if (getCalibrationState() != that.getCalibrationState()) {
            return false;
        }
        if (getMultipleOrientationCalibrationHint() == null ? that.getMultipleOrientationCalibrationHint() != null : !getMultipleOrientationCalibrationHint().equals(that.getMultipleOrientationCalibrationHint())) {
            return false;
        }
        if (getSubIMUState() == null || that.getSubIMUState() == null) {
            if (getSubIMUState() == null && that.getSubIMUState() == null) {
                return true;
            }
            return false;
        } else if (getSubIMUState().length != that.getSubIMUState().length) {
            return false;
        } else {
            for (int i = 0; i < getSubIMUState().length; i++) {
                if (!getSubIMUState()[i].equals(that.getSubIMUState()[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    public int hashCode() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6 = 0;
        int hashCode = ((((Arrays.hashCode(getNeedCalibrationSide()) * 31) + Arrays.hashCode(getFinishCalibrationSide())) * 31) + Arrays.hashCode(getCurrentDownside())) * 31;
        if (isConnected()) {
            i = 1;
        } else {
            i = 0;
        }
        int i7 = (((((hashCode + i) * 31) + this.index) * 31) + this.currentSideStatus) * 31;
        if (this.gyroscopeValue != 0.0f) {
            i2 = Float.floatToIntBits(this.gyroscopeValue);
        } else {
            i2 = 0;
        }
        int i8 = (i7 + i2) * 31;
        if (this.accelerometerValue != 0.0f) {
            i3 = Float.floatToIntBits(this.accelerometerValue);
        } else {
            i3 = 0;
        }
        int i9 = (i8 + i3) * 31;
        if (getGyroscopeState() != null) {
            i4 = getGyroscopeState().hashCode();
        } else {
            i4 = 0;
        }
        int i10 = (i9 + i4) * 31;
        if (getAccelerometerState() != null) {
            i5 = getAccelerometerState().hashCode();
        } else {
            i5 = 0;
        }
        int calibrationProgress2 = (((i10 + i5) * 31) + getCalibrationProgress()) * 31;
        if (getCalibrationState() != null) {
            i6 = getCalibrationState().hashCode();
        }
        int result = calibrationProgress2 + i6;
        if (getSubIMUState() != null) {
            for (int i11 = 0; i11 < getSubIMUState().length; i11++) {
                result += getSubIMUState()[i11].hashCode();
            }
        }
        return result;
    }
}
