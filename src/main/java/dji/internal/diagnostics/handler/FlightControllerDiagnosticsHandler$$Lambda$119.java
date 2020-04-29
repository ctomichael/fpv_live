package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataOsdGetPushCommon;

final /* synthetic */ class FlightControllerDiagnosticsHandler$$Lambda$119 implements Runnable {
    private final FlightControllerDiagnosticsHandler arg$1;
    private final DataOsdGetPushCommon arg$2;

    FlightControllerDiagnosticsHandler$$Lambda$119(FlightControllerDiagnosticsHandler flightControllerDiagnosticsHandler, DataOsdGetPushCommon dataOsdGetPushCommon) {
        this.arg$1 = flightControllerDiagnosticsHandler;
        this.arg$2 = dataOsdGetPushCommon;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$67$FlightControllerDiagnosticsHandler(this.arg$2);
    }
}
