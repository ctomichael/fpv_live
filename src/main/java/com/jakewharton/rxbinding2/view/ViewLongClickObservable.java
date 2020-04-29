package com.jakewharton.rxbinding2.view;

import android.view.View;
import com.jakewharton.rxbinding2.internal.Notification;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import java.util.concurrent.Callable;

final class ViewLongClickObservable extends Observable<Object> {
    private final Callable<Boolean> handled;
    private final View view;

    ViewLongClickObservable(View view2, Callable<Boolean> handled2) {
        this.view = view2;
        this.handled = handled2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super Object> observer) {
        if (Preconditions.checkMainThread(observer)) {
            Listener listener = new Listener(this.view, this.handled, observer);
            observer.onSubscribe(listener);
            this.view.setOnLongClickListener(listener);
        }
    }

    static final class Listener extends MainThreadDisposable implements View.OnLongClickListener {
        private final Callable<Boolean> handled;
        private final Observer<? super Object> observer;
        private final View view;

        Listener(View view2, Callable<Boolean> handled2, Observer<? super Object> observer2) {
            this.view = view2;
            this.observer = observer2;
            this.handled = handled2;
        }

        public boolean onLongClick(View v) {
            if (!isDisposed()) {
                try {
                    if (this.handled.call().booleanValue()) {
                        this.observer.onNext(Notification.INSTANCE);
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
            this.view.setOnLongClickListener(null);
        }
    }
}
