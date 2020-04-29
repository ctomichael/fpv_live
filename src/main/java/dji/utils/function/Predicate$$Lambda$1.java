package dji.utils.function;

final /* synthetic */ class Predicate$$Lambda$1 implements Predicate {
    private final Predicate arg$1;

    Predicate$$Lambda$1(Predicate predicate) {
        this.arg$1 = predicate;
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
        return Predicate$$CC.lambda$negate$1$Predicate$$CC(this.arg$1, obj);
    }
}
