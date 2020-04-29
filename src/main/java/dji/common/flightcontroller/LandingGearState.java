package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum LandingGearState {
    UNKNOWN((byte) 0),
    DEPLOYED((byte) 1),
    RETRACTED((byte) 3),
    DEPLOYING((byte) 2),
    RETRACTING((byte) 4),
    STOPPED((byte) 5);
    
    private byte _value = 0;

    private LandingGearState(byte value) {
        this._value = value;
    }

    public byte value() {
        return this._value;
    }

    public boolean _equals(byte b) {
        return this._value == b;
    }

    public static LandingGearState find(byte b) {
        LandingGearState result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
