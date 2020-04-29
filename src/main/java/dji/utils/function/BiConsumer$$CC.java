package dji.utils.function;

import java.util.Objects;

public abstract /* synthetic */ class BiConsumer$$CC {
    public static BiConsumer andThen(BiConsumer biConsumer, BiConsumer biConsumer2) {
        Objects.requireNonNull(biConsumer2);
        return new BiConsumer$$Lambda$0(biConsumer, biConsumer2);
    }

    static /* synthetic */ void lambda$andThen$0$BiConsumer$$CC(BiConsumer biConsumer, BiConsumer after, Object l, Object r) {
        biConsumer.accept(l, r);
        after.accept(l, r);
    }
}
