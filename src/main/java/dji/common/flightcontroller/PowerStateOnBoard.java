package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class PowerStateOnBoard {
    private int electricCurrent = 0;
    private boolean overLoaded = false;
    private boolean powerOn = false;

    public PowerStateOnBoard() {
    }

    public PowerStateOnBoard(boolean powerOn2) {
        this.powerOn = powerOn2;
    }

    public boolean isPowerOn() {
        return this.powerOn;
    }

    public void setPowerOn(boolean powerOn2) {
        this.powerOn = powerOn2;
    }

    public boolean isOverLoaded() {
        return this.overLoaded;
    }

    public void setOverLoaded(boolean overLoaded2) {
        this.overLoaded = overLoaded2;
    }

    public int getElectricCurrent() {
        return this.electricCurrent;
    }

    public void setElectricCurrent(int electricCurrent2) {
        this.electricCurrent = electricCurrent2;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (!(o instanceof PowerStateOnBoard)) {
            return false;
        }
        PowerStateOnBoard that = (PowerStateOnBoard) o;
        if (this.powerOn != that.powerOn || this.overLoaded != that.overLoaded) {
            return false;
        }
        if (this.electricCurrent != that.electricCurrent) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i = 1;
        if (this.powerOn) {
            result = 1;
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (!this.overLoaded) {
            i = 0;
        }
        return ((i2 + i) * 31) + this.electricCurrent;
    }
}
