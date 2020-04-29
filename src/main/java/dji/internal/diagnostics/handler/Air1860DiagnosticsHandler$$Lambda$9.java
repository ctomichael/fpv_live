package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataOsdGetPushCheckStatusV2;

final /* synthetic */ class Air1860DiagnosticsHandler$$Lambda$9 implements Runnable {
    private final Air1860DiagnosticsHandler arg$1;
    private final DataOsdGetPushCheckStatusV2 arg$2;

    Air1860DiagnosticsHandler$$Lambda$9(Air1860DiagnosticsHandler air1860DiagnosticsHandler, DataOsdGetPushCheckStatusV2 dataOsdGetPushCheckStatusV2) {
        this.arg$1 = air1860DiagnosticsHandler;
        this.arg$2 = dataOsdGetPushCheckStatusV2;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$3$Air1860DiagnosticsHandler(this.arg$2);
    }
}
