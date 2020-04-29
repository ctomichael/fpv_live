package dji.component.playback.model.audio;

public enum PlaybackAudioType {
    WAV(0),
    MP3(1),
    ACC(2),
    UNKNOWN(255);
    
    private final int mValue;

    private PlaybackAudioType(int value) {
        this.mValue = value;
    }

    public int value() {
        return this.mValue;
    }

    private boolean _equals(int b) {
        return this.mValue == b;
    }

    public static PlaybackAudioType find(int b) {
        PlaybackAudioType result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
