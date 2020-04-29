package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class RedundancySensorUsedState {
    private int accIndex;
    private int baroIndex;
    private int gpsIndex;
    private int gyroIndex;
    private boolean isRTKUsed;
    private boolean isRadarUsed;
    private boolean isUSUsed;
    private boolean isVOUsed;
    private int magIndex;
    private int nsIndex;

    public RedundancySensorUsedState(Builder builder) {
        this.nsIndex = builder.nsIndex;
        this.accIndex = builder.accIndex;
        this.gyroIndex = builder.gyroIndex;
        this.magIndex = builder.magIndex;
        this.gpsIndex = builder.gpsIndex;
        this.baroIndex = builder.baroIndex;
        this.isRTKUsed = builder.isRTKUsed;
        this.isVOUsed = builder.isVOUsed;
        this.isUSUsed = builder.isUSUsed;
        this.isRadarUsed = builder.isRadarUsed;
    }

    public int getNsIndex() {
        return this.nsIndex;
    }

    public int getAccIndex() {
        return this.accIndex;
    }

    public int getGyroIndex() {
        return this.gyroIndex;
    }

    public int getMagIndex() {
        return this.magIndex;
    }

    public int getGpsIndex() {
        return this.gpsIndex;
    }

    public int getBaroIndex() {
        return this.baroIndex;
    }

    public boolean isRTKUsed() {
        return this.isRTKUsed;
    }

    public boolean isVOUsed() {
        return this.isVOUsed;
    }

    public boolean isUSUsed() {
        return this.isUSUsed;
    }

    public boolean isRadarUsed() {
        return this.isRadarUsed;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RedundancySensorUsedState that = (RedundancySensorUsedState) o;
        if (this.nsIndex != that.nsIndex || this.accIndex != that.accIndex || this.gyroIndex != that.gyroIndex || this.magIndex != that.magIndex || this.gpsIndex != that.gpsIndex || this.baroIndex != that.baroIndex || this.isRTKUsed != that.isRTKUsed || this.isVOUsed != that.isVOUsed || this.isUSUsed != that.isUSUsed) {
            return false;
        }
        if (this.isRadarUsed != that.isRadarUsed) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int i;
        int i2;
        int i3;
        int i4 = 1;
        int i5 = ((((((((((this.nsIndex * 31) + this.accIndex) * 31) + this.gyroIndex) * 31) + this.magIndex) * 31) + this.gpsIndex) * 31) + this.baroIndex) * 31;
        if (this.isRTKUsed) {
            i = 1;
        } else {
            i = 0;
        }
        int i6 = (i5 + i) * 31;
        if (this.isVOUsed) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        int i7 = (i6 + i2) * 31;
        if (this.isUSUsed) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        int i8 = (i7 + i3) * 31;
        if (!this.isRadarUsed) {
            i4 = 0;
        }
        return i8 + i4;
    }

    public String toString() {
        return "RedundancySensorUsedState{nsIndex=" + this.nsIndex + ", accIndex=" + this.accIndex + ", gyroIndex=" + this.gyroIndex + ", magIndex=" + this.magIndex + ", gpsIndex=" + this.gpsIndex + ", baroIndex=" + this.baroIndex + ", isRTKUsed=" + this.isRTKUsed + ", isVOUsed=" + this.isVOUsed + ", isUSUsed=" + this.isUSUsed + ", isRadarUsed=" + this.isRadarUsed + '}';
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public int accIndex;
        /* access modifiers changed from: private */
        public int baroIndex;
        /* access modifiers changed from: private */
        public int gpsIndex;
        /* access modifiers changed from: private */
        public int gyroIndex;
        /* access modifiers changed from: private */
        public boolean isRTKUsed;
        /* access modifiers changed from: private */
        public boolean isRadarUsed;
        /* access modifiers changed from: private */
        public boolean isUSUsed;
        /* access modifiers changed from: private */
        public boolean isVOUsed;
        /* access modifiers changed from: private */
        public int magIndex;
        /* access modifiers changed from: private */
        public int nsIndex;

        public Builder setNsIndex(int nsIndex2) {
            this.nsIndex = nsIndex2;
            return this;
        }

        public Builder setAccIndex(int accIndex2) {
            this.accIndex = accIndex2;
            return this;
        }

        public Builder setGyroIndex(int gyroIndex2) {
            this.gyroIndex = gyroIndex2;
            return this;
        }

        public Builder setMagIndex(int magIndex2) {
            this.magIndex = magIndex2;
            return this;
        }

        public Builder setGpsIndex(int gpsIndex2) {
            this.gpsIndex = gpsIndex2;
            return this;
        }

        public Builder setBaroIndex(int baroIndex2) {
            this.baroIndex = baroIndex2;
            return this;
        }

        public Builder setRTKUsed(boolean RTKUsed) {
            this.isRTKUsed = RTKUsed;
            return this;
        }

        public Builder setVOUsed(boolean VOUsed) {
            this.isVOUsed = VOUsed;
            return this;
        }

        public Builder setUSUsed(boolean USUsed) {
            this.isUSUsed = USUsed;
            return this;
        }

        public Builder setRadarUsed(boolean radarUsed) {
            this.isRadarUsed = radarUsed;
            return this;
        }

        public RedundancySensorUsedState build() {
            return new RedundancySensorUsedState(this);
        }
    }
}
