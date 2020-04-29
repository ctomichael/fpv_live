package com.dji.video.framing.internal.decoder;

final /* synthetic */ class DJIVideoDecoder$$Lambda$2 implements Runnable {
    private final DJIVideoDecoder arg$1;
    private final boolean arg$2;

    DJIVideoDecoder$$Lambda$2(DJIVideoDecoder dJIVideoDecoder, boolean z) {
        this.arg$1 = dJIVideoDecoder;
        this.arg$2 = z;
    }

    public void run() {
        this.arg$1.lambda$stopGetAsyncRgbaData$2$DJIVideoDecoder(this.arg$2);
    }
}
