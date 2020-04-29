package dji.common.airlink;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum LightbridgeDataRate {
    BANDWIDTH_4_MBPS(1),
    BANDWIDTH_6_MBPS(2),
    BANDWIDTH_8_MBPS(3),
    BANDWIDTH_10_MBPS(4),
    UNKNOWN(255);
    
    private int value;

    private LightbridgeDataRate(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    private boolean _equals(int b) {
        return this.value == b;
    }

    public static LightbridgeDataRate find(int value2) {
        LightbridgeDataRate result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
