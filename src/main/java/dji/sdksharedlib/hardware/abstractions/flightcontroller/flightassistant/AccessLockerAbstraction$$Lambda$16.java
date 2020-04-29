package dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant;

import dji.common.flightcontroller.accesslocker.UserAccountInfo;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;

final /* synthetic */ class AccessLockerAbstraction$$Lambda$16 implements Observable.OnSubscribe {
    private final AccessLockerAbstraction arg$1;
    private final UserAccountInfo arg$2;
    private final String arg$3;

    AccessLockerAbstraction$$Lambda$16(AccessLockerAbstraction accessLockerAbstraction, UserAccountInfo userAccountInfo, String str) {
        this.arg$1 = accessLockerAbstraction;
        this.arg$2 = userAccountInfo;
        this.arg$3 = str;
    }

    public void call(Object obj) {
        this.arg$1.lambda$changeUserInfo$20$AccessLockerAbstraction(this.arg$2, this.arg$3, (Subscriber) obj);
    }
}
