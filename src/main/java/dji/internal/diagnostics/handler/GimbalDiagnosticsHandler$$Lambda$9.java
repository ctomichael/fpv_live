package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataGimbalGetPushCheckStatus;
import dji.utils.function.Predicate;
import dji.utils.function.Predicate$$CC;

final /* synthetic */ class GimbalDiagnosticsHandler$$Lambda$9 implements Predicate {
    private final GimbalDiagnosticsHandler arg$1;

    GimbalDiagnosticsHandler$$Lambda$9(GimbalDiagnosticsHandler gimbalDiagnosticsHandler) {
        this.arg$1 = gimbalDiagnosticsHandler;
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
        return this.arg$1.lambda$initDiagnostics$9$GimbalDiagnosticsHandler((DataGimbalGetPushCheckStatus) obj);
    }
}
