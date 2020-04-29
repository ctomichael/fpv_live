package dji.thirdparty.io.reactivex.internal.schedulers;

import dji.thirdparty.io.reactivex.disposables.Disposable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

final class DisposeOnCancel implements Future<Object> {
    final Disposable d;

    DisposeOnCancel(Disposable d2) {
        this.d = d2;
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        this.d.dispose();
        return false;
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        return false;
    }

    public Object get() throws InterruptedException, ExecutionException {
        return null;
    }

    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}
