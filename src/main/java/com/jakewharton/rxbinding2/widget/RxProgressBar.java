package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.ProgressBar;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.functions.Consumer;

public final class RxProgressBar {
    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Integer> incrementProgressBy(@NonNull ProgressBar view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxProgressBar$$Lambda$0.get$Lambda(view);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Integer> incrementSecondaryProgressBy(@NonNull ProgressBar view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxProgressBar$$Lambda$1.get$Lambda(view);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Boolean> indeterminate(@NonNull ProgressBar view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxProgressBar$$Lambda$2.get$Lambda(view);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Integer> max(@NonNull ProgressBar view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxProgressBar$$Lambda$3.get$Lambda(view);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Integer> progress(@NonNull ProgressBar view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxProgressBar$$Lambda$4.get$Lambda(view);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Integer> secondaryProgress(@NonNull ProgressBar view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxProgressBar$$Lambda$5.get$Lambda(view);
    }

    private RxProgressBar() {
        throw new AssertionError("No instances.");
    }
}
