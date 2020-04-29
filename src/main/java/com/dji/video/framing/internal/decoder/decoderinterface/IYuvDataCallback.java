package com.dji.video.framing.internal.decoder.decoderinterface;

import java.nio.ByteBuffer;

public interface IYuvDataCallback {
    void onYuvDataReceived(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, int i5, long j);
}
