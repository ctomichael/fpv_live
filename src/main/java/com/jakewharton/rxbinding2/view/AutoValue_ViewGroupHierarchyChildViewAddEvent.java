package com.jakewharton.rxbinding2.view;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

final class AutoValue_ViewGroupHierarchyChildViewAddEvent extends ViewGroupHierarchyChildViewAddEvent {
    private final View child;
    private final ViewGroup view;

    AutoValue_ViewGroupHierarchyChildViewAddEvent(ViewGroup view2, View child2) {
        if (view2 == null) {
            throw new NullPointerException("Null view");
        }
        this.view = view2;
        if (child2 == null) {
            throw new NullPointerException("Null child");
        }
        this.child = child2;
    }

    @NonNull
    public ViewGroup view() {
        return this.view;
    }

    @NonNull
    public View child() {
        return this.child;
    }

    public String toString() {
        return "ViewGroupHierarchyChildViewAddEvent{view=" + this.view + ", child=" + this.child + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ViewGroupHierarchyChildViewAddEvent)) {
            return false;
        }
        ViewGroupHierarchyChildViewAddEvent that = (ViewGroupHierarchyChildViewAddEvent) o;
        if (!this.view.equals(that.view()) || !this.child.equals(that.child())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((1 * 1000003) ^ this.view.hashCode()) * 1000003) ^ this.child.hashCode();
    }
}
