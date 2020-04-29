package dji.component.playback.model.video;

public enum PlaybackVideoType {
    NORMAL(0),
    TIME_LAPSE(1),
    SPEED_CHANGE(2),
    QUICK_SHOT(3),
    HLG(4),
    HYPER_LAPSE(5),
    UNKNOWN(255);
    
    private final int mValue;

    private PlaybackVideoType(int value) {
        this.mValue = value;
    }

    public int value() {
        return this.mValue;
    }

    private boolean _equals(int b) {
        return this.mValue == b;
    }

    public static PlaybackVideoType find(int b) {
        PlaybackVideoType result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
