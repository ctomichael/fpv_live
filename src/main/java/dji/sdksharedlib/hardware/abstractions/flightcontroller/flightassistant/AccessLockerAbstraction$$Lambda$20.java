package dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.functions.Func1;
import java.util.concurrent.TimeUnit;

final /* synthetic */ class AccessLockerAbstraction$$Lambda$20 implements Func1 {
    static final Func1 $instance = new AccessLockerAbstraction$$Lambda$20();

    private AccessLockerAbstraction$$Lambda$20() {
    }

    public Object call(Object obj) {
        return Observable.timer(1, TimeUnit.SECONDS);
    }
}
