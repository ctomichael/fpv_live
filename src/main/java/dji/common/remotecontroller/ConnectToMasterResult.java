package dji.common.remotecontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum ConnectToMasterResult {
    ACCEPTED(0),
    PASSWORD_ERROR(1),
    REJECTED(2),
    MAXIMUM_CAPACITY(3),
    TIMEOUT(4),
    UNKNOWN(5);
    
    private int value;

    private ConnectToMasterResult(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static ConnectToMasterResult find(int value2) {
        ConnectToMasterResult result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
