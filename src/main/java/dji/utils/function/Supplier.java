package dji.utils.function;

@FunctionalInterface
public interface Supplier<T> {
    T get();
}
