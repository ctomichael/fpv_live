package dji.thirdparty.io.reactivex.internal.schedulers;

import dji.thirdparty.io.reactivex.Scheduler;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.disposables.Disposables;
import dji.thirdparty.io.reactivex.internal.disposables.EmptyDisposable;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class TrampolineScheduler extends Scheduler {
    private static final TrampolineScheduler INSTANCE = new TrampolineScheduler();

    public static TrampolineScheduler instance() {
        return INSTANCE;
    }

    public Scheduler.Worker createWorker() {
        return new TrampolineWorker();
    }

    TrampolineScheduler() {
    }

    public Disposable scheduleDirect(Runnable run) {
        run.run();
        return EmptyDisposable.INSTANCE;
    }

    public Disposable scheduleDirect(Runnable run, long delay, TimeUnit unit) {
        try {
            unit.sleep(delay);
            run.run();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            RxJavaPlugins.onError(ex);
        }
        return EmptyDisposable.INSTANCE;
    }

    static final class TrampolineWorker extends Scheduler.Worker implements Disposable {
        final AtomicInteger counter = new AtomicInteger();
        volatile boolean disposed;
        final PriorityBlockingQueue<TimedRunnable> queue = new PriorityBlockingQueue<>();
        private final AtomicInteger wip = new AtomicInteger();

        TrampolineWorker() {
        }

        public Disposable schedule(Runnable action) {
            return enqueue(action, now(TimeUnit.MILLISECONDS));
        }

        public Disposable schedule(Runnable action, long delayTime, TimeUnit unit) {
            long execTime = now(TimeUnit.MILLISECONDS) + unit.toMillis(delayTime);
            return enqueue(new SleepingRunnable(action, this, execTime), execTime);
        }

        /* access modifiers changed from: package-private */
        public Disposable enqueue(Runnable action, long execTime) {
            if (this.disposed) {
                return EmptyDisposable.INSTANCE;
            }
            final TimedRunnable timedRunnable = new TimedRunnable(action, Long.valueOf(execTime), this.counter.incrementAndGet());
            this.queue.add(timedRunnable);
            if (this.wip.getAndIncrement() != 0) {
                return Disposables.fromRunnable(new Runnable() {
                    /* class dji.thirdparty.io.reactivex.internal.schedulers.TrampolineScheduler.TrampolineWorker.AnonymousClass1 */

                    public void run() {
                        timedRunnable.disposed = true;
                        TrampolineWorker.this.queue.remove(timedRunnable);
                    }
                });
            }
            int missed = 1;
            while (true) {
                TimedRunnable polled = this.queue.poll();
                if (polled == null) {
                    missed = this.wip.addAndGet(-missed);
                    if (missed == 0) {
                        return EmptyDisposable.INSTANCE;
                    }
                } else if (!polled.disposed) {
                    polled.run.run();
                }
            }
        }

        public void dispose() {
            this.disposed = true;
        }

        public boolean isDisposed() {
            return this.disposed;
        }
    }

    static final class TimedRunnable implements Comparable<TimedRunnable> {
        final int count;
        volatile boolean disposed;
        final long execTime;
        final Runnable run;

        TimedRunnable(Runnable run2, Long execTime2, int count2) {
            this.run = run2;
            this.execTime = execTime2.longValue();
            this.count = count2;
        }

        public int compareTo(TimedRunnable that) {
            int result = ObjectHelper.compare(this.execTime, that.execTime);
            if (result == 0) {
                return ObjectHelper.compare(this.count, that.count);
            }
            return result;
        }
    }

    static final class SleepingRunnable implements Runnable {
        private final long execTime;
        private final Runnable run;
        private final TrampolineWorker worker;

        SleepingRunnable(Runnable run2, TrampolineWorker worker2, long execTime2) {
            this.run = run2;
            this.worker = worker2;
            this.execTime = execTime2;
        }

        public void run() {
            if (!this.worker.disposed) {
                long t = this.worker.now(TimeUnit.MILLISECONDS);
                if (this.execTime > t) {
                    long delay = this.execTime - t;
                    if (delay > 0) {
                        try {
                            Thread.sleep(delay);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            RxJavaPlugins.onError(e);
                            return;
                        }
                    }
                }
                if (!this.worker.disposed) {
                    this.run.run();
                }
            }
        }
    }
}
