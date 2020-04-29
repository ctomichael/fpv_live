package dji.thirdparty.rx.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Observer;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.functions.Func0;
import dji.thirdparty.rx.subscriptions.SerialSubscription;

public final class OperatorWhileDoWhile<T> implements Observable.OnSubscribe<T> {
    final Func0<Boolean> postCondition;
    final Func0<Boolean> preCondition;
    final Observable<? extends T> source;

    public /* bridge */ /* synthetic */ void call(Object x0) {
        call((Subscriber) ((Subscriber) x0));
    }

    public OperatorWhileDoWhile(Observable<? extends T> source2, Func0<Boolean> preCondition2, Func0<Boolean> postCondition2) {
        this.source = source2;
        this.preCondition = preCondition2;
        this.postCondition = postCondition2;
    }

    public void call(Subscriber<? super T> child) {
        try {
            if (this.preCondition.call().booleanValue()) {
                SerialSubscription cancel = new SerialSubscription();
                child.add(cancel);
                final OperatorWhileDoWhile<T>.SourceObserver sourceObserver = new SourceObserver(child, cancel);
                Subscriber<T> firstSubscription = new Subscriber<T>() {
                    /* class dji.thirdparty.rx.operators.OperatorWhileDoWhile.AnonymousClass1 */

                    public void onCompleted() {
                        sourceObserver.onCompleted();
                    }

                    public void onError(Throwable e) {
                        sourceObserver.onError(e);
                    }

                    public void onNext(T t) {
                        sourceObserver.onNext(t);
                    }
                };
                cancel.set(firstSubscription);
                this.source.unsafeSubscribe(firstSubscription);
                return;
            }
            child.onCompleted();
        } catch (Throwable t) {
            child.onError(t);
        }
    }

    final class SourceObserver implements Observer<T> {
        final Subscriber<? super T> actual;
        final SerialSubscription cancel;

        public SourceObserver(Subscriber<? super T> actual2, SerialSubscription cancel2) {
            this.actual = actual2;
            this.cancel = cancel2;
        }

        public void onNext(T args) {
            this.actual.onNext(args);
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }

        public void onCompleted() {
            try {
                if (OperatorWhileDoWhile.this.postCondition.call().booleanValue()) {
                    Subscriber<T> newSubscription = new Subscriber<T>() {
                        /* class dji.thirdparty.rx.operators.OperatorWhileDoWhile.SourceObserver.AnonymousClass1 */

                        public void onCompleted() {
                            SourceObserver.this.onCompleted();
                        }

                        public void onError(Throwable e) {
                            SourceObserver.this.onError(e);
                        }

                        public void onNext(T t) {
                            SourceObserver.this.onNext(t);
                        }
                    };
                    this.cancel.set(newSubscription);
                    OperatorWhileDoWhile.this.source.unsafeSubscribe(newSubscription);
                    return;
                }
                this.actual.onCompleted();
            } catch (Throwable t) {
                this.actual.onError(t);
            }
        }
    }
}
