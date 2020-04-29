package com.jakewharton.rxbinding2.view;

import android.support.annotation.NonNull;
import android.view.View;

public abstract class ViewAttachEvent {
    @NonNull
    public abstract View view();

    ViewAttachEvent() {
    }
}
