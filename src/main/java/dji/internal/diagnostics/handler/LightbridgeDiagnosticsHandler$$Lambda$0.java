package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataDm368_gGetPushCheckStatus;

final /* synthetic */ class LightbridgeDiagnosticsHandler$$Lambda$0 implements Runnable {
    private final LightbridgeDiagnosticsHandler arg$1;
    private final DataDm368_gGetPushCheckStatus arg$2;

    LightbridgeDiagnosticsHandler$$Lambda$0(LightbridgeDiagnosticsHandler lightbridgeDiagnosticsHandler, DataDm368_gGetPushCheckStatus dataDm368_gGetPushCheckStatus) {
        this.arg$1 = lightbridgeDiagnosticsHandler;
        this.arg$2 = dataDm368_gGetPushCheckStatus;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$0$LightbridgeDiagnosticsHandler(this.arg$2);
    }
}
