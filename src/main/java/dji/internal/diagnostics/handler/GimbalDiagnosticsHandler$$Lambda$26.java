package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataGimbalGetPushCheckStatus;

final /* synthetic */ class GimbalDiagnosticsHandler$$Lambda$26 implements Runnable {
    private final GimbalDiagnosticsHandler arg$1;
    private final DataGimbalGetPushCheckStatus arg$2;

    GimbalDiagnosticsHandler$$Lambda$26(GimbalDiagnosticsHandler gimbalDiagnosticsHandler, DataGimbalGetPushCheckStatus dataGimbalGetPushCheckStatus) {
        this.arg$1 = gimbalDiagnosticsHandler;
        this.arg$2 = dataGimbalGetPushCheckStatus;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$19$GimbalDiagnosticsHandler(this.arg$2);
    }
}
