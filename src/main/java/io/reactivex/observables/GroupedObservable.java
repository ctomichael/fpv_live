package io.reactivex.observables;

import io.reactivex.Observable;
import io.reactivex.annotations.Nullable;

public abstract class GroupedObservable<K, T> extends Observable<T> {
    final K key;

    protected GroupedObservable(@Nullable K key2) {
        this.key = key2;
    }

    @Nullable
    public K getKey() {
        return this.key;
    }
}
