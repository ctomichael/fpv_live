package com.dji.component.fpv.widget.preview;

import io.reactivex.functions.Consumer;
import java.util.List;

final /* synthetic */ class PreviewShell$$Lambda$13 implements Consumer {
    private final PreviewShell arg$1;

    PreviewShell$$Lambda$13(PreviewShell previewShell) {
        this.arg$1 = previewShell;
    }

    public void accept(Object obj) {
        this.arg$1.lambda$onAttachedToWindow$13$PreviewShell((List) obj);
    }
}
