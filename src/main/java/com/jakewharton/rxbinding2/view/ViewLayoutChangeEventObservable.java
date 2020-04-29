package com.jakewharton.rxbinding2.view;

import android.view.View;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

final class ViewLayoutChangeEventObservable extends Observable<ViewLayoutChangeEvent> {
    private final View view;

    ViewLayoutChangeEventObservable(View view2) {
        this.view = view2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super ViewLayoutChangeEvent> observer) {
        if (Preconditions.checkMainThread(observer)) {
            Listener listener = new Listener(this.view, observer);
            observer.onSubscribe(listener);
            this.view.addOnLayoutChangeListener(listener);
        }
    }

    static final class Listener extends MainThreadDisposable implements View.OnLayoutChangeListener {
        private final Observer<? super ViewLayoutChangeEvent> observer;
        private final View view;

        Listener(View view2, Observer<? super ViewLayoutChangeEvent> observer2) {
            this.view = view2;
            this.observer = observer2;
        }

        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            if (!isDisposed()) {
                this.observer.onNext(ViewLayoutChangeEvent.create(v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom));
            }
        }

        /* access modifiers changed from: protected */
        public void onDispose() {
            this.view.removeOnLayoutChangeListener(this);
        }
    }
}
