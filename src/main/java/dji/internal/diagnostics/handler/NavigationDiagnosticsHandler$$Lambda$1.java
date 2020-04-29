package dji.internal.diagnostics.handler;

import dji.utils.function.BiPredicate;
import dji.utils.function.BiPredicate$$CC;

final /* synthetic */ class NavigationDiagnosticsHandler$$Lambda$1 implements BiPredicate {
    private final NavigationDiagnosticsHandler arg$1;

    NavigationDiagnosticsHandler$$Lambda$1(NavigationDiagnosticsHandler navigationDiagnosticsHandler) {
        this.arg$1 = navigationDiagnosticsHandler;
    }

    public BiPredicate and(BiPredicate biPredicate) {
        return BiPredicate$$CC.and(this, biPredicate);
    }

    public BiPredicate negate() {
        return BiPredicate$$CC.negate(this);
    }

    public BiPredicate or(BiPredicate biPredicate) {
        return BiPredicate$$CC.or(this, biPredicate);
    }

    public boolean test(Object obj, Object obj2) {
        return this.arg$1.lambda$initModels$0$NavigationDiagnosticsHandler((Boolean) obj, (Boolean) obj2);
    }
}
