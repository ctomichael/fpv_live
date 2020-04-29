package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.internal.producers.SingleDelayedProducer;

public final class OperatorAll<T> implements Observable.Operator<Boolean, T> {
    final Func1<? super T, Boolean> predicate;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber<? super Boolean>) ((Subscriber) x0));
    }

    public OperatorAll(Func1<? super T, Boolean> predicate2) {
        this.predicate = predicate2;
    }

    public Subscriber<? super T> call(final Subscriber<? super Boolean> child) {
        final SingleDelayedProducer<Boolean> producer = new SingleDelayedProducer<>(child);
        Subscriber<T> s = new Subscriber<T>() {
            /* class dji.thirdparty.rx.internal.operators.OperatorAll.AnonymousClass1 */
            boolean done;

            public void onNext(T t) {
                try {
                    if (!OperatorAll.this.predicate.call(t).booleanValue() && !this.done) {
                        this.done = true;
                        producer.setValue(false);
                        unsubscribe();
                    }
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, this, t);
                }
            }

            public void onError(Throwable e) {
                child.onError(e);
            }

            public void onCompleted() {
                if (!this.done) {
                    this.done = true;
                    producer.setValue(true);
                }
            }
        };
        child.add(s);
        child.setProducer(producer);
        return s;
    }
}
