package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.internal.producers.SingleDelayedProducer;

public final class OperatorAny<T> implements Observable.Operator<Boolean, T> {
    final Func1<? super T, Boolean> predicate;
    final boolean returnOnEmpty;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber<? super Boolean>) ((Subscriber) x0));
    }

    public OperatorAny(Func1<? super T, Boolean> predicate2, boolean returnOnEmpty2) {
        this.predicate = predicate2;
        this.returnOnEmpty = returnOnEmpty2;
    }

    public Subscriber<? super T> call(final Subscriber<? super Boolean> child) {
        final SingleDelayedProducer<Boolean> producer = new SingleDelayedProducer<>(child);
        Subscriber<T> s = new Subscriber<T>() {
            /* class dji.thirdparty.rx.internal.operators.OperatorAny.AnonymousClass1 */
            boolean done;
            boolean hasElements;

            public void onNext(T t) {
                this.hasElements = true;
                try {
                    if (OperatorAny.this.predicate.call(t).booleanValue() && !this.done) {
                        this.done = true;
                        producer.setValue(Boolean.valueOf(!OperatorAny.this.returnOnEmpty));
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
                    if (this.hasElements) {
                        producer.setValue(false);
                    } else {
                        producer.setValue(Boolean.valueOf(OperatorAny.this.returnOnEmpty));
                    }
                }
            }
        };
        child.add(s);
        child.setProducer(producer);
        return s;
    }
}
