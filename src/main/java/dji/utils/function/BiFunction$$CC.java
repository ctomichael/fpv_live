package dji.utils.function;

import java.util.Objects;

public abstract /* synthetic */ class BiFunction$$CC {
    public static BiFunction andThen(BiFunction biFunction, Function function) {
        Objects.requireNonNull(function);
        return new BiFunction$$Lambda$0(biFunction, function);
    }
}
