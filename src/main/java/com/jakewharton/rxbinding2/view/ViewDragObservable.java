package com.jakewharton.rxbinding2.view;

import android.view.DragEvent;
import android.view.View;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.functions.Predicate;

final class ViewDragObservable extends Observable<DragEvent> {
    private final Predicate<? super DragEvent> handled;
    private final View view;

    ViewDragObservable(View view2, Predicate<? super DragEvent> handled2) {
        this.view = view2;
        this.handled = handled2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super DragEvent> observer) {
        if (Preconditions.checkMainThread(observer)) {
            Listener listener = new Listener(this.view, this.handled, observer);
            observer.onSubscribe(listener);
            this.view.setOnDragListener(listener);
        }
    }

    static final class Listener extends MainThreadDisposable implements View.OnDragListener {
        private final Predicate<? super DragEvent> handled;
        private final Observer<? super DragEvent> observer;
        private final View view;

        Listener(View view2, Predicate<? super DragEvent> handled2, Observer<? super DragEvent> observer2) {
            this.view = view2;
            this.handled = handled2;
            this.observer = observer2;
        }

        public boolean onDrag(View v, DragEvent event) {
            if (!isDisposed()) {
                try {
                    if (this.handled.test(event)) {
                        this.observer.onNext(event);
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
            this.view.setOnDragListener(null);
        }
    }
}
