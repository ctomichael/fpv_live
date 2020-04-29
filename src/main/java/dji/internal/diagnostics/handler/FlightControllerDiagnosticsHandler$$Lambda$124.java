package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataFlycGetPushFlycInstallError;

final /* synthetic */ class FlightControllerDiagnosticsHandler$$Lambda$124 implements Runnable {
    private final FlightControllerDiagnosticsHandler arg$1;
    private final DataFlycGetPushFlycInstallError arg$2;

    FlightControllerDiagnosticsHandler$$Lambda$124(FlightControllerDiagnosticsHandler flightControllerDiagnosticsHandler, DataFlycGetPushFlycInstallError dataFlycGetPushFlycInstallError) {
        this.arg$1 = flightControllerDiagnosticsHandler;
        this.arg$2 = dataFlycGetPushFlycInstallError;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$73$FlightControllerDiagnosticsHandler(this.arg$2);
    }
}
