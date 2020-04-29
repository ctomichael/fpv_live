package com.dji.rx.sharedlib;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class DJIRxHelper {
    public static <T> Function<T, Indexed<T>> mapIndexed() {
        return new IndexedFunction();
    }

    public static <T> ObservableTransformer<T, Indexed<T>> indexed() {
        return DJIRxHelper$$Lambda$0.$instance;
    }

    public static <T> ObservableTransformer<T, T> retry(long times, long delay, TimeUnit timeUnit, Scheduler scheduler) {
        return new DJIRxHelper$$Lambda$1(times, delay, timeUnit, scheduler);
    }

    public static <T> ObservableTransformer<T, T> retry(Predicate<Throwable> handler, long times, long delay, TimeUnit timeUnit, Scheduler scheduler) {
        return new DJIRxHelper$$Lambda$2(handler, times, delay, timeUnit, scheduler);
    }

    public static <T> ObservableTransformer<T, T> retry(Predicate<Throwable> handler, long times, long delay, TimeUnit timeUnit) {
        return new DJIRxHelper$$Lambda$3(handler, times, delay, timeUnit);
    }

    public static <T> ObservableTransformer<T, T> retrySwallowError(long times, long delay, TimeUnit timeUnit, Scheduler scheduler) {
        return new DJIRxHelper$$Lambda$4(times, delay, timeUnit, scheduler);
    }

    public static Function<Observable<Throwable>, ObservableSource<?>> retryTimesDelay(Predicate<Throwable> handler, long times, long delay, TimeUnit timeUnit, Scheduler scheduler) {
        return new DJIRxHelper$$Lambda$5(handler, new AtomicLong(), times, delay, timeUnit, scheduler);
    }

    static final /* synthetic */ void lambda$null$5$DJIRxHelper(Predicate handler, AtomicLong count, long times, Throwable throwable) throws Exception {
        if (!handler.test(throwable) || count.incrementAndGet() > times) {
            throw Exceptions.propagate(throwable);
        }
    }

    public static Function<Observable<Throwable>, ObservableSource<?>> retryTimesDelaySwallow(long times, long delay, TimeUnit timeUnit, Scheduler scheduler) {
        return new DJIRxHelper$$Lambda$6(times, delay, timeUnit, scheduler);
    }

    static final class IndexedFunction<T> implements Function<T, Indexed<T>> {
        final AtomicLong atomicLong = new AtomicLong();

        IndexedFunction() {
        }

        public Indexed<T> apply(T t) throws Exception {
            return new Indexed<>(t, this.atomicLong.getAndIncrement());
        }
    }
}
