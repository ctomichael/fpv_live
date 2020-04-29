package dji.midware.media.transcode.online;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface DecoderTranscodeListener {
    void feedRawFrame(Frame frame);

    Frame getRawFrameContainer();

    boolean needMakeIFrame(long j);

    void onColorFormatChanged(int i);

    void onSpsPpsChanged(byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3, int i3);

    void onWidthHeightChanged(int i, int i2);
}
