package com.jakewharton.rxbinding2.widget;

import android.view.View;
import android.widget.AdapterView;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import java.util.concurrent.Callable;

final class AdapterViewItemLongClickObservable extends Observable<Integer> {
    private final Callable<Boolean> handled;
    private final AdapterView<?> view;

    AdapterViewItemLongClickObservable(AdapterView<?> view2, Callable<Boolean> handled2) {
        this.view = view2;
        this.handled = handled2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super Integer> observer) {
        if (Preconditions.checkMainThread(observer)) {
            Listener listener = new Listener(this.view, observer, this.handled);
            observer.onSubscribe(listener);
            this.view.setOnItemLongClickListener(listener);
        }
    }

    static final class Listener extends MainThreadDisposable implements AdapterView.OnItemLongClickListener {
        private final Callable<Boolean> handled;
        private final Observer<? super Integer> observer;
        private final AdapterView<?> view;

        Listener(AdapterView<?> view2, Observer<? super Integer> observer2, Callable<Boolean> handled2) {
            this.view = view2;
            this.observer = observer2;
            this.handled = handled2;
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view2, int position, long id) {
            if (!isDisposed()) {
                try {
                    if (this.handled.call().booleanValue()) {
                        this.observer.onNext(Integer.valueOf(position));
                        return true;
                    }
                } catch (Exception e) {
                    this.observer.onError(e);
                    dispose();
                }
            }
            return false;
        }

        /* access modifiers changed from: protected */
        public void onDispose() {
            this.view.setOnItemLongClickListener(null);
        }
    }
}
