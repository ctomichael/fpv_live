package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.Flowable;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.internal.fuseable.ConditionalSubscriber;
import dji.thirdparty.io.reactivex.internal.subscriptions.BasicQueueSubscription;
import dji.thirdparty.io.reactivex.internal.subscriptions.SubscriptionHelper;
import dji.thirdparty.io.reactivex.internal.util.BackpressureHelper;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;

public final class FlowableFromArray<T> extends Flowable<T> {
    final T[] array;

    public FlowableFromArray(T[] array2) {
        this.array = array2;
    }

    public void subscribeActual(Subscriber<? super T> s) {
        if (s instanceof ConditionalSubscriber) {
            s.onSubscribe(new ArrayConditionalSubscription((ConditionalSubscriber) s, this.array));
        } else {
            s.onSubscribe(new ArraySubscription(s, this.array));
        }
    }

    static abstract class BaseArraySubscription<T> extends BasicQueueSubscription<T> {
        private static final long serialVersionUID = -2252972430506210021L;
        final T[] array;
        volatile boolean cancelled;
        int index;

        /* access modifiers changed from: package-private */
        public abstract void fastPath();

        /* access modifiers changed from: package-private */
        public abstract void slowPath(long j);

        BaseArraySubscription(T[] array2) {
            this.array = array2;
        }

        public final int requestFusion(int mode) {
            return mode & 1;
        }

        public final T poll() {
            int i = this.index;
            T[] arr = this.array;
            if (i == arr.length) {
                return null;
            }
            this.index = i + 1;
            return ObjectHelper.requireNonNull(arr[i], "array element is null");
        }

        public final boolean isEmpty() {
            return this.index == this.array.length;
        }

        public final void clear() {
            this.index = this.array.length;
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

    static final class ArraySubscription<T> extends BaseArraySubscription<T> {
        private static final long serialVersionUID = 2587302975077663557L;
        final Subscriber<? super T> actual;

        ArraySubscription(Subscriber<? super T> actual2, T[] array) {
            super(array);
            this.actual = actual2;
        }

        /* access modifiers changed from: package-private */
        public void fastPath() {
            T[] arr = this.array;
            int f = arr.length;
            Subscriber<? super T> a = this.actual;
            int i = this.index;
            while (i != f) {
                if (!this.cancelled) {
                    T t = arr[i];
                    if (t == null) {
                        a.onError(new NullPointerException("array element is null"));
                        return;
                    } else {
                        a.onNext(t);
                        i++;
                    }
                } else {
                    return;
                }
            }
            if (!this.cancelled) {
                a.onComplete();
            }
        }

        /* access modifiers changed from: package-private */
        public void slowPath(long r) {
            long e = 0;
            T[] arr = this.array;
            int f = arr.length;
            int i = this.index;
            Subscriber<? super T> a = this.actual;
            while (true) {
                if (e == r || i == f) {
                    if (i != f) {
                        r = get();
                        if (e == r) {
                            this.index = i;
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
                        a.onComplete();
                        return;
                    } else {
                        return;
                    }
                } else if (!this.cancelled) {
                    T t = arr[i];
                    if (t == null) {
                        a.onError(new NullPointerException("array element is null"));
                        return;
                    }
                    a.onNext(t);
                    e++;
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    static final class ArrayConditionalSubscription<T> extends BaseArraySubscription<T> {
        private static final long serialVersionUID = 2587302975077663557L;
        final ConditionalSubscriber<? super T> actual;

        ArrayConditionalSubscription(ConditionalSubscriber<? super T> actual2, T[] array) {
            super(array);
            this.actual = actual2;
        }

        /* access modifiers changed from: package-private */
        public void fastPath() {
            T[] arr = this.array;
            int f = arr.length;
            ConditionalSubscriber<? super T> a = this.actual;
            int i = this.index;
            while (i != f) {
                if (!this.cancelled) {
                    T t = arr[i];
                    if (t == null) {
                        a.onError(new NullPointerException("array element is null"));
                        return;
                    } else {
                        a.tryOnNext(t);
                        i++;
                    }
                } else {
                    return;
                }
            }
            if (!this.cancelled) {
                a.onComplete();
            }
        }

        /* access modifiers changed from: package-private */
        public void slowPath(long r) {
            long e = 0;
            T[] arr = this.array;
            int f = arr.length;
            int i = this.index;
            ConditionalSubscriber<? super T> a = this.actual;
            while (true) {
                if (e == r || i == f) {
                    if (i != f) {
                        r = get();
                        if (e == r) {
                            this.index = i;
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
                        a.onComplete();
                        return;
                    } else {
                        return;
                    }
                } else if (!this.cancelled) {
                    T t = arr[i];
                    if (t == null) {
                        a.onError(new NullPointerException("array element is null"));
                        return;
                    }
                    if (a.tryOnNext(t)) {
                        e++;
                    }
                    i++;
                } else {
                    return;
                }
            }
        }
    }
}
