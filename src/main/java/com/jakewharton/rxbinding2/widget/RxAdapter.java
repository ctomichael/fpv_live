package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.Adapter;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.internal.Preconditions;

public final class RxAdapter {
    @CheckResult
    @NonNull
    public static <T extends Adapter> InitialValueObservable<T> dataChanges(@NonNull T adapter) {
        Preconditions.checkNotNull(adapter, "adapter == null");
        return new AdapterDataChangeObservable(adapter);
    }

    private RxAdapter() {
        throw new AssertionError("No instances.");
    }
}
