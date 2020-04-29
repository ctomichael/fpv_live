package com.jakewharton.rxbinding2.widget;

import android.widget.CompoundButton;
import io.reactivex.functions.Consumer;

final /* synthetic */ class RxCompoundButton$$Lambda$0 implements Consumer {
    private final CompoundButton arg$1;

    private RxCompoundButton$$Lambda$0(CompoundButton compoundButton) {
        this.arg$1 = compoundButton;
    }

    static Consumer get$Lambda(CompoundButton compoundButton) {
        return new RxCompoundButton$$Lambda$0(compoundButton);
    }

    public void accept(Object obj) {
        this.arg$1.setChecked(((Boolean) obj).booleanValue());
    }
}
