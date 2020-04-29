package dji.component.playback.model.audio;

public enum PlaybackAudioSampleBit {
    BIT_8(0),
    BIT_16(1),
    UNKNOWN(255);
    
    private final int mValue;

    private PlaybackAudioSampleBit(int value) {
        this.mValue = value;
    }

    public int value() {
        return this.mValue;
    }

    private boolean _equals(int b) {
        return this.mValue == b;
    }

    public static PlaybackAudioSampleBit find(int b) {
        PlaybackAudioSampleBit result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
