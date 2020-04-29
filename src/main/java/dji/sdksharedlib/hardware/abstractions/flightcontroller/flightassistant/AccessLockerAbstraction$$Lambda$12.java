package dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.functions.Func1;

final /* synthetic */ class AccessLockerAbstraction$$Lambda$12 implements Func1 {
    static final Func1 $instance = new AccessLockerAbstraction$$Lambda$12();

    private AccessLockerAbstraction$$Lambda$12() {
    }

    public Object call(Object obj) {
        return ((Observable) obj).zipWith(Observable.range(1, 3), AccessLockerAbstraction$$Lambda$21.$instance).flatMap(AccessLockerAbstraction$$Lambda$22.$instance);
    }
}
