package com.jakewharton.rxbinding2.widget;

import android.widget.AdapterView;
import io.reactivex.functions.Consumer;

final /* synthetic */ class RxAdapterView$$Lambda$0 implements Consumer {
    private final AdapterView arg$1;

    private RxAdapterView$$Lambda$0(AdapterView adapterView) {
        this.arg$1 = adapterView;
    }

    static Consumer get$Lambda(AdapterView adapterView) {
        return new RxAdapterView$$Lambda$0(adapterView);
    }

    public void accept(Object obj) {
        this.arg$1.setSelection(((Integer) obj).intValue());
    }
}
