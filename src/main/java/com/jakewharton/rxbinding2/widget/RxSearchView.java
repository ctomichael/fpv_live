package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.SearchView;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.functions.Consumer;

public final class RxSearchView {
    @CheckResult
    @NonNull
    public static InitialValueObservable<SearchViewQueryTextEvent> queryTextChangeEvents(@NonNull SearchView view) {
        Preconditions.checkNotNull(view, "view == null");
        return new SearchViewQueryTextChangeEventsObservable(view);
    }

    @CheckResult
    @NonNull
    public static InitialValueObservable<CharSequence> queryTextChanges(@NonNull SearchView view) {
        Preconditions.checkNotNull(view, "view == null");
        return new SearchViewQueryTextChangesObservable(view);
    }

    @CheckResult
    @NonNull
    public static Consumer<? super CharSequence> query(@NonNull SearchView view, boolean submit) {
        Preconditions.checkNotNull(view, "view == null");
        return new RxSearchView$$Lambda$0(view, submit);
    }

    private RxSearchView() {
        throw new AssertionError("No instances.");
    }
}
