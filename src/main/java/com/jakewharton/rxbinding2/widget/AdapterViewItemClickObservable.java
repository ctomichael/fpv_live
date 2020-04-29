package com.jakewharton.rxbinding2.widget;

import android.view.View;
import android.widget.AdapterView;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

final class AdapterViewItemClickObservable extends Observable<Integer> {
    private final AdapterView<?> view;

    AdapterViewItemClickObservable(AdapterView<?> view2) {
        this.view = view2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super Integer> observer) {
        if (Preconditions.checkMainThread(observer)) {
            Listener listener = new Listener(this.view, observer);
            observer.onSubscribe(listener);
            this.view.setOnItemClickListener(listener);
        }
    }

    static final class Listener extends MainThreadDisposable implements AdapterView.OnItemClickListener {
        private final Observer<? super Integer> observer;
        private final AdapterView<?> view;

        Listener(AdapterView<?> view2, Observer<? super Integer> observer2) {
            this.view = view2;
            this.observer = observer2;
        }

        public void onItemClick(AdapterView<?> adapterView, View view2, int position, long id) {
            if (!isDisposed()) {
                this.observer.onNext(Integer.valueOf(position));
            }
        }

        /* access modifiers changed from: protected */
        public void onDispose() {
            this.view.setOnItemClickListener(null);
        }
    }
}
