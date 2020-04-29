package dji.component.playback.model.audio;

public enum PlaybackAudioFrequency {
    FREQUENCY_8K(0),
    FREQUENCY_16K(1),
    FREQUENCY_24K(2),
    FREQUENCY_32K(3),
    FREQUENCY_44_DOT_1K(4),
    FREQUENCY_64K(5),
    UNKNOWN(255);
    
    private final int mValue;

    private PlaybackAudioFrequency(int value) {
        this.mValue = value;
    }

    public int value() {
        return this.mValue;
    }

    private boolean _equals(int b) {
        return this.mValue == b;
    }

    public static PlaybackAudioFrequency find(int b) {
        PlaybackAudioFrequency result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
