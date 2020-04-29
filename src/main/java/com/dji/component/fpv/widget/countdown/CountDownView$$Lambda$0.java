package com.dji.component.fpv.widget.countdown;

import io.reactivex.functions.Consumer;

final /* synthetic */ class CountDownView$$Lambda$0 implements Consumer {
    private final CountDownView arg$1;

    CountDownView$$Lambda$0(CountDownView countDownView) {
        this.arg$1 = countDownView;
    }

    public void accept(Object obj) {
        this.arg$1.setText((String) obj);
    }
}
