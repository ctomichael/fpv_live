package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Scheduler;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.functions.Action0;
import java.util.concurrent.TimeUnit;

public final class OnSubscribeTimerOnce implements Observable.OnSubscribe<Long> {
    final Scheduler scheduler;
    final long time;
    final TimeUnit unit;

    public /* bridge */ /* synthetic */ void call(Object x0) {
        call((Subscriber<? super Long>) ((Subscriber) x0));
    }

    public OnSubscribeTimerOnce(long time2, TimeUnit unit2, Scheduler scheduler2) {
        this.time = time2;
        this.unit = unit2;
        this.scheduler = scheduler2;
    }

    public void call(final Subscriber<? super Long> child) {
        Scheduler.Worker worker = this.scheduler.createWorker();
        child.add(worker);
        worker.schedule(new Action0() {
            /* class dji.thirdparty.rx.internal.operators.OnSubscribeTimerOnce.AnonymousClass1 */

            public void call() {
                try {
                    child.onNext(0L);
                    child.onCompleted();
                } catch (Throwable t) {
                    Exceptions.throwOrReport(t, child);
                }
            }
        }, this.time, this.unit);
    }
}
