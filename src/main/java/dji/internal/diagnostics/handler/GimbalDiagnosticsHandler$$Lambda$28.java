package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataOsdGetPushCommon;

final /* synthetic */ class GimbalDiagnosticsHandler$$Lambda$28 implements Runnable {
    private final GimbalDiagnosticsHandler arg$1;
    private final DataOsdGetPushCommon arg$2;

    GimbalDiagnosticsHandler$$Lambda$28(GimbalDiagnosticsHandler gimbalDiagnosticsHandler, DataOsdGetPushCommon dataOsdGetPushCommon) {
        this.arg$1 = gimbalDiagnosticsHandler;
        this.arg$2 = dataOsdGetPushCommon;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$21$GimbalDiagnosticsHandler(this.arg$2);
    }
}
