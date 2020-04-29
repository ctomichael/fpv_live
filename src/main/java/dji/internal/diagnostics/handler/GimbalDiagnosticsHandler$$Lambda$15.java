package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataGimbalGetPushCheckStatus;
import dji.utils.function.Predicate;
import dji.utils.function.Predicate$$CC;

final /* synthetic */ class GimbalDiagnosticsHandler$$Lambda$15 implements Predicate {
    static final Predicate $instance = new GimbalDiagnosticsHandler$$Lambda$15();

    private GimbalDiagnosticsHandler$$Lambda$15() {
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
        return ((DataGimbalGetPushCheckStatus) obj).getGyroscopeStatus();
    }
}
