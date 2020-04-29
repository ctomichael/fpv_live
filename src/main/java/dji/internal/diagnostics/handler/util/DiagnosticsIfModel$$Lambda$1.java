package dji.internal.diagnostics.handler.util;

import dji.utils.function.BiPredicate;
import dji.utils.function.BiPredicate$$CC;
import dji.utils.function.Predicate;

final /* synthetic */ class DiagnosticsIfModel$$Lambda$1 implements BiPredicate {
    private final Predicate arg$1;

    DiagnosticsIfModel$$Lambda$1(Predicate predicate) {
        this.arg$1 = predicate;
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
        return this.arg$1.test(obj);
    }
}
