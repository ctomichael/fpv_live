package dji.internal.diagnostics.handler;

import dji.thirdparty.io.reactivex.functions.Consumer;
import java.util.Set;

final /* synthetic */ class FlightControllerDiagnosticsHandler$$Lambda$121 implements Consumer {
    private final FlightControllerDiagnosticsHandler arg$1;

    FlightControllerDiagnosticsHandler$$Lambda$121(FlightControllerDiagnosticsHandler flightControllerDiagnosticsHandler) {
        this.arg$1 = flightControllerDiagnosticsHandler;
    }

    public void accept(Object obj) {
        this.arg$1.lambda$applyRedundancyError$70$FlightControllerDiagnosticsHandler((Set) obj);
    }
}
