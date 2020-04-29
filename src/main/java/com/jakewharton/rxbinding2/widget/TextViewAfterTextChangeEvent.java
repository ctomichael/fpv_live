package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.widget.TextView;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TextViewAfterTextChangeEvent {
    @Nullable
    public abstract Editable editable();

    @NonNull
    public abstract TextView view();

    @CheckResult
    @NonNull
    public static TextViewAfterTextChangeEvent create(@NonNull TextView view, @Nullable Editable editable) {
        return new AutoValue_TextViewAfterTextChangeEvent(view, editable);
    }

    TextViewAfterTextChangeEvent() {
    }
}
