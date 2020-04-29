package dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;

final /* synthetic */ class AccessLockerAbstraction$$Lambda$17 implements Observable.OnSubscribe {
    private final AccessLockerAbstraction arg$1;

    AccessLockerAbstraction$$Lambda$17(AccessLockerAbstraction accessLockerAbstraction) {
        this.arg$1 = accessLockerAbstraction;
    }

    public void call(Object obj) {
        this.arg$1.lambda$getDataLockerState$21$AccessLockerAbstraction((Subscriber) obj);
    }
}
