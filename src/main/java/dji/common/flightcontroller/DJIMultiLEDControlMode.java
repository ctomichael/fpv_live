package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJIMultiLEDControlMode {
    private boolean backLEDs;
    private boolean frontLEDs;
    private boolean navigationLED;
    private boolean statusLED;

    public void setFrontLEDs(boolean frontLEDs2) {
        this.frontLEDs = frontLEDs2;
    }

    public void setBackLEDs(boolean backLEDs2) {
        this.backLEDs = backLEDs2;
    }

    public void setNavigationLED(boolean navigationLED2) {
        this.navigationLED = navigationLED2;
    }

    public void setStatusLED(boolean statusLED2) {
        this.statusLED = statusLED2;
    }

    public boolean isFrontLEDs() {
        return this.frontLEDs;
    }

    public boolean isBackLEDs() {
        return this.backLEDs;
    }

    public boolean isNavigationLED() {
        return this.navigationLED;
    }

    public boolean isStatusLED() {
        return this.statusLED;
    }

    public DJIMultiLEDControlMode(int flag) {
        boolean z;
        boolean z2;
        boolean z3 = true;
        this.frontLEDs = (flag & 1) == 1;
        if (((flag >> 1) & 1) == 1) {
            z = true;
        } else {
            z = false;
        }
        this.backLEDs = z;
        if (((flag >> 2) & 1) == 1) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.navigationLED = z2;
        this.statusLED = ((flag >> 3) & 1) != 1 ? false : z3;
    }

    public DJIMultiLEDControlMode() {
        this(255);
    }

    public byte getDate() {
        byte res = 0;
        if (this.frontLEDs) {
            res = (byte) 1;
        }
        if (this.backLEDs) {
            res = (byte) (res + 2);
        }
        if (this.navigationLED) {
            res = (byte) (res + 4);
        }
        if (this.statusLED) {
            return (byte) (res + 8);
        }
        return res;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3 = 1;
        if (this.frontLEDs) {
            result = 1;
        } else {
            result = 0;
        }
        int i4 = result * 31;
        if (this.backLEDs) {
            i = 1;
        } else {
            i = 0;
        }
        int i5 = (i4 + i) * 31;
        if (this.navigationLED) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        int i6 = (i5 + i2) * 31;
        if (!this.statusLED) {
            i3 = 0;
        }
        return i6 + i3;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof DJIMultiLEDControlMode)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        return this.frontLEDs == ((DJIMultiLEDControlMode) o).frontLEDs && this.backLEDs == ((DJIMultiLEDControlMode) o).backLEDs && this.navigationLED == ((DJIMultiLEDControlMode) o).navigationLED && this.statusLED == ((DJIMultiLEDControlMode) o).statusLED;
    }

    public String toString() {
        return "frontLEDs=" + this.frontLEDs + " backLEDs=" + this.backLEDs + " navigationLED=" + this.navigationLED + " statusLED=" + this.statusLED;
    }
}
