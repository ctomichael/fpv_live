package dji.thirdparty.io.reactivex.internal.schedulers;

import dji.thirdparty.io.reactivex.Scheduler;
import dji.thirdparty.io.reactivex.disposables.CompositeDisposable;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.disposables.Disposables;
import dji.thirdparty.io.reactivex.internal.disposables.EmptyDisposable;
import dji.thirdparty.io.reactivex.internal.disposables.SequentialDisposable;
import dji.thirdparty.io.reactivex.internal.queue.MpscLinkedQueue;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import dji.thirdparty.io.reactivex.schedulers.Schedulers;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public final class ExecutorScheduler extends Scheduler {
    static final Scheduler HELPER = Schedulers.single();
    final Executor executor;

    public ExecutorScheduler(Executor executor2) {
        this.executor = executor2;
    }

    public Scheduler.Worker createWorker() {
        return new ExecutorWorker(this.executor);
    }

    public Disposable scheduleDirect(Runnable run) {
        Runnable decoratedRun = RxJavaPlugins.onSchedule(run);
        try {
            if (this.executor instanceof ExecutorService) {
                return Disposables.fromFuture(((ExecutorService) this.executor).submit(decoratedRun));
            }
            ExecutorWorker.BooleanRunnable br = new ExecutorWorker.BooleanRunnable(decoratedRun);
            this.executor.execute(br);
            return br;
        } catch (RejectedExecutionException ex) {
            RxJavaPlugins.onError(ex);
            return EmptyDisposable.INSTANCE;
        }
    }

    public Disposable scheduleDirect(Runnable run, long delay, TimeUnit unit) {
        final Runnable decoratedRun = RxJavaPlugins.onSchedule(run);
        if (this.executor instanceof ScheduledExecutorService) {
            try {
                return Disposables.fromFuture(((ScheduledExecutorService) this.executor).schedule(decoratedRun, delay, unit));
            } catch (RejectedExecutionException ex) {
                RxJavaPlugins.onError(ex);
                return EmptyDisposable.INSTANCE;
            }
        } else {
            SequentialDisposable first = new SequentialDisposable();
            final SequentialDisposable mar = new SequentialDisposable(first);
            first.replace(HELPER.scheduleDirect(new Runnable() {
                /* class dji.thirdparty.io.reactivex.internal.schedulers.ExecutorScheduler.AnonymousClass1 */

                public void run() {
                    mar.replace(ExecutorScheduler.this.scheduleDirect(decoratedRun));
                }
            }, delay, unit));
            return mar;
        }
    }

    public Disposable schedulePeriodicallyDirect(Runnable run, long initialDelay, long period, TimeUnit unit) {
        if (!(this.executor instanceof ScheduledExecutorService)) {
            return super.schedulePeriodicallyDirect(run, initialDelay, period, unit);
        }
        try {
            return Disposables.fromFuture(((ScheduledExecutorService) this.executor).scheduleAtFixedRate(RxJavaPlugins.onSchedule(run), initialDelay, period, unit));
        } catch (RejectedExecutionException ex) {
            RxJavaPlugins.onError(ex);
            return EmptyDisposable.INSTANCE;
        }
    }

    public static final class ExecutorWorker extends Scheduler.Worker implements Runnable {
        volatile boolean disposed;
        final Executor executor;
        final MpscLinkedQueue<Runnable> queue;
        final CompositeDisposable tasks = new CompositeDisposable();
        final AtomicInteger wip = new AtomicInteger();

        public ExecutorWorker(Executor executor2) {
            this.executor = executor2;
            this.queue = new MpscLinkedQueue<>();
        }

        public Disposable schedule(Runnable run) {
            if (this.disposed) {
                return EmptyDisposable.INSTANCE;
            }
            BooleanRunnable br = new BooleanRunnable(RxJavaPlugins.onSchedule(run));
            this.queue.offer(br);
            if (this.wip.getAndIncrement() != 0) {
                return br;
            }
            try {
                this.executor.execute(this);
                return br;
            } catch (RejectedExecutionException ex) {
                this.disposed = true;
                this.queue.clear();
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
        public Disposable schedule(Runnable run, long delay, TimeUnit unit) {
            if (delay <= 0) {
                return schedule(run);
            }
            if (this.disposed) {
                return EmptyDisposable.INSTANCE;
            }
            SequentialDisposable first = new SequentialDisposable();
            final SequentialDisposable mar = new SequentialDisposable(first);
            final Runnable decoratedRun = RxJavaPlugins.onSchedule(run);
            ScheduledRunnable sr = new ScheduledRunnable(new Runnable() {
                /* class dji.thirdparty.io.reactivex.internal.schedulers.ExecutorScheduler.ExecutorWorker.AnonymousClass1 */

                public void run() {
                    mar.replace(ExecutorWorker.this.schedule(decoratedRun));
                }
            }, this.tasks);
            this.tasks.add(sr);
            if (this.executor instanceof ScheduledExecutorService) {
                try {
                    sr.setFuture(((ScheduledExecutorService) this.executor).schedule((Callable) sr, delay, unit));
                } catch (RejectedExecutionException ex) {
                    this.disposed = true;
                    RxJavaPlugins.onError(ex);
                    return EmptyDisposable.INSTANCE;
                }
            } else {
                sr.setFuture(new DisposeOnCancel(ExecutorScheduler.HELPER.scheduleDirect(sr, delay, unit)));
            }
            first.replace(sr);
            return mar;
        }

        public void dispose() {
            if (!this.disposed) {
                this.disposed = true;
                this.tasks.dispose();
                if (this.wip.getAndIncrement() == 0) {
                    this.queue.clear();
                }
            }
        }

        public boolean isDisposed() {
            return this.disposed;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0026, code lost:
            r0 = r5.wip.addAndGet(-r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x002d, code lost:
            if (r0 != 0) goto L_0x0003;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:7:0x0015, code lost:
            if (r5.disposed == false) goto L_0x0026;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x0017, code lost:
            r1.clear();
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r5 = this;
                r0 = 1
                dji.thirdparty.io.reactivex.internal.queue.MpscLinkedQueue<java.lang.Runnable> r1 = r5.queue
            L_0x0003:
                boolean r3 = r5.disposed
                if (r3 == 0) goto L_0x000b
                r1.clear()
            L_0x000a:
                return
            L_0x000b:
                java.lang.Object r2 = r1.poll()
                java.lang.Runnable r2 = (java.lang.Runnable) r2
                if (r2 != 0) goto L_0x001b
                boolean r3 = r5.disposed
                if (r3 == 0) goto L_0x0026
                r1.clear()
                goto L_0x000a
            L_0x001b:
                r2.run()
                boolean r3 = r5.disposed
                if (r3 == 0) goto L_0x000b
                r1.clear()
                goto L_0x000a
            L_0x0026:
                java.util.concurrent.atomic.AtomicInteger r3 = r5.wip
                int r4 = -r0
                int r0 = r3.addAndGet(r4)
                if (r0 != 0) goto L_0x0003
                goto L_0x000a
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.internal.schedulers.ExecutorScheduler.ExecutorWorker.run():void");
        }

        static final class BooleanRunnable extends AtomicBoolean implements Runnable, Disposable {
            private static final long serialVersionUID = -2421395018820541164L;
            final Runnable actual;

            BooleanRunnable(Runnable actual2) {
                this.actual = actual2;
            }

            public void run() {
                if (!get()) {
                    this.actual.run();
                }
            }

            public void dispose() {
                lazySet(true);
            }

            public boolean isDisposed() {
                return get();
            }
        }
    }
}
