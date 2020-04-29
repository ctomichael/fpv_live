package dji.midware.media.record;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface H264FrameListener {
    void onH264FrameInput(byte[] bArr, int i, long j, boolean z);
}
