package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.TextView;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TextViewTextChangeEvent {
    public abstract int before();

    public abstract int count();

    public abstract int start();

    @NonNull
    public abstract CharSequence text();

    @NonNull
    public abstract TextView view();

    @CheckResult
    @NonNull
    public static TextViewTextChangeEvent create(@NonNull TextView view, @NonNull CharSequence text, int start, int before, int count) {
        return new AutoValue_TextViewTextChangeEvent(view, text, start, before, count);
    }

    TextViewTextChangeEvent() {
    }
}
