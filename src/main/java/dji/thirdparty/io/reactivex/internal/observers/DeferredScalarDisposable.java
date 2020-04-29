package dji.thirdparty.io.reactivex.internal.observers;

import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;

public class DeferredScalarDisposable<T> extends BasicIntQueueDisposable<T> {
    static final int DISPOSED = 4;
    static final int FUSED_CONSUMED = 32;
    static final int FUSED_EMPTY = 8;
    static final int FUSED_READY = 16;
    static final int TERMINATED = 2;
    private static final long serialVersionUID = -5502432239815349361L;
    protected final Observer<? super T> actual;
    protected T value;

    public DeferredScalarDisposable(Observer<? super T> actual2) {
        this.actual = actual2;
    }

    public final int requestFusion(int mode) {
        if ((mode & 2) == 0) {
            return 0;
        }
        lazySet(8);
        return 2;
    }

    public final void complete(T value2) {
        int state = get();
        if ((state & 54) == 0) {
            if (state == 8) {
                this.value = value2;
                lazySet(16);
            } else {
                lazySet(2);
            }
            Observer<? super T> a = this.actual;
            a.onNext(value2);
            if (get() != 4) {
                a.onComplete();
            }
        }
    }

    public final void error(Throwable t) {
        if ((get() & 54) != 0) {
            RxJavaPlugins.onError(t);
            return;
        }
        lazySet(2);
        this.actual.onError(t);
    }

    public final void complete() {
        if ((get() & 54) == 0) {
            lazySet(2);
            this.actual.onComplete();
        }
    }

    public final T poll() throws Exception {
        if (get() != 16) {
            return null;
        }
        T v = this.value;
        this.value = null;
        lazySet(32);
        return v;
    }

    public final boolean isEmpty() {
        return get() != 16;
    }

    public final void clear() {
        lazySet(32);
        this.value = null;
    }

    public void dispose() {
        set(4);
        this.value = null;
    }

    public final boolean tryDispose() {
        return getAndSet(4) != 4;
    }

    public final boolean isDisposed() {
        return get() == 4;
    }
}
