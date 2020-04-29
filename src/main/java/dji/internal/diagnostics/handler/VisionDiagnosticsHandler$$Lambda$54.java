package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataWM160VisionGetPushCheckStatus;

final /* synthetic */ class VisionDiagnosticsHandler$$Lambda$54 implements Runnable {
    private final VisionDiagnosticsHandler arg$1;
    private final DataWM160VisionGetPushCheckStatus arg$2;

    VisionDiagnosticsHandler$$Lambda$54(VisionDiagnosticsHandler visionDiagnosticsHandler, DataWM160VisionGetPushCheckStatus dataWM160VisionGetPushCheckStatus) {
        this.arg$1 = visionDiagnosticsHandler;
        this.arg$2 = dataWM160VisionGetPushCheckStatus;
    }

    public void run() {
        this.arg$1.lambda$onEvent3MainThread$10$VisionDiagnosticsHandler(this.arg$2);
    }
}
