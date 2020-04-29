package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Observer;
import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.functions.Action0;
import dji.thirdparty.rx.internal.util.atomic.SpscLinkedAtomicQueue;
import dji.thirdparty.rx.internal.util.atomic.SpscUnboundedAtomicArrayQueue;
import dji.thirdparty.rx.internal.util.unsafe.SpscLinkedQueue;
import dji.thirdparty.rx.internal.util.unsafe.SpscUnboundedArrayQueue;
import dji.thirdparty.rx.internal.util.unsafe.UnsafeAccess;
import dji.thirdparty.rx.subjects.Subject;
import dji.thirdparty.rx.subscriptions.Subscriptions;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public final class UnicastSubject<T> extends Subject<T, T> {
    final State<T> state;

    public static <T> UnicastSubject<T> create() {
        return create(16);
    }

    public static <T> UnicastSubject<T> create(int capacityHint) {
        return new UnicastSubject<>(new State<>(capacityHint, null));
    }

    public static <T> UnicastSubject<T> create(int capacityHint, Action0 onTerminated) {
        return new UnicastSubject<>(new State<>(capacityHint, onTerminated));
    }

    private UnicastSubject(State<T> state2) {
        super(state2);
        this.state = state2;
    }

    public void onNext(T t) {
        this.state.onNext(t);
    }

    public void onError(Throwable e) {
        this.state.onError(e);
    }

    public void onCompleted() {
        this.state.onCompleted();
    }

    public boolean hasObservers() {
        return this.state.subscriber.get() != null;
    }

    static final class State<T> extends AtomicLong implements Producer, Observer<T>, Action0, Observable.OnSubscribe<T> {
        private static final long serialVersionUID = -9044104859202255786L;
        volatile boolean caughtUp;
        volatile boolean done;
        boolean emitting;
        Throwable error;
        boolean missed;
        final NotificationLite<T> nl = NotificationLite.instance();
        final Queue<Object> queue;
        final AtomicReference<Subscriber<? super T>> subscriber = new AtomicReference<>();
        final AtomicReference<Action0> terminateOnce;

        public /* bridge */ /* synthetic */ void call(Object x0) {
            call((Subscriber) ((Subscriber) x0));
        }

        public State(int capacityHint, Action0 onTerminated) {
            Queue<Object> q;
            this.terminateOnce = onTerminated != null ? new AtomicReference<>(onTerminated) : null;
            if (capacityHint > 1) {
                q = UnsafeAccess.isUnsafeAvailable() ? new SpscUnboundedArrayQueue<>(capacityHint) : new SpscUnboundedAtomicArrayQueue<>(capacityHint);
            } else {
                q = UnsafeAccess.isUnsafeAvailable() ? new SpscLinkedQueue<>() : new SpscLinkedAtomicQueue<>();
            }
            this.queue = q;
        }

        public void onNext(T t) {
            if (!this.done) {
                if (!this.caughtUp) {
                    boolean stillReplay = false;
                    synchronized (this) {
                        if (!this.caughtUp) {
                            this.queue.offer(this.nl.next(t));
                            stillReplay = true;
                        }
                    }
                    if (stillReplay) {
                        replay();
                        return;
                    }
                }
                Subscriber<? super T> s = this.subscriber.get();
                try {
                    s.onNext(t);
                } catch (Throwable ex) {
                    Exceptions.throwOrReport(ex, s, t);
                }
            }
        }

        public void onError(Throwable e) {
            boolean stillReplay;
            if (!this.done) {
                doTerminate();
                this.error = e;
                this.done = true;
                if (!this.caughtUp) {
                    synchronized (this) {
                        stillReplay = !this.caughtUp;
                    }
                    if (stillReplay) {
                        replay();
                        return;
                    }
                }
                this.subscriber.get().onError(e);
            }
        }

        public void onCompleted() {
            boolean stillReplay;
            if (!this.done) {
                doTerminate();
                this.done = true;
                if (!this.caughtUp) {
                    synchronized (this) {
                        stillReplay = !this.caughtUp;
                    }
                    if (stillReplay) {
                        replay();
                        return;
                    }
                }
                this.subscriber.get().onCompleted();
            }
        }

        public void request(long n) {
            if (n < 0) {
                throw new IllegalArgumentException("n >= 0 required");
            } else if (n > 0) {
                BackpressureUtils.getAndAddRequest(this, n);
                replay();
            } else if (this.done) {
                replay();
            }
        }

        public void call(Subscriber<? super T> subscriber2) {
            if (this.subscriber.compareAndSet(null, subscriber2)) {
                subscriber2.add(Subscriptions.create(this));
                subscriber2.setProducer(this);
                return;
            }
            subscriber2.onError(new IllegalStateException("Only a single subscriber is allowed"));
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0019, code lost:
            if (r8 == null) goto L_0x005a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0025, code lost:
            if (checkTerminated(r14.done, r5.isEmpty(), r8) != false) goto L_0x0009;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0027, code lost:
            r6 = get();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0032, code lost:
            if (r6 != kotlin.jvm.internal.LongCompanionObject.MAX_VALUE) goto L_0x0075;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0034, code lost:
            r9 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0035, code lost:
            r2 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x003b, code lost:
            if (r6 == 0) goto L_0x004e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x003d, code lost:
            r0 = r14.done;
            r10 = r5.poll();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0043, code lost:
            if (r10 != null) goto L_0x0077;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0045, code lost:
            r1 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x004a, code lost:
            if (checkTerminated(r0, r1, r8) != false) goto L_0x0009;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x004c, code lost:
            if (r1 == false) goto L_0x0079;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x004e, code lost:
            if (r9 != false) goto L_0x005a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x0054, code lost:
            if (r2 == 0) goto L_0x005a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x0056, code lost:
            addAndGet(-r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x005a, code lost:
            monitor-enter(r14);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x005d, code lost:
            if (r14.missed != false) goto L_0x0099;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x005f, code lost:
            if (r9 == false) goto L_0x006a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x0065, code lost:
            if (r5.isEmpty() == false) goto L_0x006a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x0067, code lost:
            r14.caughtUp = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x006a, code lost:
            r14.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x006d, code lost:
            monitor-exit(r14);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:0x0075, code lost:
            r9 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x0077, code lost:
            r1 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x0079, code lost:
            r11 = r14.nl.getValue(r10);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:51:?, code lost:
            r8.onNext(r11);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:52:0x0082, code lost:
            r6 = r6 - 1;
            r2 = r2 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:0x0089, code lost:
            r4 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x008a, code lost:
            r5.clear();
            dji.thirdparty.rx.exceptions.Exceptions.throwIfFatal(r4);
            r8.onError(dji.thirdparty.rx.exceptions.OnErrorThrowable.addValueAsLastCause(r4, r11));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:57:?, code lost:
            r14.missed = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:58:0x009c, code lost:
            monitor-exit(r14);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:68:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:69:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:70:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:71:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x000e, code lost:
            r5 = r14.queue;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0010, code lost:
            r8 = r14.subscriber.get();
            r9 = false;
         */
        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void replay() {
            /*
                r14 = this;
                monitor-enter(r14)
                boolean r12 = r14.emitting     // Catch:{ all -> 0x0072 }
                if (r12 == 0) goto L_0x000a
                r12 = 1
                r14.missed = r12     // Catch:{ all -> 0x0072 }
                monitor-exit(r14)     // Catch:{ all -> 0x0072 }
            L_0x0009:
                return
            L_0x000a:
                r12 = 1
                r14.emitting = r12     // Catch:{ all -> 0x0072 }
                monitor-exit(r14)     // Catch:{ all -> 0x0072 }
                java.util.Queue<java.lang.Object> r5 = r14.queue
            L_0x0010:
                java.util.concurrent.atomic.AtomicReference<dji.thirdparty.rx.Subscriber<? super T>> r12 = r14.subscriber
                java.lang.Object r8 = r12.get()
                dji.thirdparty.rx.Subscriber r8 = (dji.thirdparty.rx.Subscriber) r8
                r9 = 0
                if (r8 == 0) goto L_0x005a
                boolean r0 = r14.done
                boolean r1 = r5.isEmpty()
                boolean r12 = r14.checkTerminated(r0, r1, r8)
                if (r12 != 0) goto L_0x0009
                long r6 = r14.get()
                r12 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r12 = (r6 > r12 ? 1 : (r6 == r12 ? 0 : -1))
                if (r12 != 0) goto L_0x0075
                r9 = 1
            L_0x0035:
                r2 = 0
            L_0x0037:
                r12 = 0
                int r12 = (r6 > r12 ? 1 : (r6 == r12 ? 0 : -1))
                if (r12 == 0) goto L_0x004e
                boolean r0 = r14.done
                java.lang.Object r10 = r5.poll()
                if (r10 != 0) goto L_0x0077
                r1 = 1
            L_0x0046:
                boolean r12 = r14.checkTerminated(r0, r1, r8)
                if (r12 != 0) goto L_0x0009
                if (r1 == 0) goto L_0x0079
            L_0x004e:
                if (r9 != 0) goto L_0x005a
                r12 = 0
                int r12 = (r2 > r12 ? 1 : (r2 == r12 ? 0 : -1))
                if (r12 == 0) goto L_0x005a
                long r12 = -r2
                r14.addAndGet(r12)
            L_0x005a:
                monitor-enter(r14)
                boolean r12 = r14.missed     // Catch:{ all -> 0x006f }
                if (r12 != 0) goto L_0x0099
                if (r9 == 0) goto L_0x006a
                boolean r12 = r5.isEmpty()     // Catch:{ all -> 0x006f }
                if (r12 == 0) goto L_0x006a
                r12 = 1
                r14.caughtUp = r12     // Catch:{ all -> 0x006f }
            L_0x006a:
                r12 = 0
                r14.emitting = r12     // Catch:{ all -> 0x006f }
                monitor-exit(r14)     // Catch:{ all -> 0x006f }
                goto L_0x0009
            L_0x006f:
                r12 = move-exception
                monitor-exit(r14)     // Catch:{ all -> 0x006f }
                throw r12
            L_0x0072:
                r12 = move-exception
                monitor-exit(r14)     // Catch:{ all -> 0x0072 }
                throw r12
            L_0x0075:
                r9 = 0
                goto L_0x0035
            L_0x0077:
                r1 = 0
                goto L_0x0046
            L_0x0079:
                dji.thirdparty.rx.internal.operators.NotificationLite<T> r12 = r14.nl
                java.lang.Object r11 = r12.getValue(r10)
                r8.onNext(r11)     // Catch:{ Throwable -> 0x0089 }
                r12 = 1
                long r6 = r6 - r12
                r12 = 1
                long r2 = r2 + r12
                goto L_0x0037
            L_0x0089:
                r4 = move-exception
                r5.clear()
                dji.thirdparty.rx.exceptions.Exceptions.throwIfFatal(r4)
                java.lang.Throwable r12 = dji.thirdparty.rx.exceptions.OnErrorThrowable.addValueAsLastCause(r4, r11)
                r8.onError(r12)
                goto L_0x0009
            L_0x0099:
                r12 = 0
                r14.missed = r12     // Catch:{ all -> 0x006f }
                monitor-exit(r14)     // Catch:{ all -> 0x006f }
                goto L_0x0010
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.UnicastSubject.State.replay():void");
        }

        public void call() {
            doTerminate();
            this.done = true;
            synchronized (this) {
                if (!this.emitting) {
                    this.emitting = true;
                    this.queue.clear();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public boolean checkTerminated(boolean done2, boolean empty, Subscriber<? super T> s) {
            if (s.isUnsubscribed()) {
                this.queue.clear();
                return true;
            }
            if (done2) {
                Throwable e = this.error;
                if (e != null) {
                    this.queue.clear();
                    s.onError(e);
                    return true;
                } else if (empty) {
                    s.onCompleted();
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: package-private */
        public void doTerminate() {
            Action0 a;
            AtomicReference<Action0> ref = this.terminateOnce;
            if (ref != null && (a = ref.get()) != null && ref.compareAndSet(a, null)) {
                a.call();
            }
        }
    }
}
