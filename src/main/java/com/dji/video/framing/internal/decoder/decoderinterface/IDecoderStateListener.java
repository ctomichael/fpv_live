package com.dji.video.framing.internal.decoder.decoderinterface;

import com.dji.video.framing.internal.decoder.DJIVideoDecoder;

public interface IDecoderStateListener {
    void onStateChange(DJIVideoDecoder.VideoDecoderState videoDecoderState);
}
