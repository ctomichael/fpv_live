package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.DataWM160VisionGetPushCheckStatus;
import dji.utils.function.Predicate;
import dji.utils.function.Predicate$$CC;

final /* synthetic */ class VisionDiagnosticsHandler$$Lambda$11 implements Predicate {
    static final Predicate $instance = new VisionDiagnosticsHandler$$Lambda$11();

    private VisionDiagnosticsHandler$$Lambda$11() {
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
        return ((DataWM160VisionGetPushCheckStatus) obj).cnnStatus();
    }
}
