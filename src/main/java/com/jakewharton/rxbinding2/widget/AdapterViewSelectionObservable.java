package com.jakewharton.rxbinding2.widget;

import android.view.View;
import android.widget.AdapterView;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

final class AdapterViewSelectionObservable extends InitialValueObservable<AdapterViewSelectionEvent> {
    private final AdapterView<?> view;

    AdapterViewSelectionObservable(AdapterView<?> view2) {
        this.view = view2;
    }

    /* access modifiers changed from: protected */
    public void subscribeListener(Observer<? super AdapterViewSelectionEvent> observer) {
        if (Preconditions.checkMainThread(observer)) {
            Listener listener = new Listener(this.view, observer);
            this.view.setOnItemSelectedListener(listener);
            observer.onSubscribe(listener);
        }
    }

    /* access modifiers changed from: protected */
    public AdapterViewSelectionEvent getInitialValue() {
        int selectedPosition = this.view.getSelectedItemPosition();
        if (selectedPosition == -1) {
            return AdapterViewNothingSelectionEvent.create(this.view);
        }
        return AdapterViewItemSelectionEvent.create(this.view, this.view.getSelectedView(), selectedPosition, this.view.getSelectedItemId());
    }

    static final class Listener extends MainThreadDisposable implements AdapterView.OnItemSelectedListener {
        private final Observer<? super AdapterViewSelectionEvent> observer;
        private final AdapterView<?> view;

        Listener(AdapterView<?> view2, Observer<? super AdapterViewSelectionEvent> observer2) {
            this.view = view2;
            this.observer = observer2;
        }

        public void onItemSelected(AdapterView<?> parent, View view2, int position, long id) {
            if (!isDisposed()) {
                this.observer.onNext(AdapterViewItemSelectionEvent.create(parent, view2, position, id));
            }
        }

        public void onNothingSelected(AdapterView<?> parent) {
            if (!isDisposed()) {
                this.observer.onNext(AdapterViewNothingSelectionEvent.create(parent));
            }
        }

        /* access modifiers changed from: protected */
        public void onDispose() {
            this.view.setOnItemSelectedListener(null);
        }
    }
}
