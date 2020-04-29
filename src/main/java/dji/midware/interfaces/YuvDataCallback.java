package dji.midware.interfaces;

import dji.fieldAnnotation.EXClassNullAway;
import java.nio.ByteBuffer;

@EXClassNullAway
public interface YuvDataCallback {
    void onYuvDataReceived(ByteBuffer byteBuffer, int i, int i2, int i3);
}
