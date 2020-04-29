package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataRcGetPushGpsInfo;

final /* synthetic */ class RemoteControllerDiagnosticsHandler$$Lambda$27 implements Runnable {
    private final RemoteControllerDiagnosticsHandler arg$1;
    private final DataRcGetPushGpsInfo arg$2;

    RemoteControllerDiagnosticsHandler$$Lambda$27(RemoteControllerDiagnosticsHandler remoteControllerDiagnosticsHandler, DataRcGetPushGpsInfo dataRcGetPushGpsInfo) {
        this.arg$1 = remoteControllerDiagnosticsHandler;
        this.arg$2 = dataRcGetPushGpsInfo;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$9$RemoteControllerDiagnosticsHandler(this.arg$2);
    }
}
