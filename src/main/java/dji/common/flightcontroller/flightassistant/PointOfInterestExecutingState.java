package dji.common.flightcontroller.flightassistant;

public enum PointOfInterestExecutingState {
    NONE(0),
    IDLE(1),
    WATCHING(2),
    READY_TO_ESTIMATE(3),
    ESTIMATING(4),
    EXECUTING(5),
    PAUSED(6),
    UNKNOWN(255);
    
    int data;

    private PointOfInterestExecutingState(int value) {
        this.data = value;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static PointOfInterestExecutingState find(int b) {
        PointOfInterestExecutingState result = UNKNOWN;
        PointOfInterestExecutingState[] values = values();
        for (PointOfInterestExecutingState tmp : values) {
            if (tmp._equals(b)) {
                return tmp;
            }
        }
        return result;
    }
}
