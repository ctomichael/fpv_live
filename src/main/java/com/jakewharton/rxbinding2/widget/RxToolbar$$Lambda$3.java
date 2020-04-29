package com.jakewharton.rxbinding2.widget;

import android.widget.Toolbar;
import io.reactivex.functions.Consumer;

final /* synthetic */ class RxToolbar$$Lambda$3 implements Consumer {
    private final Toolbar arg$1;

    private RxToolbar$$Lambda$3(Toolbar toolbar) {
        this.arg$1 = toolbar;
    }

    static Consumer get$Lambda(Toolbar toolbar) {
        return new RxToolbar$$Lambda$3(toolbar);
    }

    public void accept(Object obj) {
        this.arg$1.setSubtitle(((Integer) obj).intValue());
    }
}
