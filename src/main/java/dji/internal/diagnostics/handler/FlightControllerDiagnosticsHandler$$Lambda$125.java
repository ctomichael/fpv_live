package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataADSBGetPushWarning;

final /* synthetic */ class FlightControllerDiagnosticsHandler$$Lambda$125 implements Runnable {
    private final FlightControllerDiagnosticsHandler arg$1;
    private final DataADSBGetPushWarning arg$2;

    FlightControllerDiagnosticsHandler$$Lambda$125(FlightControllerDiagnosticsHandler flightControllerDiagnosticsHandler, DataADSBGetPushWarning dataADSBGetPushWarning) {
        this.arg$1 = flightControllerDiagnosticsHandler;
        this.arg$2 = dataADSBGetPushWarning;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$74$FlightControllerDiagnosticsHandler(this.arg$2);
    }
}
