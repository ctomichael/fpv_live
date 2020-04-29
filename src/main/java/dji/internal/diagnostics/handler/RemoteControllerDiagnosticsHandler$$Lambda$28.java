package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataWifi_gGetPushCheckStatus;

final /* synthetic */ class RemoteControllerDiagnosticsHandler$$Lambda$28 implements Runnable {
    private final RemoteControllerDiagnosticsHandler arg$1;
    private final DataWifi_gGetPushCheckStatus arg$2;

    RemoteControllerDiagnosticsHandler$$Lambda$28(RemoteControllerDiagnosticsHandler remoteControllerDiagnosticsHandler, DataWifi_gGetPushCheckStatus dataWifi_gGetPushCheckStatus) {
        this.arg$1 = remoteControllerDiagnosticsHandler;
        this.arg$2 = dataWifi_gGetPushCheckStatus;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$10$RemoteControllerDiagnosticsHandler(this.arg$2);
    }
}
