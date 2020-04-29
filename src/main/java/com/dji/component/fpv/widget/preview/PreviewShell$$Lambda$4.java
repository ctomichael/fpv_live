package com.dji.component.fpv.widget.preview;

import android.graphics.Rect;
import io.reactivex.functions.Consumer;

final /* synthetic */ class PreviewShell$$Lambda$4 implements Consumer {
    private final PreviewShell arg$1;

    PreviewShell$$Lambda$4(PreviewShell previewShell) {
        this.arg$1 = previewShell;
    }

    public void accept(Object obj) {
        this.arg$1.lambda$onAttachedToWindow$4$PreviewShell((Rect) obj);
    }
}
