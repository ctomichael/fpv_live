package com.jakewharton.rxbinding2.view;

import android.view.MenuItem;
import io.reactivex.functions.Consumer;

final /* synthetic */ class RxMenuItem$$Lambda$0 implements Consumer {
    private final MenuItem arg$1;

    private RxMenuItem$$Lambda$0(MenuItem menuItem) {
        this.arg$1 = menuItem;
    }

    static Consumer get$Lambda(MenuItem menuItem) {
        return new RxMenuItem$$Lambda$0(menuItem);
    }

    public void accept(Object obj) {
        this.arg$1.setChecked(((Boolean) obj).booleanValue());
    }
}
