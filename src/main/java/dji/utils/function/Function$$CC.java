package dji.utils.function;

import java.util.Objects;

public abstract /* synthetic */ class Function$$CC {
    public static Function compose(Function function, Function function2) {
        Objects.requireNonNull(function2);
        return new Function$$Lambda$0(function, function2);
    }

    public static Function andThen(Function function, Function function2) {
        Objects.requireNonNull(function2);
        return new Function$$Lambda$1(function, function2);
    }

    public static <T> Function<T, T> identity$$STATIC$$() {
        return Function$$Lambda$2.$instance;
    }

    static /* synthetic */ Object lambda$identity$2$Function$$CC(Object t) {
        return t;
    }
}
