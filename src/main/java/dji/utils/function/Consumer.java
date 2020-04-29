package dji.utils.function;

@FunctionalInterface
public interface Consumer<T> {
    void accept(T t);

    Consumer<T> andThen(Consumer<? super T> consumer);
}
