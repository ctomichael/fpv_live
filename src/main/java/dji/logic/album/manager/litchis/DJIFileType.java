package dji.logic.album.manager.litchis;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;

@EXClassNullAway
public enum DJIFileType {
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

    private DJIFileType(int _data) {
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
        return !(this == DNG || this == BOKEH || this == PANO) || panoCanDownload();
    }

    public boolean panoCanDownload() {
        boolean isRemotePano;
        ProductType type = DJIProductManager.getInstance().getType();
        if (type == ProductType.WM230 || type == ProductType.WM240 || type == ProductType.WM245) {
            isRemotePano = true;
        } else {
            isRemotePano = false;
        }
        if (!isRemotePano || this != PANO) {
            return false;
        }
        return true;
    }

    public boolean isRawImage() {
        return this == DNG;
    }

    public static DJIFileType find(int b) {
        DJIFileType result = UNDEFINED;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }

    public static DJIFileType find(String ext) {
        DJIFileType result = UNDEFINED;
        for (int i = 0; i < values().length; i++) {
            if (values()[i].toString().equals(ext)) {
                return values()[i];
            }
        }
        return result;
    }
}
