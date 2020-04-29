package io.reactivex;

import io.reactivex.annotations.NonNull;

public interface FlowableConverter<T, R> {
    @NonNull
    R apply(@NonNull Flowable<T> flowable);
}
