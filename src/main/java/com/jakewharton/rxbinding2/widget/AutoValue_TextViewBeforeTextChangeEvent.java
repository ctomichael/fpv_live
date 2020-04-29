package com.jakewharton.rxbinding2.widget;

import android.support.annotation.NonNull;
import android.widget.TextView;

final class AutoValue_TextViewBeforeTextChangeEvent extends TextViewBeforeTextChangeEvent {
    private final int after;
    private final int count;
    private final int start;
    private final CharSequence text;
    private final TextView view;

    AutoValue_TextViewBeforeTextChangeEvent(TextView view2, CharSequence text2, int start2, int count2, int after2) {
        if (view2 == null) {
            throw new NullPointerException("Null view");
        }
        this.view = view2;
        if (text2 == null) {
            throw new NullPointerException("Null text");
        }
        this.text = text2;
        this.start = start2;
        this.count = count2;
        this.after = after2;
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

    public int count() {
        return this.count;
    }

    public int after() {
        return this.after;
    }

    public String toString() {
        return "TextViewBeforeTextChangeEvent{view=" + this.view + ", text=" + ((Object) this.text) + ", start=" + this.start + ", count=" + this.count + ", after=" + this.after + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TextViewBeforeTextChangeEvent)) {
            return false;
        }
        TextViewBeforeTextChangeEvent that = (TextViewBeforeTextChangeEvent) o;
        if (this.view.equals(that.view()) && this.text.equals(that.text()) && this.start == that.start() && this.count == that.count() && this.after == that.after()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (((((((((1 * 1000003) ^ this.view.hashCode()) * 1000003) ^ this.text.hashCode()) * 1000003) ^ this.start) * 1000003) ^ this.count) * 1000003) ^ this.after;
    }
}
