package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.observers.SerializedSubscriber;
import dji.thirdparty.rx.subscriptions.CompositeSubscription;
import java.util.LinkedList;
import java.util.List;

public final class OperatorBufferWithStartEndObservable<T, TOpening, TClosing> implements Observable.Operator<List<T>, T> {
    final Func1<? super TOpening, ? extends Observable<? extends TClosing>> bufferClosing;
    final Observable<? extends TOpening> bufferOpening;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    public OperatorBufferWithStartEndObservable(Observable<? extends TOpening> bufferOpenings, Func1<? super TOpening, ? extends Observable<? extends TClosing>> bufferClosingSelector) {
        this.bufferOpening = bufferOpenings;
        this.bufferClosing = bufferClosingSelector;
    }

    public Subscriber<? super T> call(Subscriber<? super List<T>> child) {
        final OperatorBufferWithStartEndObservable<T, TOpening, TClosing>.BufferingSubscriber bsub = new BufferingSubscriber(new SerializedSubscriber(child));
        Subscriber<TOpening> openSubscriber = new Subscriber<TOpening>() {
            /* class dji.thirdparty.rx.internal.operators.OperatorBufferWithStartEndObservable.AnonymousClass1 */

            public void onNext(TOpening t) {
                bsub.startBuffer(t);
            }

            public void onError(Throwable e) {
                bsub.onError(e);
            }

            public void onCompleted() {
                bsub.onCompleted();
            }
        };
        child.add(openSubscriber);
        child.add(bsub);
        this.bufferOpening.unsafeSubscribe(openSubscriber);
        return bsub;
    }

    final class BufferingSubscriber extends Subscriber<T> {
        final Subscriber<? super List<T>> child;
        final List<List<T>> chunks = new LinkedList();
        final CompositeSubscription closingSubscriptions = new CompositeSubscription();
        boolean done;

        public BufferingSubscriber(Subscriber<? super List<T>> child2) {
            this.child = child2;
            add(this.closingSubscriptions);
        }

        public void onNext(T t) {
            synchronized (this) {
                for (List<T> chunk : this.chunks) {
                    chunk.add(t);
                }
            }
        }

        public void onError(Throwable e) {
            synchronized (this) {
                if (!this.done) {
                    this.done = true;
                    this.chunks.clear();
                    this.child.onError(e);
                    unsubscribe();
                }
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:11:0x001f, code lost:
            if (r1.hasNext() == false) goto L_0x0037;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0021, code lost:
            r5.child.onNext(r1.next());
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0037, code lost:
            r5.child.onCompleted();
            unsubscribe();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:?, code lost:
            r1 = r3.iterator();
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onCompleted() {
            /*
                r5 = this;
                monitor-enter(r5)     // Catch:{ Throwable -> 0x002d }
                boolean r4 = r5.done     // Catch:{ all -> 0x0034 }
                if (r4 == 0) goto L_0x0007
                monitor-exit(r5)     // Catch:{ all -> 0x0034 }
            L_0x0006:
                return
            L_0x0007:
                r4 = 1
                r5.done = r4     // Catch:{ all -> 0x0034 }
                java.util.LinkedList r3 = new java.util.LinkedList     // Catch:{ all -> 0x0034 }
                java.util.List<java.util.List<T>> r4 = r5.chunks     // Catch:{ all -> 0x0034 }
                r3.<init>(r4)     // Catch:{ all -> 0x0034 }
                java.util.List<java.util.List<T>> r4 = r5.chunks     // Catch:{ all -> 0x0034 }
                r4.clear()     // Catch:{ all -> 0x0034 }
                monitor-exit(r5)     // Catch:{ all -> 0x0034 }
                java.util.Iterator r1 = r3.iterator()     // Catch:{ Throwable -> 0x002d }
            L_0x001b:
                boolean r4 = r1.hasNext()     // Catch:{ Throwable -> 0x002d }
                if (r4 == 0) goto L_0x0037
                java.lang.Object r0 = r1.next()     // Catch:{ Throwable -> 0x002d }
                java.util.List r0 = (java.util.List) r0     // Catch:{ Throwable -> 0x002d }
                dji.thirdparty.rx.Subscriber<? super java.util.List<T>> r4 = r5.child     // Catch:{ Throwable -> 0x002d }
                r4.onNext(r0)     // Catch:{ Throwable -> 0x002d }
                goto L_0x001b
            L_0x002d:
                r2 = move-exception
                dji.thirdparty.rx.Subscriber<? super java.util.List<T>> r4 = r5.child
                dji.thirdparty.rx.exceptions.Exceptions.throwOrReport(r2, r4)
                goto L_0x0006
            L_0x0034:
                r4 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x0034 }
                throw r4     // Catch:{ Throwable -> 0x002d }
            L_0x0037:
                dji.thirdparty.rx.Subscriber<? super java.util.List<T>> r4 = r5.child
                r4.onCompleted()
                r5.unsubscribe()
                goto L_0x0006
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorBufferWithStartEndObservable.BufferingSubscriber.onCompleted():void");
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void startBuffer(TOpening r6) {
            /*
                r5 = this;
                java.util.ArrayList r0 = new java.util.ArrayList
                r0.<init>()
                monitor-enter(r5)
                boolean r4 = r5.done     // Catch:{ all -> 0x002a }
                if (r4 == 0) goto L_0x000c
                monitor-exit(r5)     // Catch:{ all -> 0x002a }
            L_0x000b:
                return
            L_0x000c:
                java.util.List<java.util.List<T>> r4 = r5.chunks     // Catch:{ all -> 0x002a }
                r4.add(r0)     // Catch:{ all -> 0x002a }
                monitor-exit(r5)     // Catch:{ all -> 0x002a }
                dji.thirdparty.rx.internal.operators.OperatorBufferWithStartEndObservable r4 = dji.thirdparty.rx.internal.operators.OperatorBufferWithStartEndObservable.this     // Catch:{ Throwable -> 0x002d }
                dji.thirdparty.rx.functions.Func1<? super TOpening, ? extends dji.thirdparty.rx.Observable<? extends TClosing>> r4 = r4.bufferClosing     // Catch:{ Throwable -> 0x002d }
                java.lang.Object r2 = r4.call(r6)     // Catch:{ Throwable -> 0x002d }
                dji.thirdparty.rx.Observable r2 = (dji.thirdparty.rx.Observable) r2     // Catch:{ Throwable -> 0x002d }
                dji.thirdparty.rx.internal.operators.OperatorBufferWithStartEndObservable$BufferingSubscriber$1 r1 = new dji.thirdparty.rx.internal.operators.OperatorBufferWithStartEndObservable$BufferingSubscriber$1
                r1.<init>(r0)
                dji.thirdparty.rx.subscriptions.CompositeSubscription r4 = r5.closingSubscriptions
                r4.add(r1)
                r2.unsafeSubscribe(r1)
                goto L_0x000b
            L_0x002a:
                r4 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x002a }
                throw r4
            L_0x002d:
                r3 = move-exception
                dji.thirdparty.rx.exceptions.Exceptions.throwOrReport(r3, r5)
                goto L_0x000b
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorBufferWithStartEndObservable.BufferingSubscriber.startBuffer(java.lang.Object):void");
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0021, code lost:
            if (r0 == false) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0023, code lost:
            r4.child.onNext(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void endBuffer(java.util.List<T> r5) {
            /*
                r4 = this;
                r0 = 0
                monitor-enter(r4)
                boolean r3 = r4.done     // Catch:{ all -> 0x0029 }
                if (r3 == 0) goto L_0x0008
                monitor-exit(r4)     // Catch:{ all -> 0x0029 }
            L_0x0007:
                return
            L_0x0008:
                java.util.List<java.util.List<T>> r3 = r4.chunks     // Catch:{ all -> 0x0029 }
                java.util.Iterator r2 = r3.iterator()     // Catch:{ all -> 0x0029 }
            L_0x000e:
                boolean r3 = r2.hasNext()     // Catch:{ all -> 0x0029 }
                if (r3 == 0) goto L_0x0020
                java.lang.Object r1 = r2.next()     // Catch:{ all -> 0x0029 }
                java.util.List r1 = (java.util.List) r1     // Catch:{ all -> 0x0029 }
                if (r1 != r5) goto L_0x000e
                r0 = 1
                r2.remove()     // Catch:{ all -> 0x0029 }
            L_0x0020:
                monitor-exit(r4)     // Catch:{ all -> 0x0029 }
                if (r0 == 0) goto L_0x0007
                dji.thirdparty.rx.Subscriber<? super java.util.List<T>> r3 = r4.child
                r3.onNext(r5)
                goto L_0x0007
            L_0x0029:
                r3 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x0029 }
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorBufferWithStartEndObservable.BufferingSubscriber.endBuffer(java.util.List):void");
        }
    }
}
