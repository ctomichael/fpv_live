package dji.utils.function;

final /* synthetic */ class Predicate$$Lambda$0 implements Predicate {
    private final Predicate arg$1;
    private final Predicate arg$2;

    Predicate$$Lambda$0(Predicate predicate, Predicate predicate2) {
        this.arg$1 = predicate;
        this.arg$2 = predicate2;
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
        return Predicate$$CC.lambda$and$0$Predicate$$CC(this.arg$1, this.arg$2, obj);
    }
}
