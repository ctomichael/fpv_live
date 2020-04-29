package dji.common.flightcontroller;

public class LEDsSettings {
    private final boolean frontLEDsOn;
    private final boolean navigationLEDsOn;
    private final boolean rearLEDsOn;
    private final boolean statusIndicatorOn;

    private LEDsSettings(Builder builder) {
        this.frontLEDsOn = builder.frontLEDsOn;
        this.rearLEDsOn = builder.rearLEDsOn;
        this.statusIndicatorOn = builder.statusIndicatorOn;
        this.navigationLEDsOn = builder.navigationLEDsOn;
    }

    public boolean areFrontLEDsOn() {
        return this.frontLEDsOn;
    }

    public boolean areRearLEDsOn() {
        return this.rearLEDsOn;
    }

    public boolean isStatusIndicatorOn() {
        return this.statusIndicatorOn;
    }

    public boolean areNavigationLEDsOn() {
        return this.navigationLEDsOn;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LEDsSettings that = (LEDsSettings) o;
        if (this.frontLEDsOn != that.frontLEDsOn || this.rearLEDsOn != that.rearLEDsOn) {
            return false;
        }
        if (isStatusIndicatorOn() != that.isStatusIndicatorOn()) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2 = 1;
        if (this.frontLEDsOn) {
            result = 1;
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (this.rearLEDsOn) {
            i = 1;
        } else {
            i = 0;
        }
        int i4 = (i3 + i) * 31;
        if (!isStatusIndicatorOn()) {
            i2 = 0;
        }
        return i4 + i2;
    }

    public Byte getByte() {
        byte res = 0;
        if (this.frontLEDsOn) {
            res = (byte) 1;
        }
        if (this.rearLEDsOn) {
            res = (byte) (res + 2);
        }
        if (this.statusIndicatorOn) {
            res = (byte) (res + 4);
        }
        if (this.navigationLEDsOn) {
            res = (byte) (res + Tnaf.POW_2_WIDTH);
        }
        return Byte.valueOf(res);
    }

    public static LEDsSettings generateLEDsEnabledSettings(int data) {
        boolean frontLEDEnabled;
        boolean rearLEDEnabled;
        boolean statusIndicatorOn2;
        boolean navigationLEDsOn2 = true;
        if ((data & 1) == 1) {
            frontLEDEnabled = true;
        } else {
            frontLEDEnabled = false;
        }
        if (((data >> 1) & 1) == 1) {
            rearLEDEnabled = true;
        } else {
            rearLEDEnabled = false;
        }
        if (((data >> 2) & 1) == 1) {
            statusIndicatorOn2 = true;
        } else {
            statusIndicatorOn2 = false;
        }
        if (((data >> 4) & 1) != 1) {
            navigationLEDsOn2 = false;
        }
        return new Builder().frontLEDsOn(frontLEDEnabled).rearLEDsOn(rearLEDEnabled).statusIndicatorOn(statusIndicatorOn2).navigationLEDsOn(navigationLEDsOn2).build();
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public boolean frontLEDsOn;
        /* access modifiers changed from: private */
        public boolean navigationLEDsOn;
        /* access modifiers changed from: private */
        public boolean rearLEDsOn;
        /* access modifiers changed from: private */
        public boolean statusIndicatorOn;

        public Builder frontLEDsOn(boolean frontLEDsOn2) {
            this.frontLEDsOn = frontLEDsOn2;
            return this;
        }

        public Builder rearLEDsOn(boolean rearLEDsOn2) {
            this.rearLEDsOn = rearLEDsOn2;
            return this;
        }

        public Builder statusIndicatorOn(boolean statusIndicatorOn2) {
            this.statusIndicatorOn = statusIndicatorOn2;
            return this;
        }

        public Builder navigationLEDsOn(boolean nagivationLEDsOn) {
            this.navigationLEDsOn = nagivationLEDsOn;
            return this;
        }

        public LEDsSettings build() {
            return new LEDsSettings(this);
        }
    }
}
