package dji.component.playback.model;

public enum PlaybackFileType {
    JPG(0),
    DNG(1),
    MOV(2),
    MP4(3),
    PANO(4),
    TIF(5),
    BOKEH(6),
    SEQ(8),
    TIFF_SEQ(9),
    AUDIO(10),
    UNDEFINED(100);
    
    private int data;

    private PlaybackFileType(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public boolean isVideo() {
        return this == MOV || this == MP4;
    }

    public boolean isPicture() {
        return (this == MOV || this == MP4 || this == UNDEFINED) ? false : true;
    }

    public boolean isPano() {
        return this == PANO;
    }

    public boolean canDownload() {
        return (this == DNG || this == BOKEH || this == PANO) ? false : true;
    }

    public boolean isRawImage() {
        return this == DNG;
    }

    public static PlaybackFileType find(int b) {
        PlaybackFileType result = UNDEFINED;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }

    public static PlaybackFileType find(String ext) {
        PlaybackFileType result = UNDEFINED;
        for (int i = 0; i < values().length; i++) {
            if (values()[i].toString().equals(ext)) {
                return values()[i];
            }
        }
        return result;
    }
}
