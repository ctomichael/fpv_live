package com.jakewharton.rxbinding2.widget;

import android.widget.CheckedTextView;
import io.reactivex.functions.Consumer;

final /* synthetic */ class RxCheckedTextView$$Lambda$0 implements Consumer {
    private final CheckedTextView arg$1;

    private RxCheckedTextView$$Lambda$0(CheckedTextView checkedTextView) {
        this.arg$1 = checkedTextView;
    }

    static Consumer get$Lambda(CheckedTextView checkedTextView) {
        return new RxCheckedTextView$$Lambda$0(checkedTextView);
    }

    public void accept(Object obj) {
        this.arg$1.setChecked(((Boolean) obj).booleanValue());
    }
}
