package dji.thirdparty.rx;

import dji.thirdparty.rx.functions.Action0;
import dji.thirdparty.rx.subscriptions.MultipleAssignmentSubscription;
import java.util.concurrent.TimeUnit;

public abstract class Scheduler {
    static final long CLOCK_DRIFT_TOLERANCE_NANOS = TimeUnit.MINUTES.toNanos(Long.getLong("rx.scheduler.drift-tolerance", 15).longValue());

    public abstract Worker createWorker();

    public static abstract class Worker implements Subscription {
        public abstract Subscription schedule(Action0 action0);

        public abstract Subscription schedule(Action0 action0, long j, TimeUnit timeUnit);

        public Subscription schedulePeriodically(Action0 action, long initialDelay, long period, TimeUnit unit) {
            final long periodInNanos = unit.toNanos(period);
            final long firstNowNanos = TimeUnit.MILLISECONDS.toNanos(now());
            final long firstStartInNanos = firstNowNanos + unit.toNanos(initialDelay);
            final MultipleAssignmentSubscription mas = new MultipleAssignmentSubscription();
            final Action0 action0 = action;
            Action0 recursiveAction = new Action0() {
                /* class dji.thirdparty.rx.Scheduler.Worker.AnonymousClass1 */
                long count;
                long lastNowNanos = firstNowNanos;
                long startInNanos = firstStartInNanos;

                public void call() {
                    long nextTick;
                    if (!mas.isUnsubscribed()) {
                        action0.call();
                        long nowNanos = TimeUnit.MILLISECONDS.toNanos(Worker.this.now());
                        if (Scheduler.CLOCK_DRIFT_TOLERANCE_NANOS + nowNanos < this.lastNowNanos || nowNanos >= this.lastNowNanos + periodInNanos + Scheduler.CLOCK_DRIFT_TOLERANCE_NANOS) {
                            nextTick = nowNanos + periodInNanos;
                            long j = periodInNanos;
                            long j2 = this.count + 1;
                            this.count = j2;
                            this.startInNanos = nextTick - (j * j2);
                        } else {
                            long j3 = this.startInNanos;
                            long j4 = this.count + 1;
                            this.count = j4;
                            nextTick = j3 + (j4 * periodInNanos);
                        }
                        this.lastNowNanos = nowNanos;
                        mas.set(Worker.this.schedule(this, nextTick - nowNanos, TimeUnit.NANOSECONDS));
                    }
                }
            };
            MultipleAssignmentSubscription s = new MultipleAssignmentSubscription();
            mas.set(s);
            s.set(schedule(recursiveAction, initialDelay, unit));
            return mas;
        }

        public long now() {
            return System.currentTimeMillis();
        }
    }

    public long now() {
        return System.currentTimeMillis();
    }
}
