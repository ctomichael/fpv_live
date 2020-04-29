package com.dji.video.framing.internal.decoder;

import com.dji.video.framing.internal.decoder.ExtraImageReaderManager;

final /* synthetic */ class DJIVideoDecoder$$Lambda$1 implements Runnable {
    private final DJIVideoDecoder arg$1;
    private final ExtraImageReaderManager.ExtraImageReaderCallback arg$2;

    DJIVideoDecoder$$Lambda$1(DJIVideoDecoder dJIVideoDecoder, ExtraImageReaderManager.ExtraImageReaderCallback extraImageReaderCallback) {
        this.arg$1 = dJIVideoDecoder;
        this.arg$2 = extraImageReaderCallback;
    }

    public void run() {
        this.arg$1.lambda$startGetAsyncRgbaData$1$DJIVideoDecoder(this.arg$2);
    }
}
