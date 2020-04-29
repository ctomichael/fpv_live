package com.jakewharton.rxbinding2.view;

import android.view.View;
import com.jakewharton.rxbinding2.internal.Notification;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

final class ViewAttachesObservable extends Observable<Object> {
    private final boolean callOnAttach;
    private final View view;

    ViewAttachesObservable(View view2, boolean callOnAttach2) {
        this.view = view2;
        this.callOnAttach = callOnAttach2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super Object> observer) {
        if (Preconditions.checkMainThread(observer)) {
            Listener listener = new Listener(this.view, this.callOnAttach, observer);
            observer.onSubscribe(listener);
            this.view.addOnAttachStateChangeListener(listener);
        }
    }

    static final class Listener extends MainThreadDisposable implements View.OnAttachStateChangeListener {
        private final boolean callOnAttach;
        private final Observer<? super Object> observer;
        private final View view;

        Listener(View view2, boolean callOnAttach2, Observer<? super Object> observer2) {
            this.view = view2;
            this.callOnAttach = callOnAttach2;
            this.observer = observer2;
        }

        public void onViewAttachedToWindow(View v) {
            if (this.callOnAttach && !isDisposed()) {
                this.observer.onNext(Notification.INSTANCE);
            }
        }

        public void onViewDetachedFromWindow(View v) {
            if (!this.callOnAttach && !isDisposed()) {
                this.observer.onNext(Notification.INSTANCE);
            }
        }

        /* access modifiers changed from: protected */
        public void onDispose() {
            this.view.removeOnAttachStateChangeListener(this);
        }
    }
}
