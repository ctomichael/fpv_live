package com.dji.component.fpv.widget.preview;

import dji.midware.util.AutoVideoSizeCalculator;

final /* synthetic */ class PreviewHelper$$Lambda$0 implements AutoVideoSizeCalculator.AutoVideoSizeListener {
    private final PreviewHelper arg$1;

    PreviewHelper$$Lambda$0(PreviewHelper previewHelper) {
        this.arg$1 = previewHelper;
    }

    public void onVideoSizeChanged(int i, int i2, int i3, int i4) {
        this.arg$1.lambda$init$0$PreviewHelper(i, i2, i3, i4);
    }
}
