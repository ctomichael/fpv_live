package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.HalfSerializer;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableWithLatestFromMany<T, R> extends AbstractFlowableWithUpstream<T, R> {
    final Function<? super Object[], R> combiner;
    @Nullable
    final Publisher<?>[] otherArray;
    @Nullable
    final Iterable<? extends Publisher<?>> otherIterable;

    public FlowableWithLatestFromMany(@NonNull Flowable flowable, @NonNull Publisher<?>[] otherArray2, Function function) {
        super(flowable);
        this.otherArray = otherArray2;
        this.otherIterable = null;
        this.combiner = function;
    }

    public FlowableWithLatestFromMany(@NonNull Flowable flowable, @NonNull Iterable<? extends Publisher<?>> otherIterable2, @NonNull Function function) {
        super(flowable);
        this.otherArray = null;
        this.otherIterable = otherIterable2;
        this.combiner = function;
    }

    /* JADX WARN: Type inference failed for: r7v9, types: [java.lang.Object[]], assign insn: 0x0022: INVOKE  (r7v9 ? I:java.lang.Object[]) = (r4v3 'others' org.reactivestreams.Publisher<?>[] A[D('others' org.reactivestreams.Publisher<?>[])]), (r7v8 int) type: STATIC call: java.util.Arrays.copyOf(java.lang.Object[], int):java.lang.Object[] */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void subscribeActual(org.reactivestreams.Subscriber<? super R> r11) {
        /*
            r10 = this;
            org.reactivestreams.Publisher<?>[] r4 = r10.otherArray
            r2 = 0
            if (r4 != 0) goto L_0x004b
            r7 = 8
            org.reactivestreams.Publisher[] r4 = new org.reactivestreams.Publisher[r7]
            java.lang.Iterable<? extends org.reactivestreams.Publisher<?>> r7 = r10.otherIterable     // Catch:{ Throwable -> 0x0043 }
            java.util.Iterator r8 = r7.iterator()     // Catch:{ Throwable -> 0x0043 }
            r3 = r2
        L_0x0010:
            boolean r7 = r8.hasNext()     // Catch:{ Throwable -> 0x0060 }
            if (r7 == 0) goto L_0x0030
            java.lang.Object r5 = r8.next()     // Catch:{ Throwable -> 0x0060 }
            org.reactivestreams.Publisher r5 = (org.reactivestreams.Publisher) r5     // Catch:{ Throwable -> 0x0060 }
            int r7 = r4.length     // Catch:{ Throwable -> 0x0060 }
            if (r3 != r7) goto L_0x002a
            int r7 = r3 >> 1
            int r7 = r7 + r3
            java.lang.Object[] r7 = java.util.Arrays.copyOf(r4, r7)     // Catch:{ Throwable -> 0x0060 }
            r0 = r7
            org.reactivestreams.Publisher[] r0 = (org.reactivestreams.Publisher[]) r0     // Catch:{ Throwable -> 0x0060 }
            r4 = r0
        L_0x002a:
            int r2 = r3 + 1
            r4[r3] = r5     // Catch:{ Throwable -> 0x0043 }
            r3 = r2
            goto L_0x0010
        L_0x0030:
            r2 = r3
        L_0x0031:
            if (r2 != 0) goto L_0x004d
            io.reactivex.internal.operators.flowable.FlowableMap r7 = new io.reactivex.internal.operators.flowable.FlowableMap
            io.reactivex.Flowable r8 = r10.source
            io.reactivex.internal.operators.flowable.FlowableWithLatestFromMany$SingletonArrayFunc r9 = new io.reactivex.internal.operators.flowable.FlowableWithLatestFromMany$SingletonArrayFunc
            r9.<init>()
            r7.<init>(r8, r9)
            r7.subscribeActual(r11)
        L_0x0042:
            return
        L_0x0043:
            r1 = move-exception
        L_0x0044:
            io.reactivex.exceptions.Exceptions.throwIfFatal(r1)
            io.reactivex.internal.subscriptions.EmptySubscription.error(r1, r11)
            goto L_0x0042
        L_0x004b:
            int r2 = r4.length
            goto L_0x0031
        L_0x004d:
            io.reactivex.internal.operators.flowable.FlowableWithLatestFromMany$WithLatestFromSubscriber r6 = new io.reactivex.internal.operators.flowable.FlowableWithLatestFromMany$WithLatestFromSubscriber
            io.reactivex.functions.Function<? super java.lang.Object[], R> r7 = r10.combiner
            r6.<init>(r11, r7, r2)
            r11.onSubscribe(r6)
            r6.subscribe(r4, r2)
            io.reactivex.Flowable r7 = r10.source
            r7.subscribe(r6)
            goto L_0x0042
        L_0x0060:
            r1 = move-exception
            r2 = r3
            goto L_0x0044
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.flowable.FlowableWithLatestFromMany.subscribeActual(org.reactivestreams.Subscriber):void");
    }

    static final class WithLatestFromSubscriber<T, R> extends AtomicInteger implements ConditionalSubscriber<T>, Subscription {
        private static final long serialVersionUID = 1577321883966341961L;
        final Function<? super Object[], R> combiner;
        volatile boolean done;
        final Subscriber<? super R> downstream;
        final AtomicThrowable error;
        final AtomicLong requested;
        final WithLatestInnerSubscriber[] subscribers;
        final AtomicReference<Subscription> upstream;
        final AtomicReferenceArray<Object> values;

        WithLatestFromSubscriber(Subscriber<? super R> actual, Function<? super Object[], R> combiner2, int n) {
            this.downstream = actual;
            this.combiner = combiner2;
            WithLatestInnerSubscriber[] s = new WithLatestInnerSubscriber[n];
            for (int i = 0; i < n; i++) {
                s[i] = new WithLatestInnerSubscriber(this, i);
            }
            this.subscribers = s;
            this.values = new AtomicReferenceArray<>(n);
            this.upstream = new AtomicReference<>();
            this.requested = new AtomicLong();
            this.error = new AtomicThrowable();
        }

        /* access modifiers changed from: package-private */
        public void subscribe(Publisher<?>[] others, int n) {
            WithLatestInnerSubscriber[] subscribers2 = this.subscribers;
            AtomicReference<Subscription> upstream2 = this.upstream;
            for (int i = 0; i < n && upstream2.get() != SubscriptionHelper.CANCELLED; i++) {
                others[i].subscribe(subscribers2[i]);
            }
        }

        public void onSubscribe(Subscription s) {
            SubscriptionHelper.deferredSetOnce(this.upstream, this.requested, s);
        }

        public void onNext(T t) {
            if (!tryOnNext(t) && !this.done) {
                this.upstream.get().request(1);
            }
        }

        public boolean tryOnNext(T t) {
            if (this.done) {
                return false;
            }
            AtomicReferenceArray<Object> ara = this.values;
            int n = ara.length();
            Object[] objects = new Object[(n + 1)];
            objects[0] = t;
            for (int i = 0; i < n; i++) {
                Object o = ara.get(i);
                if (o == null) {
                    return false;
                }
                objects[i + 1] = o;
            }
            try {
                HalfSerializer.onNext(this.downstream, ObjectHelper.requireNonNull(this.combiner.apply(objects), "The combiner returned a null value"), this, this.error);
                return true;
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                cancel();
                onError(ex);
                return false;
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            cancelAllBut(-1);
            HalfSerializer.onError(this.downstream, t, this, this.error);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                cancelAllBut(-1);
                HalfSerializer.onComplete(this.downstream, this, this.error);
            }
        }

        public void request(long n) {
            SubscriptionHelper.deferredRequest(this.upstream, this.requested, n);
        }

        public void cancel() {
            SubscriptionHelper.cancel(this.upstream);
            for (WithLatestInnerSubscriber s : this.subscribers) {
                s.dispose();
            }
        }

        /* access modifiers changed from: package-private */
        public void innerNext(int index, Object o) {
            this.values.set(index, o);
        }

        /* access modifiers changed from: package-private */
        public void innerError(int index, Throwable t) {
            this.done = true;
            SubscriptionHelper.cancel(this.upstream);
            cancelAllBut(index);
            HalfSerializer.onError(this.downstream, t, this, this.error);
        }

        /* access modifiers changed from: package-private */
        public void innerComplete(int index, boolean nonEmpty) {
            if (!nonEmpty) {
                this.done = true;
                SubscriptionHelper.cancel(this.upstream);
                cancelAllBut(index);
                HalfSerializer.onComplete(this.downstream, this, this.error);
            }
        }

        /* access modifiers changed from: package-private */
        public void cancelAllBut(int index) {
            WithLatestInnerSubscriber[] subscribers2 = this.subscribers;
            for (int i = 0; i < subscribers2.length; i++) {
                if (i != index) {
                    subscribers2[i].dispose();
                }
            }
        }
    }

    static final class WithLatestInnerSubscriber extends AtomicReference<Subscription> implements FlowableSubscriber<Object> {
        private static final long serialVersionUID = 3256684027868224024L;
        boolean hasValue;
        final int index;
        final WithLatestFromSubscriber<?, ?> parent;

        WithLatestInnerSubscriber(WithLatestFromSubscriber<?, ?> parent2, int index2) {
            this.parent = parent2;
            this.index = index2;
        }

        public void onSubscribe(Subscription s) {
            SubscriptionHelper.setOnce(this, s, LongCompanionObject.MAX_VALUE);
        }

        public void onNext(Object t) {
            if (!this.hasValue) {
                this.hasValue = true;
            }
            this.parent.innerNext(this.index, t);
        }

        public void onError(Throwable t) {
            this.parent.innerError(this.index, t);
        }

        public void onComplete() {
            this.parent.innerComplete(this.index, this.hasValue);
        }

        /* access modifiers changed from: package-private */
        public void dispose() {
            SubscriptionHelper.cancel(this);
        }
    }

    final class SingletonArrayFunc implements Function<T, R> {
        SingletonArrayFunc() {
        }

        public R apply(T t) throws Exception {
            return ObjectHelper.requireNonNull(FlowableWithLatestFromMany.this.combiner.apply(new Object[]{t}), "The combiner returned a null value");
        }
    }
}
