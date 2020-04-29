package com.jakewharton.rxbinding2.widget;

import android.widget.ProgressBar;
import io.reactivex.functions.Consumer;

final /* synthetic */ class RxProgressBar$$Lambda$5 implements Consumer {
    private final ProgressBar arg$1;

    private RxProgressBar$$Lambda$5(ProgressBar progressBar) {
        this.arg$1 = progressBar;
    }

    static Consumer get$Lambda(ProgressBar progressBar) {
        return new RxProgressBar$$Lambda$5(progressBar);
    }

    public void accept(Object obj) {
        this.arg$1.setSecondaryProgress(((Integer) obj).intValue());
    }
}
