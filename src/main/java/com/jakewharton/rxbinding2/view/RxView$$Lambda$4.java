package com.jakewharton.rxbinding2.view;

import android.view.View;
import io.reactivex.functions.Consumer;

final /* synthetic */ class RxView$$Lambda$4 implements Consumer {
    private final View arg$1;

    private RxView$$Lambda$4(View view) {
        this.arg$1 = view;
    }

    static Consumer get$Lambda(View view) {
        return new RxView$$Lambda$4(view);
    }

    public void accept(Object obj) {
        this.arg$1.setSelected(((Boolean) obj).booleanValue());
    }
}
