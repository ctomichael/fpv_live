package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Observer;
import dji.thirdparty.rx.Scheduler;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.functions.Action0;
import dji.thirdparty.rx.observers.SerializedObserver;
import dji.thirdparty.rx.observers.SerializedSubscriber;
import dji.thirdparty.rx.subscriptions.Subscriptions;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.LongCompanionObject;

public final class OperatorWindowWithTime<T> implements Observable.Operator<Observable<T>, T> {
    static final Object NEXT_SUBJECT = new Object();
    static final NotificationLite<Object> nl = NotificationLite.instance();
    final Scheduler scheduler;
    final int size;
    final long timeshift;
    final long timespan;
    final TimeUnit unit;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    public OperatorWindowWithTime(long timespan2, long timeshift2, TimeUnit unit2, int size2, Scheduler scheduler2) {
        this.timespan = timespan2;
        this.timeshift = timeshift2;
        this.unit = unit2;
        this.size = size2;
        this.scheduler = scheduler2;
    }

    public Subscriber<? super T> call(Subscriber<? super Observable<T>> child) {
        Scheduler.Worker worker = this.scheduler.createWorker();
        if (this.timespan == this.timeshift) {
            OperatorWindowWithTime<T>.ExactSubscriber s = new ExactSubscriber(child, worker);
            s.add(worker);
            s.scheduleExact();
            return s;
        }
        OperatorWindowWithTime<T>.InexactSubscriber s2 = new InexactSubscriber(child, worker);
        s2.add(worker);
        s2.startNewChunk();
        s2.scheduleChunk();
        return s2;
    }

    static final class State<T> {
        static final State<Object> EMPTY = new State<>(null, null, 0);
        final Observer<T> consumer;
        final int count;
        final Observable<T> producer;

        public State(Observer<T> consumer2, Observable<T> producer2, int count2) {
            this.consumer = consumer2;
            this.producer = producer2;
            this.count = count2;
        }

        public State<T> next() {
            return new State<>(this.consumer, this.producer, this.count + 1);
        }

        public State<T> create(Observer<T> consumer2, Observable<T> producer2) {
            return new State<>(consumer2, producer2, 0);
        }

        public State<T> clear() {
            return empty();
        }

        public static <T> State<T> empty() {
            return EMPTY;
        }
    }

    final class ExactSubscriber extends Subscriber<T> {
        final Subscriber<? super Observable<T>> child;
        boolean emitting;
        final Object guard = new Object();
        List<Object> queue;
        volatile State<T> state = State.empty();
        final Scheduler.Worker worker;

        public ExactSubscriber(Subscriber<? super Observable<T>> child2, Scheduler.Worker worker2) {
            this.child = new SerializedSubscriber(child2);
            this.worker = worker2;
            child2.add(Subscriptions.create(new Action0(OperatorWindowWithTime.this) {
                /* class dji.thirdparty.rx.internal.operators.OperatorWindowWithTime.ExactSubscriber.AnonymousClass1 */

                public void call() {
                    if (ExactSubscriber.this.state.consumer == null) {
                        ExactSubscriber.this.unsubscribe();
                    }
                }
            }));
        }

        public void onStart() {
            request(LongCompanionObject.MAX_VALUE);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:100:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:101:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:102:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:103:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0022, code lost:
            if (emitValue(r6) != false) goto L_0x0034;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0024, code lost:
            if (0 != 0) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0026, code lost:
            r3 = r5.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0028, code lost:
            monitor-enter(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
            r5.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x002c, code lost:
            monitor-exit(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
            r3 = r5.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x0036, code lost:
            monitor-enter(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
            r0 = r5.queue;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x0039, code lost:
            if (r0 != null) goto L_0x004d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x003b, code lost:
            r5.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x003f, code lost:
            monitor-exit(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x0040, code lost:
            if (1 != 0) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x0042, code lost:
            r3 = r5.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x0044, code lost:
            monitor-enter(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:?, code lost:
            r5.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x0048, code lost:
            monitor-exit(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:52:?, code lost:
            r5.queue = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:0x0050, code lost:
            monitor-exit(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:56:0x0055, code lost:
            if (drain(r0) != false) goto L_0x0034;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:57:0x0057, code lost:
            if (0 != 0) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:58:0x0059, code lost:
            r3 = r5.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:59:0x005b, code lost:
            monitor-enter(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:62:?, code lost:
            r5.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:63:0x005f, code lost:
            monitor-exit(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:73:0x0067, code lost:
            r2 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:74:0x0068, code lost:
            if (0 == 0) goto L_0x006a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:76:0x006c, code lost:
            monitor-enter(r5.guard);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:79:?, code lost:
            r5.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:81:0x0071, code lost:
            throw r2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:98:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:99:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onNext(T r6) {
            /*
                r5 = this;
                java.lang.Object r3 = r5.guard
                monitor-enter(r3)
                boolean r2 = r5.emitting     // Catch:{ all -> 0x0031 }
                if (r2 == 0) goto L_0x0019
                java.util.List<java.lang.Object> r2 = r5.queue     // Catch:{ all -> 0x0031 }
                if (r2 != 0) goto L_0x0012
                java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ all -> 0x0031 }
                r2.<init>()     // Catch:{ all -> 0x0031 }
                r5.queue = r2     // Catch:{ all -> 0x0031 }
            L_0x0012:
                java.util.List<java.lang.Object> r2 = r5.queue     // Catch:{ all -> 0x0031 }
                r2.add(r6)     // Catch:{ all -> 0x0031 }
                monitor-exit(r3)     // Catch:{ all -> 0x0031 }
            L_0x0018:
                return
            L_0x0019:
                r2 = 1
                r5.emitting = r2     // Catch:{ all -> 0x0031 }
                monitor-exit(r3)     // Catch:{ all -> 0x0031 }
                r1 = 0
                boolean r2 = r5.emitValue(r6)     // Catch:{ all -> 0x0067 }
                if (r2 != 0) goto L_0x0034
                if (r1 != 0) goto L_0x0018
                java.lang.Object r3 = r5.guard
                monitor-enter(r3)
                r2 = 0
                r5.emitting = r2     // Catch:{ all -> 0x002e }
                monitor-exit(r3)     // Catch:{ all -> 0x002e }
                goto L_0x0018
            L_0x002e:
                r2 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x002e }
                throw r2
            L_0x0031:
                r2 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x0031 }
                throw r2
            L_0x0034:
                java.lang.Object r3 = r5.guard     // Catch:{ all -> 0x0067 }
                monitor-enter(r3)     // Catch:{ all -> 0x0067 }
                java.util.List<java.lang.Object> r0 = r5.queue     // Catch:{ all -> 0x0064 }
                if (r0 != 0) goto L_0x004d
                r2 = 0
                r5.emitting = r2     // Catch:{ all -> 0x0064 }
                r1 = 1
                monitor-exit(r3)     // Catch:{ all -> 0x0064 }
                if (r1 != 0) goto L_0x0018
                java.lang.Object r3 = r5.guard
                monitor-enter(r3)
                r2 = 0
                r5.emitting = r2     // Catch:{ all -> 0x004a }
                monitor-exit(r3)     // Catch:{ all -> 0x004a }
                goto L_0x0018
            L_0x004a:
                r2 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x004a }
                throw r2
            L_0x004d:
                r2 = 0
                r5.queue = r2     // Catch:{ all -> 0x0064 }
                monitor-exit(r3)     // Catch:{ all -> 0x0064 }
                boolean r2 = r5.drain(r0)     // Catch:{ all -> 0x0067 }
                if (r2 != 0) goto L_0x0034
                if (r1 != 0) goto L_0x0018
                java.lang.Object r3 = r5.guard
                monitor-enter(r3)
                r2 = 0
                r5.emitting = r2     // Catch:{ all -> 0x0061 }
                monitor-exit(r3)     // Catch:{ all -> 0x0061 }
                goto L_0x0018
            L_0x0061:
                r2 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x0061 }
                throw r2
            L_0x0064:
                r2 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x0064 }
                throw r2     // Catch:{ all -> 0x0067 }
            L_0x0067:
                r2 = move-exception
                if (r1 != 0) goto L_0x0071
                java.lang.Object r3 = r5.guard
                monitor-enter(r3)
                r4 = 0
                r5.emitting = r4     // Catch:{ all -> 0x0072 }
                monitor-exit(r3)     // Catch:{ all -> 0x0072 }
            L_0x0071:
                throw r2
            L_0x0072:
                r2 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x0072 }
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorWindowWithTime.ExactSubscriber.onNext(java.lang.Object):void");
        }

        /* access modifiers changed from: package-private */
        public boolean drain(List<Object> queue2) {
            if (queue2 == null) {
                return true;
            }
            for (T o : queue2) {
                if (o == OperatorWindowWithTime.NEXT_SUBJECT) {
                    if (!replaceSubject()) {
                        return false;
                    }
                } else if (OperatorWindowWithTime.nl.isError(o)) {
                    error(OperatorWindowWithTime.nl.getError(o));
                    return true;
                } else if (OperatorWindowWithTime.nl.isCompleted(o)) {
                    complete();
                    return true;
                } else if (!emitValue(o)) {
                    return false;
                }
            }
            return true;
        }

        /* access modifiers changed from: package-private */
        public boolean replaceSubject() {
            Observer<T> s = this.state.consumer;
            if (s != null) {
                s.onCompleted();
            }
            if (this.child.isUnsubscribed()) {
                this.state = this.state.clear();
                unsubscribe();
                return false;
            }
            UnicastSubject<T> bus = UnicastSubject.create();
            this.state = this.state.create(bus, bus);
            this.child.onNext(bus);
            return true;
        }

        /* access modifiers changed from: package-private */
        public boolean emitValue(T t) {
            State<T> s;
            State<T> s2 = this.state;
            if (s2.consumer == null) {
                if (!replaceSubject()) {
                    return false;
                }
                s2 = this.state;
            }
            s2.consumer.onNext(t);
            if (s2.count == OperatorWindowWithTime.this.size - 1) {
                s2.consumer.onCompleted();
                s = s2.clear();
            } else {
                s = s2.next();
            }
            this.state = s;
            return true;
        }

        public void onError(Throwable e) {
            synchronized (this.guard) {
                if (this.emitting) {
                    this.queue = Collections.singletonList(OperatorWindowWithTime.nl.error(e));
                    return;
                }
                this.queue = null;
                this.emitting = true;
                error(e);
            }
        }

        /* access modifiers changed from: package-private */
        public void error(Throwable e) {
            Observer<T> s = this.state.consumer;
            this.state = this.state.clear();
            if (s != null) {
                s.onError(e);
            }
            this.child.onError(e);
            unsubscribe();
        }

        /* access modifiers changed from: package-private */
        public void complete() {
            Observer<T> s = this.state.consumer;
            this.state = this.state.clear();
            if (s != null) {
                s.onCompleted();
            }
            this.child.onCompleted();
            unsubscribe();
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
                dji.thirdparty.rx.internal.operators.NotificationLite<java.lang.Object> r4 = dji.thirdparty.rx.internal.operators.OperatorWindowWithTime.nl     // Catch:{ all -> 0x002f }
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
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorWindowWithTime.ExactSubscriber.onCompleted():void");
        }

        /* access modifiers changed from: package-private */
        public void scheduleExact() {
            this.worker.schedulePeriodically(new Action0() {
                /* class dji.thirdparty.rx.internal.operators.OperatorWindowWithTime.ExactSubscriber.AnonymousClass2 */

                public void call() {
                    ExactSubscriber.this.nextWindow();
                }
            }, 0, OperatorWindowWithTime.this.timespan, OperatorWindowWithTime.this.unit);
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:100:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:101:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:102:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:103:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0024, code lost:
            if (replaceSubject() != false) goto L_0x0036;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0026, code lost:
            if (0 != 0) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0028, code lost:
            r3 = r5.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x002a, code lost:
            monitor-enter(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
            r5.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x002e, code lost:
            monitor-exit(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
            r3 = r5.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x0038, code lost:
            monitor-enter(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
            r0 = r5.queue;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x003b, code lost:
            if (r0 != null) goto L_0x004f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x003d, code lost:
            r5.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x0041, code lost:
            monitor-exit(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x0042, code lost:
            if (1 != 0) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x0044, code lost:
            r3 = r5.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x0046, code lost:
            monitor-enter(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:?, code lost:
            r5.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x004a, code lost:
            monitor-exit(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:52:?, code lost:
            r5.queue = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:0x0052, code lost:
            monitor-exit(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:56:0x0057, code lost:
            if (drain(r0) != false) goto L_0x0036;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:57:0x0059, code lost:
            if (0 != 0) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:58:0x005b, code lost:
            r3 = r5.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:59:0x005d, code lost:
            monitor-enter(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:62:?, code lost:
            r5.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:63:0x0061, code lost:
            monitor-exit(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:73:0x0069, code lost:
            r2 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:74:0x006a, code lost:
            if (0 == 0) goto L_0x006c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:76:0x006e, code lost:
            monitor-enter(r5.guard);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:79:?, code lost:
            r5.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:81:0x0073, code lost:
            throw r2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:98:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:99:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void nextWindow() {
            /*
                r5 = this;
                java.lang.Object r3 = r5.guard
                monitor-enter(r3)
                boolean r2 = r5.emitting     // Catch:{ all -> 0x0033 }
                if (r2 == 0) goto L_0x001b
                java.util.List<java.lang.Object> r2 = r5.queue     // Catch:{ all -> 0x0033 }
                if (r2 != 0) goto L_0x0012
                java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ all -> 0x0033 }
                r2.<init>()     // Catch:{ all -> 0x0033 }
                r5.queue = r2     // Catch:{ all -> 0x0033 }
            L_0x0012:
                java.util.List<java.lang.Object> r2 = r5.queue     // Catch:{ all -> 0x0033 }
                java.lang.Object r4 = dji.thirdparty.rx.internal.operators.OperatorWindowWithTime.NEXT_SUBJECT     // Catch:{ all -> 0x0033 }
                r2.add(r4)     // Catch:{ all -> 0x0033 }
                monitor-exit(r3)     // Catch:{ all -> 0x0033 }
            L_0x001a:
                return
            L_0x001b:
                r2 = 1
                r5.emitting = r2     // Catch:{ all -> 0x0033 }
                monitor-exit(r3)     // Catch:{ all -> 0x0033 }
                r1 = 0
                boolean r2 = r5.replaceSubject()     // Catch:{ all -> 0x0069 }
                if (r2 != 0) goto L_0x0036
                if (r1 != 0) goto L_0x001a
                java.lang.Object r3 = r5.guard
                monitor-enter(r3)
                r2 = 0
                r5.emitting = r2     // Catch:{ all -> 0x0030 }
                monitor-exit(r3)     // Catch:{ all -> 0x0030 }
                goto L_0x001a
            L_0x0030:
                r2 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x0030 }
                throw r2
            L_0x0033:
                r2 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x0033 }
                throw r2
            L_0x0036:
                java.lang.Object r3 = r5.guard     // Catch:{ all -> 0x0069 }
                monitor-enter(r3)     // Catch:{ all -> 0x0069 }
                java.util.List<java.lang.Object> r0 = r5.queue     // Catch:{ all -> 0x0066 }
                if (r0 != 0) goto L_0x004f
                r2 = 0
                r5.emitting = r2     // Catch:{ all -> 0x0066 }
                r1 = 1
                monitor-exit(r3)     // Catch:{ all -> 0x0066 }
                if (r1 != 0) goto L_0x001a
                java.lang.Object r3 = r5.guard
                monitor-enter(r3)
                r2 = 0
                r5.emitting = r2     // Catch:{ all -> 0x004c }
                monitor-exit(r3)     // Catch:{ all -> 0x004c }
                goto L_0x001a
            L_0x004c:
                r2 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x004c }
                throw r2
            L_0x004f:
                r2 = 0
                r5.queue = r2     // Catch:{ all -> 0x0066 }
                monitor-exit(r3)     // Catch:{ all -> 0x0066 }
                boolean r2 = r5.drain(r0)     // Catch:{ all -> 0x0069 }
                if (r2 != 0) goto L_0x0036
                if (r1 != 0) goto L_0x001a
                java.lang.Object r3 = r5.guard
                monitor-enter(r3)
                r2 = 0
                r5.emitting = r2     // Catch:{ all -> 0x0063 }
                monitor-exit(r3)     // Catch:{ all -> 0x0063 }
                goto L_0x001a
            L_0x0063:
                r2 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x0063 }
                throw r2
            L_0x0066:
                r2 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x0066 }
                throw r2     // Catch:{ all -> 0x0069 }
            L_0x0069:
                r2 = move-exception
                if (r1 != 0) goto L_0x0073
                java.lang.Object r3 = r5.guard
                monitor-enter(r3)
                r4 = 0
                r5.emitting = r4     // Catch:{ all -> 0x0074 }
                monitor-exit(r3)     // Catch:{ all -> 0x0074 }
            L_0x0073:
                throw r2
            L_0x0074:
                r2 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x0074 }
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorWindowWithTime.ExactSubscriber.nextWindow():void");
        }
    }

    static final class CountedSerializedSubject<T> {
        final Observer<T> consumer;
        int count;
        final Observable<T> producer;

        public CountedSerializedSubject(Observer<T> consumer2, Observable<T> producer2) {
            this.consumer = new SerializedObserver(consumer2);
            this.producer = producer2;
        }
    }

    final class InexactSubscriber extends Subscriber<T> {
        final Subscriber<? super Observable<T>> child;
        final List<CountedSerializedSubject<T>> chunks = new LinkedList();
        boolean done;
        final Object guard = new Object();
        final Scheduler.Worker worker;

        public InexactSubscriber(Subscriber<? super Observable<T>> child2, Scheduler.Worker worker2) {
            super(child2);
            this.child = child2;
            this.worker = worker2;
        }

        public void onStart() {
            request(LongCompanionObject.MAX_VALUE);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0036, code lost:
            r1 = r3.iterator();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x003e, code lost:
            if (r1.hasNext() == false) goto L_0x0008;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0040, code lost:
            r0 = r1.next();
            r0.consumer.onNext(r8);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0051, code lost:
            if (r0.count != r7.this$0.size) goto L_0x003a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0053, code lost:
            r0.consumer.onCompleted();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onNext(T r8) {
            /*
                r7 = this;
                java.lang.Object r5 = r7.guard
                monitor-enter(r5)
                boolean r4 = r7.done     // Catch:{ all -> 0x0032 }
                if (r4 == 0) goto L_0x0009
                monitor-exit(r5)     // Catch:{ all -> 0x0032 }
            L_0x0008:
                return
            L_0x0009:
                java.util.ArrayList r3 = new java.util.ArrayList     // Catch:{ all -> 0x0032 }
                java.util.List<dji.thirdparty.rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject<T>> r4 = r7.chunks     // Catch:{ all -> 0x0032 }
                r3.<init>(r4)     // Catch:{ all -> 0x0032 }
                java.util.List<dji.thirdparty.rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject<T>> r4 = r7.chunks     // Catch:{ all -> 0x0032 }
                java.util.Iterator r2 = r4.iterator()     // Catch:{ all -> 0x0032 }
            L_0x0016:
                boolean r4 = r2.hasNext()     // Catch:{ all -> 0x0032 }
                if (r4 == 0) goto L_0x0035
                java.lang.Object r0 = r2.next()     // Catch:{ all -> 0x0032 }
                dji.thirdparty.rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject r0 = (dji.thirdparty.rx.internal.operators.OperatorWindowWithTime.CountedSerializedSubject) r0     // Catch:{ all -> 0x0032 }
                int r4 = r0.count     // Catch:{ all -> 0x0032 }
                int r4 = r4 + 1
                r0.count = r4     // Catch:{ all -> 0x0032 }
                dji.thirdparty.rx.internal.operators.OperatorWindowWithTime r6 = dji.thirdparty.rx.internal.operators.OperatorWindowWithTime.this     // Catch:{ all -> 0x0032 }
                int r6 = r6.size     // Catch:{ all -> 0x0032 }
                if (r4 != r6) goto L_0x0016
                r2.remove()     // Catch:{ all -> 0x0032 }
                goto L_0x0016
            L_0x0032:
                r4 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x0032 }
                throw r4
            L_0x0035:
                monitor-exit(r5)     // Catch:{ all -> 0x0032 }
                java.util.Iterator r1 = r3.iterator()
            L_0x003a:
                boolean r4 = r1.hasNext()
                if (r4 == 0) goto L_0x0008
                java.lang.Object r0 = r1.next()
                dji.thirdparty.rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject r0 = (dji.thirdparty.rx.internal.operators.OperatorWindowWithTime.CountedSerializedSubject) r0
                dji.thirdparty.rx.Observer<T> r4 = r0.consumer
                r4.onNext(r8)
                int r4 = r0.count
                dji.thirdparty.rx.internal.operators.OperatorWindowWithTime r5 = dji.thirdparty.rx.internal.operators.OperatorWindowWithTime.this
                int r5 = r5.size
                if (r4 != r5) goto L_0x003a
                dji.thirdparty.rx.Observer<T> r4 = r0.consumer
                r4.onCompleted()
                goto L_0x003a
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorWindowWithTime.InexactSubscriber.onNext(java.lang.Object):void");
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0021, code lost:
            if (r1.hasNext() == false) goto L_0x0032;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0023, code lost:
            r1.next().consumer.onError(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0032, code lost:
            r5.child.onError(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x0019, code lost:
            r1 = r2.iterator();
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onError(java.lang.Throwable r6) {
            /*
                r5 = this;
                java.lang.Object r4 = r5.guard
                monitor-enter(r4)
                boolean r3 = r5.done     // Catch:{ all -> 0x002f }
                if (r3 == 0) goto L_0x0009
                monitor-exit(r4)     // Catch:{ all -> 0x002f }
            L_0x0008:
                return
            L_0x0009:
                r3 = 1
                r5.done = r3     // Catch:{ all -> 0x002f }
                java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ all -> 0x002f }
                java.util.List<dji.thirdparty.rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject<T>> r3 = r5.chunks     // Catch:{ all -> 0x002f }
                r2.<init>(r3)     // Catch:{ all -> 0x002f }
                java.util.List<dji.thirdparty.rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject<T>> r3 = r5.chunks     // Catch:{ all -> 0x002f }
                r3.clear()     // Catch:{ all -> 0x002f }
                monitor-exit(r4)     // Catch:{ all -> 0x002f }
                java.util.Iterator r1 = r2.iterator()
            L_0x001d:
                boolean r3 = r1.hasNext()
                if (r3 == 0) goto L_0x0032
                java.lang.Object r0 = r1.next()
                dji.thirdparty.rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject r0 = (dji.thirdparty.rx.internal.operators.OperatorWindowWithTime.CountedSerializedSubject) r0
                dji.thirdparty.rx.Observer<T> r3 = r0.consumer
                r3.onError(r6)
                goto L_0x001d
            L_0x002f:
                r3 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x002f }
                throw r3
            L_0x0032:
                dji.thirdparty.rx.Subscriber<? super dji.thirdparty.rx.Observable<T>> r3 = r5.child
                r3.onError(r6)
                goto L_0x0008
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorWindowWithTime.InexactSubscriber.onError(java.lang.Throwable):void");
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0021, code lost:
            if (r1.hasNext() == false) goto L_0x0032;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0023, code lost:
            r1.next().consumer.onCompleted();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0032, code lost:
            r5.child.onCompleted();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x0019, code lost:
            r1 = r2.iterator();
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onCompleted() {
            /*
                r5 = this;
                java.lang.Object r4 = r5.guard
                monitor-enter(r4)
                boolean r3 = r5.done     // Catch:{ all -> 0x002f }
                if (r3 == 0) goto L_0x0009
                monitor-exit(r4)     // Catch:{ all -> 0x002f }
            L_0x0008:
                return
            L_0x0009:
                r3 = 1
                r5.done = r3     // Catch:{ all -> 0x002f }
                java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ all -> 0x002f }
                java.util.List<dji.thirdparty.rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject<T>> r3 = r5.chunks     // Catch:{ all -> 0x002f }
                r2.<init>(r3)     // Catch:{ all -> 0x002f }
                java.util.List<dji.thirdparty.rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject<T>> r3 = r5.chunks     // Catch:{ all -> 0x002f }
                r3.clear()     // Catch:{ all -> 0x002f }
                monitor-exit(r4)     // Catch:{ all -> 0x002f }
                java.util.Iterator r1 = r2.iterator()
            L_0x001d:
                boolean r3 = r1.hasNext()
                if (r3 == 0) goto L_0x0032
                java.lang.Object r0 = r1.next()
                dji.thirdparty.rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject r0 = (dji.thirdparty.rx.internal.operators.OperatorWindowWithTime.CountedSerializedSubject) r0
                dji.thirdparty.rx.Observer<T> r3 = r0.consumer
                r3.onCompleted()
                goto L_0x001d
            L_0x002f:
                r3 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x002f }
                throw r3
            L_0x0032:
                dji.thirdparty.rx.Subscriber<? super dji.thirdparty.rx.Observable<T>> r3 = r5.child
                r3.onCompleted()
                goto L_0x0008
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorWindowWithTime.InexactSubscriber.onCompleted():void");
        }

        /* access modifiers changed from: package-private */
        public void scheduleChunk() {
            this.worker.schedulePeriodically(new Action0() {
                /* class dji.thirdparty.rx.internal.operators.OperatorWindowWithTime.InexactSubscriber.AnonymousClass1 */

                public void call() {
                    InexactSubscriber.this.startNewChunk();
                }
            }, OperatorWindowWithTime.this.timeshift, OperatorWindowWithTime.this.timeshift, OperatorWindowWithTime.this.unit);
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void startNewChunk() {
            /*
                r7 = this;
                dji.thirdparty.rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject r0 = r7.createCountedSerializedSubject()
                java.lang.Object r3 = r7.guard
                monitor-enter(r3)
                boolean r2 = r7.done     // Catch:{ all -> 0x002d }
                if (r2 == 0) goto L_0x000d
                monitor-exit(r3)     // Catch:{ all -> 0x002d }
            L_0x000c:
                return
            L_0x000d:
                java.util.List<dji.thirdparty.rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject<T>> r2 = r7.chunks     // Catch:{ all -> 0x002d }
                r2.add(r0)     // Catch:{ all -> 0x002d }
                monitor-exit(r3)     // Catch:{ all -> 0x002d }
                dji.thirdparty.rx.Subscriber<? super dji.thirdparty.rx.Observable<T>> r2 = r7.child     // Catch:{ Throwable -> 0x0030 }
                dji.thirdparty.rx.Observable<T> r3 = r0.producer     // Catch:{ Throwable -> 0x0030 }
                r2.onNext(r3)     // Catch:{ Throwable -> 0x0030 }
                dji.thirdparty.rx.Scheduler$Worker r2 = r7.worker
                dji.thirdparty.rx.internal.operators.OperatorWindowWithTime$InexactSubscriber$2 r3 = new dji.thirdparty.rx.internal.operators.OperatorWindowWithTime$InexactSubscriber$2
                r3.<init>(r0)
                dji.thirdparty.rx.internal.operators.OperatorWindowWithTime r4 = dji.thirdparty.rx.internal.operators.OperatorWindowWithTime.this
                long r4 = r4.timespan
                dji.thirdparty.rx.internal.operators.OperatorWindowWithTime r6 = dji.thirdparty.rx.internal.operators.OperatorWindowWithTime.this
                java.util.concurrent.TimeUnit r6 = r6.unit
                r2.schedule(r3, r4, r6)
                goto L_0x000c
            L_0x002d:
                r2 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x002d }
                throw r2
            L_0x0030:
                r1 = move-exception
                r7.onError(r1)
                goto L_0x000c
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorWindowWithTime.InexactSubscriber.startNewChunk():void");
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
        public void terminateChunk(dji.thirdparty.rx.internal.operators.OperatorWindowWithTime.CountedSerializedSubject<T> r6) {
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
                java.util.List<dji.thirdparty.rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject<T>> r3 = r5.chunks     // Catch:{ all -> 0x002b }
                java.util.Iterator r1 = r3.iterator()     // Catch:{ all -> 0x002b }
            L_0x0010:
                boolean r3 = r1.hasNext()     // Catch:{ all -> 0x002b }
                if (r3 == 0) goto L_0x0022
                java.lang.Object r0 = r1.next()     // Catch:{ all -> 0x002b }
                dji.thirdparty.rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject r0 = (dji.thirdparty.rx.internal.operators.OperatorWindowWithTime.CountedSerializedSubject) r0     // Catch:{ all -> 0x002b }
                if (r0 != r6) goto L_0x0010
                r2 = 1
                r1.remove()     // Catch:{ all -> 0x002b }
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
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OperatorWindowWithTime.InexactSubscriber.terminateChunk(dji.thirdparty.rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject):void");
        }

        /* access modifiers changed from: package-private */
        public CountedSerializedSubject<T> createCountedSerializedSubject() {
            UnicastSubject<T> bus = UnicastSubject.create();
            return new CountedSerializedSubject<>(bus, bus);
        }
    }
}
