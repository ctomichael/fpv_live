package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.RatingBar;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.functions.Consumer;

public final class RxRatingBar {
    @CheckResult
    @NonNull
    public static InitialValueObservable<Float> ratingChanges(@NonNull RatingBar view) {
        Preconditions.checkNotNull(view, "view == null");
        return new RatingBarRatingChangeObservable(view);
    }

    @CheckResult
    @NonNull
    public static InitialValueObservable<RatingBarChangeEvent> ratingChangeEvents(@NonNull RatingBar view) {
        Preconditions.checkNotNull(view, "view == null");
        return new RatingBarRatingChangeEventObservable(view);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Float> rating(@NonNull RatingBar view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxRatingBar$$Lambda$0.get$Lambda(view);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Boolean> isIndicator(@NonNull RatingBar view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxRatingBar$$Lambda$1.get$Lambda(view);
    }

    private RxRatingBar() {
        throw new AssertionError("No instances.");
    }
}
