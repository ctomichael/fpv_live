package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.utils.function.Function;
import dji.utils.function.Function$$CC;

final /* synthetic */ class FlightControllerDiagnosticsHandler$$Lambda$98 implements Function {
    static final Function $instance = new FlightControllerDiagnosticsHandler$$Lambda$98();

    private FlightControllerDiagnosticsHandler$$Lambda$98() {
    }

    public Function andThen(Function function) {
        return Function$$CC.andThen(this, function);
    }

    public Object apply(Object obj) {
        return Boolean.valueOf(DataOsdGetPushCommon.getInstance().isMotorUp());
    }

    public Function compose(Function function) {
        return Function$$CC.compose(this, function);
    }
}
