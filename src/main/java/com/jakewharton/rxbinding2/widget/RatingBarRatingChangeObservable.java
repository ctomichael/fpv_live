package com.jakewharton.rxbinding2.widget;

import android.widget.RatingBar;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

final class RatingBarRatingChangeObservable extends InitialValueObservable<Float> {
    private final RatingBar view;

    RatingBarRatingChangeObservable(RatingBar view2) {
        this.view = view2;
    }

    /* access modifiers changed from: protected */
    public void subscribeListener(Observer<? super Float> observer) {
        if (Preconditions.checkMainThread(observer)) {
            Listener listener = new Listener(this.view, observer);
            this.view.setOnRatingBarChangeListener(listener);
            observer.onSubscribe(listener);
        }
    }

    /* access modifiers changed from: protected */
    public Float getInitialValue() {
        return Float.valueOf(this.view.getRating());
    }

    static final class Listener extends MainThreadDisposable implements RatingBar.OnRatingBarChangeListener {
        private final Observer<? super Float> observer;
        private final RatingBar view;

        Listener(RatingBar view2, Observer<? super Float> observer2) {
            this.view = view2;
            this.observer = observer2;
        }

        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            if (!isDisposed()) {
                this.observer.onNext(Float.valueOf(rating));
            }
        }

        /* access modifiers changed from: protected */
        public void onDispose() {
            this.view.setOnRatingBarChangeListener(null);
        }
    }
}
