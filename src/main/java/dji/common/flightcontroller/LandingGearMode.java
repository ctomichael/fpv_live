package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum LandingGearMode {
    TRANSPORT(0),
    AUTO(1),
    MANUAL(2),
    UNKNOWN(3);
    
    private int data;

    private LandingGearMode(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static LandingGearMode find(int b) {
        LandingGearMode result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
