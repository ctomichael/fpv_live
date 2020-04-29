package dji.thirdparty.io.reactivex.internal.schedulers;

import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableContainer;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReferenceArray;

public final class ScheduledRunnable extends AtomicReferenceArray<Object> implements Runnable, Callable<Object>, Disposable {
    static final Object DISPOSED = new Object();
    static final Object DONE = new Object();
    static final int FUTURE_INDEX = 1;
    static final int PARENT_INDEX = 0;
    private static final long serialVersionUID = -6120223772001106981L;
    final Runnable actual;

    public ScheduledRunnable(Runnable actual2, DisposableContainer parent) {
        super(2);
        this.actual = actual2;
        lazySet(0, parent);
    }

    public Object call() {
        run();
        return null;
    }

    public void run() {
        Object o;
        Object o2;
        try {
            this.actual.run();
        } catch (Throwable th) {
            Throwable th2 = th;
            Object o3 = get(0);
            if (!(o3 == DISPOSED || o3 == null || !compareAndSet(0, o3, DONE))) {
                ((DisposableContainer) o3).delete(this);
            }
            do {
                o = get(1);
                if (o == DISPOSED) {
                    break;
                }
            } while (!compareAndSet(1, o, DONE));
            throw th2;
        }
        Object o4 = get(0);
        if (!(o4 == DISPOSED || o4 == null || !compareAndSet(0, o4, DONE))) {
            ((DisposableContainer) o4).delete(this);
        }
        do {
            o2 = get(1);
            if (o2 == DISPOSED) {
                return;
            }
        } while (!compareAndSet(1, o2, DONE));
    }

    public void setFuture(Future<?> f) {
        Object o;
        do {
            o = get(1);
            if (o != DONE) {
                if (o == DISPOSED) {
                    f.cancel(true);
                    return;
                }
            } else {
                return;
            }
        } while (!compareAndSet(1, o, f));
    }

    public void dispose() {
        Object o;
        while (true) {
            Object o2 = get(1);
            if (o2 == DONE || o2 == DISPOSED) {
                break;
            } else if (compareAndSet(1, o2, DISPOSED)) {
                if (o2 != null) {
                    ((Future) o2).cancel(true);
                }
            }
        }
        do {
            o = get(0);
            if (o == DONE || o == DISPOSED || o == null) {
                return;
            }
        } while (!compareAndSet(0, o, DISPOSED));
        ((DisposableContainer) o).delete(this);
    }

    public boolean isDisposed() {
        Object o = get(1);
        if (o == DISPOSED || o == DONE) {
            return true;
        }
        return false;
    }
}
