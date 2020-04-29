package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.functions.Func0;
import dji.thirdparty.rx.observers.SerializedSubscriber;
import dji.thirdparty.rx.observers.Subscribers;
import java.util.ArrayList;
import java.util.List;

public final class OperatorBufferWithSingleObservable<T, TClosing> implements Observable.Operator<List<T>, T> {
    final Func0<? extends Observable<? extends TClosing>> bufferClosingSelector;
    final int initialCapacity;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    public OperatorBufferWithSingleObservable(Func0 func0, int initialCapacity2) {
        this.bufferClosingSelector = func0;
        this.initialCapacity = initialCapacity2;
    }

    public OperatorBufferWithSingleObservable(final Observable observable, int initialCapacity2) {
        this.bufferClosingSelector = new Func0<Observable<? extends TClosing>>() {
            /* class dji.thirdparty.rx.internal.operators.OperatorBufferWithSingleObservable.AnonymousClass1 */

            public Observable<? extends TClosing> call() {
                return observable;
            }
        };
        this.initialCapacity = initialCapacity2;
    }

    public Subscriber<? super T> call(Subscriber<? super List<T>> child) {
        try {
            Observable<? extends TClosing> closing = (Observable) this.bufferClosingSelector.call();
            final OperatorBufferWithSingleObservable<T, TClosing>.BufferingSubscriber bsub = new BufferingSubscriber(new SerializedSubscriber(child));
            Subscriber<TClosing> closingSubscriber = new Subscriber<TClosing>() {
                /* class dji.thirdparty.rx.internal.operators.OperatorBufferWithSingleObservable.AnonymousClass2 */

                public void onNext(TClosing tclosing) {
                    bsub.emit();
                }

                public void onError(Throwable e) {
                    bsub.onError(e);
                }

                public void onCompleted() {
                    bsub.onCompleted();
                }
            };
            child.add(closingSubscriber);
            child.add(bsub);
            closing.unsafeSubscribe(closingSubscriber);
            return bsub;
        } catch (Throwable t) {
            Exceptions.throwOrReport(t, child);
            return Subscribers.empty();
        }
    }

    final class BufferingSubscriber extends Subscriber<T> {
        final Subscriber<? super List<T>> child;
        List<T> chunk;
        boolean done;

        public BufferingSubscriber(Subscriber<? super List<T>> child2) {
            this.child = child2;
            this.chunk = new ArrayList(OperatorBufferWithSingleObservable.this.initialCapacity);
        }

        public void onNext(T t) {
            synchronized (this) {
                if (!this.done) {
                    this.chunk.add(t);
                }
            }
        }

        public void onError(Throwable e) {
            synchronized (this) {
                if (!this.done) {
                    this.done = true;
                    this.chunk = null;
                    this.child.onError(e);
                    unsubscribe();
                }
            }
        }

        public void onCompleted() {
            try {
                synchronized (this) {
                    if (!this.done) {
                        this.done = true;
                        List<T> toEmit = this.chunk;
                        this.chunk = null;
                        this.child.onNext(toEmit);
                        this.child.onCompleted();
                        unsubscribe();
                    }
                }
            } catch (Throwable t) {
                Exceptions.throwOrReport(t, this.child);
            }
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emit() {
            /*
                r4 = this;
                monitor-enter(r4)
                boolean r2 = r4.done     // Catch:{ all -> 0x0029 }
                if (r2 == 0) goto L_0x0007
                monitor-exit(r4)     // Catch:{ all -> 0x0029 }
            L_0x0006:
                return
            L_0x0007:
                java.util.List<T> r1 = r4.chunk     // Catch:{ all -> 0x0029 }
                java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ all -> 0x0029 }
                dji.thirdparty.rx.internal.operators.OperatorBufferWithSingleObservable r3 = dji.thirdparty.rx.internal.operators.OperatorBufferWithSingleObservable.this     // Catch:{ all -> 0x0029 }
                int r3 = r3.initialCapacity     // Catch:{ all -> 0x0029 }
                r2.<init>(r3)     // Catch:{ all -> 0x0029 }
                r4.chunk = r2     // Catch:{ all -> 0x0029 }
                monitor-exit(r4)     // Catch:{ all -> 0x0029 }
                dji.thirdparty.rx.Subscriber<? super java.util.List<T>> r2 = r4.child     // Catch:{ Throwable -> 0x001b }
                r2.onNext(r1)     // Catch:{ Throwable -> 0x001b }
                goto L_0x0006
            L_0x001b:
                r0 = move-exception
                r4.unsubscribe()
                monitor-enter(r4)
                boolean r2 = r4.done     // Catch:{ all -> 0x0026 }
                if (r2 == 0) goto L_0x002c
                monitor-exit(r4)     // Catch:{ all -> 0x0026 }
                goto L_0x0006
            L_0x0026:
                r2 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x0026 }
                throw r2
            L_0x0029:
                r2 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x0029 }
                throw r2
            L_0x002c:
                r2 = 1
                r4.done = r2     // Catch:{ all -> 0x0026 }
                monitor-exit(r4)     // Catch:{ all -> 0x0026 }
                dji.thirdparty.rx.Subscriber<? super java.util.List<T>> r2 = r4.child
                dji.thirdparty.rx.exceptions.Exceptions.throwOrReport(r0, r2)
                goto L_0x0006
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorBufferWithSingleObservable.BufferingSubscriber.emit():void");
        }
    }
}
