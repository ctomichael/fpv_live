package dji.common.remotecontroller;

public enum MultiDeviceState {
    UNPAIRED(0),
    PAIRING(1),
    PAIRED(2),
    DISCONNECTED(3),
    CONNECTED(4),
    UNKNOWN(255);
    
    private int value;

    private MultiDeviceState(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    private boolean _equals(int b) {
        return this.value == b;
    }

    public static MultiDeviceState find(int value2) {
        MultiDeviceState result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
