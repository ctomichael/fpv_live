package com.jakewharton.rxbinding2.widget;

import android.widget.RadioGroup;
import io.reactivex.functions.Consumer;

final /* synthetic */ class RxRadioGroup$$Lambda$0 implements Consumer {
    private final RadioGroup arg$1;

    RxRadioGroup$$Lambda$0(RadioGroup radioGroup) {
        this.arg$1 = radioGroup;
    }

    public void accept(Object obj) {
        RxRadioGroup.lambda$checked$0$RxRadioGroup(this.arg$1, (Integer) obj);
    }
}
