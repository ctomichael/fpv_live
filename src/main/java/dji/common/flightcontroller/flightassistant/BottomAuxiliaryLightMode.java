package dji.common.flightcontroller.flightassistant;

public enum BottomAuxiliaryLightMode {
    AUTO(0),
    ON(1),
    OFF(2),
    BEACON(3),
    UNKNOWN(255);
    
    private final int data;

    private BottomAuxiliaryLightMode(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static BottomAuxiliaryLightMode find(int b) {
        BottomAuxiliaryLightMode result = UNKNOWN;
        BottomAuxiliaryLightMode[] values = values();
        for (BottomAuxiliaryLightMode tmp : values) {
            if (tmp._equals(b)) {
                return tmp;
            }
        }
        return result;
    }
}
