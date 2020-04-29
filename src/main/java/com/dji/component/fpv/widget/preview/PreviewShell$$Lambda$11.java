package com.dji.component.fpv.widget.preview;

import dji.common.camera.CameraRecordingState;
import io.reactivex.functions.Predicate;

final /* synthetic */ class PreviewShell$$Lambda$11 implements Predicate {
    static final Predicate $instance = new PreviewShell$$Lambda$11();

    private PreviewShell$$Lambda$11() {
    }

    public boolean test(Object obj) {
        return PreviewShell.lambda$onAttachedToWindow$11$PreviewShell((CameraRecordingState) obj);
    }
}
