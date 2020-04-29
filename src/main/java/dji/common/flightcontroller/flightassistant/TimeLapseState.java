package dji.common.flightcontroller.flightassistant;

public enum TimeLapseState {
    IDLE(0),
    EXECUTING(1),
    PROCESSING(2),
    PAUSED(3),
    INTERRUPTED(4),
    READY_TO_GO(5),
    WATCHING(6),
    ESTIMATING(7),
    UNKNOWN(255);
    
    private final int data;

    private TimeLapseState(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static TimeLapseState find(int b) {
        TimeLapseState result = UNKNOWN;
        TimeLapseState[] values = values();
        for (TimeLapseState tmp : values) {
            if (tmp._equals(b)) {
                return tmp;
            }
        }
        return result;
    }
}
