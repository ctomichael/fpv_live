package com.jakewharton.rxbinding2.widget;

import android.database.DataSetObserver;
import android.widget.Adapter;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

final class AdapterDataChangeObservable<T extends Adapter> extends InitialValueObservable<T> {
    private final T adapter;

    AdapterDataChangeObservable(T adapter2) {
        this.adapter = adapter2;
    }

    /* access modifiers changed from: protected */
    public void subscribeListener(Observer<? super T> observer) {
        if (Preconditions.checkMainThread(observer)) {
            ObserverDisposable<T> disposableDataSetObserver = new ObserverDisposable<>(this.adapter, observer);
            this.adapter.registerDataSetObserver(disposableDataSetObserver.dataSetObserver);
            observer.onSubscribe(disposableDataSetObserver);
        }
    }

    /* access modifiers changed from: protected */
    public T getInitialValue() {
        return this.adapter;
    }

    static final class ObserverDisposable<T extends Adapter> extends MainThreadDisposable {
        private final T adapter;
        final DataSetObserver dataSetObserver;

        ObserverDisposable(final T adapter2, final Observer<? super T> observer) {
            this.adapter = adapter2;
            this.dataSetObserver = new DataSetObserver() {
                /* class com.jakewharton.rxbinding2.widget.AdapterDataChangeObservable.ObserverDisposable.AnonymousClass1 */

                public void onChanged() {
                    if (!ObserverDisposable.this.isDisposed()) {
                        observer.onNext(adapter2);
                    }
                }
            };
        }

        /* access modifiers changed from: protected */
        public void onDispose() {
            this.adapter.unregisterDataSetObserver(this.dataSetObserver);
        }
    }
}
