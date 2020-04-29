package com.jakewharton.rxbinding2.widget;

import android.support.annotation.NonNull;
import android.widget.TextView;

final class AutoValue_TextViewTextChangeEvent extends TextViewTextChangeEvent {
    private final int before;
    private final int count;
    private final int start;
    private final CharSequence text;
    private final TextView view;

    AutoValue_TextViewTextChangeEvent(TextView view2, CharSequence text2, int start2, int before2, int count2) {
        if (view2 == null) {
            throw new NullPointerException("Null view");
        }
        this.view = view2;
        if (text2 == null) {
            throw new NullPointerException("Null text");
        }
        this.text = text2;
        this.start = start2;
        this.before = before2;
        this.count = count2;
    }

    @NonNull
    public TextView view() {
        return this.view;
    }

    @NonNull
    public CharSequence text() {
        return this.text;
    }

    public int start() {
        return this.start;
    }

    public int before() {
        return this.before;
    }

    public int count() {
        return this.count;
    }

    public String toString() {
        return "TextViewTextChangeEvent{view=" + this.view + ", text=" + ((Object) this.text) + ", start=" + this.start + ", before=" + this.before + ", count=" + this.count + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TextViewTextChangeEvent)) {
            return false;
        }
        TextViewTextChangeEvent that = (TextViewTextChangeEvent) o;
        if (this.view.equals(that.view()) && this.text.equals(that.text()) && this.start == that.start() && this.before == that.before() && this.count == that.count()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (((((((((1 * 1000003) ^ this.view.hashCode()) * 1000003) ^ this.text.hashCode()) * 1000003) ^ this.start) * 1000003) ^ this.before) * 1000003) ^ this.count;
    }
}
