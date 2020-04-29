package dji.common.battery;

public enum SelfHeatingState {
    IDLE(0),
    WARMING_UP(1),
    PRESERVING(2),
    UNKNOWN(255);
    
    private int data;

    private SelfHeatingState(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    private boolean _equals(int b) {
        return this.data == b;
    }

    public static SelfHeatingState find(int b) {
        SelfHeatingState result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
