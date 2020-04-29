package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.CompoundButton;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.functions.Consumer;

public final class RxCompoundButton {
    @CheckResult
    @NonNull
    public static InitialValueObservable<Boolean> checkedChanges(@NonNull CompoundButton view) {
        Preconditions.checkNotNull(view, "view == null");
        return new CompoundButtonCheckedChangeObservable(view);
    }

    @CheckResult
    @NonNull
    @Deprecated
    public static Consumer<? super Boolean> checked(@NonNull CompoundButton view) {
        Preconditions.checkNotNull(view, "view == null");
        view.getClass();
        return RxCompoundButton$$Lambda$0.get$Lambda(view);
    }

    @CheckResult
    @NonNull
    public static Consumer<? super Object> toggle(@NonNull CompoundButton view) {
        Preconditions.checkNotNull(view, "view == null");
        return new RxCompoundButton$$Lambda$1(view);
    }

    private RxCompoundButton() {
        throw new AssertionError("No instances.");
    }
}
