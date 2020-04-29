package dji.internal.diagnostics.handler;

import dji.thirdparty.io.reactivex.functions.Consumer;

final /* synthetic */ class FlightControllerDiagnosticsHandler$$Lambda$122 implements Consumer {
    static final Consumer $instance = new FlightControllerDiagnosticsHandler$$Lambda$122();

    private FlightControllerDiagnosticsHandler$$Lambda$122() {
    }

    public void accept(Object obj) {
        FlightControllerDiagnosticsHandler.lambda$applyRedundancyError$71$FlightControllerDiagnosticsHandler((Throwable) obj);
    }
}
