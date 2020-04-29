package dji.internal.diagnostics.handler;

import dji.midware.data.model.P3.Data2100GetPushCheckStatus;
import dji.utils.function.Predicate;
import dji.utils.function.Predicate$$CC;

final /* synthetic */ class VisionDiagnosticsHandler$$Lambda$31 implements Predicate {
    static final Predicate $instance = new VisionDiagnosticsHandler$$Lambda$31();

    private VisionDiagnosticsHandler$$Lambda$31() {
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
        return ((Data2100GetPushCheckStatus) obj).isDownUltraTOFAbnormal();
    }
}
