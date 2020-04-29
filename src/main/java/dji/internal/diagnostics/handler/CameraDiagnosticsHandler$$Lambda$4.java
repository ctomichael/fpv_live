package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataCameraGetPushStateInfo;

final /* synthetic */ class CameraDiagnosticsHandler$$Lambda$4 implements Runnable {
    private final CameraDiagnosticsHandler arg$1;
    private final DataCameraGetPushStateInfo arg$2;

    CameraDiagnosticsHandler$$Lambda$4(CameraDiagnosticsHandler cameraDiagnosticsHandler, DataCameraGetPushStateInfo dataCameraGetPushStateInfo) {
        this.arg$1 = cameraDiagnosticsHandler;
        this.arg$2 = dataCameraGetPushStateInfo;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$2$CameraDiagnosticsHandler(this.arg$2);
    }
}
