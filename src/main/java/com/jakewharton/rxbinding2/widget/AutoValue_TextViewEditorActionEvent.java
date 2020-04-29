package com.jakewharton.rxbinding2.widget;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.widget.TextView;

final class AutoValue_TextViewEditorActionEvent extends TextViewEditorActionEvent {
    private final int actionId;
    private final KeyEvent keyEvent;
    private final TextView view;

    AutoValue_TextViewEditorActionEvent(TextView view2, int actionId2, @Nullable KeyEvent keyEvent2) {
        if (view2 == null) {
            throw new NullPointerException("Null view");
        }
        this.view = view2;
        this.actionId = actionId2;
        this.keyEvent = keyEvent2;
    }

    @NonNull
    public TextView view() {
        return this.view;
    }

    public int actionId() {
        return this.actionId;
    }

    @Nullable
    public KeyEvent keyEvent() {
        return this.keyEvent;
    }

    public String toString() {
        return "TextViewEditorActionEvent{view=" + this.view + ", actionId=" + this.actionId + ", keyEvent=" + this.keyEvent + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TextViewEditorActionEvent)) {
            return false;
        }
        TextViewEditorActionEvent that = (TextViewEditorActionEvent) o;
        if (this.view.equals(that.view()) && this.actionId == that.actionId()) {
            if (this.keyEvent == null) {
                if (that.keyEvent() == null) {
                    return true;
                }
            } else if (this.keyEvent.equals(that.keyEvent())) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return (((((1 * 1000003) ^ this.view.hashCode()) * 1000003) ^ this.actionId) * 1000003) ^ (this.keyEvent == null ? 0 : this.keyEvent.hashCode());
    }
}
