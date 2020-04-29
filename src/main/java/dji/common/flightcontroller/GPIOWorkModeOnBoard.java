package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum GPIOWorkModeOnBoard {
    FLOAT_INPUT(0),
    PULL_UP_INPUT(1),
    PULL_DOWN_INPUT(2),
    PUSH_PULL_OUTPUT(3),
    OTHER(255);
    
    private int data;

    private GPIOWorkModeOnBoard(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static GPIOWorkModeOnBoard find(int b) {
        GPIOWorkModeOnBoard result = OTHER;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
