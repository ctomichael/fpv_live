package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataOsdGetPushHome;

final /* synthetic */ class FlightControllerDiagnosticsHandler$$Lambda$118 implements Runnable {
    private final FlightControllerDiagnosticsHandler arg$1;
    private final DataOsdGetPushHome arg$2;

    FlightControllerDiagnosticsHandler$$Lambda$118(FlightControllerDiagnosticsHandler flightControllerDiagnosticsHandler, DataOsdGetPushHome dataOsdGetPushHome) {
        this.arg$1 = flightControllerDiagnosticsHandler;
        this.arg$2 = dataOsdGetPushHome;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$66$FlightControllerDiagnosticsHandler(this.arg$2);
    }
}
