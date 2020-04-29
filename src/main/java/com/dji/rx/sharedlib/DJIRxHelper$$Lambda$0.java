package com.dji.rx.sharedlib;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

final /* synthetic */ class DJIRxHelper$$Lambda$0 implements ObservableTransformer {
    static final ObservableTransformer $instance = new DJIRxHelper$$Lambda$0();

    private DJIRxHelper$$Lambda$0() {
    }

    public ObservableSource apply(Observable observable) {
        return observable.map(DJIRxHelper.mapIndexed());
    }
}
