package dji.component.playback.model.video;

public enum PlaybackVideoSubType {
    NONE(0),
    CIRCLE(1),
    DIAGONAL(2),
    SPIRAL(3),
    ROCKY(4),
    COMET(6),
    PLANET(8),
    DOLLY_ZOOM(10),
    UNKNOWN(255);
    
    private final int mValue;

    private PlaybackVideoSubType(int value) {
        this.mValue = value;
    }

    public int value() {
        return this.mValue;
    }

    private boolean _equals(int b) {
        return this.mValue == b;
    }

    public static PlaybackVideoSubType find(int b) {
        PlaybackVideoSubType result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
