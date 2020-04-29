package com.jakewharton.rxbinding2.widget;

import android.widget.SearchView;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

final class SearchViewQueryTextChangesObservable extends InitialValueObservable<CharSequence> {
    private final SearchView view;

    SearchViewQueryTextChangesObservable(SearchView view2) {
        this.view = view2;
    }

    /* access modifiers changed from: protected */
    public void subscribeListener(Observer<? super CharSequence> observer) {
        if (Preconditions.checkMainThread(observer)) {
            Listener listener = new Listener(this.view, observer);
            this.view.setOnQueryTextListener(listener);
            observer.onSubscribe(listener);
        }
    }

    /* access modifiers changed from: protected */
    public CharSequence getInitialValue() {
        return this.view.getQuery();
    }

    static final class Listener extends MainThreadDisposable implements SearchView.OnQueryTextListener {
        private final Observer<? super CharSequence> observer;
        private final SearchView view;

        Listener(SearchView view2, Observer<? super CharSequence> observer2) {
            this.view = view2;
            this.observer = observer2;
        }

        public boolean onQueryTextChange(String s) {
            if (isDisposed()) {
                return false;
            }
            this.observer.onNext(s);
            return true;
        }

        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        /* access modifiers changed from: protected */
        public void onDispose() {
            this.view.setOnQueryTextListener(null);
        }
    }
}
