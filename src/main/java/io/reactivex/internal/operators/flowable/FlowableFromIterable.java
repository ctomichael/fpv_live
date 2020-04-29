package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.annotations.Nullable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscriptions.BasicQueueSubscription;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import java.util.Iterator;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;

public final class FlowableFromIterable<T> extends Flowable<T> {
    final Iterable<? extends T> source;

    public FlowableFromIterable(Iterable<? extends T> source2) {
        this.source = source2;
    }

    public void subscribeActual(Subscriber<? super T> s) {
        try {
            subscribe(s, this.source.iterator());
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            EmptySubscription.error(e, s);
        }
    }

    public static <T> void subscribe(Subscriber subscriber, Iterator it2) {
        try {
            if (!it2.hasNext()) {
                EmptySubscription.complete(subscriber);
            } else if (subscriber instanceof ConditionalSubscriber) {
                subscriber.onSubscribe(new IteratorConditionalSubscription((ConditionalSubscriber) subscriber, it2));
            } else {
                subscriber.onSubscribe(new IteratorSubscription(subscriber, it2));
            }
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            EmptySubscription.error(e, subscriber);
        }
    }

    static abstract class BaseRangeSubscription<T> extends BasicQueueSubscription<T> {
        private static final long serialVersionUID = -2252972430506210021L;
        volatile boolean cancelled;

        /* renamed from: it  reason: collision with root package name */
        Iterator<? extends T> f13it;
        boolean once;

        /* access modifiers changed from: package-private */
        public abstract void fastPath();

        /* access modifiers changed from: package-private */
        public abstract void slowPath(long j);

        BaseRangeSubscription(Iterator<? extends T> it2) {
            this.f13it = it2;
        }

        public final int requestFusion(int mode) {
            return mode & 1;
        }

        @Nullable
        public final T poll() {
            if (this.f13it == null) {
                return null;
            }
            if (!this.once) {
                this.once = true;
            } else if (!this.f13it.hasNext()) {
                return null;
            }
            return ObjectHelper.requireNonNull(this.f13it.next(), "Iterator.next() returned a null value");
        }

        public final boolean isEmpty() {
            return this.f13it == null || !this.f13it.hasNext();
        }

        public final void clear() {
            this.f13it = null;
        }

        public final void request(long n) {
            if (SubscriptionHelper.validate(n) && BackpressureHelper.add(this, n) == 0) {
                if (n == LongCompanionObject.MAX_VALUE) {
                    fastPath();
                } else {
                    slowPath(n);
                }
            }
        }

        public final void cancel() {
            this.cancelled = true;
        }
    }

    static final class IteratorSubscription<T> extends BaseRangeSubscription<T> {
        private static final long serialVersionUID = -6022804456014692607L;
        final Subscriber<? super T> downstream;

        IteratorSubscription(Subscriber<? super T> actual, Iterator<? extends T> it2) {
            super(it2);
            this.downstream = actual;
        }

        /* access modifiers changed from: package-private */
        public void fastPath() {
            Iterator<? extends T> it2 = this.f13it;
            Subscriber<? super T> a = this.downstream;
            while (!this.cancelled) {
                try {
                    T t = it2.next();
                    if (this.cancelled) {
                        return;
                    }
                    if (t == null) {
                        a.onError(new NullPointerException("Iterator.next() returned a null value"));
                        return;
                    }
                    a.onNext(t);
                    if (!this.cancelled) {
                        try {
                            if (!it2.hasNext()) {
                                if (!this.cancelled) {
                                    a.onComplete();
                                    return;
                                }
                                return;
                            }
                        } catch (Throwable ex) {
                            Exceptions.throwIfFatal(ex);
                            a.onError(ex);
                            return;
                        }
                    } else {
                        return;
                    }
                } catch (Throwable ex2) {
                    Exceptions.throwIfFatal(ex2);
                    a.onError(ex2);
                    return;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void slowPath(long r) {
            long e = 0;
            Iterator<? extends T> it2 = this.f13it;
            Subscriber<? super T> a = this.downstream;
            while (true) {
                if (e == r) {
                    r = get();
                    if (e == r) {
                        r = addAndGet(-e);
                        if (r != 0) {
                            e = 0;
                        } else {
                            return;
                        }
                    } else {
                        continue;
                    }
                } else if (!this.cancelled) {
                    try {
                        T t = it2.next();
                        if (this.cancelled) {
                            return;
                        }
                        if (t == null) {
                            a.onError(new NullPointerException("Iterator.next() returned a null value"));
                            return;
                        }
                        a.onNext(t);
                        if (!this.cancelled) {
                            try {
                                if (it2.hasNext()) {
                                    e++;
                                } else if (!this.cancelled) {
                                    a.onComplete();
                                    return;
                                } else {
                                    return;
                                }
                            } catch (Throwable ex) {
                                Exceptions.throwIfFatal(ex);
                                a.onError(ex);
                                return;
                            }
                        } else {
                            return;
                        }
                    } catch (Throwable ex2) {
                        Exceptions.throwIfFatal(ex2);
                        a.onError(ex2);
                        return;
                    }
                } else {
                    return;
                }
            }
        }
    }

    static final class IteratorConditionalSubscription<T> extends BaseRangeSubscription<T> {
        private static final long serialVersionUID = -6022804456014692607L;
        final ConditionalSubscriber<? super T> downstream;

        IteratorConditionalSubscription(ConditionalSubscriber<? super T> actual, Iterator<? extends T> it2) {
            super(it2);
            this.downstream = actual;
        }

        /* access modifiers changed from: package-private */
        public void fastPath() {
            Iterator<? extends T> it2 = this.f13it;
            ConditionalSubscriber<? super T> a = this.downstream;
            while (!this.cancelled) {
                try {
                    T t = it2.next();
                    if (this.cancelled) {
                        return;
                    }
                    if (t == null) {
                        a.onError(new NullPointerException("Iterator.next() returned a null value"));
                        return;
                    }
                    a.tryOnNext(t);
                    if (!this.cancelled) {
                        try {
                            if (!it2.hasNext()) {
                                if (!this.cancelled) {
                                    a.onComplete();
                                    return;
                                }
                                return;
                            }
                        } catch (Throwable ex) {
                            Exceptions.throwIfFatal(ex);
                            a.onError(ex);
                            return;
                        }
                    } else {
                        return;
                    }
                } catch (Throwable ex2) {
                    Exceptions.throwIfFatal(ex2);
                    a.onError(ex2);
                    return;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void slowPath(long r) {
            long e = 0;
            Iterator<? extends T> it2 = this.f13it;
            ConditionalSubscriber<? super T> a = this.downstream;
            while (true) {
                if (e == r) {
                    r = get();
                    if (e == r) {
                        r = addAndGet(-e);
                        if (r != 0) {
                            e = 0;
                        } else {
                            return;
                        }
                    } else {
                        continue;
                    }
                } else if (!this.cancelled) {
                    try {
                        T t = it2.next();
                        if (this.cancelled) {
                            return;
                        }
                        if (t == null) {
                            a.onError(new NullPointerException("Iterator.next() returned a null value"));
                            return;
                        }
                        boolean b = a.tryOnNext(t);
                        if (!this.cancelled) {
                            try {
                                if (!it2.hasNext()) {
                                    if (!this.cancelled) {
                                        a.onComplete();
                                        return;
                                    }
                                    return;
                                } else if (b) {
                                    e++;
                                }
                            } catch (Throwable ex) {
                                Exceptions.throwIfFatal(ex);
                                a.onError(ex);
                                return;
                            }
                        } else {
                            return;
                        }
                    } catch (Throwable ex2) {
                        Exceptions.throwIfFatal(ex2);
                        a.onError(ex2);
                        return;
                    }
                } else {
                    return;
                }
            }
        }
    }
}
