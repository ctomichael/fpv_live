package com.jakewharton.rxbinding2.widget;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.widget.TextView;

final class AutoValue_TextViewAfterTextChangeEvent extends TextViewAfterTextChangeEvent {
    private final Editable editable;
    private final TextView view;

    AutoValue_TextViewAfterTextChangeEvent(TextView view2, @Nullable Editable editable2) {
        if (view2 == null) {
            throw new NullPointerException("Null view");
        }
        this.view = view2;
        this.editable = editable2;
    }

    @NonNull
    public TextView view() {
        return this.view;
    }

    @Nullable
    public Editable editable() {
        return this.editable;
    }

    public String toString() {
        return "TextViewAfterTextChangeEvent{view=" + this.view + ", editable=" + ((Object) this.editable) + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TextViewAfterTextChangeEvent)) {
            return false;
        }
        TextViewAfterTextChangeEvent that = (TextViewAfterTextChangeEvent) o;
        if (this.view.equals(that.view())) {
            if (this.editable == null) {
                if (that.editable() == null) {
                    return true;
                }
            } else if (this.editable.equals(that.editable())) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return (((1 * 1000003) ^ this.view.hashCode()) * 1000003) ^ (this.editable == null ? 0 : this.editable.hashCode());
    }
}
