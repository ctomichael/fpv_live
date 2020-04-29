package dji.common.flightcontroller;

import android.support.annotation.NonNull;
import dji.fieldAnnotation.EXClassNullAway;
import java.util.Arrays;

@EXClassNullAway
public class CompassState {
    private int index = -1;
    private CompassSensorState sensorState;
    private int sensorValue;
    private CompassState[] subCompassStates;

    public interface Callback {
        void onUpdate(@NonNull CompassState compassState);
    }

    public CompassState(int subCount) {
        if (subCount > 0) {
            this.subCompassStates = new CompassState[subCount];
            for (int i = 0; i < subCount; i++) {
                CompassState state = new CompassState(0);
                state.setIndex(i);
                this.subCompassStates[i] = state;
            }
        }
    }

    public CompassSensorState getSensorState() {
        return this.sensorState;
    }

    public void setSensorState(CompassSensorState sensorState2) {
        this.sensorState = sensorState2;
    }

    public int getSensorValue() {
        return this.sensorValue;
    }

    public void setSensorValue(int sensorValue2) {
        this.sensorValue = sensorValue2;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index2) {
        this.index = index2;
    }

    public CompassState[] getSubCompassStates() {
        return this.subCompassStates;
    }

    public String toString() {
        return "CompassState{sensorState=" + this.sensorState + ", sensorValue=" + this.sensorValue + ", index=" + this.index + ", subCompassStates=" + Arrays.toString(this.subCompassStates) + '}';
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CompassState that = (CompassState) o;
        if (this.sensorValue == that.sensorValue && this.index == that.index && this.sensorState == that.sensorState) {
            return Arrays.equals(this.subCompassStates, that.subCompassStates);
        }
        return false;
    }

    public int hashCode() {
        return ((((((this.sensorState != null ? this.sensorState.hashCode() : 0) * 31) + this.sensorValue) * 31) + this.index) * 31) + Arrays.hashCode(this.subCompassStates);
    }
}
