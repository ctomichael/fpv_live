package dji.component.playback.model.video;

public enum PlaybackVideoEncodeType {
    H264(0),
    H265(1),
    MJPEG(2),
    NUM(3),
    RESERVE(255);
    
    private static volatile PlaybackVideoEncodeType[] sValues = null;
    private int data;
    public String names;

    private PlaybackVideoEncodeType(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static PlaybackVideoEncodeType find(int b) {
        if (sValues == null) {
            sValues = values();
        }
        PlaybackVideoEncodeType result = RESERVE;
        for (int i = 0; i < sValues.length; i++) {
            if (sValues[i]._equals(b)) {
                return sValues[i];
            }
        }
        return result;
    }
}
