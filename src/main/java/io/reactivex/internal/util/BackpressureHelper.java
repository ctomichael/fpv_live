package io.reactivex.internal.util;

import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.jvm.internal.LongCompanionObject;

public final class BackpressureHelper {
    private BackpressureHelper() {
        throw new IllegalStateException("No instances!");
    }

    public static long addCap(long a, long b) {
        long u = a + b;
        if (u < 0) {
            return LongCompanionObject.MAX_VALUE;
        }
        return u;
    }

    public static long multiplyCap(long a, long b) {
        long u = a * b;
        if (((a | b) >>> 31) == 0 || u / a == b) {
            return u;
        }
        return LongCompanionObject.MAX_VALUE;
    }

    public static long add(AtomicLong requested, long n) {
        long r;
        do {
            r = requested.get();
            if (r == LongCompanionObject.MAX_VALUE) {
                return LongCompanionObject.MAX_VALUE;
            }
        } while (!requested.compareAndSet(r, addCap(r, n)));
        return r;
    }

    public static long addCancel(AtomicLong requested, long n) {
        long r;
        do {
            r = requested.get();
            if (r == Long.MIN_VALUE) {
                return Long.MIN_VALUE;
            }
            if (r == LongCompanionObject.MAX_VALUE) {
                return LongCompanionObject.MAX_VALUE;
            }
        } while (!requested.compareAndSet(r, addCap(r, n)));
        return r;
    }

    public static long produced(AtomicLong requested, long n) {
        long current;
        long update;
        do {
            current = requested.get();
            if (current == LongCompanionObject.MAX_VALUE) {
                return LongCompanionObject.MAX_VALUE;
            }
            update = current - n;
            if (update < 0) {
                RxJavaPlugins.onError(new IllegalStateException("More produced than requested: " + update));
                update = 0;
            }
        } while (!requested.compareAndSet(current, update));
        return update;
    }

    public static long producedCancel(AtomicLong requested, long n) {
        long current;
        long update;
        do {
            current = requested.get();
            if (current == Long.MIN_VALUE) {
                return Long.MIN_VALUE;
            }
            if (current == LongCompanionObject.MAX_VALUE) {
                return LongCompanionObject.MAX_VALUE;
            }
            update = current - n;
            if (update < 0) {
                RxJavaPlugins.onError(new IllegalStateException("More produced than requested: " + update));
                update = 0;
            }
        } while (!requested.compareAndSet(current, update));
        return update;
    }
}
