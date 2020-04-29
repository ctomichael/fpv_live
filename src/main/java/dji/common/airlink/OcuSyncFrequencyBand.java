package dji.common.airlink;

public enum OcuSyncFrequencyBand {
    FREQUENCY_BAND_DUAL(0),
    FREQUENCY_BAND_2_DOT_4_GHZ(1),
    FREQUENCY_BAND_5_DOT_8_GHZ(2),
    UNKNOWN(255);
    
    private int value;

    private OcuSyncFrequencyBand(int value2) {
        this.value = value2;
    }

    public static OcuSyncFrequencyBand find(int value2) {
        OcuSyncFrequencyBand[] values = values();
        for (OcuSyncFrequencyBand mode : values) {
            if (mode.value == value2) {
                return mode;
            }
        }
        return UNKNOWN;
    }

    public int getValue() {
        return this.value;
    }
}
