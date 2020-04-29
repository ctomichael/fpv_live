package dji.utils.function;

import java.util.Objects;

public abstract /* synthetic */ class BiPredicate$$CC {
    public static BiPredicate and(BiPredicate biPredicate, BiPredicate biPredicate2) {
        Objects.requireNonNull(biPredicate2);
        return new BiPredicate$$Lambda$0(biPredicate, biPredicate2);
    }

    static /* synthetic */ boolean lambda$and$0$BiPredicate$$CC(BiPredicate biPredicate, BiPredicate other, Object t, Object u) {
        return biPredicate.test(t, u) && other.test(t, u);
    }

    static /* synthetic */ boolean lambda$negate$1$BiPredicate$$CC(BiPredicate biPredicate, Object t, Object u) {
        return !biPredicate.test(t, u);
    }

    public static BiPredicate negate(BiPredicate biPredicate) {
        return new BiPredicate$$Lambda$1(biPredicate);
    }

    public static BiPredicate or(BiPredicate biPredicate, BiPredicate biPredicate2) {
        Objects.requireNonNull(biPredicate2);
        return new BiPredicate$$Lambda$2(biPredicate, biPredicate2);
    }

    static /* synthetic */ boolean lambda$or$2$BiPredicate$$CC(BiPredicate biPredicate, BiPredicate other, Object t, Object u) {
        return biPredicate.test(t, u) || other.test(t, u);
    }
}
