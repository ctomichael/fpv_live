package dji.internal.diagnostics.handler;

import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.thirdparty.io.reactivex.functions.Cancellable;

final /* synthetic */ class NavigationDiagnosticsHandler$$Lambda$9 implements Cancellable {
    private final DJIParamAccessListener arg$1;

    NavigationDiagnosticsHandler$$Lambda$9(DJIParamAccessListener dJIParamAccessListener) {
        this.arg$1 = dJIParamAccessListener;
    }

    public void cancel() {
        CacheHelper.removeListener(this.arg$1);
    }
}
