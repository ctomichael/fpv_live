package com.jakewharton.rxbinding2.widget;

import android.widget.TextView;
import io.reactivex.functions.Consumer;

final /* synthetic */ class RxTextView$$Lambda$4 implements Consumer {
    private final TextView arg$1;

    private RxTextView$$Lambda$4(TextView textView) {
        this.arg$1 = textView;
    }

    static Consumer get$Lambda(TextView textView) {
        return new RxTextView$$Lambda$4(textView);
    }

    public void accept(Object obj) {
        this.arg$1.setHint((CharSequence) obj);
    }
}
