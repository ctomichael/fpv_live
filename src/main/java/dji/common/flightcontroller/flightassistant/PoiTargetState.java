package dji.common.flightcontroller.flightassistant;

public enum PoiTargetState {
    NONE(0),
    CANT_NOT_EXECUTE(1),
    IDLE(2),
    WATCHING(3),
    READY_TO_ESTIMATE(4),
    ESTIMATING(5),
    EXECUTING(6),
    PAUSED(7),
    UNKNOWN(255);
    
    int data;

    private PoiTargetState(int value) {
        this.data = value;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static PoiTargetState find(int b) {
        PoiTargetState result = UNKNOWN;
        PoiTargetState[] values = values();
        for (PoiTargetState tmp : values) {
            if (tmp._equals(b)) {
                return tmp;
            }
        }
        return result;
    }
}
