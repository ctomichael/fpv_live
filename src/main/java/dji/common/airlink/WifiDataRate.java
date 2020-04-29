package dji.common.airlink;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum WifiDataRate {
    RATE_1_MBPS(1),
    RATE_2_MBPS(2),
    RATE_4_MBPS(4),
    UNKNOWN(255);
    
    private int value;

    private WifiDataRate(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    private boolean _equals(int b) {
        return this.value == b;
    }

    public static WifiDataRate find(int value2) {
        WifiDataRate result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
