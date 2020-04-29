package com.dji.video.framing.internal.decoder.decoderinterface;

public interface IReceiveDataCallback {
    void oneFrameComeIn();

    void resetVideoSurface(int i, int i2);
}
