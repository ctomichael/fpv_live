package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataOsdGetPushWirelessState;

final /* synthetic */ class Air1860DiagnosticsHandler$$Lambda$8 implements Runnable {
    private final Air1860DiagnosticsHandler arg$1;
    private final DataOsdGetPushWirelessState arg$2;

    Air1860DiagnosticsHandler$$Lambda$8(Air1860DiagnosticsHandler air1860DiagnosticsHandler, DataOsdGetPushWirelessState dataOsdGetPushWirelessState) {
        this.arg$1 = air1860DiagnosticsHandler;
        this.arg$2 = dataOsdGetPushWirelessState;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$2$Air1860DiagnosticsHandler(this.arg$2);
    }
}
