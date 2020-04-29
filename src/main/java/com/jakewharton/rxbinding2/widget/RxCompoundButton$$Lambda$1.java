package com.jakewharton.rxbinding2.widget;

import android.widget.CompoundButton;
import io.reactivex.functions.Consumer;

final /* synthetic */ class RxCompoundButton$$Lambda$1 implements Consumer {
    private final CompoundButton arg$1;

    RxCompoundButton$$Lambda$1(CompoundButton compoundButton) {
        this.arg$1 = compoundButton;
    }

    public void accept(Object obj) {
        this.arg$1.toggle();
    }
}
