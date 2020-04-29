package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.CheckedTextView;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.functions.Consumer;

public final class RxCheckedTextView {
    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Boolean> check(@NonNull CheckedTextView view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxCheckedTextView$$Lambda$0.get$Lambda(view);
    }

    private RxCheckedTextView() {
        throw new AssertionError("No instances.");
    }
}
