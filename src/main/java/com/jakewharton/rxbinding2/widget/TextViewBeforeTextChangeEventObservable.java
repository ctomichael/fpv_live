package com.jakewharton.rxbinding2.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import com.jakewharton.rxbinding2.InitialValueObservable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

final class TextViewBeforeTextChangeEventObservable extends InitialValueObservable<TextViewBeforeTextChangeEvent> {
    private final TextView view;

    TextViewBeforeTextChangeEventObservable(TextView view2) {
        this.view = view2;
    }

    /* access modifiers changed from: protected */
    public void subscribeListener(Observer<? super TextViewBeforeTextChangeEvent> observer) {
        Listener listener = new Listener(this.view, observer);
        observer.onSubscribe(listener);
        this.view.addTextChangedListener(listener);
    }

    /* access modifiers changed from: protected */
    public TextViewBeforeTextChangeEvent getInitialValue() {
        return TextViewBeforeTextChangeEvent.create(this.view, this.view.getText(), 0, 0, 0);
    }

    static final class Listener extends MainThreadDisposable implements TextWatcher {
        private final Observer<? super TextViewBeforeTextChangeEvent> observer;
        private final TextView view;

        Listener(TextView view2, Observer<? super TextViewBeforeTextChangeEvent> observer2) {
            this.view = view2;
            this.observer = observer2;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (!isDisposed()) {
                this.observer.onNext(TextViewBeforeTextChangeEvent.create(this.view, s, start, count, after));
            }
        }

        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
        }

        /* access modifiers changed from: protected */
        public void onDispose() {
            this.view.removeTextChangedListener(this);
        }
    }
}
