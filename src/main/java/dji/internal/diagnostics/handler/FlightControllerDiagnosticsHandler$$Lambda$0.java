package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.utils.function.BiFunction;
import dji.utils.function.BiFunction$$CC;
import dji.utils.function.Function;

final /* synthetic */ class FlightControllerDiagnosticsHandler$$Lambda$0 implements BiFunction {
    private final FlightControllerDiagnosticsHandler arg$1;

    FlightControllerDiagnosticsHandler$$Lambda$0(FlightControllerDiagnosticsHandler flightControllerDiagnosticsHandler) {
        this.arg$1 = flightControllerDiagnosticsHandler;
    }

    public BiFunction andThen(Function function) {
        return BiFunction$$CC.andThen(this, function);
    }

    public Object apply(Object obj, Object obj2) {
        return this.arg$1.lambda$initDiagnosticsList$0$FlightControllerDiagnosticsHandler((DataOsdGetPushCommon.FLYC_STATE) obj, (DataOsdGetPushCommon) obj2);
    }
}
