package com.dji.video.framing.internal.decoder.decoderinterface;

public interface IDJIVideoDecoder {
    long getLastFrameOutTime();

    int getVideoHeight();

    int getVideoWidth();
}
