package dji.common.flightcontroller.adsb;

public enum AirSenseAvoidanceMode {
    NONE(0),
    LANDING(1),
    AVOIDING(2),
    UNKNOWN(255);
    
    private final int data;

    private AirSenseAvoidanceMode(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    private boolean _equals(int b) {
        return this.data == b;
    }

    public static AirSenseAvoidanceMode find(int b) {
        AirSenseAvoidanceMode result = UNKNOWN;
        AirSenseAvoidanceMode[] values = values();
        for (AirSenseAvoidanceMode tmp : values) {
            if (tmp._equals(b)) {
                return tmp;
            }
        }
        return result;
    }
}
