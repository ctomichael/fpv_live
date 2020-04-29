package dji.internal.diagnostics.handler;

import dji.sdksharedlib.keycatalog.DJISDKCacheKey;

final /* synthetic */ class ProductDiagnosticsHandler$$Lambda$4 implements Runnable {
    private final ProductDiagnosticsHandler arg$1;
    private final DJISDKCacheKey arg$2;

    ProductDiagnosticsHandler$$Lambda$4(ProductDiagnosticsHandler productDiagnosticsHandler, DJISDKCacheKey dJISDKCacheKey) {
        this.arg$1 = productDiagnosticsHandler;
        this.arg$2 = dJISDKCacheKey;
    }

    public void run() {
        this.arg$1.lambda$onValueChange$2$ProductDiagnosticsHandler(this.arg$2);
    }
}
