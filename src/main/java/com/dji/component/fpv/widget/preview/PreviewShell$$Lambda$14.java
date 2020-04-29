package com.dji.component.fpv.widget.preview;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

final /* synthetic */ class PreviewShell$$Lambda$14 implements ObservableOnSubscribe {
    private final PreviewShell arg$1;

    PreviewShell$$Lambda$14(PreviewShell previewShell) {
        this.arg$1 = previewShell;
    }

    public void subscribe(ObservableEmitter observableEmitter) {
        this.arg$1.lambda$switchToLarge$17$PreviewShell(observableEmitter);
    }
}
