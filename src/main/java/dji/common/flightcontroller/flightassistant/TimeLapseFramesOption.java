package dji.common.flightcontroller.flightassistant;

public enum TimeLapseFramesOption {
    FRAMES_25(0),
    FRAMES_30(1);
    
    private final int data;

    private TimeLapseFramesOption(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static TimeLapseFramesOption find(int b) {
        TimeLapseFramesOption result = FRAMES_25;
        TimeLapseFramesOption[] values = values();
        for (TimeLapseFramesOption tmp : values) {
            if (tmp._equals(b)) {
                return tmp;
            }
        }
        return result;
    }
}
