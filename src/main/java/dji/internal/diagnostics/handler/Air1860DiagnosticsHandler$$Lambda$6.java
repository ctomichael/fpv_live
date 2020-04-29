package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataOsdGetPushSignalQuality;

final /* synthetic */ class Air1860DiagnosticsHandler$$Lambda$6 implements Runnable {
    private final Air1860DiagnosticsHandler arg$1;
    private final DataOsdGetPushSignalQuality arg$2;

    Air1860DiagnosticsHandler$$Lambda$6(Air1860DiagnosticsHandler air1860DiagnosticsHandler, DataOsdGetPushSignalQuality dataOsdGetPushSignalQuality) {
        this.arg$1 = air1860DiagnosticsHandler;
        this.arg$2 = dataOsdGetPushSignalQuality;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$0$Air1860DiagnosticsHandler(this.arg$2);
    }
}
