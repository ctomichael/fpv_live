package com.jakewharton.rxbinding2.widget;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;

final class AutoValue_AdapterViewItemSelectionEvent extends AdapterViewItemSelectionEvent {
    private final long id;
    private final int position;
    private final View selectedView;
    private final AdapterView<?> view;

    AutoValue_AdapterViewItemSelectionEvent(AdapterView<?> view2, View selectedView2, int position2, long id2) {
        if (view2 == null) {
            throw new NullPointerException("Null view");
        }
        this.view = view2;
        if (selectedView2 == null) {
            throw new NullPointerException("Null selectedView");
        }
        this.selectedView = selectedView2;
        this.position = position2;
        this.id = id2;
    }

    @NonNull
    public AdapterView<?> view() {
        return this.view;
    }

    @NonNull
    public View selectedView() {
        return this.selectedView;
    }

    public int position() {
        return this.position;
    }

    public long id() {
        return this.id;
    }

    public String toString() {
        return "AdapterViewItemSelectionEvent{view=" + this.view + ", selectedView=" + this.selectedView + ", position=" + this.position + ", id=" + this.id + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AdapterViewItemSelectionEvent)) {
            return false;
        }
        AdapterViewItemSelectionEvent that = (AdapterViewItemSelectionEvent) o;
        if (!this.view.equals(that.view()) || !this.selectedView.equals(that.selectedView()) || this.position != that.position() || this.id != that.id()) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((((((1 * 1000003) ^ this.view.hashCode()) * 1000003) ^ this.selectedView.hashCode()) * 1000003) ^ this.position) * 1000003) ^ ((int) ((this.id >>> 32) ^ this.id));
    }
}
