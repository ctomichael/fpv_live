package dji.internal.diagnostics.handler;

import dji.utils.function.Predicate;
import dji.utils.function.Predicate$$CC;

final /* synthetic */ class ProductDiagnosticsHandler$$Lambda$1 implements Predicate {
    private final ProductDiagnosticsHandler arg$1;

    ProductDiagnosticsHandler$$Lambda$1(ProductDiagnosticsHandler productDiagnosticsHandler) {
        this.arg$1 = productDiagnosticsHandler;
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
        return this.arg$1.lambda$init$1$ProductDiagnosticsHandler((Boolean) obj);
    }
}
