package dji.thirdparty.io.reactivex.internal.observers;

import dji.thirdparty.io.reactivex.CompletableObserver;
import dji.thirdparty.io.reactivex.MaybeObserver;
import dji.thirdparty.io.reactivex.SingleObserver;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.internal.util.ExceptionHelper;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class BlockingMultiObserver<T> extends CountDownLatch implements SingleObserver<T>, CompletableObserver, MaybeObserver<T> {
    volatile boolean cancelled;
    Disposable d;
    Throwable error;
    T value;

    public BlockingMultiObserver() {
        super(1);
    }

    /* access modifiers changed from: package-private */
    public void dispose() {
        this.cancelled = true;
        Disposable d2 = this.d;
        if (d2 != null) {
            d2.dispose();
        }
    }

    public void onSubscribe(Disposable d2) {
        this.d = d2;
        if (this.cancelled) {
            d2.dispose();
        }
    }

    public void onSuccess(T value2) {
        this.value = value2;
        countDown();
    }

    public void onError(Throwable e) {
        this.error = e;
        countDown();
    }

    public void onComplete() {
        countDown();
    }

    public T blockingGet() {
        if (getCount() != 0) {
            try {
                await();
            } catch (InterruptedException ex) {
                dispose();
                throw ExceptionHelper.wrapOrThrow(ex);
            }
        }
        Throwable ex2 = this.error;
        if (ex2 == null) {
            return this.value;
        }
        throw ExceptionHelper.wrapOrThrow(ex2);
    }

    public T blockingGet(T defaultValue) {
        if (getCount() != 0) {
            try {
                await();
            } catch (InterruptedException ex) {
                dispose();
                throw ExceptionHelper.wrapOrThrow(ex);
            }
        }
        Throwable ex2 = this.error;
        if (ex2 != null) {
            throw ExceptionHelper.wrapOrThrow(ex2);
        }
        T v = this.value;
        return v != null ? v : defaultValue;
    }

    public Throwable blockingGetError() {
        if (getCount() != 0) {
            try {
                await();
            } catch (InterruptedException e) {
                dispose();
                return e;
            }
        }
        return this.error;
    }

    public Throwable blockingGetError(long timeout, TimeUnit unit) {
        if (getCount() != 0) {
            try {
                if (!await(timeout, unit)) {
                    dispose();
                    throw ExceptionHelper.wrapOrThrow(new TimeoutException());
                }
            } catch (InterruptedException ex) {
                dispose();
                throw ExceptionHelper.wrapOrThrow(ex);
            }
        }
        return this.error;
    }

    /* JADX INFO: Multiple debug info for r0v0 java.lang.Throwable: [D('ex' java.lang.InterruptedException), D('ex' java.lang.Throwable)] */
    public boolean blockingAwait(long timeout, TimeUnit unit) {
        if (getCount() != 0) {
            try {
                if (!await(timeout, unit)) {
                    dispose();
                    return false;
                }
            } catch (InterruptedException ex) {
                dispose();
                throw ExceptionHelper.wrapOrThrow(ex);
            }
        }
        Throwable ex2 = this.error;
        if (ex2 == null) {
            return true;
        }
        throw ExceptionHelper.wrapOrThrow(ex2);
    }
}
