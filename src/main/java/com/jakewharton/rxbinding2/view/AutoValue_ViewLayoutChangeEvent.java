package com.jakewharton.rxbinding2.view;

import android.support.annotation.NonNull;
import android.view.View;

final class AutoValue_ViewLayoutChangeEvent extends ViewLayoutChangeEvent {
    private final int bottom;
    private final int left;
    private final int oldBottom;
    private final int oldLeft;
    private final int oldRight;
    private final int oldTop;
    private final int right;
    private final int top;
    private final View view;

    AutoValue_ViewLayoutChangeEvent(View view2, int left2, int top2, int right2, int bottom2, int oldLeft2, int oldTop2, int oldRight2, int oldBottom2) {
        if (view2 == null) {
            throw new NullPointerException("Null view");
        }
        this.view = view2;
        this.left = left2;
        this.top = top2;
        this.right = right2;
        this.bottom = bottom2;
        this.oldLeft = oldLeft2;
        this.oldTop = oldTop2;
        this.oldRight = oldRight2;
        this.oldBottom = oldBottom2;
    }

    @NonNull
    public View view() {
        return this.view;
    }

    public int left() {
        return this.left;
    }

    public int top() {
        return this.top;
    }

    public int right() {
        return this.right;
    }

    public int bottom() {
        return this.bottom;
    }

    public int oldLeft() {
        return this.oldLeft;
    }

    public int oldTop() {
        return this.oldTop;
    }

    public int oldRight() {
        return this.oldRight;
    }

    public int oldBottom() {
        return this.oldBottom;
    }

    public String toString() {
        return "ViewLayoutChangeEvent{view=" + this.view + ", left=" + this.left + ", top=" + this.top + ", right=" + this.right + ", bottom=" + this.bottom + ", oldLeft=" + this.oldLeft + ", oldTop=" + this.oldTop + ", oldRight=" + this.oldRight + ", oldBottom=" + this.oldBottom + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ViewLayoutChangeEvent)) {
            return false;
        }
        ViewLayoutChangeEvent that = (ViewLayoutChangeEvent) o;
        if (this.view.equals(that.view()) && this.left == that.left() && this.top == that.top() && this.right == that.right() && this.bottom == that.bottom() && this.oldLeft == that.oldLeft() && this.oldTop == that.oldTop() && this.oldRight == that.oldRight() && this.oldBottom == that.oldBottom()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (((((((((((((((((1 * 1000003) ^ this.view.hashCode()) * 1000003) ^ this.left) * 1000003) ^ this.top) * 1000003) ^ this.right) * 1000003) ^ this.bottom) * 1000003) ^ this.oldLeft) * 1000003) ^ this.oldTop) * 1000003) ^ this.oldRight) * 1000003) ^ this.oldBottom;
    }
}
