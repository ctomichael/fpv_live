package com.dji.findmydrone.ui;

import io.reactivex.functions.Consumer;

final /* synthetic */ class DebugApp$$Lambda$1 implements Consumer {
    private final DebugApp arg$1;

    DebugApp$$Lambda$1(DebugApp debugApp) {
        this.arg$1 = debugApp;
    }

    public void accept(Object obj) {
        this.arg$1.lambda$configRxJavaErrorHandler$1$DebugApp((Throwable) obj);
    }
}
