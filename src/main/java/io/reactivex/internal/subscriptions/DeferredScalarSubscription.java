package io.reactivex.internal.subscriptions;

import io.reactivex.annotations.Nullable;
import org.reactivestreams.Subscriber;

public class DeferredScalarSubscription<T> extends BasicIntQueueSubscription<T> {
    static final int CANCELLED = 4;
    static final int FUSED_CONSUMED = 32;
    static final int FUSED_EMPTY = 8;
    static final int FUSED_READY = 16;
    static final int HAS_REQUEST_HAS_VALUE = 3;
    static final int HAS_REQUEST_NO_VALUE = 2;
    static final int NO_REQUEST_HAS_VALUE = 1;
    static final int NO_REQUEST_NO_VALUE = 0;
    private static final long serialVersionUID = -2151279923272604993L;
    protected final Subscriber<? super T> downstream;
    protected T value;

    public DeferredScalarSubscription(Subscriber<? super T> downstream2) {
        this.downstream = downstream2;
    }

    public final void request(long n) {
        T v;
        if (SubscriptionHelper.validate(n)) {
            do {
                int state = get();
                if ((state & -2) == 0) {
                    if (state == 1) {
                        if (compareAndSet(1, 3) && (v = this.value) != null) {
                            this.value = null;
                            Subscriber<? super T> a = this.downstream;
                            a.onNext(v);
                            if (get() != 4) {
                                a.onComplete();
                                return;
                            }
                            return;
                        }
                        return;
                    }
                } else {
                    return;
                }
            } while (!compareAndSet(0, 2));
        }
    }

    public final void complete(T v) {
        int state = get();
        while (state != 8) {
            if ((state & -3) != 0) {
                return;
            }
            if (state == 2) {
                lazySet(3);
                Subscriber<? super T> a = this.downstream;
                a.onNext(v);
                if (get() != 4) {
                    a.onComplete();
                    return;
                }
                return;
            }
            this.value = v;
            if (!compareAndSet(0, 1)) {
                state = get();
                if (state == 4) {
                    this.value = null;
                    return;
                }
            } else {
                return;
            }
        }
        this.value = v;
        lazySet(16);
        Subscriber<? super T> a2 = this.downstream;
        a2.onNext(v);
        if (get() != 4) {
            a2.onComplete();
        }
    }

    public final int requestFusion(int mode) {
        if ((mode & 2) == 0) {
            return 0;
        }
        lazySet(8);
        return 2;
    }

    @Nullable
    public final T poll() {
        if (get() != 16) {
            return null;
        }
        lazySet(32);
        T v = this.value;
        this.value = null;
        return v;
    }

    public final boolean isEmpty() {
        return get() != 16;
    }

    public final void clear() {
        lazySet(32);
        this.value = null;
    }

    public void cancel() {
        set(4);
        this.value = null;
    }

    public final boolean isCancelled() {
        return get() == 4;
    }

    public final boolean tryCancel() {
        return getAndSet(4) != 4;
    }
}
