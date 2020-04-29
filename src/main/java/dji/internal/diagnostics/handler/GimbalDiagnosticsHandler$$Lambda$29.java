package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataEyeGetPushPointState;

final /* synthetic */ class GimbalDiagnosticsHandler$$Lambda$29 implements Runnable {
    private final GimbalDiagnosticsHandler arg$1;
    private final DataEyeGetPushPointState arg$2;

    GimbalDiagnosticsHandler$$Lambda$29(GimbalDiagnosticsHandler gimbalDiagnosticsHandler, DataEyeGetPushPointState dataEyeGetPushPointState) {
        this.arg$1 = gimbalDiagnosticsHandler;
        this.arg$2 = dataEyeGetPushPointState;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$22$GimbalDiagnosticsHandler(this.arg$2);
    }
}
