package dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant;

import dji.common.flightcontroller.accesslocker.UserAccountInfo;
import dji.sdksharedlib.util.CallbackResult;
import dji.thirdparty.rx.functions.Func1;

final /* synthetic */ class AccessLockerAbstraction$$Lambda$9 implements Func1 {
    private final AccessLockerAbstraction arg$1;
    private final UserAccountInfo arg$2;
    private final UserAccountInfo arg$3;

    AccessLockerAbstraction$$Lambda$9(AccessLockerAbstraction accessLockerAbstraction, UserAccountInfo userAccountInfo, UserAccountInfo userAccountInfo2) {
        this.arg$1 = accessLockerAbstraction;
        this.arg$2 = userAccountInfo;
        this.arg$3 = userAccountInfo2;
    }

    public Object call(Object obj) {
        return this.arg$1.lambda$changePassword$9$AccessLockerAbstraction(this.arg$2, this.arg$3, (CallbackResult) obj);
    }
}
