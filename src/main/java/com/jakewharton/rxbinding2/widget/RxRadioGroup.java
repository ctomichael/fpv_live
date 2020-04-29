package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.RadioGroup;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.functions.Consumer;

public final class RxRadioGroup {
    @CheckResult
    @NonNull
    public static InitialValueObservable<Integer> checkedChanges(@NonNull RadioGroup view) {
        Preconditions.checkNotNull(view, "view == null");
        return new RadioGroupCheckedChangeObservable(view);
    }

    @CheckResult
    @NonNull
    public static Consumer<? super Integer> checked(@NonNull RadioGroup view) {
        Preconditions.checkNotNull(view, "view == null");
        return new RxRadioGroup$$Lambda$0(view);
    }

    static final /* synthetic */ void lambda$checked$0$RxRadioGroup(@NonNull RadioGroup view, Integer value) throws Exception {
        if (value.intValue() == -1) {
            view.clearCheck();
        } else {
            view.check(value.intValue());
        }
    }

    private RxRadioGroup() {
        throw new AssertionError("No instances.");
    }
}
