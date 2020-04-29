package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataOsdGetPushCheckStatus;

final /* synthetic */ class RemoteControllerDiagnosticsHandler$$Lambda$24 implements Runnable {
    private final RemoteControllerDiagnosticsHandler arg$1;
    private final DataOsdGetPushCheckStatus arg$2;

    RemoteControllerDiagnosticsHandler$$Lambda$24(RemoteControllerDiagnosticsHandler remoteControllerDiagnosticsHandler, DataOsdGetPushCheckStatus dataOsdGetPushCheckStatus) {
        this.arg$1 = remoteControllerDiagnosticsHandler;
        this.arg$2 = dataOsdGetPushCheckStatus;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$6$RemoteControllerDiagnosticsHandler(this.arg$2);
    }
}
