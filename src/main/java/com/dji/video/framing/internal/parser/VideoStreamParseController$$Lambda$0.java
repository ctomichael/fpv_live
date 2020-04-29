package com.dji.video.framing.internal.parser;

import com.dji.video.framing.internal.parser.IFrameParser;

final /* synthetic */ class VideoStreamParseController$$Lambda$0 implements IFrameParser.OnFrameParserListener {
    private final VideoStreamParseController arg$1;

    VideoStreamParseController$$Lambda$0(VideoStreamParseController videoStreamParseController) {
        this.arg$1 = videoStreamParseController;
    }

    public void onRecvData(byte[] bArr, int i, boolean z, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, boolean z2) {
        this.arg$1.lambda$new$0$VideoStreamParseController(bArr, i, z, i2, i3, i4, i5, i6, i7, i8, i9, z2);
    }
}
