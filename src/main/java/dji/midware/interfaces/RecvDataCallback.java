package dji.midware.interfaces;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface RecvDataCallback {
    void oneFrameComeIn();

    void resetVideoSurface(int i, int i2);
}
