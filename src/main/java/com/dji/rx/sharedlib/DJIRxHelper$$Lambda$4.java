package com.dji.rx.sharedlib;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import java.util.concurrent.TimeUnit;

final /* synthetic */ class DJIRxHelper$$Lambda$4 implements ObservableTransformer {
    private final long arg$1;
    private final long arg$2;
    private final TimeUnit arg$3;
    private final Scheduler arg$4;

    DJIRxHelper$$Lambda$4(long j, long j2, TimeUnit timeUnit, Scheduler scheduler) {
        this.arg$1 = j;
        this.arg$2 = j2;
        this.arg$3 = timeUnit;
        this.arg$4 = scheduler;
    }

    public ObservableSource apply(Observable observable) {
        return observable.retryWhen(DJIRxHelper.retryTimesDelaySwallow(this.arg$1, this.arg$2, this.arg$3, this.arg$4));
    }
}
