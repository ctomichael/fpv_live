package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataOsdGetPushCheckStatusV2;

final /* synthetic */ class RemoteControllerDiagnosticsHandler$$Lambda$23 implements Runnable {
    private final RemoteControllerDiagnosticsHandler arg$1;
    private final DataOsdGetPushCheckStatusV2 arg$2;

    RemoteControllerDiagnosticsHandler$$Lambda$23(RemoteControllerDiagnosticsHandler remoteControllerDiagnosticsHandler, DataOsdGetPushCheckStatusV2 dataOsdGetPushCheckStatusV2) {
        this.arg$1 = remoteControllerDiagnosticsHandler;
        this.arg$2 = dataOsdGetPushCheckStatusV2;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$5$RemoteControllerDiagnosticsHandler(this.arg$2);
    }
}
