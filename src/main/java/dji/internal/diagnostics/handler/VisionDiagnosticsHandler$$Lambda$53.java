package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataEyeGetPushSensorException;

final /* synthetic */ class VisionDiagnosticsHandler$$Lambda$53 implements Runnable {
    private final VisionDiagnosticsHandler arg$1;
    private final DataEyeGetPushSensorException arg$2;

    VisionDiagnosticsHandler$$Lambda$53(VisionDiagnosticsHandler visionDiagnosticsHandler, DataEyeGetPushSensorException dataEyeGetPushSensorException) {
        this.arg$1 = visionDiagnosticsHandler;
        this.arg$2 = dataEyeGetPushSensorException;
    }

    public void run() {
        this.arg$1.lambda$onEvent3MainThread$9$VisionDiagnosticsHandler(this.arg$2);
    }
}
