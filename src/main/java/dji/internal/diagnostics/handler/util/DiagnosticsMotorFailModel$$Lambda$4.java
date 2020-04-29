package dji.internal.diagnostics.handler.util;

import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.thirdparty.io.reactivex.ObservableEmitter;
import dji.thirdparty.io.reactivex.ObservableOnSubscribe;

final /* synthetic */ class DiagnosticsMotorFailModel$$Lambda$4 implements ObservableOnSubscribe {
    private final DJISDKCacheKey arg$1;

    DiagnosticsMotorFailModel$$Lambda$4(DJISDKCacheKey dJISDKCacheKey) {
        this.arg$1 = dJISDKCacheKey;
    }

    public void subscribe(ObservableEmitter observableEmitter) {
        DiagnosticsMotorFailModel.lambda$getSharedLibListenerObservable$6$DiagnosticsMotorFailModel(this.arg$1, observableEmitter);
    }
}
