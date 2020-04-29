package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.SeekBar;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.internal.Preconditions;

public final class RxSeekBar {
    @CheckResult
    @NonNull
    public static InitialValueObservable<Integer> changes(@NonNull SeekBar view) {
        Preconditions.checkNotNull(view, "view == null");
        return new SeekBarChangeObservable(view, null);
    }

    @CheckResult
    @NonNull
    public static InitialValueObservable<Integer> userChanges(@NonNull SeekBar view) {
        Preconditions.checkNotNull(view, "view == null");
        return new SeekBarChangeObservable(view, true);
    }

    @CheckResult
    @NonNull
    public static InitialValueObservable<Integer> systemChanges(@NonNull SeekBar view) {
        Preconditions.checkNotNull(view, "view == null");
        return new SeekBarChangeObservable(view, false);
    }

    @CheckResult
    @NonNull
    public static InitialValueObservable<SeekBarChangeEvent> changeEvents(@NonNull SeekBar view) {
        Preconditions.checkNotNull(view, "view == null");
        return new SeekBarChangeEventObservable(view);
    }

    private RxSeekBar() {
        throw new AssertionError("No instances.");
    }
}
