package dji.common.airlink;

public enum OcuSyncUnit {
    IMPERIAL(0),
    METRIC(1),
    UNKNOWN(255);
    
    private int value;

    private OcuSyncUnit(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    private boolean _equals(int b) {
        return this.value == b;
    }

    public static OcuSyncUnit find(int value2) {
        OcuSyncUnit result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
