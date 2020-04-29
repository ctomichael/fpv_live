package dji.internal.diagnostics.handler;

import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.thirdparty.io.reactivex.ObservableEmitter;
import dji.thirdparty.io.reactivex.ObservableOnSubscribe;

final /* synthetic */ class NavigationDiagnosticsHandler$$Lambda$5 implements ObservableOnSubscribe {
    private final DJISDKCacheKey arg$1;

    NavigationDiagnosticsHandler$$Lambda$5(DJISDKCacheKey dJISDKCacheKey) {
        this.arg$1 = dJISDKCacheKey;
    }

    public void subscribe(ObservableEmitter observableEmitter) {
        NavigationDiagnosticsHandler.lambda$observeSharedLibKey$3$NavigationDiagnosticsHandler(this.arg$1, observableEmitter);
    }
}
