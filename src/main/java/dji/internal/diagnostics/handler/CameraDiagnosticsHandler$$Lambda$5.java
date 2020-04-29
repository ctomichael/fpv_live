package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataCameraGetPushStorageInfo;

final /* synthetic */ class CameraDiagnosticsHandler$$Lambda$5 implements Runnable {
    private final CameraDiagnosticsHandler arg$1;
    private final DataCameraGetPushStorageInfo arg$2;

    CameraDiagnosticsHandler$$Lambda$5(CameraDiagnosticsHandler cameraDiagnosticsHandler, DataCameraGetPushStorageInfo dataCameraGetPushStorageInfo) {
        this.arg$1 = cameraDiagnosticsHandler;
        this.arg$2 = dataCameraGetPushStorageInfo;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$3$CameraDiagnosticsHandler(this.arg$2);
    }
}
