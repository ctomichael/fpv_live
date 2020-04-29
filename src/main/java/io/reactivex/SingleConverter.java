package io.reactivex;

import io.reactivex.annotations.NonNull;

public interface SingleConverter<T, R> {
    @NonNull
    R apply(@NonNull Single<T> single);
}
