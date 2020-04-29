package dji.common.remotecontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum RequestGimbalControlResult {
    ACCEPTED(0),
    REJECTED(1),
    TIMEOUT(2),
    GETTED(3),
    UNKNOWN(4);
    
    private int value;

    private RequestGimbalControlResult(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static RequestGimbalControlResult find(int value2) {
        RequestGimbalControlResult result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
