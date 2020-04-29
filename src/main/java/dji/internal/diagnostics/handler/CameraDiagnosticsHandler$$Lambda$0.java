package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataDm368_gGetPushCheckStatus;
import dji.utils.function.Predicate;
import dji.utils.function.Predicate$$CC;

final /* synthetic */ class CameraDiagnosticsHandler$$Lambda$0 implements Predicate {
    static final Predicate $instance = new CameraDiagnosticsHandler$$Lambda$0();

    private CameraDiagnosticsHandler$$Lambda$0() {
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
        return CameraDiagnosticsHandler.lambda$initDiagnosticsModels$0$CameraDiagnosticsHandler((DataDm368_gGetPushCheckStatus) obj);
    }
}
