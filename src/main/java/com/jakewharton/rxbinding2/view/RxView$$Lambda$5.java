package com.jakewharton.rxbinding2.view;

import android.view.View;
import io.reactivex.functions.Consumer;

final /* synthetic */ class RxView$$Lambda$5 implements Consumer {
    private final View arg$1;
    private final int arg$2;

    RxView$$Lambda$5(View view, int i) {
        this.arg$1 = view;
        this.arg$2 = i;
    }

    public void accept(Object obj) {
        RxView.lambda$visibility$0$RxView(this.arg$1, this.arg$2, (Boolean) obj);
    }
}
