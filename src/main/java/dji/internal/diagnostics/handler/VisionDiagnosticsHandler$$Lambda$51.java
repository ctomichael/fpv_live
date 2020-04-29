package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.Data2100GetPushCheckStatus;

final /* synthetic */ class VisionDiagnosticsHandler$$Lambda$51 implements Runnable {
    private final VisionDiagnosticsHandler arg$1;
    private final Data2100GetPushCheckStatus arg$2;

    VisionDiagnosticsHandler$$Lambda$51(VisionDiagnosticsHandler visionDiagnosticsHandler, Data2100GetPushCheckStatus data2100GetPushCheckStatus) {
        this.arg$1 = visionDiagnosticsHandler;
        this.arg$2 = data2100GetPushCheckStatus;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$7$VisionDiagnosticsHandler(this.arg$2);
    }
}
