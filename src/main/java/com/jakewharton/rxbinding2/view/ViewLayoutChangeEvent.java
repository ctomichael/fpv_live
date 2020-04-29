package com.jakewharton.rxbinding2.view;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ViewLayoutChangeEvent {
    public abstract int bottom();

    public abstract int left();

    public abstract int oldBottom();

    public abstract int oldLeft();

    public abstract int oldRight();

    public abstract int oldTop();

    public abstract int right();

    public abstract int top();

    @NonNull
    public abstract View view();

    @CheckResult
    @NonNull
    public static ViewLayoutChangeEvent create(@NonNull View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        return new AutoValue_ViewLayoutChangeEvent(view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom);
    }

    ViewLayoutChangeEvent() {
    }
}
