package com.jakewharton.rxbinding2.widget;

import android.support.annotation.NonNull;
import android.widget.AbsListView;

final class AutoValue_AbsListViewScrollEvent extends AbsListViewScrollEvent {
    private final int firstVisibleItem;
    private final int scrollState;
    private final int totalItemCount;
    private final AbsListView view;
    private final int visibleItemCount;

    AutoValue_AbsListViewScrollEvent(AbsListView view2, int scrollState2, int firstVisibleItem2, int visibleItemCount2, int totalItemCount2) {
        if (view2 == null) {
            throw new NullPointerException("Null view");
        }
        this.view = view2;
        this.scrollState = scrollState2;
        this.firstVisibleItem = firstVisibleItem2;
        this.visibleItemCount = visibleItemCount2;
        this.totalItemCount = totalItemCount2;
    }

    @NonNull
    public AbsListView view() {
        return this.view;
    }

    public int scrollState() {
        return this.scrollState;
    }

    public int firstVisibleItem() {
        return this.firstVisibleItem;
    }

    public int visibleItemCount() {
        return this.visibleItemCount;
    }

    public int totalItemCount() {
        return this.totalItemCount;
    }

    public String toString() {
        return "AbsListViewScrollEvent{view=" + this.view + ", scrollState=" + this.scrollState + ", firstVisibleItem=" + this.firstVisibleItem + ", visibleItemCount=" + this.visibleItemCount + ", totalItemCount=" + this.totalItemCount + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AbsListViewScrollEvent)) {
            return false;
        }
        AbsListViewScrollEvent that = (AbsListViewScrollEvent) o;
        if (this.view.equals(that.view()) && this.scrollState == that.scrollState() && this.firstVisibleItem == that.firstVisibleItem() && this.visibleItemCount == that.visibleItemCount() && this.totalItemCount == that.totalItemCount()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (((((((((1 * 1000003) ^ this.view.hashCode()) * 1000003) ^ this.scrollState) * 1000003) ^ this.firstVisibleItem) * 1000003) ^ this.visibleItemCount) * 1000003) ^ this.totalItemCount;
    }
}
