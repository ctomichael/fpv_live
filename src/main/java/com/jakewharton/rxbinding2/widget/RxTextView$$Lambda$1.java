package com.jakewharton.rxbinding2.widget;

import android.widget.TextView;
import io.reactivex.functions.Consumer;

final /* synthetic */ class RxTextView$$Lambda$1 implements Consumer {
    private final TextView arg$1;

    private RxTextView$$Lambda$1(TextView textView) {
        this.arg$1 = textView;
    }

    static Consumer get$Lambda(TextView textView) {
        return new RxTextView$$Lambda$1(textView);
    }

    public void accept(Object obj) {
        this.arg$1.setText(((Integer) obj).intValue());
    }
}
