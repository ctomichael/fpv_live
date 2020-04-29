package com.jakewharton.rxbinding2.widget;

import android.widget.SearchView;
import io.reactivex.functions.Consumer;

final /* synthetic */ class RxSearchView$$Lambda$0 implements Consumer {
    private final SearchView arg$1;
    private final boolean arg$2;

    RxSearchView$$Lambda$0(SearchView searchView, boolean z) {
        this.arg$1 = searchView;
        this.arg$2 = z;
    }

    public void accept(Object obj) {
        this.arg$1.setQuery((CharSequence) obj, this.arg$2);
    }
}
