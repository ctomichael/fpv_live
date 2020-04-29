package com.dji.findmydrone.ui.view.right.camera;

import android.graphics.Rect;
import io.reactivex.functions.Consumer;

final /* synthetic */ class CameraLiveView$$Lambda$0 implements Consumer {
    private final CameraLiveView arg$1;

    CameraLiveView$$Lambda$0(CameraLiveView cameraLiveView) {
        this.arg$1 = cameraLiveView;
    }

    public void accept(Object obj) {
        this.arg$1.lambda$onAttachedToWindow$0$CameraLiveView((Rect) obj);
    }
}
