package com.jakewharton.rxbinding2.widget;

import android.widget.AutoCompleteTextView;
import io.reactivex.functions.Consumer;

final /* synthetic */ class RxAutoCompleteTextView$$Lambda$1 implements Consumer {
    private final AutoCompleteTextView arg$1;

    private RxAutoCompleteTextView$$Lambda$1(AutoCompleteTextView autoCompleteTextView) {
        this.arg$1 = autoCompleteTextView;
    }

    static Consumer get$Lambda(AutoCompleteTextView autoCompleteTextView) {
        return new RxAutoCompleteTextView$$Lambda$1(autoCompleteTextView);
    }

    public void accept(Object obj) {
        this.arg$1.setThreshold(((Integer) obj).intValue());
    }
}
