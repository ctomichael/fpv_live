package com.jakewharton.rxbinding2.view;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ViewGroupHierarchyChildViewAddEvent extends ViewGroupHierarchyChangeEvent {
    @CheckResult
    @NonNull
    public static ViewGroupHierarchyChildViewAddEvent create(@NonNull ViewGroup viewGroup, @NonNull View child) {
        return new AutoValue_ViewGroupHierarchyChildViewAddEvent(viewGroup, child);
    }

    ViewGroupHierarchyChildViewAddEvent() {
    }
}
