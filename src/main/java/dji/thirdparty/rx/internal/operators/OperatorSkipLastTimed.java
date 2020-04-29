package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Scheduler;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.schedulers.Timestamped;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

public class OperatorSkipLastTimed<T> implements Observable.Operator<T, T> {
    final Scheduler scheduler;
    final long timeInMillis;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    public OperatorSkipLastTimed(long time, TimeUnit unit, Scheduler scheduler2) {
        this.timeInMillis = unit.toMillis(time);
        this.scheduler = scheduler2;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>(subscriber) {
            /* class dji.thirdparty.rx.internal.operators.OperatorSkipLastTimed.AnonymousClass1 */
            private Deque<Timestamped<T>> buffer = new ArrayDeque();

            private void emitItemsOutOfWindow(long now) {
                long limit = now - OperatorSkipLastTimed.this.timeInMillis;
                while (!this.buffer.isEmpty()) {
                    Timestamped<T> v = this.buffer.getFirst();
                    if (v.getTimestampMillis() < limit) {
                        this.buffer.removeFirst();
                        subscriber.onNext(v.getValue());
                    } else {
                        return;
                    }
                }
            }

            public void onNext(T value) {
                long now = OperatorSkipLastTimed.this.scheduler.now();
                emitItemsOutOfWindow(now);
                this.buffer.offerLast(new Timestamped(now, value));
            }

            public void onError(Throwable e) {
                subscriber.onError(e);
            }

            public void onCompleted() {
                emitItemsOutOfWindow(OperatorSkipLastTimed.this.scheduler.now());
                subscriber.onCompleted();
            }
        };
    }
}
