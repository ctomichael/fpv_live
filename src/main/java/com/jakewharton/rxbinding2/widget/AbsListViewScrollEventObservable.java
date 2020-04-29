package com.jakewharton.rxbinding2.widget;

import android.widget.AbsListView;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

final class AbsListViewScrollEventObservable extends Observable<AbsListViewScrollEvent> {
    private final AbsListView view;

    AbsListViewScrollEventObservable(AbsListView view2) {
        this.view = view2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super AbsListViewScrollEvent> observer) {
        if (Preconditions.checkMainThread(observer)) {
            Listener listener = new Listener(this.view, observer);
            observer.onSubscribe(listener);
            this.view.setOnScrollListener(listener);
        }
    }

    static final class Listener extends MainThreadDisposable implements AbsListView.OnScrollListener {
        private int currentScrollState = 0;
        private final Observer<? super AbsListViewScrollEvent> observer;
        private final AbsListView view;

        Listener(AbsListView view2, Observer<? super AbsListViewScrollEvent> observer2) {
            this.view = view2;
            this.observer = observer2;
        }

        public void onScrollStateChanged(AbsListView absListView, int scrollState) {
            this.currentScrollState = scrollState;
            if (!isDisposed()) {
                this.observer.onNext(AbsListViewScrollEvent.create(this.view, scrollState, this.view.getFirstVisiblePosition(), this.view.getChildCount(), this.view.getCount()));
            }
        }

        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (!isDisposed()) {
                this.observer.onNext(AbsListViewScrollEvent.create(this.view, this.currentScrollState, firstVisibleItem, visibleItemCount, totalItemCount));
            }
        }

        /* access modifiers changed from: protected */
        public void onDispose() {
            this.view.setOnScrollListener(null);
        }
    }
}
