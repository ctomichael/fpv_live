package dji.thirdparty.io.reactivex.internal.schedulers;

import dji.thirdparty.io.reactivex.Scheduler;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.disposables.Disposables;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableContainer;
import dji.thirdparty.io.reactivex.internal.disposables.EmptyDisposable;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class NewThreadWorker extends Scheduler.Worker implements Disposable {
    volatile boolean disposed;
    private final ScheduledExecutorService executor;

    public NewThreadWorker(ThreadFactory threadFactory) {
        this.executor = SchedulerPoolFactory.create(threadFactory);
    }

    public Disposable schedule(Runnable run) {
        return schedule(run, 0, null);
    }

    public Disposable schedule(Runnable action, long delayTime, TimeUnit unit) {
        if (this.disposed) {
            return EmptyDisposable.INSTANCE;
        }
        return scheduleActual(action, delayTime, unit, null);
    }

    public Disposable scheduleDirect(Runnable run, long delayTime, TimeUnit unit) {
        Future<?> f;
        Runnable decoratedRun = RxJavaPlugins.onSchedule(run);
        if (delayTime <= 0) {
            try {
                f = this.executor.submit(decoratedRun);
            } catch (RejectedExecutionException ex) {
                RxJavaPlugins.onError(ex);
                return EmptyDisposable.INSTANCE;
            }
        } else {
            f = this.executor.schedule(decoratedRun, delayTime, unit);
        }
        return Disposables.fromFuture(f);
    }

    public Disposable schedulePeriodicallyDirect(Runnable run, long initialDelay, long period, TimeUnit unit) {
        try {
            return Disposables.fromFuture(this.executor.scheduleAtFixedRate(RxJavaPlugins.onSchedule(run), initialDelay, period, unit));
        } catch (RejectedExecutionException ex) {
            RxJavaPlugins.onError(ex);
            return EmptyDisposable.INSTANCE;
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{<V> java.util.concurrent.ScheduledExecutorService.schedule(java.util.concurrent.Callable, long, java.util.concurrent.TimeUnit):java.util.concurrent.ScheduledFuture<V>}
     arg types: [dji.thirdparty.io.reactivex.internal.schedulers.ScheduledRunnable, long, java.util.concurrent.TimeUnit]
     candidates:
      ClspMth{java.util.concurrent.ScheduledExecutorService.schedule(java.lang.Runnable, long, java.util.concurrent.TimeUnit):java.util.concurrent.ScheduledFuture<?>}
      ClspMth{<V> java.util.concurrent.ScheduledExecutorService.schedule(java.util.concurrent.Callable, long, java.util.concurrent.TimeUnit):java.util.concurrent.ScheduledFuture<V>} */
    public ScheduledRunnable scheduleActual(Runnable run, long delayTime, TimeUnit unit, DisposableContainer parent) {
        Future<?> f;
        ScheduledRunnable sr = new ScheduledRunnable(RxJavaPlugins.onSchedule(run), parent);
        if (parent == null || parent.add(sr)) {
            if (delayTime <= 0) {
                try {
                    f = this.executor.submit((Callable) sr);
                } catch (RejectedExecutionException ex) {
                    parent.remove(sr);
                    RxJavaPlugins.onError(ex);
                }
            } else {
                f = this.executor.schedule((Callable) sr, delayTime, unit);
            }
            sr.setFuture(f);
        }
        return sr;
    }

    public void dispose() {
        if (!this.disposed) {
            this.disposed = true;
            this.executor.shutdownNow();
        }
    }

    public boolean isDisposed() {
        return this.disposed;
    }
}
