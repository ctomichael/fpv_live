package dji.internal.mission.abstraction.tapfly;

import dji.common.error.DJIError;
import dji.common.util.CommonCallbacks;

final /* synthetic */ class TapFlyAbstraction$$Lambda$1 implements CommonCallbacks.CompletionCallback {
    private final TapFlyAbstraction arg$1;
    private final int arg$2;
    private final CommonCallbacks.CompletionCallback arg$3;

    TapFlyAbstraction$$Lambda$1(TapFlyAbstraction tapFlyAbstraction, int i, CommonCallbacks.CompletionCallback completionCallback) {
        this.arg$1 = tapFlyAbstraction;
        this.arg$2 = i;
        this.arg$3 = completionCallback;
    }

    public void onResult(DJIError dJIError) {
        this.arg$1.lambda$configureStartingParams$1$TapFlyAbstraction(this.arg$2, this.arg$3, dJIError);
    }
}
