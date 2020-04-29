package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataGimbalGetPushParams;

final /* synthetic */ class GimbalDiagnosticsHandler$$Lambda$27 implements Runnable {
    private final GimbalDiagnosticsHandler arg$1;
    private final DataGimbalGetPushParams arg$2;

    GimbalDiagnosticsHandler$$Lambda$27(GimbalDiagnosticsHandler gimbalDiagnosticsHandler, DataGimbalGetPushParams dataGimbalGetPushParams) {
        this.arg$1 = gimbalDiagnosticsHandler;
        this.arg$2 = dataGimbalGetPushParams;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$20$GimbalDiagnosticsHandler(this.arg$2);
    }
}
