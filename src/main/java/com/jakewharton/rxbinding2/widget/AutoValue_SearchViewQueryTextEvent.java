package com.jakewharton.rxbinding2.widget;

import android.support.annotation.NonNull;
import android.widget.SearchView;

final class AutoValue_SearchViewQueryTextEvent extends SearchViewQueryTextEvent {
    private final boolean isSubmitted;
    private final CharSequence queryText;
    private final SearchView view;

    AutoValue_SearchViewQueryTextEvent(SearchView view2, CharSequence queryText2, boolean isSubmitted2) {
        if (view2 == null) {
            throw new NullPointerException("Null view");
        }
        this.view = view2;
        if (queryText2 == null) {
            throw new NullPointerException("Null queryText");
        }
        this.queryText = queryText2;
        this.isSubmitted = isSubmitted2;
    }

    @NonNull
    public SearchView view() {
        return this.view;
    }

    @NonNull
    public CharSequence queryText() {
        return this.queryText;
    }

    public boolean isSubmitted() {
        return this.isSubmitted;
    }

    public String toString() {
        return "SearchViewQueryTextEvent{view=" + this.view + ", queryText=" + ((Object) this.queryText) + ", isSubmitted=" + this.isSubmitted + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SearchViewQueryTextEvent)) {
            return false;
        }
        SearchViewQueryTextEvent that = (SearchViewQueryTextEvent) o;
        if (!this.view.equals(that.view()) || !this.queryText.equals(that.queryText()) || this.isSubmitted != that.isSubmitted()) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((((1 * 1000003) ^ this.view.hashCode()) * 1000003) ^ this.queryText.hashCode()) * 1000003) ^ (this.isSubmitted ? 1231 : 1237);
    }
}
