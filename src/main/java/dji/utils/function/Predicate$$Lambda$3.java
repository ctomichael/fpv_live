package dji.utils.function;

import java.util.Objects;

final /* synthetic */ class Predicate$$Lambda$3 implements Predicate {
    private final Object arg$1;

    Predicate$$Lambda$3(Object obj) {
        this.arg$1 = obj;
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
        return Objects.equals(obj, this.arg$1);
    }
}
