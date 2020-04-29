package io.reactivex.disposables;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

final class FutureDisposable extends AtomicReference<Future<?>> implements Disposable {
    private static final long serialVersionUID = 6545242830671168775L;
    private final boolean allowInterrupt;

    FutureDisposable(Future<?> run, boolean allowInterrupt2) {
        super(run);
        this.allowInterrupt = allowInterrupt2;
    }

    public boolean isDisposed() {
        Future<?> f = (Future) get();
        return f == null || f.isDone();
    }

    public void dispose() {
        Future<?> f = (Future) getAndSet(null);
        if (f != null) {
            f.cancel(this.allowInterrupt);
        }
    }
}
