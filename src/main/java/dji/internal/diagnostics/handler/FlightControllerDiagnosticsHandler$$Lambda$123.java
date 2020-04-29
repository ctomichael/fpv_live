package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataFlycGetPushForbidStatus;

final /* synthetic */ class FlightControllerDiagnosticsHandler$$Lambda$123 implements Runnable {
    private final FlightControllerDiagnosticsHandler arg$1;
    private final DataFlycGetPushForbidStatus arg$2;

    FlightControllerDiagnosticsHandler$$Lambda$123(FlightControllerDiagnosticsHandler flightControllerDiagnosticsHandler, DataFlycGetPushForbidStatus dataFlycGetPushForbidStatus) {
        this.arg$1 = flightControllerDiagnosticsHandler;
        this.arg$2 = dataFlycGetPushForbidStatus;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$72$FlightControllerDiagnosticsHandler(this.arg$2);
    }
}
