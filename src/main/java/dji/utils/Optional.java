package dji.utils;

import dji.utils.function.Consumer;
import dji.utils.function.Function;
import dji.utils.function.Predicate;
import dji.utils.function.Supplier;
import java.util.NoSuchElementException;
import java.util.Objects;

public final class Optional<T> {
    private static final Optional<?> EMPTY = new Optional<>();
    private final T value;

    private Optional() {
        this.value = null;
    }

    public static <T> Optional<T> empty() {
        return EMPTY;
    }

    private Optional(T value2) {
        this.value = Objects.requireNonNull(value2);
    }

    public static <T> Optional<T> of(T value2) {
        return new Optional<>(value2);
    }

    public static <T> Optional<T> ofNullable(T value2) {
        return value2 == null ? empty() : of(value2);
    }

    public T get() {
        if (this.value != null) {
            return this.value;
        }
        throw new NoSuchElementException("No value present");
    }

    public boolean isPresent() {
        return this.value != null;
    }

    public void ifPresent(Consumer<? super T> consumer) {
        if (this.value != null) {
            consumer.accept(this.value);
        }
    }

    public Optional<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        return (isPresent() && !predicate.test(this.value)) ? empty() : this;
    }

    public <U> Optional<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent()) {
            return empty();
        }
        return ofNullable(mapper.apply(this.value));
    }

    public <U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent()) {
            return empty();
        }
        return (Optional) Objects.requireNonNull(mapper.apply(this.value));
    }

    public T orElse(T other) {
        return this.value != null ? this.value : other;
    }

    public T orElseGet(Supplier<? extends T> other) {
        return this.value != null ? this.value : other.get();
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws Throwable {
        if (this.value != null) {
            return this.value;
        }
        throw ((Throwable) exceptionSupplier.get());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Optional)) {
            return false;
        }
        return Objects.equals(this.value, ((Optional) obj).value);
    }

    public int hashCode() {
        return Objects.hashCode(this.value);
    }

    public String toString() {
        if (this.value == null) {
            return "Optional.empty";
        }
        return String.format("Optional[%s]", this.value);
    }
}
