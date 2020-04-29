package dji.common.flightcontroller.rtk;

public enum Mode {
    BS_4G_SDR(1),
    UNKNOWN(255);
    
    private final int value;

    private Mode(int value2) {
        this.value = value2;
    }

    public int getValue() {
        return this.value;
    }

    private boolean _equals(int b) {
        return this.value == b;
    }

    public static Mode find(int value2) {
        Mode result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
