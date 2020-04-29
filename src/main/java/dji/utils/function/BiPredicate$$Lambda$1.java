package dji.utils.function;

final /* synthetic */ class BiPredicate$$Lambda$1 implements BiPredicate {
    private final BiPredicate arg$1;

    BiPredicate$$Lambda$1(BiPredicate biPredicate) {
        this.arg$1 = biPredicate;
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
        return BiPredicate$$CC.lambda$negate$1$BiPredicate$$CC(this.arg$1, obj, obj2);
    }
}
