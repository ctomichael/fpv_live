package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.utils.function.Function;
import dji.utils.function.Function$$CC;

final /* synthetic */ class FlightControllerDiagnosticsHandler$$Lambda$77 implements Function {
    static final Function $instance = new FlightControllerDiagnosticsHandler$$Lambda$77();

    private FlightControllerDiagnosticsHandler$$Lambda$77() {
    }

    public Function andThen(Function function) {
        return Function$$CC.andThen(this, function);
    }

    public Object apply(Object obj) {
        return Boolean.valueOf(((DataOsdGetPushCommon) obj).isMotorUp());
    }

    public Function compose(Function function) {
        return Function$$CC.compose(this, function);
    }
}
