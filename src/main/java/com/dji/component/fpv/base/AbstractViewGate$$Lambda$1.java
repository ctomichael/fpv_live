package com.dji.component.fpv.base;

import io.reactivex.functions.Consumer;

final /* synthetic */ class AbstractViewGate$$Lambda$1 implements Consumer {
    private final AbstractViewGate arg$1;

    AbstractViewGate$$Lambda$1(AbstractViewGate abstractViewGate) {
        this.arg$1 = abstractViewGate;
    }

    public void accept(Object obj) {
        this.arg$1.lambda$onInflatePermit$1$AbstractViewGate(obj);
    }
}
