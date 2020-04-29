package dji.common.remotecontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum PairingState {
    UNPAIRED(0),
    PAIRING(1),
    PAIRED(2),
    UNKNOWN(255);
    
    private int value;

    private PairingState(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    private boolean _equals(int b) {
        return this.value == b;
    }

    public static PairingState find(int value2) {
        PairingState result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
