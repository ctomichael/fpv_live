package com.dji.rx.sharedlib;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

final /* synthetic */ class DJIRxHelper$$Lambda$5 implements Function {
    private final Predicate arg$1;
    private final AtomicLong arg$2;
    private final long arg$3;
    private final long arg$4;
    private final TimeUnit arg$5;
    private final Scheduler arg$6;

    DJIRxHelper$$Lambda$5(Predicate predicate, AtomicLong atomicLong, long j, long j2, TimeUnit timeUnit, Scheduler scheduler) {
        this.arg$1 = predicate;
        this.arg$2 = atomicLong;
        this.arg$3 = j;
        this.arg$4 = j2;
        this.arg$5 = timeUnit;
        this.arg$6 = scheduler;
    }

    public Object apply(Object obj) {
        return ((Observable) obj).doOnNext(new DJIRxHelper$$Lambda$7(this.arg$1, this.arg$2, this.arg$3)).delay(this.arg$4, this.arg$5, this.arg$6);
    }
}
