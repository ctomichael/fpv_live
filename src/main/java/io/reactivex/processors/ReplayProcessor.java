package io.reactivex.processors;

import io.reactivex.Scheduler;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ReplayProcessor<T> extends FlowableProcessor<T> {
    static final ReplaySubscription[] EMPTY = new ReplaySubscription[0];
    private static final Object[] EMPTY_ARRAY = new Object[0];
    static final ReplaySubscription[] TERMINATED = new ReplaySubscription[0];
    final ReplayBuffer<T> buffer;
    boolean done;
    final AtomicReference<ReplaySubscription<T>[]> subscribers = new AtomicReference<>(EMPTY);

    interface ReplayBuffer<T> {
        void complete();

        void error(Throwable th);

        Throwable getError();

        @Nullable
        T getValue();

        T[] getValues(T[] tArr);

        boolean isDone();

        void next(T t);

        void replay(ReplaySubscription<T> replaySubscription);

        int size();

        void trimHead();
    }

    @CheckReturnValue
    @NonNull
    public static <T> ReplayProcessor<T> create() {
        return new ReplayProcessor<>(new UnboundedReplayBuffer(16));
    }

    @CheckReturnValue
    @NonNull
    public static <T> ReplayProcessor<T> create(int capacityHint) {
        return new ReplayProcessor<>(new UnboundedReplayBuffer(capacityHint));
    }

    @CheckReturnValue
    @NonNull
    public static <T> ReplayProcessor<T> createWithSize(int maxSize) {
        return new ReplayProcessor<>(new SizeBoundReplayBuffer(maxSize));
    }

    static <T> ReplayProcessor<T> createUnbounded() {
        return new ReplayProcessor<>(new SizeBoundReplayBuffer(Integer.MAX_VALUE));
    }

    @CheckReturnValue
    @NonNull
    public static <T> ReplayProcessor<T> createWithTime(long maxAge, TimeUnit unit, Scheduler scheduler) {
        return new ReplayProcessor<>(new SizeAndTimeBoundReplayBuffer(Integer.MAX_VALUE, maxAge, unit, scheduler));
    }

    @CheckReturnValue
    @NonNull
    public static <T> ReplayProcessor<T> createWithTimeAndSize(long maxAge, TimeUnit unit, Scheduler scheduler, int maxSize) {
        return new ReplayProcessor<>(new SizeAndTimeBoundReplayBuffer(maxSize, maxAge, unit, scheduler));
    }

    ReplayProcessor(ReplayBuffer<T> buffer2) {
        this.buffer = buffer2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        ReplaySubscription<T> rs = new ReplaySubscription<>(s, this);
        s.onSubscribe(rs);
        if (!add(rs) || !rs.cancelled) {
            this.buffer.replay(rs);
        } else {
            remove(rs);
        }
    }

    public void onSubscribe(Subscription s) {
        if (this.done) {
            s.cancel();
        } else {
            s.request(LongCompanionObject.MAX_VALUE);
        }
    }

    public void onNext(T t) {
        ObjectHelper.requireNonNull(t, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
        if (!this.done) {
            ReplayBuffer<T> b = this.buffer;
            b.next(t);
            for (ReplaySubscription<T> rs : (ReplaySubscription[]) this.subscribers.get()) {
                b.replay(rs);
            }
        }
    }

    public void onError(Throwable t) {
        ObjectHelper.requireNonNull(t, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        if (this.done) {
            RxJavaPlugins.onError(t);
            return;
        }
        this.done = true;
        ReplayBuffer<T> b = this.buffer;
        b.error(t);
        for (ReplaySubscription<T> rs : (ReplaySubscription[]) this.subscribers.getAndSet(TERMINATED)) {
            b.replay(rs);
        }
    }

    public void onComplete() {
        if (!this.done) {
            this.done = true;
            ReplayBuffer<T> b = this.buffer;
            b.complete();
            for (ReplaySubscription<T> rs : (ReplaySubscription[]) this.subscribers.getAndSet(TERMINATED)) {
                b.replay(rs);
            }
        }
    }

    public boolean hasSubscribers() {
        return ((ReplaySubscription[]) this.subscribers.get()).length != 0;
    }

    /* access modifiers changed from: package-private */
    public int subscriberCount() {
        return ((ReplaySubscription[]) this.subscribers.get()).length;
    }

    @Nullable
    public Throwable getThrowable() {
        ReplayBuffer<T> b = this.buffer;
        if (b.isDone()) {
            return b.getError();
        }
        return null;
    }

    public void cleanupBuffer() {
        this.buffer.trimHead();
    }

    public T getValue() {
        return this.buffer.getValue();
    }

    public Object[] getValues() {
        T[] b = getValues((Object[]) EMPTY_ARRAY);
        if (b == EMPTY_ARRAY) {
            return new Object[0];
        }
        return b;
    }

    public T[] getValues(T[] array) {
        return this.buffer.getValues(array);
    }

    public boolean hasComplete() {
        ReplayBuffer<T> b = this.buffer;
        return b.isDone() && b.getError() == null;
    }

    public boolean hasThrowable() {
        ReplayBuffer<T> b = this.buffer;
        return b.isDone() && b.getError() != null;
    }

    public boolean hasValue() {
        return this.buffer.size() != 0;
    }

    /* access modifiers changed from: package-private */
    public int size() {
        return this.buffer.size();
    }

    /* access modifiers changed from: package-private */
    public boolean add(ReplaySubscription<T> rs) {
        ReplaySubscription<T>[] a;
        ReplaySubscription<T>[] b;
        do {
            a = (ReplaySubscription[]) this.subscribers.get();
            if (a == TERMINATED) {
                return false;
            }
            int len = a.length;
            b = new ReplaySubscription[(len + 1)];
            System.arraycopy(a, 0, b, 0, len);
            b[len] = rs;
        } while (!this.subscribers.compareAndSet(a, b));
        return true;
    }

    /* access modifiers changed from: package-private */
    public void remove(ReplaySubscription<T> rs) {
        ReplaySubscription<T>[] a;
        ReplaySubscription<T>[] b;
        do {
            a = (ReplaySubscription[]) this.subscribers.get();
            if (a != TERMINATED && a != EMPTY) {
                int len = a.length;
                int j = -1;
                int i = 0;
                while (true) {
                    if (i >= len) {
                        break;
                    } else if (a[i] == rs) {
                        j = i;
                        break;
                    } else {
                        i++;
                    }
                }
                if (j < 0) {
                    return;
                }
                if (len == 1) {
                    b = EMPTY;
                } else {
                    b = new ReplaySubscription[(len - 1)];
                    System.arraycopy(a, 0, b, 0, j);
                    System.arraycopy(a, j + 1, b, j, (len - j) - 1);
                }
            } else {
                return;
            }
        } while (!this.subscribers.compareAndSet(a, b));
    }

    static final class ReplaySubscription<T> extends AtomicInteger implements Subscription {
        private static final long serialVersionUID = 466549804534799122L;
        volatile boolean cancelled;
        final Subscriber<? super T> downstream;
        long emitted;
        Object index;
        final AtomicLong requested = new AtomicLong();
        final ReplayProcessor<T> state;

        ReplaySubscription(Subscriber<? super T> actual, ReplayProcessor<T> state2) {
            this.downstream = actual;
            this.state = state2;
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this.requested, n);
                this.state.buffer.replay(this);
            }
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.state.remove(this);
            }
        }
    }

    static final class UnboundedReplayBuffer<T> implements ReplayBuffer<T> {
        final List<T> buffer;
        volatile boolean done;
        Throwable error;
        volatile int size;

        UnboundedReplayBuffer(int capacityHint) {
            this.buffer = new ArrayList(ObjectHelper.verifyPositive(capacityHint, "capacityHint"));
        }

        public void next(T value) {
            this.buffer.add(value);
            this.size++;
        }

        public void error(Throwable ex) {
            this.error = ex;
            this.done = true;
        }

        public void complete() {
            this.done = true;
        }

        public void trimHead() {
        }

        @Nullable
        public T getValue() {
            int s = this.size;
            if (s == 0) {
                return null;
            }
            return this.buffer.get(s - 1);
        }

        public T[] getValues(T[] array) {
            int s = this.size;
            if (s == 0) {
                if (array.length != 0) {
                    array[0] = null;
                }
                return array;
            }
            List<T> b = this.buffer;
            if (array.length < s) {
                array = (Object[]) ((Object[]) Array.newInstance(array.getClass().getComponentType(), s));
            }
            for (int i = 0; i < s; i++) {
                array[i] = b.get(i);
            }
            if (array.length > s) {
                array[s] = null;
            }
            return array;
        }

        public void replay(ReplaySubscription<T> rs) {
            int index;
            if (rs.getAndIncrement() == 0) {
                int missed = 1;
                List<T> b = this.buffer;
                Subscriber<? super T> a = rs.downstream;
                Integer indexObject = (Integer) rs.index;
                if (indexObject != null) {
                    index = indexObject.intValue();
                } else {
                    index = 0;
                    rs.index = 0;
                }
                long e = rs.emitted;
                do {
                    long r = rs.requested.get();
                    while (e != r) {
                        if (rs.cancelled) {
                            rs.index = null;
                            return;
                        }
                        boolean d = this.done;
                        int s = this.size;
                        if (d && index == s) {
                            rs.index = null;
                            rs.cancelled = true;
                            Throwable ex = this.error;
                            if (ex == null) {
                                a.onComplete();
                                return;
                            } else {
                                a.onError(ex);
                                return;
                            }
                        } else if (index == s) {
                            break;
                        } else {
                            a.onNext(b.get(index));
                            index++;
                            e++;
                        }
                    }
                    if (e == r) {
                        if (rs.cancelled) {
                            rs.index = null;
                            return;
                        }
                        boolean d2 = this.done;
                        int s2 = this.size;
                        if (d2 && index == s2) {
                            rs.index = null;
                            rs.cancelled = true;
                            Throwable ex2 = this.error;
                            if (ex2 == null) {
                                a.onComplete();
                                return;
                            } else {
                                a.onError(ex2);
                                return;
                            }
                        }
                    }
                    rs.index = Integer.valueOf(index);
                    rs.emitted = e;
                    missed = rs.addAndGet(-missed);
                } while (missed != 0);
            }
        }

        public int size() {
            return this.size;
        }

        public boolean isDone() {
            return this.done;
        }

        public Throwable getError() {
            return this.error;
        }
    }

    static final class Node<T> extends AtomicReference<Node<T>> {
        private static final long serialVersionUID = 6404226426336033100L;
        final T value;

        Node(T value2) {
            this.value = value2;
        }
    }

    static final class TimedNode<T> extends AtomicReference<TimedNode<T>> {
        private static final long serialVersionUID = 6404226426336033100L;
        final long time;
        final T value;

        TimedNode(T value2, long time2) {
            this.value = value2;
            this.time = time2;
        }
    }

    static final class SizeBoundReplayBuffer<T> implements ReplayBuffer<T> {
        volatile boolean done;
        Throwable error;
        volatile Node<T> head;
        final int maxSize;
        int size;
        Node<T> tail;

        SizeBoundReplayBuffer(int maxSize2) {
            this.maxSize = ObjectHelper.verifyPositive(maxSize2, "maxSize");
            Node<T> h = new Node<>(null);
            this.tail = h;
            this.head = h;
        }

        /* access modifiers changed from: package-private */
        public void trim() {
            if (this.size > this.maxSize) {
                this.size--;
                this.head = (Node) this.head.get();
            }
        }

        public void next(T value) {
            Node<T> n = new Node<>(value);
            Node<T> t = this.tail;
            this.tail = n;
            this.size++;
            t.set(n);
            trim();
        }

        public void error(Throwable ex) {
            this.error = ex;
            trimHead();
            this.done = true;
        }

        public void complete() {
            trimHead();
            this.done = true;
        }

        public void trimHead() {
            if (this.head.value != null) {
                Node<T> n = new Node<>(null);
                n.lazySet(this.head.get());
                this.head = n;
            }
        }

        public boolean isDone() {
            return this.done;
        }

        public Throwable getError() {
            return this.error;
        }

        public T getValue() {
            Node<T> h = this.head;
            while (true) {
                Node<T> n = (Node) h.get();
                if (n == null) {
                    return h.value;
                }
                h = n;
            }
        }

        public T[] getValues(T[] array) {
            int s = 0;
            Node<T> h = this.head;
            Node<T> h0 = h;
            while (true) {
                Node<T> next = (Node) h0.get();
                if (next == null) {
                    break;
                }
                s++;
                h0 = next;
            }
            if (array.length < s) {
                array = (Object[]) ((Object[]) Array.newInstance(array.getClass().getComponentType(), s));
            }
            for (int j = 0; j < s; j++) {
                h = (Node) h.get();
                array[j] = h.value;
            }
            if (array.length > s) {
                array[s] = null;
            }
            return array;
        }

        public void replay(ReplaySubscription<T> rs) {
            if (rs.getAndIncrement() == 0) {
                int missed = 1;
                Subscriber<? super T> a = rs.downstream;
                Node<T> index = (Node) rs.index;
                if (index == null) {
                    index = this.head;
                }
                long e = rs.emitted;
                do {
                    long r = rs.requested.get();
                    while (e != r) {
                        if (rs.cancelled) {
                            rs.index = null;
                            return;
                        }
                        boolean d = this.done;
                        Node<T> next = (Node) index.get();
                        boolean empty = next == null;
                        if (d && empty) {
                            rs.index = null;
                            rs.cancelled = true;
                            Throwable ex = this.error;
                            if (ex == null) {
                                a.onComplete();
                                return;
                            } else {
                                a.onError(ex);
                                return;
                            }
                        } else if (empty) {
                            break;
                        } else {
                            a.onNext(next.value);
                            e++;
                            index = next;
                        }
                    }
                    if (e == r) {
                        if (rs.cancelled) {
                            rs.index = null;
                            return;
                        } else if (this.done && index.get() == null) {
                            rs.index = null;
                            rs.cancelled = true;
                            Throwable ex2 = this.error;
                            if (ex2 == null) {
                                a.onComplete();
                                return;
                            } else {
                                a.onError(ex2);
                                return;
                            }
                        }
                    }
                    rs.index = index;
                    rs.emitted = e;
                    missed = rs.addAndGet(-missed);
                } while (missed != 0);
            }
        }

        public int size() {
            Node<T> next;
            int s = 0;
            Node<T> h = this.head;
            while (s != Integer.MAX_VALUE && (next = (Node) h.get()) != null) {
                s++;
                h = next;
            }
            return s;
        }
    }

    static final class SizeAndTimeBoundReplayBuffer<T> implements ReplayBuffer<T> {
        volatile boolean done;
        Throwable error;
        volatile TimedNode<T> head;
        final long maxAge;
        final int maxSize;
        final Scheduler scheduler;
        int size;
        TimedNode<T> tail;
        final TimeUnit unit;

        SizeAndTimeBoundReplayBuffer(int maxSize2, long maxAge2, TimeUnit unit2, Scheduler scheduler2) {
            this.maxSize = ObjectHelper.verifyPositive(maxSize2, "maxSize");
            this.maxAge = ObjectHelper.verifyPositive(maxAge2, "maxAge");
            this.unit = (TimeUnit) ObjectHelper.requireNonNull(unit2, "unit is null");
            this.scheduler = (Scheduler) ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
            TimedNode<T> h = new TimedNode<>(null, 0);
            this.tail = h;
            this.head = h;
        }

        /* access modifiers changed from: package-private */
        public void trim() {
            if (this.size > this.maxSize) {
                this.size--;
                this.head = (TimedNode) this.head.get();
            }
            long limit = this.scheduler.now(this.unit) - this.maxAge;
            TimedNode<T> h = this.head;
            while (this.size > 1) {
                TimedNode<T> next = (TimedNode) h.get();
                if (next == null) {
                    this.head = h;
                    return;
                } else if (next.time > limit) {
                    this.head = h;
                    return;
                } else {
                    h = next;
                    this.size--;
                }
            }
            this.head = h;
        }

        /* access modifiers changed from: package-private */
        public void trimFinal() {
            long limit = this.scheduler.now(this.unit) - this.maxAge;
            TimedNode<T> h = this.head;
            while (true) {
                TimedNode<T> next = (TimedNode) h.get();
                if (next == null) {
                    if (h.value != null) {
                        this.head = new TimedNode<>(null, 0);
                        return;
                    } else {
                        this.head = h;
                        return;
                    }
                } else if (next.time <= limit) {
                    h = next;
                } else if (h.value != null) {
                    TimedNode<T> n = new TimedNode<>(null, 0);
                    n.lazySet(h.get());
                    this.head = n;
                    return;
                } else {
                    this.head = h;
                    return;
                }
            }
        }

        public void trimHead() {
            if (this.head.value != null) {
                TimedNode<T> n = new TimedNode<>(null, 0);
                n.lazySet(this.head.get());
                this.head = n;
            }
        }

        public void next(T value) {
            TimedNode<T> n = new TimedNode<>(value, this.scheduler.now(this.unit));
            TimedNode<T> t = this.tail;
            this.tail = n;
            this.size++;
            t.set(n);
            trim();
        }

        public void error(Throwable ex) {
            trimFinal();
            this.error = ex;
            this.done = true;
        }

        public void complete() {
            trimFinal();
            this.done = true;
        }

        @Nullable
        public T getValue() {
            TimedNode<T> h = this.head;
            while (true) {
                TimedNode<T> next = (TimedNode) h.get();
                if (next == null) {
                    break;
                }
                h = next;
            }
            if (h.time < this.scheduler.now(this.unit) - this.maxAge) {
                return null;
            }
            return h.value;
        }

        public T[] getValues(T[] array) {
            TimedNode<T> h = getHead();
            int s = size(h);
            if (s != 0) {
                if (array.length < s) {
                    array = (Object[]) ((Object[]) Array.newInstance(array.getClass().getComponentType(), s));
                }
                int i = 0;
                while (i != s) {
                    TimedNode<T> next = (TimedNode) h.get();
                    array[i] = next.value;
                    i++;
                    h = next;
                }
                if (array.length > s) {
                    array[s] = null;
                }
            } else if (array.length != 0) {
                array[0] = null;
            }
            return array;
        }

        /* access modifiers changed from: package-private */
        public TimedNode<T> getHead() {
            TimedNode<T> index = this.head;
            long limit = this.scheduler.now(this.unit) - this.maxAge;
            TimedNode<T> next = (TimedNode) index.get();
            while (next != null && next.time <= limit) {
                index = next;
                next = (TimedNode) index.get();
            }
            return index;
        }

        public void replay(ReplaySubscription<T> rs) {
            if (rs.getAndIncrement() == 0) {
                int missed = 1;
                Subscriber<? super T> a = rs.downstream;
                TimedNode<T> index = (TimedNode) rs.index;
                if (index == null) {
                    index = getHead();
                }
                long e = rs.emitted;
                do {
                    long r = rs.requested.get();
                    while (e != r) {
                        if (rs.cancelled) {
                            rs.index = null;
                            return;
                        }
                        boolean d = this.done;
                        TimedNode<T> next = (TimedNode) index.get();
                        boolean empty = next == null;
                        if (d && empty) {
                            rs.index = null;
                            rs.cancelled = true;
                            Throwable ex = this.error;
                            if (ex == null) {
                                a.onComplete();
                                return;
                            } else {
                                a.onError(ex);
                                return;
                            }
                        } else if (empty) {
                            break;
                        } else {
                            a.onNext(next.value);
                            e++;
                            index = next;
                        }
                    }
                    if (e == r) {
                        if (rs.cancelled) {
                            rs.index = null;
                            return;
                        } else if (this.done && index.get() == null) {
                            rs.index = null;
                            rs.cancelled = true;
                            Throwable ex2 = this.error;
                            if (ex2 == null) {
                                a.onComplete();
                                return;
                            } else {
                                a.onError(ex2);
                                return;
                            }
                        }
                    }
                    rs.index = index;
                    rs.emitted = e;
                    missed = rs.addAndGet(-missed);
                } while (missed != 0);
            }
        }

        public int size() {
            return size(getHead());
        }

        /* access modifiers changed from: package-private */
        public int size(TimedNode<T> h) {
            TimedNode<T> next;
            int s = 0;
            while (s != Integer.MAX_VALUE && (next = (TimedNode) h.get()) != null) {
                s++;
                h = next;
            }
            return s;
        }

        public Throwable getError() {
            return this.error;
        }

        public boolean isDone() {
            return this.done;
        }
    }
}
