package com.jakewharton.rxbinding2.view;

import android.support.annotation.NonNull;
import android.view.View;

final class AutoValue_ViewScrollChangeEvent extends ViewScrollChangeEvent {
    private final int oldScrollX;
    private final int oldScrollY;
    private final int scrollX;
    private final int scrollY;
    private final View view;

    AutoValue_ViewScrollChangeEvent(View view2, int scrollX2, int scrollY2, int oldScrollX2, int oldScrollY2) {
        if (view2 == null) {
            throw new NullPointerException("Null view");
        }
        this.view = view2;
        this.scrollX = scrollX2;
        this.scrollY = scrollY2;
        this.oldScrollX = oldScrollX2;
        this.oldScrollY = oldScrollY2;
    }

    @NonNull
    public View view() {
        return this.view;
    }

    public int scrollX() {
        return this.scrollX;
    }

    public int scrollY() {
        return this.scrollY;
    }

    public int oldScrollX() {
        return this.oldScrollX;
    }

    public int oldScrollY() {
        return this.oldScrollY;
    }

    public String toString() {
        return "ViewScrollChangeEvent{view=" + this.view + ", scrollX=" + this.scrollX + ", scrollY=" + this.scrollY + ", oldScrollX=" + this.oldScrollX + ", oldScrollY=" + this.oldScrollY + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ViewScrollChangeEvent)) {
            return false;
        }
        ViewScrollChangeEvent that = (ViewScrollChangeEvent) o;
        if (this.view.equals(that.view()) && this.scrollX == that.scrollX() && this.scrollY == that.scrollY() && this.oldScrollX == that.oldScrollX() && this.oldScrollY == that.oldScrollY()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (((((((((1 * 1000003) ^ this.view.hashCode()) * 1000003) ^ this.scrollX) * 1000003) ^ this.scrollY) * 1000003) ^ this.oldScrollX) * 1000003) ^ this.oldScrollY;
    }
}
