package com.jakewharton.rxbinding2.view;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ViewAttachDetachedEvent extends ViewAttachEvent {
    @CheckResult
    @NonNull
    public static ViewAttachDetachedEvent create(@NonNull View view) {
        return new AutoValue_ViewAttachDetachedEvent(view);
    }

    ViewAttachDetachedEvent() {
    }
}
