package com.dji.rx.sharedlib;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import java.util.concurrent.atomic.AtomicLong;

final /* synthetic */ class DJIRxHelper$$Lambda$7 implements Consumer {
    private final Predicate arg$1;
    private final AtomicLong arg$2;
    private final long arg$3;

    DJIRxHelper$$Lambda$7(Predicate predicate, AtomicLong atomicLong, long j) {
        this.arg$1 = predicate;
        this.arg$2 = atomicLong;
        this.arg$3 = j;
    }

    public void accept(Object obj) {
        DJIRxHelper.lambda$null$5$DJIRxHelper(this.arg$1, this.arg$2, this.arg$3, (Throwable) obj);
    }
}
