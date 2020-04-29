package dji.sdksharedlib.hardware.abstractions.remotecontroller;

import dji.internal.version.DJIVersionRC;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;

final /* synthetic */ class DJIRCAbstraction$$Lambda$0 implements DJIVersionRC.RcVersionListenerOnce {
    private final DJISDKCacheHWAbstraction.InnerCallback arg$1;

    DJIRCAbstraction$$Lambda$0(DJISDKCacheHWAbstraction.InnerCallback innerCallback) {
        this.arg$1 = innerCallback;
    }

    public void onUpdate(String str) {
        DJIRCAbstraction.lambda$getFirmwareVersion$0$DJIRCAbstraction(this.arg$1, str);
    }
}
