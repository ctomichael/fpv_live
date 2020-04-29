package dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant;

import dji.thirdparty.rx.functions.Action1;

final /* synthetic */ class AccessLockerAbstraction$$Lambda$13 implements Action1 {
    private final AccessLockerAbstraction arg$1;

    AccessLockerAbstraction$$Lambda$13(AccessLockerAbstraction accessLockerAbstraction) {
        this.arg$1 = accessLockerAbstraction;
    }

    public void call(Object obj) {
        this.arg$1.lambda$computeCurrentDataProtectionState$15$AccessLockerAbstraction(obj);
    }
}
