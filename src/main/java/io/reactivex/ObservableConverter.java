package io.reactivex;

import io.reactivex.annotations.NonNull;

public interface ObservableConverter<T, R> {
    @NonNull
    R apply(@NonNull Observable<T> observable);
}
