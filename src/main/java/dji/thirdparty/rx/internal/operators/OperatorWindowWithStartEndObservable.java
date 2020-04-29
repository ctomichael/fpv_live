package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Observer;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.observers.SerializedObserver;
import dji.thirdparty.rx.observers.SerializedSubscriber;
import dji.thirdparty.rx.subscriptions.CompositeSubscription;
import java.util.LinkedList;
import java.util.List;
import kotlin.jvm.internal.LongCompanionObject;

public final class OperatorWindowWithStartEndObservable<T, U, V> implements Observable.Operator<Observable<T>, T> {
    final Func1<? super U, ? extends Observable<? extends V>> windowClosingSelector;
    final Observable<? extends U> windowOpenings;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    public OperatorWindowWithStartEndObservable(Observable<? extends U> windowOpenings2, Func1<? super U, ? extends Observable<? extends V>> windowClosingSelector2) {
        this.windowOpenings = windowOpenings2;
        this.windowClosingSelector = windowClosingSelector2;
    }

    public Subscriber<? super T> call(Subscriber<? super Observable<T>> child) {
        CompositeSubscription csub = new CompositeSubscription();
        child.add(csub);
        final OperatorWindowWithStartEndObservable<T, U, V>.SourceSubscriber sub = new SourceSubscriber(child, csub);
        Subscriber<U> open = new Subscriber<U>() {
            /* class dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable.AnonymousClass1 */

            public void onStart() {
                request(LongCompanionObject.MAX_VALUE);
            }

            public void onNext(U t) {
                sub.beginWindow(t);
            }

            public void onError(Throwable e) {
                sub.onError(e);
            }

            public void onCompleted() {
                sub.onCompleted();
            }
        };
        csub.add(sub);
        csub.add(open);
        this.windowOpenings.unsafeSubscribe(open);
        return sub;
    }

    static final class SerializedSubject<T> {
        final Observer<T> consumer;
        final Observable<T> producer;

        public SerializedSubject(Observer<T> consumer2, Observable<T> producer2) {
            this.consumer = new SerializedObserver(consumer2);
            this.producer = producer2;
        }
    }

    final class SourceSubscriber extends Subscriber<T> {
        final Subscriber<? super Observable<T>> child;
        final List<SerializedSubject<T>> chunks = new LinkedList();
        final CompositeSubscription csub;
        boolean done;
        final Object guard = new Object();

        public SourceSubscriber(Subscriber<? super Observable<T>> child2, CompositeSubscription csub2) {
            this.child = new SerializedSubscriber(child2);
            this.csub = csub2;
        }

        public void onStart() {
            request(LongCompanionObject.MAX_VALUE);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0019, code lost:
            if (r1.hasNext() == false) goto L_0x0008;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x001b, code lost:
            r1.next().consumer.onNext(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x0011, code lost:
            r1 = r2.iterator();
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onNext(T r6) {
            /*
                r5 = this;
                java.lang.Object r4 = r5.guard
                monitor-enter(r4)
                boolean r3 = r5.done     // Catch:{ all -> 0x0027 }
                if (r3 == 0) goto L_0x0009
                monitor-exit(r4)     // Catch:{ all -> 0x0027 }
            L_0x0008:
                return
            L_0x0009:
                java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ all -> 0x0027 }
                java.util.List<dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable$SerializedSubject<T>> r3 = r5.chunks     // Catch:{ all -> 0x0027 }
                r2.<init>(r3)     // Catch:{ all -> 0x0027 }
                monitor-exit(r4)     // Catch:{ all -> 0x0027 }
                java.util.Iterator r1 = r2.iterator()
            L_0x0015:
                boolean r3 = r1.hasNext()
                if (r3 == 0) goto L_0x0008
                java.lang.Object r0 = r1.next()
                dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable$SerializedSubject r0 = (dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable.SerializedSubject) r0
                dji.thirdparty.rx.Observer<T> r3 = r0.consumer
                r3.onNext(r6)
                goto L_0x0015
            L_0x0027:
                r3 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x0027 }
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable.SourceSubscriber.onNext(java.lang.Object):void");
        }

        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            r1 = r2.iterator();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0026, code lost:
            if (r1.hasNext() == false) goto L_0x003e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0028, code lost:
            r1.next().consumer.onError(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x003e, code lost:
            r5.child.onError(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0043, code lost:
            r5.csub.unsubscribe();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onError(java.lang.Throwable r6) {
            /*
                r5 = this;
                java.lang.Object r4 = r5.guard     // Catch:{ all -> 0x0034 }
                monitor-enter(r4)     // Catch:{ all -> 0x0034 }
                boolean r3 = r5.done     // Catch:{ all -> 0x003b }
                if (r3 == 0) goto L_0x000e
                monitor-exit(r4)     // Catch:{ all -> 0x003b }
                dji.thirdparty.rx.subscriptions.CompositeSubscription r3 = r5.csub
                r3.unsubscribe()
            L_0x000d:
                return
            L_0x000e:
                r3 = 1
                r5.done = r3     // Catch:{ all -> 0x003b }
                java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ all -> 0x003b }
                java.util.List<dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable$SerializedSubject<T>> r3 = r5.chunks     // Catch:{ all -> 0x003b }
                r2.<init>(r3)     // Catch:{ all -> 0x003b }
                java.util.List<dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable$SerializedSubject<T>> r3 = r5.chunks     // Catch:{ all -> 0x003b }
                r3.clear()     // Catch:{ all -> 0x003b }
                monitor-exit(r4)     // Catch:{ all -> 0x003b }
                java.util.Iterator r1 = r2.iterator()     // Catch:{ all -> 0x0034 }
            L_0x0022:
                boolean r3 = r1.hasNext()     // Catch:{ all -> 0x0034 }
                if (r3 == 0) goto L_0x003e
                java.lang.Object r0 = r1.next()     // Catch:{ all -> 0x0034 }
                dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable$SerializedSubject r0 = (dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable.SerializedSubject) r0     // Catch:{ all -> 0x0034 }
                dji.thirdparty.rx.Observer<T> r3 = r0.consumer     // Catch:{ all -> 0x0034 }
                r3.onError(r6)     // Catch:{ all -> 0x0034 }
                goto L_0x0022
            L_0x0034:
                r3 = move-exception
                dji.thirdparty.rx.subscriptions.CompositeSubscription r4 = r5.csub
                r4.unsubscribe()
                throw r3
            L_0x003b:
                r3 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x003b }
                throw r3     // Catch:{ all -> 0x0034 }
            L_0x003e:
                dji.thirdparty.rx.Subscriber<? super dji.thirdparty.rx.Observable<T>> r3 = r5.child     // Catch:{ all -> 0x0034 }
                r3.onError(r6)     // Catch:{ all -> 0x0034 }
                dji.thirdparty.rx.subscriptions.CompositeSubscription r3 = r5.csub
                r3.unsubscribe()
                goto L_0x000d
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable.SourceSubscriber.onError(java.lang.Throwable):void");
        }

        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            r1 = r2.iterator();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0026, code lost:
            if (r1.hasNext() == false) goto L_0x003e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0028, code lost:
            r1.next().consumer.onCompleted();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x003e, code lost:
            r5.child.onCompleted();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0043, code lost:
            r5.csub.unsubscribe();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onCompleted() {
            /*
                r5 = this;
                java.lang.Object r4 = r5.guard     // Catch:{ all -> 0x0034 }
                monitor-enter(r4)     // Catch:{ all -> 0x0034 }
                boolean r3 = r5.done     // Catch:{ all -> 0x003b }
                if (r3 == 0) goto L_0x000e
                monitor-exit(r4)     // Catch:{ all -> 0x003b }
                dji.thirdparty.rx.subscriptions.CompositeSubscription r3 = r5.csub
                r3.unsubscribe()
            L_0x000d:
                return
            L_0x000e:
                r3 = 1
                r5.done = r3     // Catch:{ all -> 0x003b }
                java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ all -> 0x003b }
                java.util.List<dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable$SerializedSubject<T>> r3 = r5.chunks     // Catch:{ all -> 0x003b }
                r2.<init>(r3)     // Catch:{ all -> 0x003b }
                java.util.List<dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable$SerializedSubject<T>> r3 = r5.chunks     // Catch:{ all -> 0x003b }
                r3.clear()     // Catch:{ all -> 0x003b }
                monitor-exit(r4)     // Catch:{ all -> 0x003b }
                java.util.Iterator r1 = r2.iterator()     // Catch:{ all -> 0x0034 }
            L_0x0022:
                boolean r3 = r1.hasNext()     // Catch:{ all -> 0x0034 }
                if (r3 == 0) goto L_0x003e
                java.lang.Object r0 = r1.next()     // Catch:{ all -> 0x0034 }
                dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable$SerializedSubject r0 = (dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable.SerializedSubject) r0     // Catch:{ all -> 0x0034 }
                dji.thirdparty.rx.Observer<T> r3 = r0.consumer     // Catch:{ all -> 0x0034 }
                r3.onCompleted()     // Catch:{ all -> 0x0034 }
                goto L_0x0022
            L_0x0034:
                r3 = move-exception
                dji.thirdparty.rx.subscriptions.CompositeSubscription r4 = r5.csub
                r4.unsubscribe()
                throw r3
            L_0x003b:
                r3 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x003b }
                throw r3     // Catch:{ all -> 0x0034 }
            L_0x003e:
                dji.thirdparty.rx.Subscriber<? super dji.thirdparty.rx.Observable<T>> r3 = r5.child     // Catch:{ all -> 0x0034 }
                r3.onCompleted()     // Catch:{ all -> 0x0034 }
                dji.thirdparty.rx.subscriptions.CompositeSubscription r3 = r5.csub
                r3.unsubscribe()
                goto L_0x000d
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable.SourceSubscriber.onCompleted():void");
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void beginWindow(U r7) {
            /*
                r6 = this;
                dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable$SerializedSubject r2 = r6.createSerializedSubject()
                java.lang.Object r5 = r6.guard
                monitor-enter(r5)
                boolean r4 = r6.done     // Catch:{ all -> 0x0032 }
                if (r4 == 0) goto L_0x000d
                monitor-exit(r5)     // Catch:{ all -> 0x0032 }
            L_0x000c:
                return
            L_0x000d:
                java.util.List<dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable$SerializedSubject<T>> r4 = r6.chunks     // Catch:{ all -> 0x0032 }
                r4.add(r2)     // Catch:{ all -> 0x0032 }
                monitor-exit(r5)     // Catch:{ all -> 0x0032 }
                dji.thirdparty.rx.Subscriber<? super dji.thirdparty.rx.Observable<T>> r4 = r6.child
                dji.thirdparty.rx.Observable<T> r5 = r2.producer
                r4.onNext(r5)
                dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable r4 = dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable.this     // Catch:{ Throwable -> 0x0035 }
                dji.thirdparty.rx.functions.Func1<? super U, ? extends dji.thirdparty.rx.Observable<? extends V>> r4 = r4.windowClosingSelector     // Catch:{ Throwable -> 0x0035 }
                java.lang.Object r1 = r4.call(r7)     // Catch:{ Throwable -> 0x0035 }
                dji.thirdparty.rx.Observable r1 = (dji.thirdparty.rx.Observable) r1     // Catch:{ Throwable -> 0x0035 }
                dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable$SourceSubscriber$1 r3 = new dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable$SourceSubscriber$1
                r3.<init>(r2)
                dji.thirdparty.rx.subscriptions.CompositeSubscription r4 = r6.csub
                r4.add(r3)
                r1.unsafeSubscribe(r3)
                goto L_0x000c
            L_0x0032:
                r4 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x0032 }
                throw r4
            L_0x0035:
                r0 = move-exception
                r6.onError(r0)
                goto L_0x000c
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable.SourceSubscriber.beginWindow(java.lang.Object):void");
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0023, code lost:
            if (r2 == false) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0025, code lost:
            r6.consumer.onCompleted();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void endWindow(dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable.SerializedSubject<T> r6) {
            /*
                r5 = this;
                r2 = 0
                java.lang.Object r4 = r5.guard
                monitor-enter(r4)
                boolean r3 = r5.done     // Catch:{ all -> 0x002b }
                if (r3 == 0) goto L_0x000a
                monitor-exit(r4)     // Catch:{ all -> 0x002b }
            L_0x0009:
                return
            L_0x000a:
                java.util.List<dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable$SerializedSubject<T>> r3 = r5.chunks     // Catch:{ all -> 0x002b }
                java.util.Iterator r0 = r3.iterator()     // Catch:{ all -> 0x002b }
            L_0x0010:
                boolean r3 = r0.hasNext()     // Catch:{ all -> 0x002b }
                if (r3 == 0) goto L_0x0022
                java.lang.Object r1 = r0.next()     // Catch:{ all -> 0x002b }
                dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable$SerializedSubject r1 = (dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable.SerializedSubject) r1     // Catch:{ all -> 0x002b }
                if (r1 != r6) goto L_0x0010
                r2 = 1
                r0.remove()     // Catch:{ all -> 0x002b }
            L_0x0022:
                monitor-exit(r4)     // Catch:{ all -> 0x002b }
                if (r2 == 0) goto L_0x0009
                dji.thirdparty.rx.Observer<T> r3 = r6.consumer
                r3.onCompleted()
                goto L_0x0009
            L_0x002b:
                r3 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x002b }
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable.SourceSubscriber.endWindow(dji.thirdparty.rx.internal.operators.OperatorWindowWithStartEndObservable$SerializedSubject):void");
        }

        /* access modifiers changed from: package-private */
        public SerializedSubject<T> createSerializedSubject() {
            UnicastSubject<T> bus = UnicastSubject.create();
            return new SerializedSubject<>(bus, bus);
        }
    }
}
