package com.jakewharton.rxbinding2.widget;

import android.widget.TextView;
import io.reactivex.functions.Consumer;

final /* synthetic */ class RxTextView$$Lambda$3 implements Consumer {
    private final TextView arg$1;

    RxTextView$$Lambda$3(TextView textView) {
        this.arg$1 = textView;
    }

    public void accept(Object obj) {
        this.arg$1.setError(this.arg$1.getContext().getResources().getText(((Integer) obj).intValue()));
    }
}
