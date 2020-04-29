package dji.internal.narrowband;

import dji.midware.data.model.P3.DataNarrowBandGetPushStateInfo;

public class WorkBand {
    private final int BAND_TYPE_NUM = 8;
    private boolean[] available = new boolean[8];
    private boolean[] working = new boolean[8];

    public WorkBand(DataNarrowBandGetPushStateInfo info) {
        for (int i = 0; i < 8; i++) {
            this.available[i] = info.isBandAvailable(i);
            this.working[i] = info.isBandWorking(i);
        }
    }

    public boolean isWorkingOnBand(Band band) {
        return this.working[band.getValue()];
    }

    public boolean isBandAvailable(Band band) {
        return this.available[band.getValue()];
    }

    public boolean isWorkingOnBand(int bandIndex) {
        return this.working[bandIndex];
    }

    public boolean isBandAvailable(int bandIndex) {
        return this.available[bandIndex];
    }

    public String getStringAvailable() {
        StringBuilder s1 = new StringBuilder("Available: ");
        for (int i = 0; i < 8; i++) {
            s1.append(this.available[i] ? "1" : "0");
        }
        return s1.toString();
    }

    public String getStringWorking() {
        StringBuilder s2 = new StringBuilder("WorkingOn: ");
        for (int i = 0; i < 8; i++) {
            s2.append(this.working[i] ? "1" : "0");
        }
        return s2.toString();
    }

    public String toString() {
        return getStringAvailable() + " " + getStringWorking();
    }

    public enum Band {
        M_400(0),
        M_800(1),
        M_900(2),
        G_2_4(3),
        G_5_2(4),
        G_5_8(5),
        RESERVE(7);
        
        private int value;

        private Band(int value2) {
            this.value = value2;
        }

        public int getValue() {
            return this.value;
        }
    }
}
