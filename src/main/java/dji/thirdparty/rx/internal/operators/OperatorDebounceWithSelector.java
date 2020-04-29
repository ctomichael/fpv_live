package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.internal.operators.OperatorDebounceWithTime;
import dji.thirdparty.rx.observers.SerializedSubscriber;
import dji.thirdparty.rx.subscriptions.SerialSubscription;
import kotlin.jvm.internal.LongCompanionObject;

public final class OperatorDebounceWithSelector<T, U> implements Observable.Operator<T, T> {
    final Func1<? super T, ? extends Observable<U>> selector;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    public OperatorDebounceWithSelector(Func1<? super T, ? extends Observable<U>> selector2) {
        this.selector = selector2;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        final SerializedSubscriber<T> s = new SerializedSubscriber<>(child);
        final SerialSubscription ssub = new SerialSubscription();
        child.add(ssub);
        return new Subscriber<T>(child) {
            /* class dji.thirdparty.rx.internal.operators.OperatorDebounceWithSelector.AnonymousClass1 */
            final Subscriber<?> self = this;
            final OperatorDebounceWithTime.DebounceState<T> state = new OperatorDebounceWithTime.DebounceState<>();

            public void onStart() {
                request(LongCompanionObject.MAX_VALUE);
            }

            public void onNext(T t) {
                try {
                    Observable<U> debouncer = (Observable) OperatorDebounceWithSelector.this.selector.call(t);
                    final int index = this.state.next(t);
                    Subscriber<U> debounceSubscriber = new Subscriber<U>() {
                        /* class dji.thirdparty.rx.internal.operators.OperatorDebounceWithSelector.AnonymousClass1.AnonymousClass1 */

                        public void onNext(U u) {
                            onCompleted();
                        }

                        public void onError(Throwable e) {
                            AnonymousClass1.this.self.onError(e);
                        }

                        public void onCompleted() {
                            AnonymousClass1.this.state.emit(index, s, AnonymousClass1.this.self);
                            unsubscribe();
                        }
                    };
                    ssub.set(debounceSubscriber);
                    debouncer.unsafeSubscribe(debounceSubscriber);
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, this);
                }
            }

            public void onError(Throwable e) {
                s.onError(e);
                unsubscribe();
                this.state.clear();
            }

            public void onCompleted() {
                this.state.emitAndComplete(s, this);
            }
        };
    }
}
