package com.dji.rx.sharedlib.diagnostics;

import io.reactivex.Observer;
import io.reactivex.internal.disposables.EmptyDisposable;

public class EmptyCodeObservable extends DiagnosticsObservable {
    public static final EmptyCodeObservable INSTANCE = new EmptyCodeObservable();

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super DiagnosticsEvent> observer) {
        EmptyDisposable.complete(observer);
    }
}
