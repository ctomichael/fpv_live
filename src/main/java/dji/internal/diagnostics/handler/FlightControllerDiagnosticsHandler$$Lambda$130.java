package dji.internal.diagnostics.handler;

import java.util.Set;

final /* synthetic */ class FlightControllerDiagnosticsHandler$$Lambda$130 implements Runnable {
    private final FlightControllerDiagnosticsHandler arg$1;
    private final Set arg$2;

    FlightControllerDiagnosticsHandler$$Lambda$130(FlightControllerDiagnosticsHandler flightControllerDiagnosticsHandler, Set set) {
        this.arg$1 = flightControllerDiagnosticsHandler;
        this.arg$2 = set;
    }

    public void run() {
        this.arg$1.lambda$null$69$FlightControllerDiagnosticsHandler(this.arg$2);
    }
}
