package dji.thirdparty.io.reactivex.functions;

public interface Function<T, R> {
    R apply(T t) throws Exception;
}
