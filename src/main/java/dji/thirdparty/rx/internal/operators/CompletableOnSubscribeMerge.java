package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Completable;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.exceptions.CompositeException;
import dji.thirdparty.rx.plugins.RxJavaPlugins;
import dji.thirdparty.rx.subscriptions.CompositeSubscription;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.jvm.internal.LongCompanionObject;

public final class CompletableOnSubscribeMerge implements Completable.CompletableOnSubscribe {
    final boolean delayErrors;
    final int maxConcurrency;
    final Observable<Completable> source;

    /* JADX WARN: Type inference failed for: r1v0, types: [dji.thirdparty.rx.Observable<dji.thirdparty.rx.Completable>, dji.thirdparty.rx.Observable<? extends dji.thirdparty.rx.Completable>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public CompletableOnSubscribeMerge(dji.thirdparty.rx.Observable<? extends dji.thirdparty.rx.Completable> r1, int r2, boolean r3) {
        /*
            r0 = this;
            r0.<init>()
            r0.source = r1
            r0.maxConcurrency = r2
            r0.delayErrors = r3
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.CompletableOnSubscribeMerge.<init>(dji.thirdparty.rx.Observable, int, boolean):void");
    }

    public void call(Completable.CompletableSubscriber s) {
        CompletableMergeSubscriber parent = new CompletableMergeSubscriber(s, this.maxConcurrency, this.delayErrors);
        s.onSubscribe(parent);
        this.source.subscribe((Subscriber) parent);
    }

    static final class CompletableMergeSubscriber extends Subscriber<Completable> {
        static final AtomicReferenceFieldUpdater<CompletableMergeSubscriber, Queue> ERRORS = AtomicReferenceFieldUpdater.newUpdater(CompletableMergeSubscriber.class, Queue.class, "errors");
        static final AtomicIntegerFieldUpdater<CompletableMergeSubscriber> ONCE = AtomicIntegerFieldUpdater.newUpdater(CompletableMergeSubscriber.class, "once");
        final Completable.CompletableSubscriber actual;
        final boolean delayErrors;
        volatile boolean done;
        volatile Queue<Throwable> errors;
        final int maxConcurrency;
        volatile int once;
        final CompositeSubscription set = new CompositeSubscription();
        final AtomicInteger wip = new AtomicInteger(1);

        public CompletableMergeSubscriber(Completable.CompletableSubscriber actual2, int maxConcurrency2, boolean delayErrors2) {
            this.actual = actual2;
            this.maxConcurrency = maxConcurrency2;
            this.delayErrors = delayErrors2;
            if (maxConcurrency2 == Integer.MAX_VALUE) {
                request(LongCompanionObject.MAX_VALUE);
            } else {
                request((long) maxConcurrency2);
            }
        }

        /* access modifiers changed from: package-private */
        public Queue<Throwable> getOrCreateErrors() {
            Queue<Throwable> q = this.errors;
            if (q != null) {
                return q;
            }
            ConcurrentLinkedQueue concurrentLinkedQueue = new ConcurrentLinkedQueue();
            if (ERRORS.compareAndSet(this, null, concurrentLinkedQueue)) {
                return concurrentLinkedQueue;
            }
            return this.errors;
        }

        public void onNext(Completable t) {
            if (!this.done) {
                this.wip.getAndIncrement();
                t.subscribe(new Completable.CompletableSubscriber() {
                    /* class dji.thirdparty.rx.internal.operators.CompletableOnSubscribeMerge.CompletableMergeSubscriber.AnonymousClass1 */
                    Subscription d;
                    boolean innerDone;

                    public void onSubscribe(Subscription d2) {
                        this.d = d2;
                        CompletableMergeSubscriber.this.set.add(d2);
                    }

                    public void onError(Throwable e) {
                        if (this.innerDone) {
                            RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
                            return;
                        }
                        this.innerDone = true;
                        CompletableMergeSubscriber.this.set.remove(this.d);
                        CompletableMergeSubscriber.this.getOrCreateErrors().offer(e);
                        CompletableMergeSubscriber.this.terminate();
                        if (CompletableMergeSubscriber.this.delayErrors && !CompletableMergeSubscriber.this.done) {
                            CompletableMergeSubscriber.this.request(1);
                        }
                    }

                    public void onCompleted() {
                        if (!this.innerDone) {
                            this.innerDone = true;
                            CompletableMergeSubscriber.this.set.remove(this.d);
                            CompletableMergeSubscriber.this.terminate();
                            if (!CompletableMergeSubscriber.this.done) {
                                CompletableMergeSubscriber.this.request(1);
                            }
                        }
                    }
                });
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(t);
                return;
            }
            getOrCreateErrors().offer(t);
            this.done = true;
            terminate();
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                terminate();
            }
        }

        /* access modifiers changed from: package-private */
        public void terminate() {
            Queue<Throwable> q;
            if (this.wip.decrementAndGet() == 0) {
                Queue<Throwable> q2 = this.errors;
                if (q2 == null || q2.isEmpty()) {
                    this.actual.onCompleted();
                    return;
                }
                Throwable e = CompletableOnSubscribeMerge.collectErrors(q2);
                if (ONCE.compareAndSet(this, 0, 1)) {
                    this.actual.onError(e);
                } else {
                    RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
                }
            } else if (!this.delayErrors && (q = this.errors) != null && !q.isEmpty()) {
                Throwable e2 = CompletableOnSubscribeMerge.collectErrors(q);
                if (ONCE.compareAndSet(this, 0, 1)) {
                    this.actual.onError(e2);
                } else {
                    RxJavaPlugins.getInstance().getErrorHandler().handleError(e2);
                }
            }
        }
    }

    public static Throwable collectErrors(Queue<Throwable> q) {
        List<Throwable> list = new ArrayList<>();
        while (true) {
            Throwable t = q.poll();
            if (t == null) {
                break;
            }
            list.add(t);
        }
        if (list.isEmpty()) {
            return null;
        }
        if (list.size() == 1) {
            return (Throwable) list.get(0);
        }
        return new CompositeException(list);
    }
}
