package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataOsdGetPushCheckStatus;
import dji.utils.function.Predicate;
import dji.utils.function.Predicate$$CC;

final /* synthetic */ class RemoteControllerDiagnosticsHandler$$Lambda$1 implements Predicate {
    static final Predicate $instance = new RemoteControllerDiagnosticsHandler$$Lambda$1();

    private RemoteControllerDiagnosticsHandler$$Lambda$1() {
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
        return ((DataOsdGetPushCheckStatus) obj).get58GinitStatus();
    }
}
