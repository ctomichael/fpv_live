package com.jakewharton.rxbinding2.widget;

import android.view.KeyEvent;
import android.widget.TextView;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.functions.Predicate;

final class TextViewEditorActionEventObservable extends Observable<TextViewEditorActionEvent> {
    private final Predicate<? super TextViewEditorActionEvent> handled;
    private final TextView view;

    TextViewEditorActionEventObservable(TextView view2, Predicate<? super TextViewEditorActionEvent> handled2) {
        this.view = view2;
        this.handled = handled2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super TextViewEditorActionEvent> observer) {
        if (Preconditions.checkMainThread(observer)) {
            Listener listener = new Listener(this.view, observer, this.handled);
            observer.onSubscribe(listener);
            this.view.setOnEditorActionListener(listener);
        }
    }

    static final class Listener extends MainThreadDisposable implements TextView.OnEditorActionListener {
        private final Predicate<? super TextViewEditorActionEvent> handled;
        private final Observer<? super TextViewEditorActionEvent> observer;
        private final TextView view;

        Listener(TextView view2, Observer<? super TextViewEditorActionEvent> observer2, Predicate<? super TextViewEditorActionEvent> handled2) {
            this.view = view2;
            this.observer = observer2;
            this.handled = handled2;
        }

        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            TextViewEditorActionEvent event = TextViewEditorActionEvent.create(this.view, actionId, keyEvent);
            try {
                if (!isDisposed() && this.handled.test(event)) {
                    this.observer.onNext(event);
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
