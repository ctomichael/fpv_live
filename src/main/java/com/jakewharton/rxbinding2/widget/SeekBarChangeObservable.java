package com.jakewharton.rxbinding2.widget;

import android.support.annotation.Nullable;
import android.widget.SeekBar;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

final class SeekBarChangeObservable extends InitialValueObservable<Integer> {
    @Nullable
    private final Boolean shouldBeFromUser;
    private final SeekBar view;

    SeekBarChangeObservable(SeekBar view2, @Nullable Boolean shouldBeFromUser2) {
        this.view = view2;
        this.shouldBeFromUser = shouldBeFromUser2;
    }

    /* access modifiers changed from: protected */
    public void subscribeListener(Observer<? super Integer> observer) {
        if (Preconditions.checkMainThread(observer)) {
            Listener listener = new Listener(this.view, this.shouldBeFromUser, observer);
            this.view.setOnSeekBarChangeListener(listener);
            observer.onSubscribe(listener);
        }
    }

    /* access modifiers changed from: protected */
    public Integer getInitialValue() {
        return Integer.valueOf(this.view.getProgress());
    }

    static final class Listener extends MainThreadDisposable implements SeekBar.OnSeekBarChangeListener {
        private final Observer<? super Integer> observer;
        private final Boolean shouldBeFromUser;
        private final SeekBar view;

        Listener(SeekBar view2, Boolean shouldBeFromUser2, Observer<? super Integer> observer2) {
            this.view = view2;
            this.shouldBeFromUser = shouldBeFromUser2;
            this.observer = observer2;
        }

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (isDisposed()) {
                return;
            }
            if (this.shouldBeFromUser == null || this.shouldBeFromUser.booleanValue() == fromUser) {
                this.observer.onNext(Integer.valueOf(progress));
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        /* access modifiers changed from: protected */
        public void onDispose() {
            this.view.setOnSeekBarChangeListener(null);
        }
    }
}
