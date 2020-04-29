package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataOsdGetPushHome;
import dji.utils.function.Function;
import dji.utils.function.Function$$CC;

final /* synthetic */ class FlightControllerDiagnosticsHandler$$Lambda$28 implements Function {
    static final Function $instance = new FlightControllerDiagnosticsHandler$$Lambda$28();

    private FlightControllerDiagnosticsHandler$$Lambda$28() {
    }

    public Function andThen(Function function) {
        return Function$$CC.andThen(this, function);
    }

    public Object apply(Object obj) {
        return Float.valueOf(((DataOsdGetPushHome) obj).getHeight());
    }

    public Function compose(Function function) {
        return Function$$CC.compose(this, function);
    }
}
