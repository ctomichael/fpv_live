package com.jakewharton.rxbinding2.widget;

import android.widget.TextSwitcher;
import io.reactivex.functions.Consumer;

final /* synthetic */ class RxTextSwitcher$$Lambda$1 implements Consumer {
    private final TextSwitcher arg$1;

    private RxTextSwitcher$$Lambda$1(TextSwitcher textSwitcher) {
        this.arg$1 = textSwitcher;
    }

    static Consumer get$Lambda(TextSwitcher textSwitcher) {
        return new RxTextSwitcher$$Lambda$1(textSwitcher);
    }

    public void accept(Object obj) {
        this.arg$1.setCurrentText((CharSequence) obj);
    }
}
