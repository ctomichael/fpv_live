package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.Subscriber;
import java.util.concurrent.atomic.AtomicLong;

public final class OperatorTake<T> implements Observable.Operator<T, T> {
    final int limit;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    public OperatorTake(int limit2) {
        if (limit2 < 0) {
            throw new IllegalArgumentException("limit >= 0 required but it was " + limit2);
        }
        this.limit = limit2;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        Subscriber<T> parent = new Subscriber<T>() {
            /* class dji.thirdparty.rx.internal.operators.OperatorTake.AnonymousClass1 */
            boolean completed;
            int count;

            public void onCompleted() {
                if (!this.completed) {
                    this.completed = true;
                    child.onCompleted();
                }
            }

            public void onError(Throwable e) {
                if (!this.completed) {
                    this.completed = true;
                    try {
                        child.onError(e);
                    } finally {
                        unsubscribe();
                    }
                }
            }

            public void onNext(T i) {
                if (!isUnsubscribed()) {
                    int i2 = this.count;
                    this.count = i2 + 1;
                    if (i2 < OperatorTake.this.limit) {
                        boolean stop = this.count == OperatorTake.this.limit;
                        child.onNext(i);
                        if (stop && !this.completed) {
                            this.completed = true;
                            try {
                                child.onCompleted();
                            } finally {
                                unsubscribe();
                            }
                        }
                    }
                }
            }

            public void setProducer(final Producer producer) {
                child.setProducer(new Producer() {
                    /* class dji.thirdparty.rx.internal.operators.OperatorTake.AnonymousClass1.AnonymousClass1 */
                    final AtomicLong requested = new AtomicLong(0);

                    public void request(long n) {
                        long r;
                        long c;
                        if (n > 0 && !AnonymousClass1.this.completed) {
                            do {
                                r = this.requested.get();
                                c = Math.min(n, ((long) OperatorTake.this.limit) - r);
                                if (c == 0) {
                                    return;
                                }
                            } while (!this.requested.compareAndSet(r, r + c));
                            producer.request(c);
                        }
                    }
                });
            }
        };
        if (this.limit == 0) {
            child.onCompleted();
            parent.unsubscribe();
        }
        child.add(parent);
        return parent;
    }
}
