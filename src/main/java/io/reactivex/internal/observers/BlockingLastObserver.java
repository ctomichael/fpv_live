package io.reactivex.internal.observers;

public final class BlockingLastObserver<T> extends BlockingBaseObserver<T> {
    public void onNext(T t) {
        this.value = t;
    }

    public void onError(Throwable t) {
        this.value = null;
        this.error = t;
        countDown();
    }
}
