package com.jakewharton.rxbinding2.widget;

import android.widget.TextView;
import io.reactivex.functions.Consumer;

final /* synthetic */ class RxTextView$$Lambda$6 implements Consumer {
    private final TextView arg$1;

    private RxTextView$$Lambda$6(TextView textView) {
        this.arg$1 = textView;
    }

    static Consumer get$Lambda(TextView textView) {
        return new RxTextView$$Lambda$6(textView);
    }

    public void accept(Object obj) {
        this.arg$1.setTextColor(((Integer) obj).intValue());
    }
}
