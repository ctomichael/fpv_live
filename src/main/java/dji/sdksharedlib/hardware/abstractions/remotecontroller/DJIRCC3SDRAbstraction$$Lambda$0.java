package dji.sdksharedlib.hardware.abstractions.remotecontroller;

import dji.sdksharedlib.util.LteSignalHelper;

final /* synthetic */ class DJIRCC3SDRAbstraction$$Lambda$0 implements LteSignalHelper.KeyValueChanger {
    private final DJIRCC3SDRAbstraction arg$1;

    DJIRCC3SDRAbstraction$$Lambda$0(DJIRCC3SDRAbstraction dJIRCC3SDRAbstraction) {
        this.arg$1 = dJIRCC3SDRAbstraction;
    }

    public void notifyValueChangeForKeyPath(Object obj) {
        this.arg$1.lambda$new$0$DJIRCC3SDRAbstraction(obj);
    }
}
