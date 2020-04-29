package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.Scheduler;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.functions.Action0;

public final class OperatorSubscribeOn<T> implements Observable.OnSubscribe<T> {
    final Scheduler scheduler;
    final Observable<T> source;

    public /* bridge */ /* synthetic */ void call(Object x0) {
        call((Subscriber) ((Subscriber) x0));
    }

    public OperatorSubscribeOn(Observable<T> source2, Scheduler scheduler2) {
        this.scheduler = scheduler2;
        this.source = source2;
    }

    public void call(final Subscriber<? super T> subscriber) {
        final Scheduler.Worker inner = this.scheduler.createWorker();
        subscriber.add(inner);
        inner.schedule(new Action0() {
            /* class dji.thirdparty.rx.internal.operators.OperatorSubscribeOn.AnonymousClass1 */

            public void call() {
                final Thread t = Thread.currentThread();
                OperatorSubscribeOn.this.source.unsafeSubscribe(new Subscriber<T>(subscriber) {
                    /* class dji.thirdparty.rx.internal.operators.OperatorSubscribeOn.AnonymousClass1.AnonymousClass1 */

                    public void onNext(T t) {
                        subscriber.onNext(t);
                    }

                    public void onError(Throwable e) {
                        try {
                            subscriber.onError(e);
                        } finally {
                            inner.unsubscribe();
                        }
                    }

                    public void onCompleted() {
                        try {
                            subscriber.onCompleted();
                        } finally {
                            inner.unsubscribe();
                        }
                    }

                    public void setProducer(final Producer p) {
                        subscriber.setProducer(new Producer() {
                            /* class dji.thirdparty.rx.internal.operators.OperatorSubscribeOn.AnonymousClass1.AnonymousClass1.AnonymousClass1 */

                            public void request(final long n) {
                                if (t == Thread.currentThread()) {
                                    p.request(n);
                                } else {
                                    inner.schedule(new Action0() {
                                        /* class dji.thirdparty.rx.internal.operators.OperatorSubscribeOn.AnonymousClass1.AnonymousClass1.AnonymousClass1.AnonymousClass1 */

                                        public void call() {
                                            p.request(n);
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}
