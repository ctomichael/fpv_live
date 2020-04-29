package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.functions.Func0;
import dji.thirdparty.rx.observables.ConnectableObservable;
import dji.thirdparty.rx.subjects.Subject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public final class OperatorMulticast<T, R> extends ConnectableObservable<R> {
    final AtomicReference<Subject<? super T, ? extends R>> connectedSubject;
    final Object guard;
    Subscription guardedSubscription;
    final Observable<? extends T> source;
    final Func0<? extends Subject<? super T, ? extends R>> subjectFactory;
    Subscriber<T> subscription;
    final List<Subscriber<? super R>> waitingForConnect;

    public OperatorMulticast(Observable<? extends T> source2, Func0<? extends Subject<? super T, ? extends R>> subjectFactory2) {
        this(new Object(), new AtomicReference(), new ArrayList(), source2, subjectFactory2);
    }

    private OperatorMulticast(final Object guard2, final AtomicReference<Subject<? super T, ? extends R>> connectedSubject2, final List<Subscriber<? super R>> waitingForConnect2, Observable<? extends T> source2, Func0<? extends Subject<? super T, ? extends R>> subjectFactory2) {
        super(new Observable.OnSubscribe<R>() {
            /* class dji.thirdparty.rx.internal.operators.OperatorMulticast.AnonymousClass1 */

            public /* bridge */ /* synthetic */ void call(Object x0) {
                call((Subscriber) ((Subscriber) x0));
            }

            public void call(Subscriber<? super R> subscriber) {
                synchronized (guard2) {
                    if (connectedSubject2.get() == null) {
                        waitingForConnect2.add(subscriber);
                    } else {
                        ((Subject) connectedSubject2.get()).unsafeSubscribe(subscriber);
                    }
                }
            }
        });
        this.guard = guard2;
        this.connectedSubject = connectedSubject2;
        this.waitingForConnect = waitingForConnect2;
        this.source = source2;
        this.subjectFactory = subjectFactory2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x005e, code lost:
        r8.call(r7.guardedSubscription);
        r6 = r7.guard;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0065, code lost:
        monitor-enter(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        r3 = r7.subscription;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0068, code lost:
        monitor-exit(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0069, code lost:
        if (r3 == null) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x006b, code lost:
        r7.source.subscribe((dji.thirdparty.rx.Subscriber) r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void connect(dji.thirdparty.rx.functions.Action1<? super dji.thirdparty.rx.Subscription> r8) {
        /*
            r7 = this;
            java.lang.Object r6 = r7.guard
            monitor-enter(r6)
            dji.thirdparty.rx.Subscriber<T> r5 = r7.subscription     // Catch:{ all -> 0x0050 }
            if (r5 == 0) goto L_0x000e
            dji.thirdparty.rx.Subscription r5 = r7.guardedSubscription     // Catch:{ all -> 0x0050 }
            r8.call(r5)     // Catch:{ all -> 0x0050 }
            monitor-exit(r6)     // Catch:{ all -> 0x0050 }
        L_0x000d:
            return
        L_0x000e:
            dji.thirdparty.rx.functions.Func0<? extends dji.thirdparty.rx.subjects.Subject<? super T, ? extends R>> r5 = r7.subjectFactory     // Catch:{ all -> 0x0050 }
            java.lang.Object r4 = r5.call()     // Catch:{ all -> 0x0050 }
            dji.thirdparty.rx.subjects.Subject r4 = (dji.thirdparty.rx.subjects.Subject) r4     // Catch:{ all -> 0x0050 }
            dji.thirdparty.rx.Subscriber r5 = dji.thirdparty.rx.observers.Subscribers.from(r4)     // Catch:{ all -> 0x0050 }
            r7.subscription = r5     // Catch:{ all -> 0x0050 }
            java.util.concurrent.atomic.AtomicReference r0 = new java.util.concurrent.atomic.AtomicReference     // Catch:{ all -> 0x0050 }
            r0.<init>()     // Catch:{ all -> 0x0050 }
            dji.thirdparty.rx.internal.operators.OperatorMulticast$2 r5 = new dji.thirdparty.rx.internal.operators.OperatorMulticast$2     // Catch:{ all -> 0x0050 }
            r5.<init>(r0)     // Catch:{ all -> 0x0050 }
            dji.thirdparty.rx.Subscription r5 = dji.thirdparty.rx.subscriptions.Subscriptions.create(r5)     // Catch:{ all -> 0x0050 }
            r0.set(r5)     // Catch:{ all -> 0x0050 }
            java.lang.Object r5 = r0.get()     // Catch:{ all -> 0x0050 }
            dji.thirdparty.rx.Subscription r5 = (dji.thirdparty.rx.Subscription) r5     // Catch:{ all -> 0x0050 }
            r7.guardedSubscription = r5     // Catch:{ all -> 0x0050 }
            java.util.List<dji.thirdparty.rx.Subscriber<? super R>> r5 = r7.waitingForConnect     // Catch:{ all -> 0x0050 }
            java.util.Iterator r1 = r5.iterator()     // Catch:{ all -> 0x0050 }
        L_0x003b:
            boolean r5 = r1.hasNext()     // Catch:{ all -> 0x0050 }
            if (r5 == 0) goto L_0x0053
            java.lang.Object r2 = r1.next()     // Catch:{ all -> 0x0050 }
            dji.thirdparty.rx.Subscriber r2 = (dji.thirdparty.rx.Subscriber) r2     // Catch:{ all -> 0x0050 }
            dji.thirdparty.rx.internal.operators.OperatorMulticast$3 r5 = new dji.thirdparty.rx.internal.operators.OperatorMulticast$3     // Catch:{ all -> 0x0050 }
            r5.<init>(r2, r2)     // Catch:{ all -> 0x0050 }
            r4.unsafeSubscribe(r5)     // Catch:{ all -> 0x0050 }
            goto L_0x003b
        L_0x0050:
            r5 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x0050 }
            throw r5
        L_0x0053:
            java.util.List<dji.thirdparty.rx.Subscriber<? super R>> r5 = r7.waitingForConnect     // Catch:{ all -> 0x0050 }
            r5.clear()     // Catch:{ all -> 0x0050 }
            java.util.concurrent.atomic.AtomicReference<dji.thirdparty.rx.subjects.Subject<? super T, ? extends R>> r5 = r7.connectedSubject     // Catch:{ all -> 0x0050 }
            r5.set(r4)     // Catch:{ all -> 0x0050 }
            monitor-exit(r6)     // Catch:{ all -> 0x0050 }
            dji.thirdparty.rx.Subscription r5 = r7.guardedSubscription
            r8.call(r5)
            java.lang.Object r6 = r7.guard
            monitor-enter(r6)
            dji.thirdparty.rx.Subscriber<T> r3 = r7.subscription     // Catch:{ all -> 0x0071 }
            monitor-exit(r6)     // Catch:{ all -> 0x0071 }
            if (r3 == 0) goto L_0x000d
            dji.thirdparty.rx.Observable<? extends T> r5 = r7.source
            r5.subscribe(r3)
            goto L_0x000d
        L_0x0071:
            r5 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x0071 }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorMulticast.connect(dji.thirdparty.rx.functions.Action1):void");
    }
}
