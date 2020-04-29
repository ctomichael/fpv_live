package com.dji.rx.sharedlib;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.functions.Predicate;
import java.util.concurrent.TimeUnit;

final /* synthetic */ class DJIRxHelper$$Lambda$2 implements ObservableTransformer {
    private final Predicate arg$1;
    private final long arg$2;
    private final long arg$3;
    private final TimeUnit arg$4;
    private final Scheduler arg$5;

    DJIRxHelper$$Lambda$2(Predicate predicate, long j, long j2, TimeUnit timeUnit, Scheduler scheduler) {
        this.arg$1 = predicate;
        this.arg$2 = j;
        this.arg$3 = j2;
        this.arg$4 = timeUnit;
        this.arg$5 = scheduler;
    }

    public ObservableSource apply(Observable observable) {
        return observable.retryWhen(DJIRxHelper.retryTimesDelay(this.arg$1, this.arg$2, this.arg$3, this.arg$4, this.arg$5));
    }
}
