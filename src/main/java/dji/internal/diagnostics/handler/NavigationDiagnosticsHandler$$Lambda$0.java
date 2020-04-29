package dji.internal.diagnostics.handler;

import dji.utils.function.Predicate;
import dji.utils.function.Predicate$$CC;

final /* synthetic */ class NavigationDiagnosticsHandler$$Lambda$0 implements Predicate {
    private final Boolean arg$1;

    private NavigationDiagnosticsHandler$$Lambda$0(Boolean bool) {
        this.arg$1 = bool;
    }

    static Predicate get$Lambda(Boolean bool) {
        return new NavigationDiagnosticsHandler$$Lambda$0(bool);
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
        return this.arg$1.equals((Boolean) obj);
    }
}
