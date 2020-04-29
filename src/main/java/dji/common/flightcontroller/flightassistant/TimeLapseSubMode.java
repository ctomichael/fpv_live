package dji.common.flightcontroller.flightassistant;

public enum TimeLapseSubMode {
    NONE(0),
    FREE(1),
    PATHWAY(2),
    POI(3),
    STRAIGHT(4),
    UNKNOWN(255);
    
    private final int data;

    private TimeLapseSubMode(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static TimeLapseSubMode find(int b) {
        TimeLapseSubMode result = UNKNOWN;
        TimeLapseSubMode[] values = values();
        for (TimeLapseSubMode tmp : values) {
            if (tmp._equals(b)) {
                return tmp;
            }
        }
        return result;
    }
}
