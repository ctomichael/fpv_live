package com.jakewharton.rxbinding2.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import com.jakewharton.rxbinding2.InitialValueObservable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

final class TextViewTextObservable extends InitialValueObservable<CharSequence> {
    private final TextView view;

    TextViewTextObservable(TextView view2) {
        this.view = view2;
    }

    /* access modifiers changed from: protected */
    public void subscribeListener(Observer<? super CharSequence> observer) {
        Listener listener = new Listener(this.view, observer);
        observer.onSubscribe(listener);
        this.view.addTextChangedListener(listener);
    }

    /* access modifiers changed from: protected */
    public CharSequence getInitialValue() {
        return this.view.getText();
    }

    static final class Listener extends MainThreadDisposable implements TextWatcher {
        private final Observer<? super CharSequence> observer;
        private final TextView view;

        Listener(TextView view2, Observer<? super CharSequence> observer2) {
            this.view = view2;
            this.observer = observer2;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!isDisposed()) {
                this.observer.onNext(s);
            }
        }

        public void afterTextChanged(Editable s) {
        }

        /* access modifiers changed from: protected */
        public void onDispose() {
            this.view.removeTextChangedListener(this);
        }
    }
}
