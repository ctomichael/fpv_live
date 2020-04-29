package dji.utils.function;

import java.util.Objects;

public abstract /* synthetic */ class Predicate$$CC {
    public static Predicate and(Predicate predicate, Predicate predicate2) {
        Objects.requireNonNull(predicate2);
        return new Predicate$$Lambda$0(predicate, predicate2);
    }

    static /* synthetic */ boolean lambda$and$0$Predicate$$CC(Predicate predicate, Predicate other, Object t) {
        return predicate.test(t) && other.test(t);
    }

    static /* synthetic */ boolean lambda$negate$1$Predicate$$CC(Predicate predicate, Object t) {
        return !predicate.test(t);
    }

    public static Predicate negate(Predicate predicate) {
        return new Predicate$$Lambda$1(predicate);
    }

    public static Predicate or(Predicate predicate, Predicate predicate2) {
        Objects.requireNonNull(predicate2);
        return new Predicate$$Lambda$2(predicate, predicate2);
    }

    static /* synthetic */ boolean lambda$or$2$Predicate$$CC(Predicate predicate, Predicate other, Object t) {
        return predicate.test(t) || other.test(t);
    }

    public static <T> Predicate<T> isEqual$$STATIC$$(Object targetRef) {
        return new Predicate$$Lambda$3(targetRef);
    }
}
