package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataFlycGetPushCheckStatus;

final /* synthetic */ class FlightControllerDiagnosticsHandler$$Lambda$117 implements Runnable {
    private final FlightControllerDiagnosticsHandler arg$1;
    private final DataFlycGetPushCheckStatus arg$2;

    FlightControllerDiagnosticsHandler$$Lambda$117(FlightControllerDiagnosticsHandler flightControllerDiagnosticsHandler, DataFlycGetPushCheckStatus dataFlycGetPushCheckStatus) {
        this.arg$1 = flightControllerDiagnosticsHandler;
        this.arg$2 = dataFlycGetPushCheckStatus;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$65$FlightControllerDiagnosticsHandler(this.arg$2);
    }
}
