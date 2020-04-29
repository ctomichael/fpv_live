package dji.common.flightcontroller;

public enum FixedWingControl {
    READY(0),
    ENTER(1),
    EXIT(2),
    OTHER(100);
    
    private final int data;

    private FixedWingControl(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static FixedWingControl find(int b) {
        FixedWingControl result = READY;
        FixedWingControl[] values = values();
        for (FixedWingControl tmp : values) {
            if (tmp._equals(b)) {
                return tmp;
            }
        }
        return result;
    }
}
