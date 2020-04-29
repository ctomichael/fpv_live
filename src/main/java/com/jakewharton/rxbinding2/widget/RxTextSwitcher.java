package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.TextSwitcher;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.functions.Consumer;

public final class RxTextSwitcher {
    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super CharSequence> text(@NonNull TextSwitcher view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxTextSwitcher$$Lambda$0.get$Lambda(view);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super CharSequence> currentText(@NonNull TextSwitcher view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxTextSwitcher$$Lambda$1.get$Lambda(view);
    }

    private RxTextSwitcher() {
        throw new AssertionError("No instances.");
    }
}
