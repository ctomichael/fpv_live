package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataRcGetPushBatteryInfo;
import dji.utils.function.Predicate;
import dji.utils.function.Predicate$$CC;

final /* synthetic */ class RemoteControllerDiagnosticsHandler$$Lambda$13 implements Predicate {
    static final Predicate $instance = new RemoteControllerDiagnosticsHandler$$Lambda$13();

    private RemoteControllerDiagnosticsHandler$$Lambda$13() {
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
        return RemoteControllerDiagnosticsHandler.lambda$init$2$RemoteControllerDiagnosticsHandler((DataRcGetPushBatteryInfo) obj);
    }
}
