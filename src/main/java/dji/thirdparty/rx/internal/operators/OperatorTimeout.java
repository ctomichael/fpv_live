package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Scheduler;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.functions.Action0;
import dji.thirdparty.rx.internal.operators.OperatorTimeoutBase;
import java.util.concurrent.TimeUnit;

public final class OperatorTimeout<T> extends OperatorTimeoutBase<T> {
    public /* bridge */ /* synthetic */ Subscriber call(Subscriber x0) {
        return super.call(x0);
    }

    public OperatorTimeout(final long timeout, final TimeUnit timeUnit, Observable<? extends T> other, Scheduler scheduler) {
        super(new OperatorTimeoutBase.FirstTimeoutStub<T>() {
            /* class dji.thirdparty.rx.internal.operators.OperatorTimeout.AnonymousClass1 */

            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
             method: dji.thirdparty.rx.internal.operators.OperatorTimeout.1.call(dji.thirdparty.rx.internal.operators.OperatorTimeoutBase$TimeoutSubscriber, java.lang.Long, dji.thirdparty.rx.Scheduler$Worker):dji.thirdparty.rx.Subscription
             arg types: [java.lang.Object, java.lang.Object, java.lang.Object]
             candidates:
              dji.thirdparty.rx.internal.operators.OperatorTimeout.1.call(java.lang.Object, java.lang.Object, java.lang.Object):java.lang.Object
              dji.thirdparty.rx.functions.Func3.call(java.lang.Object, java.lang.Object, java.lang.Object):R
              dji.thirdparty.rx.internal.operators.OperatorTimeout.1.call(dji.thirdparty.rx.internal.operators.OperatorTimeoutBase$TimeoutSubscriber, java.lang.Long, dji.thirdparty.rx.Scheduler$Worker):dji.thirdparty.rx.Subscription */
            public /* bridge */ /* synthetic */ Object call(Object x0, Object x1, Object x2) {
                return call((OperatorTimeoutBase.TimeoutSubscriber) ((OperatorTimeoutBase.TimeoutSubscriber) x0), (Long) ((Long) x1), (Scheduler.Worker) ((Scheduler.Worker) x2));
            }

            public Subscription call(final OperatorTimeoutBase.TimeoutSubscriber<T> timeoutSubscriber, final Long seqId, Scheduler.Worker inner) {
                return inner.schedule(new Action0() {
                    /* class dji.thirdparty.rx.internal.operators.OperatorTimeout.AnonymousClass1.AnonymousClass1 */

                    public void call() {
                        timeoutSubscriber.onTimeout(seqId.longValue());
                    }
                }, timeout, timeUnit);
            }
        }, new OperatorTimeoutBase.TimeoutStub<T>() {
            /* class dji.thirdparty.rx.internal.operators.OperatorTimeout.AnonymousClass2 */

            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
             method: dji.thirdparty.rx.internal.operators.OperatorTimeout.2.call(dji.thirdparty.rx.internal.operators.OperatorTimeoutBase$TimeoutSubscriber, java.lang.Long, java.lang.Object, dji.thirdparty.rx.Scheduler$Worker):dji.thirdparty.rx.Subscription
             arg types: [java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object]
             candidates:
              dji.thirdparty.rx.internal.operators.OperatorTimeout.2.call(java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object):java.lang.Object
              dji.thirdparty.rx.functions.Func4.call(java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object):R
              dji.thirdparty.rx.internal.operators.OperatorTimeout.2.call(dji.thirdparty.rx.internal.operators.OperatorTimeoutBase$TimeoutSubscriber, java.lang.Long, java.lang.Object, dji.thirdparty.rx.Scheduler$Worker):dji.thirdparty.rx.Subscription */
            public /* bridge */ /* synthetic */ Object call(Object x0, Object x1, Object x2, Object x3) {
                return call((OperatorTimeoutBase.TimeoutSubscriber) ((OperatorTimeoutBase.TimeoutSubscriber) x0), (Long) ((Long) x1), x2, (Scheduler.Worker) ((Scheduler.Worker) x3));
            }

            public Subscription call(final OperatorTimeoutBase.TimeoutSubscriber<T> timeoutSubscriber, final Long seqId, T t, Scheduler.Worker inner) {
                return inner.schedule(new Action0() {
                    /* class dji.thirdparty.rx.internal.operators.OperatorTimeout.AnonymousClass2.AnonymousClass1 */

                    public void call() {
                        timeoutSubscriber.onTimeout(seqId.longValue());
                    }
                }, timeout, timeUnit);
            }
        }, other, scheduler);
    }
}
