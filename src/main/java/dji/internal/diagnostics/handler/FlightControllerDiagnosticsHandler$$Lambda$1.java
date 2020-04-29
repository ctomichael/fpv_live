package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.utils.function.BiFunction;
import dji.utils.function.BiFunction$$CC;
import dji.utils.function.Function;

final /* synthetic */ class FlightControllerDiagnosticsHandler$$Lambda$1 implements BiFunction {
    static final BiFunction $instance = new FlightControllerDiagnosticsHandler$$Lambda$1();

    private FlightControllerDiagnosticsHandler$$Lambda$1() {
    }

    public BiFunction andThen(Function function) {
        return BiFunction$$CC.andThen(this, function);
    }

    public Object apply(Object obj, Object obj2) {
        return FlightControllerDiagnosticsHandler.lambda$initDiagnosticsList$1$FlightControllerDiagnosticsHandler((DataOsdGetPushCommon.FLYC_STATE) obj, (DataOsdGetPushCommon) obj2);
    }
}
