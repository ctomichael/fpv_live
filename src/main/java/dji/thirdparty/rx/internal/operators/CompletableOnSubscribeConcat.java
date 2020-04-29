package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Completable;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.exceptions.MissingBackpressureException;
import dji.thirdparty.rx.internal.util.unsafe.SpscArrayQueue;
import dji.thirdparty.rx.plugins.RxJavaPlugins;
import dji.thirdparty.rx.subscriptions.SerialSubscription;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public final class CompletableOnSubscribeConcat implements Completable.CompletableOnSubscribe {
    final int prefetch;
    final Observable<Completable> sources;

    /* JADX WARN: Type inference failed for: r1v0, types: [dji.thirdparty.rx.Observable<dji.thirdparty.rx.Completable>, dji.thirdparty.rx.Observable<? extends dji.thirdparty.rx.Completable>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public CompletableOnSubscribeConcat(dji.thirdparty.rx.Observable<? extends dji.thirdparty.rx.Completable> r1, int r2) {
        /*
            r0 = this;
            r0.<init>()
            r0.sources = r1
            r0.prefetch = r2
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.CompletableOnSubscribeConcat.<init>(dji.thirdparty.rx.Observable, int):void");
    }

    public void call(Completable.CompletableSubscriber s) {
        CompletableConcatSubscriber parent = new CompletableConcatSubscriber(s, this.prefetch);
        s.onSubscribe(parent);
        this.sources.subscribe((Subscriber) parent);
    }

    static final class CompletableConcatSubscriber extends Subscriber<Completable> {
        static final AtomicIntegerFieldUpdater<CompletableConcatSubscriber> ONCE = AtomicIntegerFieldUpdater.newUpdater(CompletableConcatSubscriber.class, "once");
        final Completable.CompletableSubscriber actual;
        volatile boolean done;
        final ConcatInnerSubscriber inner = new ConcatInnerSubscriber();
        volatile int once;
        final int prefetch;
        final SpscArrayQueue<Completable> queue;
        final SerialSubscription sr = new SerialSubscription();
        final AtomicInteger wip = new AtomicInteger();

        public CompletableConcatSubscriber(Completable.CompletableSubscriber actual2, int prefetch2) {
            this.actual = actual2;
            this.prefetch = prefetch2;
            this.queue = new SpscArrayQueue<>(prefetch2);
            add(this.sr);
            request((long) prefetch2);
        }

        public void onNext(Completable t) {
            if (!this.queue.offer(t)) {
                onError(new MissingBackpressureException());
            } else if (this.wip.getAndIncrement() == 0) {
                next();
            }
        }

        public void onError(Throwable t) {
            if (ONCE.compareAndSet(this, 0, 1)) {
                this.actual.onError(t);
            } else {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(t);
            }
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                if (this.wip.getAndIncrement() == 0) {
                    next();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void innerError(Throwable e) {
            unsubscribe();
            onError(e);
        }

        /* access modifiers changed from: package-private */
        public void innerComplete() {
            if (this.wip.decrementAndGet() != 0) {
                next();
            }
            if (!this.done) {
                request(1);
            }
        }

        /* access modifiers changed from: package-private */
        public void next() {
            boolean d = this.done;
            Completable c = this.queue.poll();
            if (c != null) {
                c.subscribe(this.inner);
            } else if (!d) {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(new IllegalStateException("Queue is empty?!"));
            } else if (ONCE.compareAndSet(this, 0, 1)) {
                this.actual.onCompleted();
            }
        }

        final class ConcatInnerSubscriber implements Completable.CompletableSubscriber {
            ConcatInnerSubscriber() {
            }

            public void onSubscribe(Subscription d) {
                CompletableConcatSubscriber.this.sr.set(d);
            }

            public void onError(Throwable e) {
                CompletableConcatSubscriber.this.innerError(e);
            }

            public void onCompleted() {
                CompletableConcatSubscriber.this.innerComplete();
            }
        }
    }
}
