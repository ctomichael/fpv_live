package com.jakewharton.rxbinding2.widget;

import android.support.annotation.NonNull;
import android.widget.RatingBar;

final class AutoValue_RatingBarChangeEvent extends RatingBarChangeEvent {
    private final boolean fromUser;
    private final float rating;
    private final RatingBar view;

    AutoValue_RatingBarChangeEvent(RatingBar view2, float rating2, boolean fromUser2) {
        if (view2 == null) {
            throw new NullPointerException("Null view");
        }
        this.view = view2;
        this.rating = rating2;
        this.fromUser = fromUser2;
    }

    @NonNull
    public RatingBar view() {
        return this.view;
    }

    public float rating() {
        return this.rating;
    }

    public boolean fromUser() {
        return this.fromUser;
    }

    public String toString() {
        return "RatingBarChangeEvent{view=" + this.view + ", rating=" + this.rating + ", fromUser=" + this.fromUser + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RatingBarChangeEvent)) {
            return false;
        }
        RatingBarChangeEvent that = (RatingBarChangeEvent) o;
        if (this.view.equals(that.view()) && Float.floatToIntBits(this.rating) == Float.floatToIntBits(that.rating()) && this.fromUser == that.fromUser()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (((((1 * 1000003) ^ this.view.hashCode()) * 1000003) ^ Float.floatToIntBits(this.rating)) * 1000003) ^ (this.fromUser ? 1231 : 1237);
    }
}
