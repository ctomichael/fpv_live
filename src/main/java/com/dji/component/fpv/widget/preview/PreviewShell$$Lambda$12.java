package com.dji.component.fpv.widget.preview;

import dji.common.camera.CameraRecordingState;
import io.reactivex.functions.Consumer;

final /* synthetic */ class PreviewShell$$Lambda$12 implements Consumer {
    private final PreviewShell arg$1;

    PreviewShell$$Lambda$12(PreviewShell previewShell) {
        this.arg$1 = previewShell;
    }

    public void accept(Object obj) {
        this.arg$1.lambda$onAttachedToWindow$12$PreviewShell((CameraRecordingState) obj);
    }
}
