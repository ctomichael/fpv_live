package dji.common.airlink;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum WiFiFrequencyBand {
    FREQUENCY_BAND_ONLY_2_DOT_4(-1),
    FREQUENCY_BAND_2_DOT_4_GHZ(0),
    FREQUENCY_BAND_5_GHZ(1),
    FREQUENCY_BAND_DUAL(2),
    UNKNOWN(255);
    
    private int value;

    private WiFiFrequencyBand(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    private boolean _equals(int b) {
        return this.value == b;
    }

    public static WiFiFrequencyBand find(int value2) {
        WiFiFrequencyBand result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
