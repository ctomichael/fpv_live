package dji.utils.function;

import java.util.Objects;

public abstract /* synthetic */ class Consumer$$CC {
    public static Consumer andThen(Consumer consumer, Consumer consumer2) {
        Objects.requireNonNull(consumer2);
        return new Consumer$$Lambda$0(consumer, consumer2);
    }

    static /* synthetic */ void lambda$andThen$0$Consumer$$CC(Consumer consumer, Consumer after, Object t) {
        consumer.accept(t);
        after.accept(t);
    }
}
