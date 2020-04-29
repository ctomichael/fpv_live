package dji.utils.function;

final /* synthetic */ class BiPredicate$$Lambda$0 implements BiPredicate {
    private final BiPredicate arg$1;
    private final BiPredicate arg$2;

    BiPredicate$$Lambda$0(BiPredicate biPredicate, BiPredicate biPredicate2) {
        this.arg$1 = biPredicate;
        this.arg$2 = biPredicate2;
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
        return BiPredicate$$CC.lambda$and$0$BiPredicate$$CC(this.arg$1, this.arg$2, obj, obj2);
    }
}
