package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.AutoCompleteTextView;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public final class RxAutoCompleteTextView {
    @CheckResult
    @NonNull
    public static Observable<AdapterViewItemClickEvent> itemClickEvents(@NonNull AutoCompleteTextView view) {
        Preconditions.checkNotNull(view, "view == null");
        return new AutoCompleteTextViewItemClickEventObservable(view);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super CharSequence> completionHint(@NonNull AutoCompleteTextView view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxAutoCompleteTextView$$Lambda$0.get$Lambda(view);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Integer> threshold(@NonNull AutoCompleteTextView view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxAutoCompleteTextView$$Lambda$1.get$Lambda(view);
    }

    private RxAutoCompleteTextView() {
        throw new AssertionError("No instances.");
    }
}
