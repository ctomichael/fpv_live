package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataRcGetPushBatteryInfo;

final /* synthetic */ class RemoteControllerDiagnosticsHandler$$Lambda$25 implements Runnable {
    private final RemoteControllerDiagnosticsHandler arg$1;
    private final DataRcGetPushBatteryInfo arg$2;

    RemoteControllerDiagnosticsHandler$$Lambda$25(RemoteControllerDiagnosticsHandler remoteControllerDiagnosticsHandler, DataRcGetPushBatteryInfo dataRcGetPushBatteryInfo) {
        this.arg$1 = remoteControllerDiagnosticsHandler;
        this.arg$2 = dataRcGetPushBatteryInfo;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$7$RemoteControllerDiagnosticsHandler(this.arg$2);
    }
}
