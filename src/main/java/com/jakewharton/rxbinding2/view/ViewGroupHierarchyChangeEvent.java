package com.jakewharton.rxbinding2.view;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

public abstract class ViewGroupHierarchyChangeEvent {
    @NonNull
    public abstract View child();

    @NonNull
    public abstract ViewGroup view();

    ViewGroupHierarchyChangeEvent() {
    }
}
