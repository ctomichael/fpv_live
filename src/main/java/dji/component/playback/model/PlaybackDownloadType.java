package dji.component.playback.model;

public enum PlaybackDownloadType {
    ORG(0),
    THM(1),
    SCR(2),
    CLIP(3),
    Stream(4),
    PANORAMA(5),
    PANORAMA_SCR(6),
    PANORAMA_THM(7),
    TIME_LAPSE(8),
    MP4(9),
    EXIF(10),
    EXIF_NEW(11),
    BOKEH(97),
    BOKEH_THM(98),
    BOKEH_SCR(99),
    UNDEFINED(100);
    
    private int data;

    private PlaybackDownloadType(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static PlaybackDownloadType find(int b) {
        PlaybackDownloadType result = UNDEFINED;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
