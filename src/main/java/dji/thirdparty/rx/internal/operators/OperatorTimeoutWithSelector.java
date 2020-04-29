package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Scheduler;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.functions.Func0;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.internal.operators.OperatorTimeoutBase;
import dji.thirdparty.rx.schedulers.Schedulers;
import dji.thirdparty.rx.subscriptions.Subscriptions;

public class OperatorTimeoutWithSelector<T, U, V> extends OperatorTimeoutBase<T> {
    public /* bridge */ /* synthetic */ Subscriber call(Subscriber x0) {
        return super.call(x0);
    }

    public OperatorTimeoutWithSelector(final Func0<? extends Observable<U>> firstTimeoutSelector, final Func1<? super T, ? extends Observable<V>> timeoutSelector, Observable<? extends T> other) {
        super(new OperatorTimeoutBase.FirstTimeoutStub<T>() {
            /* class dji.thirdparty.rx.internal.operators.OperatorTimeoutWithSelector.AnonymousClass1 */

            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
             method: dji.thirdparty.rx.internal.operators.OperatorTimeoutWithSelector.1.call(dji.thirdparty.rx.internal.operators.OperatorTimeoutBase$TimeoutSubscriber, java.lang.Long, dji.thirdparty.rx.Scheduler$Worker):dji.thirdparty.rx.Subscription
             arg types: [java.lang.Object, java.lang.Object, java.lang.Object]
             candidates:
              dji.thirdparty.rx.internal.operators.OperatorTimeoutWithSelector.1.call(java.lang.Object, java.lang.Object, java.lang.Object):java.lang.Object
              dji.thirdparty.rx.functions.Func3.call(java.lang.Object, java.lang.Object, java.lang.Object):R
              dji.thirdparty.rx.internal.operators.OperatorTimeoutWithSelector.1.call(dji.thirdparty.rx.internal.operators.OperatorTimeoutBase$TimeoutSubscriber, java.lang.Long, dji.thirdparty.rx.Scheduler$Worker):dji.thirdparty.rx.Subscription */
            public /* bridge */ /* synthetic */ Object call(Object x0, Object x1, Object x2) {
                return call((OperatorTimeoutBase.TimeoutSubscriber) ((OperatorTimeoutBase.TimeoutSubscriber) x0), (Long) ((Long) x1), (Scheduler.Worker) ((Scheduler.Worker) x2));
            }

            public Subscription call(final OperatorTimeoutBase.TimeoutSubscriber<T> timeoutSubscriber, final Long seqId, Scheduler.Worker inner) {
                if (Func0.this == null) {
                    return Subscriptions.unsubscribed();
                }
                try {
                    return ((Observable) Func0.this.call()).unsafeSubscribe(new Subscriber<U>() {
                        /* class dji.thirdparty.rx.internal.operators.OperatorTimeoutWithSelector.AnonymousClass1.AnonymousClass1 */

                        public void onCompleted() {
                            timeoutSubscriber.onTimeout(seqId.longValue());
                        }

                        public void onError(Throwable e) {
                            timeoutSubscriber.onError(e);
                        }

                        public void onNext(U u) {
                            timeoutSubscriber.onTimeout(seqId.longValue());
                        }
                    });
                } catch (Throwable t) {
                    Exceptions.throwOrReport(t, timeoutSubscriber);
                    return Subscriptions.unsubscribed();
                }
            }
        }, new OperatorTimeoutBase.TimeoutStub<T>() {
            /* class dji.thirdparty.rx.internal.operators.OperatorTimeoutWithSelector.AnonymousClass2 */

            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
             method: dji.thirdparty.rx.internal.operators.OperatorTimeoutWithSelector.2.call(dji.thirdparty.rx.internal.operators.OperatorTimeoutBase$TimeoutSubscriber, java.lang.Long, java.lang.Object, dji.thirdparty.rx.Scheduler$Worker):dji.thirdparty.rx.Subscription
             arg types: [java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object]
             candidates:
              dji.thirdparty.rx.internal.operators.OperatorTimeoutWithSelector.2.call(java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object):java.lang.Object
              dji.thirdparty.rx.functions.Func4.call(java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object):R
              dji.thirdparty.rx.internal.operators.OperatorTimeoutWithSelector.2.call(dji.thirdparty.rx.internal.operators.OperatorTimeoutBase$TimeoutSubscriber, java.lang.Long, java.lang.Object, dji.thirdparty.rx.Scheduler$Worker):dji.thirdparty.rx.Subscription */
            public /* bridge */ /* synthetic */ Object call(Object x0, Object x1, Object x2, Object x3) {
                return call((OperatorTimeoutBase.TimeoutSubscriber) ((OperatorTimeoutBase.TimeoutSubscriber) x0), (Long) ((Long) x1), x2, (Scheduler.Worker) ((Scheduler.Worker) x3));
            }

            public Subscription call(final OperatorTimeoutBase.TimeoutSubscriber<T> timeoutSubscriber, final Long seqId, T value, Scheduler.Worker inner) {
                try {
                    return ((Observable) Func1.this.call(value)).unsafeSubscribe(new Subscriber<V>() {
                        /* class dji.thirdparty.rx.internal.operators.OperatorTimeoutWithSelector.AnonymousClass2.AnonymousClass1 */

                        public void onCompleted() {
                            timeoutSubscriber.onTimeout(seqId.longValue());
                        }

                        public void onError(Throwable e) {
                            timeoutSubscriber.onError(e);
                        }

                        public void onNext(V v) {
                            timeoutSubscriber.onTimeout(seqId.longValue());
                        }
                    });
                } catch (Throwable t) {
                    Exceptions.throwOrReport(t, timeoutSubscriber);
                    return Subscriptions.unsubscribed();
                }
            }
        }, other, Schedulers.immediate());
    }
}
