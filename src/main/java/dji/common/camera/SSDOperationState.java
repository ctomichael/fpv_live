package dji.common.camera;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum SSDOperationState {
    NOT_FOUND(0),
    IDLE(1),
    SAVING(2),
    FORMATTING(4),
    INITIALIZING(5),
    ERROR(7),
    FULL(8),
    POOR_CONNECTION(9),
    SWITCHING_LICENSE(10),
    FORMATTING_REQUIRED(11),
    NOT_INITIALIZED(12),
    INVALID_FILE_SYSTEM(13),
    UNKNOWN(255);
    
    private int value;

    private SSDOperationState(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static SSDOperationState find(int value2) {
        SSDOperationState result = UNKNOWN;
        int i = 0;
        while (true) {
            if (i >= values().length) {
                break;
            } else if (values()[i]._equals(value2)) {
                result = values()[i];
                break;
            } else {
                i++;
            }
        }
        if (value2 == 0) {
            return UNKNOWN;
        }
        if (value2 == 6) {
            return ERROR;
        }
        return result;
    }
}
