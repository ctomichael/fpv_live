package com.jakewharton.rxbinding2.widget;

import android.widget.RatingBar;
import io.reactivex.functions.Consumer;

final /* synthetic */ class RxRatingBar$$Lambda$0 implements Consumer {
    private final RatingBar arg$1;

    private RxRatingBar$$Lambda$0(RatingBar ratingBar) {
        this.arg$1 = ratingBar;
    }

    static Consumer get$Lambda(RatingBar ratingBar) {
        return new RxRatingBar$$Lambda$0(ratingBar);
    }

    public void accept(Object obj) {
        this.arg$1.setRating(((Float) obj).floatValue());
    }
}
