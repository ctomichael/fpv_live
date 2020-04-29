package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Observer;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.observers.SerializedSubscriber;
import java.util.Collections;
import java.util.List;
import kotlin.jvm.internal.LongCompanionObject;

public final class OperatorWindowWithObservable<T, U> implements Observable.Operator<Observable<T>, T> {
    static final Object NEXT_SUBJECT = new Object();
    static final NotificationLite<Object> nl = NotificationLite.instance();
    final Observable<U> other;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    public OperatorWindowWithObservable(Observable<U> other2) {
        this.other = other2;
    }

    public Subscriber<? super T> call(Subscriber<? super Observable<T>> child) {
        SourceSubscriber<T> sub = new SourceSubscriber<>(child);
        BoundarySubscriber<T, U> bs = new BoundarySubscriber<>(child, sub);
        child.add(sub);
        child.add(bs);
        sub.replaceWindow();
        this.other.unsafeSubscribe(bs);
        return sub;
    }

    static final class SourceSubscriber<T> extends Subscriber<T> {
        final Subscriber<? super Observable<T>> child;
        Observer<T> consumer;
        boolean emitting;
        final Object guard = new Object();
        Observable<T> producer;
        List<Object> queue;

        public SourceSubscriber(Subscriber<? super Observable<T>> child2) {
            this.child = new SerializedSubscriber(child2);
        }

        public void onStart() {
            request(LongCompanionObject.MAX_VALUE);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0022, code lost:
            r1 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
            drain(r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0027, code lost:
            if (r1 == false) goto L_0x002d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0029, code lost:
            r1 = false;
            emitValue(r7);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x002d, code lost:
            r4 = r6.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x002f, code lost:
            monitor-enter(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
            r0 = r6.queue;
            r6.queue = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0035, code lost:
            if (r0 != null) goto L_0x004c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0037, code lost:
            r6.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x003b, code lost:
            monitor-exit(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x003c, code lost:
            if (1 != 0) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x003e, code lost:
            r4 = r6.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0040, code lost:
            monitor-enter(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
            r6.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0044, code lost:
            monitor-exit(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
            monitor-exit(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:0x0053, code lost:
            if (r6.child.isUnsubscribed() == false) goto L_0x0024;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x0055, code lost:
            if (0 != 0) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x0057, code lost:
            r4 = r6.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:46:0x0059, code lost:
            monitor-enter(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:?, code lost:
            r6.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:50:0x005d, code lost:
            monitor-exit(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:60:0x0065, code lost:
            r3 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:61:0x0066, code lost:
            if (0 == 0) goto L_0x0068;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:63:0x006a, code lost:
            monitor-enter(r6.guard);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:66:?, code lost:
            r6.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:68:0x006f, code lost:
            throw r3;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:85:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:86:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:87:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:88:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onNext(T r7) {
            /*
                r6 = this;
                java.lang.Object r4 = r6.guard
                monitor-enter(r4)
                boolean r3 = r6.emitting     // Catch:{ all -> 0x0049 }
                if (r3 == 0) goto L_0x0019
                java.util.List<java.lang.Object> r3 = r6.queue     // Catch:{ all -> 0x0049 }
                if (r3 != 0) goto L_0x0012
                java.util.ArrayList r3 = new java.util.ArrayList     // Catch:{ all -> 0x0049 }
                r3.<init>()     // Catch:{ all -> 0x0049 }
                r6.queue = r3     // Catch:{ all -> 0x0049 }
            L_0x0012:
                java.util.List<java.lang.Object> r3 = r6.queue     // Catch:{ all -> 0x0049 }
                r3.add(r7)     // Catch:{ all -> 0x0049 }
                monitor-exit(r4)     // Catch:{ all -> 0x0049 }
            L_0x0018:
                return
            L_0x0019:
                java.util.List<java.lang.Object> r0 = r6.queue     // Catch:{ all -> 0x0049 }
                r3 = 0
                r6.queue = r3     // Catch:{ all -> 0x0049 }
                r3 = 1
                r6.emitting = r3     // Catch:{ all -> 0x0049 }
                monitor-exit(r4)     // Catch:{ all -> 0x0049 }
                r1 = 1
                r2 = 0
            L_0x0024:
                r6.drain(r0)     // Catch:{ all -> 0x0065 }
                if (r1 == 0) goto L_0x002d
                r1 = 0
                r6.emitValue(r7)     // Catch:{ all -> 0x0065 }
            L_0x002d:
                java.lang.Object r4 = r6.guard     // Catch:{ all -> 0x0065 }
                monitor-enter(r4)     // Catch:{ all -> 0x0065 }
                java.util.List<java.lang.Object> r0 = r6.queue     // Catch:{ all -> 0x0062 }
                r3 = 0
                r6.queue = r3     // Catch:{ all -> 0x0062 }
                if (r0 != 0) goto L_0x004c
                r3 = 0
                r6.emitting = r3     // Catch:{ all -> 0x0062 }
                r2 = 1
                monitor-exit(r4)     // Catch:{ all -> 0x0062 }
                if (r2 != 0) goto L_0x0018
                java.lang.Object r4 = r6.guard
                monitor-enter(r4)
                r3 = 0
                r6.emitting = r3     // Catch:{ all -> 0x0046 }
                monitor-exit(r4)     // Catch:{ all -> 0x0046 }
                goto L_0x0018
            L_0x0046:
                r3 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x0046 }
                throw r3
            L_0x0049:
                r3 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x0049 }
                throw r3
            L_0x004c:
                monitor-exit(r4)     // Catch:{ all -> 0x0062 }
                dji.thirdparty.rx.Subscriber<? super dji.thirdparty.rx.Observable<T>> r3 = r6.child     // Catch:{ all -> 0x0065 }
                boolean r3 = r3.isUnsubscribed()     // Catch:{ all -> 0x0065 }
                if (r3 == 0) goto L_0x0024
                if (r2 != 0) goto L_0x0018
                java.lang.Object r4 = r6.guard
                monitor-enter(r4)
                r3 = 0
                r6.emitting = r3     // Catch:{ all -> 0x005f }
                monitor-exit(r4)     // Catch:{ all -> 0x005f }
                goto L_0x0018
            L_0x005f:
                r3 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x005f }
                throw r3
            L_0x0062:
                r3 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x0062 }
                throw r3     // Catch:{ all -> 0x0065 }
            L_0x0065:
                r3 = move-exception
                if (r2 != 0) goto L_0x006f
                java.lang.Object r4 = r6.guard
                monitor-enter(r4)
                r5 = 0
                r6.emitting = r5     // Catch:{ all -> 0x0070 }
                monitor-exit(r4)     // Catch:{ all -> 0x0070 }
            L_0x006f:
                throw r3
            L_0x0070:
                r3 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x0070 }
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorWindowWithObservable.SourceSubscriber.onNext(java.lang.Object):void");
        }

        /* access modifiers changed from: package-private */
        public void drain(List<Object> queue2) {
            if (queue2 != null) {
                for (T o : queue2) {
                    if (o == OperatorWindowWithObservable.NEXT_SUBJECT) {
                        replaceSubject();
                    } else if (OperatorWindowWithObservable.nl.isError(o)) {
                        error(OperatorWindowWithObservable.nl.getError(o));
                        return;
                    } else if (OperatorWindowWithObservable.nl.isCompleted(o)) {
                        complete();
                        return;
                    } else {
                        emitValue(o);
                    }
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void replaceSubject() {
            Observer<T> s = this.consumer;
            if (s != null) {
                s.onCompleted();
            }
            createNewWindow();
            this.child.onNext(this.producer);
        }

        /* access modifiers changed from: package-private */
        public void createNewWindow() {
            UnicastSubject<T> bus = UnicastSubject.create();
            this.consumer = bus;
            this.producer = bus;
        }

        /* access modifiers changed from: package-private */
        public void emitValue(T t) {
            Observer<T> s = this.consumer;
            if (s != null) {
                s.onNext(t);
            }
        }

        public void onError(Throwable e) {
            synchronized (this.guard) {
                if (this.emitting) {
                    this.queue = Collections.singletonList(OperatorWindowWithObservable.nl.error(e));
                    return;
                }
                this.queue = null;
                this.emitting = true;
                error(e);
            }
        }

        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onCompleted() {
            /*
                r5 = this;
                java.lang.Object r3 = r5.guard
                monitor-enter(r3)
                boolean r2 = r5.emitting     // Catch:{ all -> 0x002f }
                if (r2 == 0) goto L_0x001f
                java.util.List<java.lang.Object> r2 = r5.queue     // Catch:{ all -> 0x002f }
                if (r2 != 0) goto L_0x0012
                java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ all -> 0x002f }
                r2.<init>()     // Catch:{ all -> 0x002f }
                r5.queue = r2     // Catch:{ all -> 0x002f }
            L_0x0012:
                java.util.List<java.lang.Object> r2 = r5.queue     // Catch:{ all -> 0x002f }
                dji.thirdparty.rx.internal.operators.NotificationLite<java.lang.Object> r4 = dji.thirdparty.rx.internal.operators.OperatorWindowWithObservable.nl     // Catch:{ all -> 0x002f }
                java.lang.Object r4 = r4.completed()     // Catch:{ all -> 0x002f }
                r2.add(r4)     // Catch:{ all -> 0x002f }
                monitor-exit(r3)     // Catch:{ all -> 0x002f }
            L_0x001e:
                return
            L_0x001f:
                java.util.List<java.lang.Object> r1 = r5.queue     // Catch:{ all -> 0x002f }
                r2 = 0
                r5.queue = r2     // Catch:{ all -> 0x002f }
                r2 = 1
                r5.emitting = r2     // Catch:{ all -> 0x002f }
                monitor-exit(r3)     // Catch:{ all -> 0x002f }
                r5.drain(r1)     // Catch:{ Throwable -> 0x0032 }
                r5.complete()
                goto L_0x001e
            L_0x002f:
                r2 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x002f }
                throw r2
            L_0x0032:
                r0 = move-exception
                r5.error(r0)
                goto L_0x001e
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorWindowWithObservable.SourceSubscriber.onCompleted():void");
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0024, code lost:
            r1 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
            drain(r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0029, code lost:
            if (r1 == false) goto L_0x002f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x002b, code lost:
            r1 = false;
            replaceSubject();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x002f, code lost:
            r4 = r6.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0031, code lost:
            monitor-enter(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
            r0 = r6.queue;
            r6.queue = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0037, code lost:
            if (r0 != null) goto L_0x004e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0039, code lost:
            r6.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x003d, code lost:
            monitor-exit(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x003e, code lost:
            if (1 != 0) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0040, code lost:
            r4 = r6.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0042, code lost:
            monitor-enter(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
            r6.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0046, code lost:
            monitor-exit(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
            monitor-exit(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:0x0055, code lost:
            if (r6.child.isUnsubscribed() == false) goto L_0x0026;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x0057, code lost:
            if (0 != 0) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x0059, code lost:
            r4 = r6.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:46:0x005b, code lost:
            monitor-enter(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:?, code lost:
            r6.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:50:0x005f, code lost:
            monitor-exit(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:60:0x0067, code lost:
            r3 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:61:0x0068, code lost:
            if (0 == 0) goto L_0x006a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:63:0x006c, code lost:
            monitor-enter(r6.guard);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:66:?, code lost:
            r6.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:68:0x0071, code lost:
            throw r3;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:85:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:86:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:87:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:88:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void replaceWindow() {
            /*
                r6 = this;
                java.lang.Object r4 = r6.guard
                monitor-enter(r4)
                boolean r3 = r6.emitting     // Catch:{ all -> 0x004b }
                if (r3 == 0) goto L_0x001b
                java.util.List<java.lang.Object> r3 = r6.queue     // Catch:{ all -> 0x004b }
                if (r3 != 0) goto L_0x0012
                java.util.ArrayList r3 = new java.util.ArrayList     // Catch:{ all -> 0x004b }
                r3.<init>()     // Catch:{ all -> 0x004b }
                r6.queue = r3     // Catch:{ all -> 0x004b }
            L_0x0012:
                java.util.List<java.lang.Object> r3 = r6.queue     // Catch:{ all -> 0x004b }
                java.lang.Object r5 = dji.thirdparty.rx.internal.operators.OperatorWindowWithObservable.NEXT_SUBJECT     // Catch:{ all -> 0x004b }
                r3.add(r5)     // Catch:{ all -> 0x004b }
                monitor-exit(r4)     // Catch:{ all -> 0x004b }
            L_0x001a:
                return
            L_0x001b:
                java.util.List<java.lang.Object> r0 = r6.queue     // Catch:{ all -> 0x004b }
                r3 = 0
                r6.queue = r3     // Catch:{ all -> 0x004b }
                r3 = 1
                r6.emitting = r3     // Catch:{ all -> 0x004b }
                monitor-exit(r4)     // Catch:{ all -> 0x004b }
                r1 = 1
                r2 = 0
            L_0x0026:
                r6.drain(r0)     // Catch:{ all -> 0x0067 }
                if (r1 == 0) goto L_0x002f
                r1 = 0
                r6.replaceSubject()     // Catch:{ all -> 0x0067 }
            L_0x002f:
                java.lang.Object r4 = r6.guard     // Catch:{ all -> 0x0067 }
                monitor-enter(r4)     // Catch:{ all -> 0x0067 }
                java.util.List<java.lang.Object> r0 = r6.queue     // Catch:{ all -> 0x0064 }
                r3 = 0
                r6.queue = r3     // Catch:{ all -> 0x0064 }
                if (r0 != 0) goto L_0x004e
                r3 = 0
                r6.emitting = r3     // Catch:{ all -> 0x0064 }
                r2 = 1
                monitor-exit(r4)     // Catch:{ all -> 0x0064 }
                if (r2 != 0) goto L_0x001a
                java.lang.Object r4 = r6.guard
                monitor-enter(r4)
                r3 = 0
                r6.emitting = r3     // Catch:{ all -> 0x0048 }
                monitor-exit(r4)     // Catch:{ all -> 0x0048 }
                goto L_0x001a
            L_0x0048:
                r3 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x0048 }
                throw r3
            L_0x004b:
                r3 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x004b }
                throw r3
            L_0x004e:
                monitor-exit(r4)     // Catch:{ all -> 0x0064 }
                dji.thirdparty.rx.Subscriber<? super dji.thirdparty.rx.Observable<T>> r3 = r6.child     // Catch:{ all -> 0x0067 }
                boolean r3 = r3.isUnsubscribed()     // Catch:{ all -> 0x0067 }
                if (r3 == 0) goto L_0x0026
                if (r2 != 0) goto L_0x001a
                java.lang.Object r4 = r6.guard
                monitor-enter(r4)
                r3 = 0
                r6.emitting = r3     // Catch:{ all -> 0x0061 }
                monitor-exit(r4)     // Catch:{ all -> 0x0061 }
                goto L_0x001a
            L_0x0061:
                r3 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x0061 }
                throw r3
            L_0x0064:
                r3 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x0064 }
                throw r3     // Catch:{ all -> 0x0067 }
            L_0x0067:
                r3 = move-exception
                if (r2 != 0) goto L_0x0071
                java.lang.Object r4 = r6.guard
                monitor-enter(r4)
                r5 = 0
                r6.emitting = r5     // Catch:{ all -> 0x0072 }
                monitor-exit(r4)     // Catch:{ all -> 0x0072 }
            L_0x0071:
                throw r3
            L_0x0072:
                r3 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x0072 }
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorWindowWithObservable.SourceSubscriber.replaceWindow():void");
        }

        /* access modifiers changed from: package-private */
        public void complete() {
            Observer<T> s = this.consumer;
            this.consumer = null;
            this.producer = null;
            if (s != null) {
                s.onCompleted();
            }
            this.child.onCompleted();
            unsubscribe();
        }

        /* access modifiers changed from: package-private */
        public void error(Throwable e) {
            Observer<T> s = this.consumer;
            this.consumer = null;
            this.producer = null;
            if (s != null) {
                s.onError(e);
            }
            this.child.onError(e);
            unsubscribe();
        }
    }

    static final class BoundarySubscriber<T, U> extends Subscriber<U> {
        final SourceSubscriber<T> sub;

        public BoundarySubscriber(Subscriber<?> subscriber, SourceSubscriber<T> sub2) {
            this.sub = sub2;
        }

        public void onStart() {
            request(LongCompanionObject.MAX_VALUE);
        }

        public void onNext(U u) {
            this.sub.replaceWindow();
        }

        public void onError(Throwable e) {
            this.sub.onError(e);
        }

        public void onCompleted() {
            this.sub.onCompleted();
        }
    }
}
