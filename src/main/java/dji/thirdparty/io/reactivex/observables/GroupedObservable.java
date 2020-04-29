package dji.thirdparty.io.reactivex.observables;

import dji.thirdparty.io.reactivex.Observable;

public abstract class GroupedObservable<K, T> extends Observable<T> {
    final K key;

    protected GroupedObservable(K key2) {
        this.key = key2;
    }

    public K getKey() {
        return this.key;
    }
}
