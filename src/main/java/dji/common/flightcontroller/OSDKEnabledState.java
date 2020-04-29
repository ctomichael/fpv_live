package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class OSDKEnabledState {
    private boolean powerOn = false;

    public OSDKEnabledState() {
    }

    public OSDKEnabledState(boolean powerOn2) {
        this.powerOn = powerOn2;
    }

    public boolean isPowerOn() {
        return this.powerOn;
    }

    public void setPowerOn(boolean powerOn2) {
        this.powerOn = powerOn2;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OSDKEnabledState)) {
            return false;
        }
        if (this.powerOn != ((OSDKEnabledState) o).powerOn) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.powerOn ? 1 : 0;
    }
}
