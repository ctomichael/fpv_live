package dji.common.flightcontroller.adsb;

import android.support.annotation.NonNull;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class AirSenseSystemInformation {
    private final AirSenseAirplaneState[] airplaneStates;
    private final AirSenseWarningLevel warningLevel;

    public interface Callback {
        void onUpdate(@NonNull AirSenseSystemInformation airSenseSystemInformation);
    }

    public AirSenseSystemInformation(AirSenseWarningLevel warningLevel2, AirSenseAirplaneState[] airplaneStates2) {
        this.warningLevel = warningLevel2;
        this.airplaneStates = airplaneStates2;
    }

    public AirSenseWarningLevel getWarningLevel() {
        return this.warningLevel;
    }

    public AirSenseAirplaneState[] getAirplaneStates() {
        return (AirSenseAirplaneState[]) this.airplaneStates.clone();
    }

    public int hashCode() {
        int result;
        int i = 0;
        if (this.warningLevel != null) {
            result = this.warningLevel.hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.airplaneStates != null) {
            i = this.airplaneStates.hashCode();
        }
        return i2 + i;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof AirSenseSystemInformation)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (this.warningLevel != ((AirSenseSystemInformation) o).warningLevel) {
            return false;
        }
        if (this.airplaneStates == null || ((AirSenseSystemInformation) o).airplaneStates == null) {
            if (this.airplaneStates == null || ((AirSenseSystemInformation) o).airplaneStates == null) {
                return false;
            }
        } else if (this.airplaneStates.length != ((AirSenseSystemInformation) o).airplaneStates.length) {
            return false;
        } else {
            for (int i = 0; i < this.airplaneStates.length; i++) {
                if (!this.airplaneStates[i].equals(((AirSenseSystemInformation) o).airplaneStates[i])) {
                    return false;
                }
            }
        }
        return true;
    }
}
