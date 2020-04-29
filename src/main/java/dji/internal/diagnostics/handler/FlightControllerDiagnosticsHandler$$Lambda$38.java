package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataOsdGetPushHome;
import dji.utils.function.Function;
import dji.utils.function.Function$$CC;

final /* synthetic */ class FlightControllerDiagnosticsHandler$$Lambda$38 implements Function {
    private final FlightControllerDiagnosticsHandler arg$1;

    FlightControllerDiagnosticsHandler$$Lambda$38(FlightControllerDiagnosticsHandler flightControllerDiagnosticsHandler) {
        this.arg$1 = flightControllerDiagnosticsHandler;
    }

    public Function andThen(Function function) {
        return Function$$CC.andThen(this, function);
    }

    public Object apply(Object obj) {
        return this.arg$1.lambda$initStatusWarningType$30$FlightControllerDiagnosticsHandler((DataOsdGetPushHome) obj);
    }

    public Function compose(Function function) {
        return Function$$CC.compose(this, function);
    }
}
