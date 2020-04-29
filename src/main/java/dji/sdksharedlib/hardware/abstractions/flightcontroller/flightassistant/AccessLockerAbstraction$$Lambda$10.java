package dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant;

import dji.common.error.DJIError;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.thirdparty.rx.functions.Action1;

final /* synthetic */ class AccessLockerAbstraction$$Lambda$10 implements Action1 {
    private final AccessLockerAbstraction arg$1;
    private final DJISDKCacheHWAbstraction.InnerCallback arg$2;

    AccessLockerAbstraction$$Lambda$10(AccessLockerAbstraction accessLockerAbstraction, DJISDKCacheHWAbstraction.InnerCallback innerCallback) {
        this.arg$1 = accessLockerAbstraction;
        this.arg$2 = innerCallback;
    }

    public void call(Object obj) {
        this.arg$1.lambda$changePassword$10$AccessLockerAbstraction(this.arg$2, (DJIError) obj);
    }
}
