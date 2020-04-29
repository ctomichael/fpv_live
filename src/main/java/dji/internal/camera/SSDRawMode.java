package dji.internal.camera;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum SSDRawMode {
    JPEG_LOSSLESS(0),
    DRAW(1),
    PRORSE_RAW(2),
    PRORES_RAW_HQ(3),
    PRORES_HQ422(16),
    PRORES_HQ444(17),
    PRORSE_OFF(32),
    UNKNOW(255);
    
    private int data;

    private SSDRawMode(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    private boolean _equals(int b) {
        return this.data == b;
    }

    public static SSDRawMode find(int b) {
        SSDRawMode result = PRORSE_OFF;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
