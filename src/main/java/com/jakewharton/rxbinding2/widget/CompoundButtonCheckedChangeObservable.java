package com.jakewharton.rxbinding2.widget;

import android.widget.CompoundButton;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

final class CompoundButtonCheckedChangeObservable extends InitialValueObservable<Boolean> {
    private final CompoundButton view;

    CompoundButtonCheckedChangeObservable(CompoundButton view2) {
        this.view = view2;
    }

    /* access modifiers changed from: protected */
    public void subscribeListener(Observer<? super Boolean> observer) {
        if (Preconditions.checkMainThread(observer)) {
            Listener listener = new Listener(this.view, observer);
            observer.onSubscribe(listener);
            this.view.setOnCheckedChangeListener(listener);
        }
    }

    /* access modifiers changed from: protected */
    public Boolean getInitialValue() {
        return Boolean.valueOf(this.view.isChecked());
    }

    static final class Listener extends MainThreadDisposable implements CompoundButton.OnCheckedChangeListener {
        private final Observer<? super Boolean> observer;
        private final CompoundButton view;

        Listener(CompoundButton view2, Observer<? super Boolean> observer2) {
            this.view = view2;
            this.observer = observer2;
        }

        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (!isDisposed()) {
                this.observer.onNext(Boolean.valueOf(isChecked));
            }
        }

        /* access modifiers changed from: protected */
        public void onDispose() {
            this.view.setOnCheckedChangeListener(null);
        }
    }
}
