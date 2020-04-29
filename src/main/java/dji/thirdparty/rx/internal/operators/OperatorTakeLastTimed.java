package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.Scheduler;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.functions.Func1;
import java.util.ArrayDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public final class OperatorTakeLastTimed<T> implements Observable.Operator<T, T> {
    final long ageMillis;
    final int count;
    final Scheduler scheduler;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    public OperatorTakeLastTimed(long time, TimeUnit unit, Scheduler scheduler2) {
        this.ageMillis = unit.toMillis(time);
        this.scheduler = scheduler2;
        this.count = -1;
    }

    public OperatorTakeLastTimed(int count2, long time, TimeUnit unit, Scheduler scheduler2) {
        if (count2 < 0) {
            throw new IndexOutOfBoundsException("count could not be negative");
        }
        this.ageMillis = unit.toMillis(time);
        this.scheduler = scheduler2;
        this.count = count2;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        final TakeLastTimedSubscriber<T> parent = new TakeLastTimedSubscriber<>(subscriber, this.count, this.ageMillis, this.scheduler);
        subscriber.add(parent);
        subscriber.setProducer(new Producer() {
            /* class dji.thirdparty.rx.internal.operators.OperatorTakeLastTimed.AnonymousClass1 */

            public void request(long n) {
                parent.requestMore(n);
            }
        });
        return parent;
    }

    static final class TakeLastTimedSubscriber<T> extends Subscriber<T> implements Func1<Object, T> {
        final Subscriber<? super T> actual;
        final long ageMillis;
        final int count;
        final NotificationLite<T> nl = NotificationLite.instance();
        final ArrayDeque<Object> queue = new ArrayDeque<>();
        final ArrayDeque<Long> queueTimes = new ArrayDeque<>();
        final AtomicLong requested = new AtomicLong();
        final Scheduler scheduler;

        public TakeLastTimedSubscriber(Subscriber<? super T> actual2, int count2, long ageMillis2, Scheduler scheduler2) {
            this.actual = actual2;
            this.count = count2;
            this.ageMillis = ageMillis2;
            this.scheduler = scheduler2;
        }

        public void onNext(T t) {
            if (this.count != 0) {
                long now = this.scheduler.now();
                if (this.queue.size() == this.count) {
                    this.queue.poll();
                    this.queueTimes.poll();
                }
                evictOld(now);
                this.queue.offer(this.nl.next(t));
                this.queueTimes.offer(Long.valueOf(now));
            }
        }

        /* access modifiers changed from: protected */
        public void evictOld(long now) {
            long minTime = now - this.ageMillis;
            while (true) {
                Long time = this.queueTimes.peek();
                if (time != null && time.longValue() < minTime) {
                    this.queue.poll();
                    this.queueTimes.poll();
                } else {
                    return;
                }
            }
        }

        public void onError(Throwable e) {
            this.queue.clear();
            this.queueTimes.clear();
            this.actual.onError(e);
        }

        public void onCompleted() {
            evictOld(this.scheduler.now());
            this.queueTimes.clear();
            BackpressureUtils.postCompleteDone(this.requested, this.queue, this.actual, this);
        }

        public T call(Object t) {
            return this.nl.getValue(t);
        }

        /* access modifiers changed from: package-private */
        public void requestMore(long n) {
            BackpressureUtils.postCompleteRequest(this.requested, n, this.queue, this.actual, this);
        }
    }
}
