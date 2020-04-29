package com.jakewharton.rxbinding2.widget;

import android.view.KeyEvent;
import android.widget.TextView;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.functions.Predicate;

final class TextViewEditorActionObservable extends Observable<Integer> {
    private final Predicate<? super Integer> handled;
    private final TextView view;

    TextViewEditorActionObservable(TextView view2, Predicate<? super Integer> handled2) {
        this.view = view2;
        this.handled = handled2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super Integer> observer) {
        if (Preconditions.checkMainThread(observer)) {
            Listener listener = new Listener(this.view, observer, this.handled);
            observer.onSubscribe(listener);
            this.view.setOnEditorActionListener(listener);
        }
    }

    static final class Listener extends MainThreadDisposable implements TextView.OnEditorActionListener {
        private final Predicate<? super Integer> handled;
        private final Observer<? super Integer> observer;
        private final TextView view;

        Listener(TextView view2, Observer<? super Integer> observer2, Predicate<? super Integer> handled2) {
            this.view = view2;
            this.observer = observer2;
            this.handled = handled2;
        }

        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            try {
                if (!isDisposed() && this.handled.test(Integer.valueOf(actionId))) {
                    this.observer.onNext(Integer.valueOf(actionId));
                    return true;
                }
            } catch (Exception e) {
                this.observer.onError(e);
                dispose();
            }
            return false;
        }

        /* access modifiers changed from: protected */
        public void onDispose() {
            this.view.setOnEditorActionListener(null);
        }
    }
}
