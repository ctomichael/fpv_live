package dji.internal.diagnostics.handler.util;

import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.thirdparty.io.reactivex.functions.Cancellable;

final /* synthetic */ class DiagnosticsMotorFailModel$$Lambda$6 implements Cancellable {
    private final DJIParamAccessListener arg$1;

    DiagnosticsMotorFailModel$$Lambda$6(DJIParamAccessListener dJIParamAccessListener) {
        this.arg$1 = dJIParamAccessListener;
    }

    public void cancel() {
        CacheHelper.removeListener(this.arg$1);
    }
}
