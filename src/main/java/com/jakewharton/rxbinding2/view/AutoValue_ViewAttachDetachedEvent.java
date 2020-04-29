package com.jakewharton.rxbinding2.view;

import android.support.annotation.NonNull;
import android.view.View;

final class AutoValue_ViewAttachDetachedEvent extends ViewAttachDetachedEvent {
    private final View view;

    AutoValue_ViewAttachDetachedEvent(View view2) {
        if (view2 == null) {
            throw new NullPointerException("Null view");
        }
        this.view = view2;
    }

    @NonNull
    public View view() {
        return this.view;
    }

    public String toString() {
        return "ViewAttachDetachedEvent{view=" + this.view + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ViewAttachDetachedEvent) {
            return this.view.equals(((ViewAttachDetachedEvent) o).view());
        }
        return false;
    }

    public int hashCode() {
        return (1 * 1000003) ^ this.view.hashCode();
    }
}
