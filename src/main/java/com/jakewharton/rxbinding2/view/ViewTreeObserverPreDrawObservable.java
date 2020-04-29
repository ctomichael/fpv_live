package com.jakewharton.rxbinding2.view;

import android.view.View;
import android.view.ViewTreeObserver;
import com.jakewharton.rxbinding2.internal.Notification;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import java.util.concurrent.Callable;

final class ViewTreeObserverPreDrawObservable extends Observable<Object> {
    private final Callable<Boolean> proceedDrawingPass;
    private final View view;

    ViewTreeObserverPreDrawObservable(View view2, Callable<Boolean> proceedDrawingPass2) {
        this.view = view2;
        this.proceedDrawingPass = proceedDrawingPass2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super Object> observer) {
        if (Preconditions.checkMainThread(observer)) {
            Listener listener = new Listener(this.view, this.proceedDrawingPass, observer);
            observer.onSubscribe(listener);
            this.view.getViewTreeObserver().addOnPreDrawListener(listener);
        }
    }

    static final class Listener extends MainThreadDisposable implements ViewTreeObserver.OnPreDrawListener {
        private final Observer<? super Object> observer;
        private final Callable<Boolean> proceedDrawingPass;
        private final View view;

        Listener(View view2, Callable<Boolean> proceedDrawingPass2, Observer<? super Object> observer2) {
            this.view = view2;
            this.proceedDrawingPass = proceedDrawingPass2;
            this.observer = observer2;
        }

        public boolean onPreDraw() {
            if (!isDisposed()) {
                this.observer.onNext(Notification.INSTANCE);
                try {
                    return this.proceedDrawingPass.call().booleanValue();
                } catch (Exception e) {
                    this.observer.onError(e);
                    dispose();
                }
            }
            return true;
        }

        /* access modifiers changed from: protected */
        public void onDispose() {
            this.view.getViewTreeObserver().removeOnPreDrawListener(this);
        }
    }
}
