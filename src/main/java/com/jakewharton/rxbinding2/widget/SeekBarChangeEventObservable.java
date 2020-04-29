package com.jakewharton.rxbinding2.widget;

import android.widget.SeekBar;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

final class SeekBarChangeEventObservable extends InitialValueObservable<SeekBarChangeEvent> {
    private final SeekBar view;

    SeekBarChangeEventObservable(SeekBar view2) {
        this.view = view2;
    }

    /* access modifiers changed from: protected */
    public void subscribeListener(Observer<? super SeekBarChangeEvent> observer) {
        if (Preconditions.checkMainThread(observer)) {
            Listener listener = new Listener(this.view, observer);
            this.view.setOnSeekBarChangeListener(listener);
            observer.onSubscribe(listener);
        }
    }

    /* access modifiers changed from: protected */
    public SeekBarChangeEvent getInitialValue() {
        return SeekBarProgressChangeEvent.create(this.view, this.view.getProgress(), false);
    }

    static final class Listener extends MainThreadDisposable implements SeekBar.OnSeekBarChangeListener {
        private final Observer<? super SeekBarChangeEvent> observer;
        private final SeekBar view;

        Listener(SeekBar view2, Observer<? super SeekBarChangeEvent> observer2) {
            this.view = view2;
            this.observer = observer2;
        }

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!isDisposed()) {
                this.observer.onNext(SeekBarProgressChangeEvent.create(seekBar, progress, fromUser));
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            if (!isDisposed()) {
                this.observer.onNext(SeekBarStartChangeEvent.create(seekBar));
            }
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            if (!isDisposed()) {
                this.observer.onNext(SeekBarStopChangeEvent.create(seekBar));
            }
        }

        /* access modifiers changed from: protected */
        public void onDispose() {
            this.view.setOnSeekBarChangeListener(null);
        }
    }
}
