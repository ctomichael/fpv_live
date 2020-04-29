package com.dji.component.fpv.base;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

final /* synthetic */ class AbstractViewGate$$Lambda$0 implements ObservableOnSubscribe {
    private final AbstractViewGate arg$1;

    AbstractViewGate$$Lambda$0(AbstractViewGate abstractViewGate) {
        this.arg$1 = abstractViewGate;
    }

    public void subscribe(ObservableEmitter observableEmitter) {
        this.arg$1.lambda$onInflatePermit$0$AbstractViewGate(observableEmitter);
    }
}
