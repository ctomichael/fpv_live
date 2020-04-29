package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataFlycGetPushSmartBattery;

final /* synthetic */ class FlightControllerDiagnosticsHandler$$Lambda$126 implements Runnable {
    private final FlightControllerDiagnosticsHandler arg$1;
    private final DataFlycGetPushSmartBattery arg$2;

    FlightControllerDiagnosticsHandler$$Lambda$126(FlightControllerDiagnosticsHandler flightControllerDiagnosticsHandler, DataFlycGetPushSmartBattery dataFlycGetPushSmartBattery) {
        this.arg$1 = flightControllerDiagnosticsHandler;
        this.arg$2 = dataFlycGetPushSmartBattery;
    }

    public void run() {
        this.arg$1.lambda$onEvent3BackgroundThread$75$FlightControllerDiagnosticsHandler(this.arg$2);
    }
}
