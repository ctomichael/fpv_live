package io.reactivex;

import io.reactivex.annotations.NonNull;

public interface MaybeConverter<T, R> {
    @NonNull
    R apply(@NonNull Maybe<T> maybe);
}
