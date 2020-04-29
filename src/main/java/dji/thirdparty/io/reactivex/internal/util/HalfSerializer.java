package dji.thirdparty.io.reactivex.internal.util;

import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Subscriber;

public final class HalfSerializer {
    private HalfSerializer() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> void onNext(Subscriber subscriber, Object obj, AtomicInteger wip, AtomicThrowable error) {
        if (wip.get() == 0 && wip.compareAndSet(0, 1)) {
            subscriber.onNext(obj);
            if (wip.decrementAndGet() != 0) {
                Throwable ex = error.terminate();
                if (ex != null) {
                    subscriber.onError(ex);
                } else {
                    subscriber.onComplete();
                }
            }
        }
    }

    public static void onError(Subscriber<?> subscriber, Throwable ex, AtomicInteger wip, AtomicThrowable error) {
        if (!error.addThrowable(ex)) {
            RxJavaPlugins.onError(ex);
        } else if (wip.getAndIncrement() == 0) {
            subscriber.onError(error.terminate());
        }
    }

    public static void onComplete(Subscriber<?> subscriber, AtomicInteger wip, AtomicThrowable error) {
        if (wip.getAndIncrement() == 0) {
            Throwable ex = error.terminate();
            if (ex != null) {
                subscriber.onError(ex);
            } else {
                subscriber.onComplete();
            }
        }
    }

    public static <T> void onNext(Observer observer, Object obj, AtomicInteger wip, AtomicThrowable error) {
        if (wip.get() == 0 && wip.compareAndSet(0, 1)) {
            observer.onNext(obj);
            if (wip.decrementAndGet() != 0) {
                Throwable ex = error.terminate();
                if (ex != null) {
                    observer.onError(ex);
                } else {
                    observer.onComplete();
                }
            }
        }
    }

    public static void onError(Observer<?> observer, Throwable ex, AtomicInteger wip, AtomicThrowable error) {
        if (!error.addThrowable(ex)) {
            RxJavaPlugins.onError(ex);
        } else if (wip.getAndIncrement() == 0) {
            observer.onError(error.terminate());
        }
    }

    public static void onComplete(Observer<?> observer, AtomicInteger wip, AtomicThrowable error) {
        if (wip.getAndIncrement() == 0) {
            Throwable ex = error.terminate();
            if (ex != null) {
                observer.onError(ex);
            } else {
                observer.onComplete();
            }
        }
    }
}
