package dji.thirdparty.io.reactivex.internal.schedulers;

import dji.thirdparty.io.reactivex.Scheduler;
import dji.thirdparty.io.reactivex.disposables.CompositeDisposable;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.disposables.Disposables;
import dji.thirdparty.io.reactivex.internal.disposables.EmptyDisposable;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleScheduler extends Scheduler {
    private static final String KEY_SINGLE_PRIORITY = "rx2.single-priority";
    static final ScheduledExecutorService SHUTDOWN = Executors.newScheduledThreadPool(0);
    static final RxThreadFactory SINGLE_THREAD_FACTORY = new RxThreadFactory(THREAD_NAME_PREFIX, Math.max(1, Math.min(10, Integer.getInteger(KEY_SINGLE_PRIORITY, 5).intValue())));
    private static final String THREAD_NAME_PREFIX = "RxSingleScheduler";
    final AtomicReference<ScheduledExecutorService> executor = new AtomicReference<>();

    static {
        SHUTDOWN.shutdown();
    }

    public SingleScheduler() {
        this.executor.lazySet(createExecutor());
    }

    static ScheduledExecutorService createExecutor() {
        return SchedulerPoolFactory.create(SINGLE_THREAD_FACTORY);
    }

    public void start() {
        ScheduledExecutorService current;
        ScheduledExecutorService next = null;
        do {
            current = this.executor.get();
            if (current != SHUTDOWN) {
                if (next != null) {
                    next.shutdown();
                    return;
                }
                return;
            } else if (next == null) {
                next = createExecutor();
            }
        } while (!this.executor.compareAndSet(current, next));
    }

    public void shutdown() {
        ScheduledExecutorService current;
        if (this.executor.get() != SHUTDOWN && (current = this.executor.getAndSet(SHUTDOWN)) != SHUTDOWN) {
            current.shutdownNow();
        }
    }

    public Scheduler.Worker createWorker() {
        return new ScheduledWorker(this.executor.get());
    }

    public Disposable scheduleDirect(Runnable run, long delay, TimeUnit unit) {
        Future<?> f;
        Runnable decoratedRun = RxJavaPlugins.onSchedule(run);
        if (delay <= 0) {
            try {
                f = this.executor.get().submit(decoratedRun);
            } catch (RejectedExecutionException ex) {
                RxJavaPlugins.onError(ex);
                return EmptyDisposable.INSTANCE;
            }
        } else {
            f = this.executor.get().schedule(decoratedRun, delay, unit);
        }
        return Disposables.fromFuture(f);
    }

    public Disposable schedulePeriodicallyDirect(Runnable run, long initialDelay, long period, TimeUnit unit) {
        try {
            return Disposables.fromFuture(this.executor.get().scheduleAtFixedRate(RxJavaPlugins.onSchedule(run), initialDelay, period, unit));
        } catch (RejectedExecutionException ex) {
            RxJavaPlugins.onError(ex);
            return EmptyDisposable.INSTANCE;
        }
    }

    static final class ScheduledWorker extends Scheduler.Worker {
        volatile boolean disposed;
        final ScheduledExecutorService executor;
        final CompositeDisposable tasks = new CompositeDisposable();

        ScheduledWorker(ScheduledExecutorService executor2) {
            this.executor = executor2;
        }

        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: ClspMth{<V> java.util.concurrent.ScheduledExecutorService.schedule(java.util.concurrent.Callable, long, java.util.concurrent.TimeUnit):java.util.concurrent.ScheduledFuture<V>}
         arg types: [dji.thirdparty.io.reactivex.internal.schedulers.ScheduledRunnable, long, java.util.concurrent.TimeUnit]
         candidates:
          ClspMth{java.util.concurrent.ScheduledExecutorService.schedule(java.lang.Runnable, long, java.util.concurrent.TimeUnit):java.util.concurrent.ScheduledFuture<?>}
          ClspMth{<V> java.util.concurrent.ScheduledExecutorService.schedule(java.util.concurrent.Callable, long, java.util.concurrent.TimeUnit):java.util.concurrent.ScheduledFuture<V>} */
        public Disposable schedule(Runnable run, long delay, TimeUnit unit) {
            Future<?> f;
            if (this.disposed) {
                return EmptyDisposable.INSTANCE;
            }
            ScheduledRunnable sr = new ScheduledRunnable(RxJavaPlugins.onSchedule(run), this.tasks);
            this.tasks.add(sr);
            if (delay <= 0) {
                try {
                    f = this.executor.submit((Callable) sr);
                } catch (RejectedExecutionException ex) {
                    dispose();
                    RxJavaPlugins.onError(ex);
                    return EmptyDisposable.INSTANCE;
                }
            } else {
                f = this.executor.schedule((Callable) sr, delay, unit);
            }
            sr.setFuture(f);
            return sr;
        }

        public void dispose() {
            if (!this.disposed) {
                this.disposed = true;
                this.tasks.dispose();
            }
        }

        public boolean isDisposed() {
            return this.disposed;
        }
    }
}
