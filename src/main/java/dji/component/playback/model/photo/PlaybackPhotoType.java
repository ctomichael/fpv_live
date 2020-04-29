package dji.component.playback.model.photo;

public enum PlaybackPhotoType {
    STOP(0),
    SINGLE(1),
    HDR(2),
    FULLVIEW(3),
    BURST(4),
    AEB(5),
    TIME(6),
    APP_FULLVIEW(7),
    TRACKING(8),
    RAWBURST(9),
    HDR_PLUS(10),
    HYPER_NIGHT(11),
    HYPER_LAPSE(12),
    PANORAMA_TRUE(13),
    BOKEH(98),
    PANORAMA(99),
    OTHER(255);
    
    private int data;

    private PlaybackPhotoType(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static PlaybackPhotoType find(int b) {
        PlaybackPhotoType result = OTHER;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
