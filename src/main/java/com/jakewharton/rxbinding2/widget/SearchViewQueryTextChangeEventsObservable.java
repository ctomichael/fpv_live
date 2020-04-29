package com.jakewharton.rxbinding2.widget;

import android.widget.SearchView;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

final class SearchViewQueryTextChangeEventsObservable extends InitialValueObservable<SearchViewQueryTextEvent> {
    private final SearchView view;

    SearchViewQueryTextChangeEventsObservable(SearchView view2) {
        this.view = view2;
    }

    /* access modifiers changed from: protected */
    public void subscribeListener(Observer<? super SearchViewQueryTextEvent> observer) {
        if (Preconditions.checkMainThread(observer)) {
            Listener listener = new Listener(this.view, observer);
            this.view.setOnQueryTextListener(listener);
            observer.onSubscribe(listener);
        }
    }

    /* access modifiers changed from: protected */
    public SearchViewQueryTextEvent getInitialValue() {
        return SearchViewQueryTextEvent.create(this.view, this.view.getQuery(), false);
    }

    static final class Listener extends MainThreadDisposable implements SearchView.OnQueryTextListener {
        private final Observer<? super SearchViewQueryTextEvent> observer;
        private final SearchView view;

        Listener(SearchView view2, Observer<? super SearchViewQueryTextEvent> observer2) {
            this.view = view2;
            this.observer = observer2;
        }

        public boolean onQueryTextChange(String s) {
            if (isDisposed()) {
                return false;
            }
            this.observer.onNext(SearchViewQueryTextEvent.create(this.view, s, false));
            return true;
        }

        public boolean onQueryTextSubmit(String query) {
            if (isDisposed()) {
                return false;
            }
            this.observer.onNext(SearchViewQueryTextEvent.create(this.view, query, true));
            return true;
        }

        /* access modifiers changed from: protected */
        public void onDispose() {
            this.view.setOnQueryTextListener(null);
        }
    }
}
