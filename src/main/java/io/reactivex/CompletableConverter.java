package io.reactivex;

import io.reactivex.annotations.NonNull;

public interface CompletableConverter<R> {
    @NonNull
    R apply(@NonNull Completable completable);
}
