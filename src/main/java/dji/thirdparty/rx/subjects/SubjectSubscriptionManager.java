package dji.thirdparty.rx.subjects;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Observer;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.functions.Action0;
import dji.thirdparty.rx.functions.Action1;
import dji.thirdparty.rx.functions.Actions;
import dji.thirdparty.rx.internal.operators.NotificationLite;
import dji.thirdparty.rx.subscriptions.Subscriptions;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

final class SubjectSubscriptionManager<T> extends AtomicReference<State<T>> implements Observable.OnSubscribe<T> {
    boolean active = true;
    volatile Object latest;
    public final NotificationLite<T> nl = NotificationLite.instance();
    Action1<SubjectObserver<T>> onAdded = Actions.empty();
    Action1<SubjectObserver<T>> onStart = Actions.empty();
    Action1<SubjectObserver<T>> onTerminated = Actions.empty();

    public /* bridge */ /* synthetic */ void call(Object x0) {
        call((Subscriber) ((Subscriber) x0));
    }

    public SubjectSubscriptionManager() {
        super(State.EMPTY);
    }

    public void call(Subscriber<? super T> child) {
        SubjectObserver<T> bo = new SubjectObserver<>(child);
        addUnsubscriber(child, bo);
        this.onStart.call(bo);
        if (!child.isUnsubscribed() && add(bo) && child.isUnsubscribed()) {
            remove(bo);
        }
    }

    /* access modifiers changed from: package-private */
    public void addUnsubscriber(Subscriber<? super T> child, final SubjectObserver<T> bo) {
        child.add(Subscriptions.create(new Action0() {
            /* class dji.thirdparty.rx.subjects.SubjectSubscriptionManager.AnonymousClass1 */

            public void call() {
                SubjectSubscriptionManager.this.remove(bo);
            }
        }));
    }

    /* access modifiers changed from: package-private */
    public void setLatest(Object value) {
        this.latest = value;
    }

    /* access modifiers changed from: package-private */
    public Object getLatest() {
        return this.latest;
    }

    /* access modifiers changed from: package-private */
    public SubjectObserver<T>[] observers() {
        return ((State) get()).observers;
    }

    /* access modifiers changed from: package-private */
    public boolean add(SubjectObserver<T> o) {
        State oldState;
        do {
            oldState = (State) get();
            if (oldState.terminated) {
                this.onTerminated.call(o);
                return false;
            }
        } while (!compareAndSet(oldState, oldState.add(o)));
        this.onAdded.call(o);
        return true;
    }

    /* access modifiers changed from: package-private */
    public void remove(SubjectObserver<T> o) {
        State oldState;
        State newState;
        do {
            oldState = (State) get();
            if (oldState.terminated || (newState = oldState.remove(o)) == oldState) {
                return;
            }
        } while (!compareAndSet(oldState, newState));
    }

    /* access modifiers changed from: package-private */
    public SubjectObserver<T>[] next(Object n) {
        setLatest(n);
        return ((State) get()).observers;
    }

    /* access modifiers changed from: package-private */
    public SubjectObserver<T>[] terminate(Object n) {
        setLatest(n);
        this.active = false;
        if (((State) get()).terminated) {
            return State.NO_OBSERVERS;
        }
        return ((State) getAndSet(State.TERMINATED)).observers;
    }

    protected static final class State<T> {
        static final State EMPTY = new State(false, NO_OBSERVERS);
        static final SubjectObserver[] NO_OBSERVERS = new SubjectObserver[0];
        static final State TERMINATED = new State(true, NO_OBSERVERS);
        final SubjectObserver[] observers;
        final boolean terminated;

        public State(boolean terminated2, SubjectObserver[] observers2) {
            this.terminated = terminated2;
            this.observers = observers2;
        }

        public State add(SubjectObserver o) {
            int n = this.observers.length;
            SubjectObserver[] b = new SubjectObserver[(n + 1)];
            System.arraycopy(this.observers, 0, b, 0, n);
            b[n] = o;
            return new State(this.terminated, b);
        }

        public State remove(SubjectObserver o) {
            int j;
            SubjectObserver[] a = this.observers;
            int n = a.length;
            if (n == 1 && a[0] == o) {
                return EMPTY;
            }
            if (n == 0) {
                return this;
            }
            SubjectObserver[] b = new SubjectObserver[(n - 1)];
            int i = 0;
            int j2 = 0;
            while (i < n) {
                SubjectObserver ai = a[i];
                if (ai == o) {
                    j = j2;
                } else if (j2 == n - 1) {
                    return this;
                } else {
                    j = j2 + 1;
                    b[j2] = ai;
                }
                i++;
                j2 = j;
            }
            if (j2 == 0) {
                return EMPTY;
            }
            if (j2 < n - 1) {
                SubjectObserver[] c = new SubjectObserver[j2];
                System.arraycopy(b, 0, c, 0, j2);
                b = c;
            }
            return new State<>(this.terminated, b);
        }
    }

    protected static final class SubjectObserver<T> implements Observer<T> {
        final Subscriber<? super T> actual;
        protected volatile boolean caughtUp;
        boolean emitting;
        boolean fastPath;
        boolean first = true;
        private volatile Object index;
        List<Object> queue;

        public SubjectObserver(Subscriber<? super T> actual2) {
            this.actual = actual2;
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }

        public void onCompleted() {
            this.actual.onCompleted();
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x001f, code lost:
            r1.fastPath = true;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitNext(java.lang.Object r2, dji.thirdparty.rx.internal.operators.NotificationLite<T> r3) {
            /*
                r1 = this;
                boolean r0 = r1.fastPath
                if (r0 != 0) goto L_0x0022
                monitor-enter(r1)
                r0 = 0
                r1.first = r0     // Catch:{ all -> 0x0028 }
                boolean r0 = r1.emitting     // Catch:{ all -> 0x0028 }
                if (r0 == 0) goto L_0x001e
                java.util.List<java.lang.Object> r0 = r1.queue     // Catch:{ all -> 0x0028 }
                if (r0 != 0) goto L_0x0017
                java.util.ArrayList r0 = new java.util.ArrayList     // Catch:{ all -> 0x0028 }
                r0.<init>()     // Catch:{ all -> 0x0028 }
                r1.queue = r0     // Catch:{ all -> 0x0028 }
            L_0x0017:
                java.util.List<java.lang.Object> r0 = r1.queue     // Catch:{ all -> 0x0028 }
                r0.add(r2)     // Catch:{ all -> 0x0028 }
                monitor-exit(r1)     // Catch:{ all -> 0x0028 }
            L_0x001d:
                return
            L_0x001e:
                monitor-exit(r1)     // Catch:{ all -> 0x0028 }
                r0 = 1
                r1.fastPath = r0
            L_0x0022:
                dji.thirdparty.rx.Subscriber<? super T> r0 = r1.actual
                r3.accept(r0, r2)
                goto L_0x001d
            L_0x0028:
                r0 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x0028 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.subjects.SubjectSubscriptionManager.SubjectObserver.emitNext(java.lang.Object, dji.thirdparty.rx.internal.operators.NotificationLite):void");
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0015, code lost:
            if (r3 == null) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0017, code lost:
            emitLoop(null, r3, r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitFirst(java.lang.Object r3, dji.thirdparty.rx.internal.operators.NotificationLite<T> r4) {
            /*
                r2 = this;
                r0 = 0
                monitor-enter(r2)
                boolean r1 = r2.first     // Catch:{ all -> 0x001c }
                if (r1 == 0) goto L_0x000a
                boolean r1 = r2.emitting     // Catch:{ all -> 0x001c }
                if (r1 == 0) goto L_0x000c
            L_0x000a:
                monitor-exit(r2)     // Catch:{ all -> 0x001c }
            L_0x000b:
                return
            L_0x000c:
                r1 = 0
                r2.first = r1     // Catch:{ all -> 0x001c }
                if (r3 == 0) goto L_0x0012
                r0 = 1
            L_0x0012:
                r2.emitting = r0     // Catch:{ all -> 0x001c }
                monitor-exit(r2)     // Catch:{ all -> 0x001c }
                if (r3 == 0) goto L_0x000b
                r0 = 0
                r2.emitLoop(r0, r3, r4)
                goto L_0x000b
            L_0x001c:
                r0 = move-exception
                monitor-exit(r2)     // Catch:{ all -> 0x001c }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.subjects.SubjectSubscriptionManager.SubjectObserver.emitFirst(java.lang.Object, dji.thirdparty.rx.internal.operators.NotificationLite):void");
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0032, code lost:
            if (1 != 0) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0034, code lost:
            monitor-enter(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
            r6.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0038, code lost:
            monitor-exit(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitLoop(java.util.List<java.lang.Object> r7, java.lang.Object r8, dji.thirdparty.rx.internal.operators.NotificationLite<T> r9) {
            /*
                r6 = this;
                r2 = 1
                r3 = 0
            L_0x0002:
                if (r7 == 0) goto L_0x001f
                java.util.Iterator r0 = r7.iterator()     // Catch:{ all -> 0x0016 }
            L_0x0008:
                boolean r4 = r0.hasNext()     // Catch:{ all -> 0x0016 }
                if (r4 == 0) goto L_0x001f
                java.lang.Object r1 = r0.next()     // Catch:{ all -> 0x0016 }
                r6.accept(r1, r9)     // Catch:{ all -> 0x0016 }
                goto L_0x0008
            L_0x0016:
                r4 = move-exception
                if (r3 != 0) goto L_0x001e
                monitor-enter(r6)
                r5 = 0
                r6.emitting = r5     // Catch:{ all -> 0x0042 }
                monitor-exit(r6)     // Catch:{ all -> 0x0042 }
            L_0x001e:
                throw r4
            L_0x001f:
                if (r2 == 0) goto L_0x0025
                r2 = 0
                r6.accept(r8, r9)     // Catch:{ all -> 0x0016 }
            L_0x0025:
                monitor-enter(r6)     // Catch:{ all -> 0x0016 }
                java.util.List<java.lang.Object> r7 = r6.queue     // Catch:{ all -> 0x003c }
                r4 = 0
                r6.queue = r4     // Catch:{ all -> 0x003c }
                if (r7 != 0) goto L_0x003a
                r4 = 0
                r6.emitting = r4     // Catch:{ all -> 0x003c }
                r3 = 1
                monitor-exit(r6)     // Catch:{ all -> 0x003c }
                if (r3 != 0) goto L_0x0039
                monitor-enter(r6)
                r4 = 0
                r6.emitting = r4     // Catch:{ all -> 0x003f }
                monitor-exit(r6)     // Catch:{ all -> 0x003f }
            L_0x0039:
                return
            L_0x003a:
                monitor-exit(r6)     // Catch:{ all -> 0x003c }
                goto L_0x0002
            L_0x003c:
                r4 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x003c }
                throw r4     // Catch:{ all -> 0x0016 }
            L_0x003f:
                r4 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x003f }
                throw r4
            L_0x0042:
                r4 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x0042 }
                throw r4
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.subjects.SubjectSubscriptionManager.SubjectObserver.emitLoop(java.util.List, java.lang.Object, dji.thirdparty.rx.internal.operators.NotificationLite):void");
        }

        /* access modifiers changed from: protected */
        public void accept(Object n, NotificationLite<T> nl) {
            if (n != null) {
                nl.accept(this.actual, n);
            }
        }

        /* access modifiers changed from: protected */
        public Observer<? super T> getActual() {
            return this.actual;
        }

        public <I> I index() {
            return this.index;
        }

        public void index(Object newIndex) {
            this.index = newIndex;
        }
    }
}
