package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.Data1860GetPushCheckStatus;

final /* synthetic */ class Air1860DiagnosticsHandler$$Lambda$10 implements Runnable {
    private final Air1860DiagnosticsHandler arg$1;
    private final Data1860GetPushCheckStatus arg$2;

    Air1860DiagnosticsHandler$$Lambda$10(Air1860DiagnosticsHandler air1860DiagnosticsHandler, Data1860GetPushCheckStatus data1860GetPushCheckStatus) {
        this.arg$1 = air1860DiagnosticsHandler;
        this.arg$2 = data1860GetPushCheckStatus;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$4$Air1860DiagnosticsHandler(this.arg$2);
    }
}
