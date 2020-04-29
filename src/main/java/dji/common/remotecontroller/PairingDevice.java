package dji.common.remotecontroller;

public enum PairingDevice {
    AIRCRAFT(0),
    RTK_BASE_STATION(1),
    UNKONWN(255);
    
    private int value;

    private PairingDevice(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    private boolean equals(int value2) {
        return this.value == value2;
    }

    public static PairingDevice find(int value2) {
        PairingDevice[] values = values();
        for (PairingDevice arg : values) {
            if (arg.equals(value2)) {
                return arg;
            }
        }
        return UNKONWN;
    }
}
