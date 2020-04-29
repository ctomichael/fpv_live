package com.dji.component.fpv.widget.preview;

import android.graphics.Rect;
import io.reactivex.functions.Consumer;

final /* synthetic */ class PreviewShellModel$$Lambda$1 implements Consumer {
    private final PreviewShellModel arg$1;

    PreviewShellModel$$Lambda$1(PreviewShellModel previewShellModel) {
        this.arg$1 = previewShellModel;
    }

    public void accept(Object obj) {
        this.arg$1.lambda$onCreateView$1$PreviewShellModel((Rect) obj);
    }
}
