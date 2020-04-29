package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataOsdGetPushHome;
import dji.utils.function.Predicate;
import dji.utils.function.Predicate$$CC;

final /* synthetic */ class FlightControllerDiagnosticsHandler$$Lambda$10 implements Predicate {
    static final Predicate $instance = new FlightControllerDiagnosticsHandler$$Lambda$10();

    private FlightControllerDiagnosticsHandler$$Lambda$10() {
    }

    public Predicate and(Predicate predicate) {
        return Predicate$$CC.and(this, predicate);
    }

    public Predicate negate() {
        return Predicate$$CC.negate(this);
    }

    public Predicate or(Predicate predicate) {
        return Predicate$$CC.or(this, predicate);
    }

    public boolean test(Object obj) {
        return FlightControllerDiagnosticsHandler.lambda$initRedundancySwitchType$10$FlightControllerDiagnosticsHandler((DataOsdGetPushHome) obj);
    }
}
