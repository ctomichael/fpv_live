package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataWifiGetPushElecSignal;

final /* synthetic */ class Air1860DiagnosticsHandler$$Lambda$11 implements Runnable {
    private final Air1860DiagnosticsHandler arg$1;
    private final DataWifiGetPushElecSignal arg$2;

    Air1860DiagnosticsHandler$$Lambda$11(Air1860DiagnosticsHandler air1860DiagnosticsHandler, DataWifiGetPushElecSignal dataWifiGetPushElecSignal) {
        this.arg$1 = air1860DiagnosticsHandler;
        this.arg$2 = dataWifiGetPushElecSignal;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$5$Air1860DiagnosticsHandler(this.arg$2);
    }
}
