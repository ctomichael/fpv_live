package dji.internal.diagnostics.handler;

import dji.utils.function.Predicate;
import dji.utils.function.Predicate$$CC;

final /* synthetic */ class FlightControllerDiagnosticsHandler$$Lambda$58 implements Predicate {
    private final Integer arg$1;

    private FlightControllerDiagnosticsHandler$$Lambda$58(Integer num) {
        this.arg$1 = num;
    }

    static Predicate get$Lambda(Integer num) {
        return new FlightControllerDiagnosticsHandler$$Lambda$58(num);
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
        return this.arg$1.equals((Integer) obj);
    }
}
