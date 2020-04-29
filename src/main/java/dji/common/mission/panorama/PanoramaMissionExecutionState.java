package dji.common.mission.panorama;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class PanoramaMissionExecutionState {
    private int currentSavedNumber;
    private int currentShotNumber;
    private int totalNumber;

    public PanoramaMissionExecutionState(int totalNumber2, int currentShotNumber2, int currentSavedNumber2) {
        this.totalNumber = totalNumber2;
        this.currentShotNumber = currentShotNumber2;
        this.currentSavedNumber = currentSavedNumber2;
    }

    public int getTotalNumber() {
        return this.totalNumber;
    }

    public int getCurrentShotNumber() {
        return this.currentShotNumber;
    }

    public int getCurrentSavedNumber() {
        return this.currentSavedNumber;
    }

    public int hashCode() {
        return (((this.totalNumber * 31) + this.currentShotNumber) * 31) + this.currentSavedNumber;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        boolean isEqual = false;
        if (obj instanceof PanoramaMissionExecutionState) {
            PanoramaMissionExecutionState object = (PanoramaMissionExecutionState) obj;
            if (object.totalNumber == this.totalNumber && object.currentShotNumber == this.currentShotNumber && object.currentSavedNumber == this.currentSavedNumber) {
                isEqual = true;
            } else {
                isEqual = false;
            }
        }
        return isEqual;
    }
}
