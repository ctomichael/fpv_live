package dji.common.remotecontroller;

public enum SoftSwitchJoyStickMode {
    SPORT(0),
    POSITION(1),
    TRIPOD(2),
    UNKNOWN(255);
    
    private int value;

    private SoftSwitchJoyStickMode(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static SoftSwitchJoyStickMode find(int value2) {
        SoftSwitchJoyStickMode result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
