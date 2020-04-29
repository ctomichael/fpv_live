package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Scheduler;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.functions.Action0;
import java.util.concurrent.TimeUnit;

public final class OperatorDelay<T> implements Observable.Operator<T, T> {
    final long delay;
    final Scheduler scheduler;
    final TimeUnit unit;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    public OperatorDelay(long delay2, TimeUnit unit2, Scheduler scheduler2) {
        this.delay = delay2;
        this.unit = unit2;
        this.scheduler = scheduler2;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        final Scheduler.Worker worker = this.scheduler.createWorker();
        child.add(worker);
        return new Subscriber<T>(child) {
            /* class dji.thirdparty.rx.internal.operators.OperatorDelay.AnonymousClass1 */
            boolean done;

            public void onCompleted() {
                worker.schedule(new Action0() {
                    /* class dji.thirdparty.rx.internal.operators.OperatorDelay.AnonymousClass1.AnonymousClass1 */

                    public void call() {
                        if (!AnonymousClass1.this.done) {
                            AnonymousClass1.this.done = true;
                            child.onCompleted();
                        }
                    }
                }, OperatorDelay.this.delay, OperatorDelay.this.unit);
            }

            public void onError(final Throwable e) {
                worker.schedule(new Action0() {
                    /* class dji.thirdparty.rx.internal.operators.OperatorDelay.AnonymousClass1.AnonymousClass2 */

                    public void call() {
                        if (!AnonymousClass1.this.done) {
                            AnonymousClass1.this.done = true;
                            child.onError(e);
                            worker.unsubscribe();
                        }
                    }
                });
            }

            public void onNext(final T t) {
                worker.schedule(new Action0() {
                    /* class dji.thirdparty.rx.internal.operators.OperatorDelay.AnonymousClass1.AnonymousClass3 */

                    public void call() {
                        if (!AnonymousClass1.this.done) {
                            child.onNext(t);
                        }
                    }
                }, OperatorDelay.this.delay, OperatorDelay.this.unit);
            }
        };
    }
}
