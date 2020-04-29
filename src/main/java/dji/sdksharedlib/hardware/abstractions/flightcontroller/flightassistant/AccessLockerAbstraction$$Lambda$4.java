package dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant;

import dji.common.flightcontroller.accesslocker.UserAccountInfo;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.util.CallbackResult;
import dji.thirdparty.rx.functions.Func1;

final /* synthetic */ class AccessLockerAbstraction$$Lambda$4 implements Func1 {
    private final AccessLockerAbstraction arg$1;
    private final UserAccountInfo arg$2;
    private final DJISDKCacheHWAbstraction.InnerCallback arg$3;

    AccessLockerAbstraction$$Lambda$4(AccessLockerAbstraction accessLockerAbstraction, UserAccountInfo userAccountInfo, DJISDKCacheHWAbstraction.InnerCallback innerCallback) {
        this.arg$1 = accessLockerAbstraction;
        this.arg$2 = userAccountInfo;
        this.arg$3 = innerCallback;
    }

    public Object call(Object obj) {
        return this.arg$1.lambda$clearPassword$4$AccessLockerAbstraction(this.arg$2, this.arg$3, (CallbackResult) obj);
    }
}
