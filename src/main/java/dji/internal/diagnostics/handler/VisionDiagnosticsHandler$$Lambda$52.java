package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.Data2150GetPushCheckStatus;

final /* synthetic */ class VisionDiagnosticsHandler$$Lambda$52 implements Runnable {
    private final VisionDiagnosticsHandler arg$1;
    private final Data2150GetPushCheckStatus arg$2;

    VisionDiagnosticsHandler$$Lambda$52(VisionDiagnosticsHandler visionDiagnosticsHandler, Data2150GetPushCheckStatus data2150GetPushCheckStatus) {
        this.arg$1 = visionDiagnosticsHandler;
        this.arg$2 = data2150GetPushCheckStatus;
    }

    public void run() {
        this.arg$1.lambda$onEvent3MainThread$8$VisionDiagnosticsHandler(this.arg$2);
    }
}
