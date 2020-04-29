package dji.midware.media.record;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface MainFrameDataListener {
    void onFrameInput(byte[] bArr, int i, int i2, boolean z, int i3, int i4);
}
