package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataDm368_gGetPushCheckStatus;

final /* synthetic */ class CameraDiagnosticsHandler$$Lambda$6 implements Runnable {
    private final CameraDiagnosticsHandler arg$1;
    private final DataDm368_gGetPushCheckStatus arg$2;

    CameraDiagnosticsHandler$$Lambda$6(CameraDiagnosticsHandler cameraDiagnosticsHandler, DataDm368_gGetPushCheckStatus dataDm368_gGetPushCheckStatus) {
        this.arg$1 = cameraDiagnosticsHandler;
        this.arg$2 = dataDm368_gGetPushCheckStatus;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$4$CameraDiagnosticsHandler(this.arg$2);
    }
}
