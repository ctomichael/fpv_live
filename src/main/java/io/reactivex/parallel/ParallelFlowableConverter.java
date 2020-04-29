package io.reactivex.parallel;

import io.reactivex.annotations.NonNull;

public interface ParallelFlowableConverter<T, R> {
    @NonNull
    R apply(@NonNull ParallelFlowable<T> parallelFlowable);
}
