package dji.common.useraccount;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum UserAccountState {
    NOT_LOGGED_IN(0),
    NOT_AUTHORIZED(1),
    AUTHORIZED(2),
    TOKEN_OUT_OF_DATE(3),
    UNKNOWN(255);
    
    private int data;

    private UserAccountState(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static UserAccountState find(int b) {
        UserAccountState result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
