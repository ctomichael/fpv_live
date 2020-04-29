package io.reactivex.subjects;

import io.reactivex.Observer;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.util.AppendOnlyLinkedArrayList;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.plugins.RxJavaPlugins;
import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class BehaviorSubject<T> extends Subject<T> {
    static final BehaviorDisposable[] EMPTY = new BehaviorDisposable[0];
    private static final Object[] EMPTY_ARRAY = new Object[0];
    static final BehaviorDisposable[] TERMINATED = new BehaviorDisposable[0];
    long index;
    final ReadWriteLock lock;
    final Lock readLock;
    final AtomicReference<BehaviorDisposable<T>[]> subscribers;
    final AtomicReference<Throwable> terminalEvent;
    final AtomicReference<Object> value;
    final Lock writeLock;

    @CheckReturnValue
    @NonNull
    public static <T> BehaviorSubject<T> create() {
        return new BehaviorSubject<>();
    }

    @CheckReturnValue
    @NonNull
    public static <T> BehaviorSubject<T> createDefault(T defaultValue) {
        return new BehaviorSubject<>(defaultValue);
    }

    BehaviorSubject() {
        this.lock = new ReentrantReadWriteLock();
        this.readLock = this.lock.readLock();
        this.writeLock = this.lock.writeLock();
        this.subscribers = new AtomicReference<>(EMPTY);
        this.value = new AtomicReference<>();
        this.terminalEvent = new AtomicReference<>();
    }

    BehaviorSubject(T defaultValue) {
        this();
        this.value.lazySet(ObjectHelper.requireNonNull(defaultValue, "defaultValue is null"));
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        BehaviorDisposable<T> bs = new BehaviorDisposable<>(observer, this);
        observer.onSubscribe(bs);
        if (!add(bs)) {
            Throwable ex = this.terminalEvent.get();
            if (ex == ExceptionHelper.TERMINATED) {
                observer.onComplete();
            } else {
                observer.onError(ex);
            }
        } else if (bs.cancelled) {
            remove(bs);
        } else {
            bs.emitFirst();
        }
    }

    public void onSubscribe(Disposable d) {
        if (this.terminalEvent.get() != null) {
            d.dispose();
        }
    }

    public void onNext(T t) {
        ObjectHelper.requireNonNull(t, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
        if (this.terminalEvent.get() == null) {
            Object o = NotificationLite.next(t);
            setCurrent(o);
            for (BehaviorDisposable<T> bs : (BehaviorDisposable[]) this.subscribers.get()) {
                bs.emitNext(o, this.index);
            }
        }
    }

    public void onError(Throwable t) {
        ObjectHelper.requireNonNull(t, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        if (!this.terminalEvent.compareAndSet(null, t)) {
            RxJavaPlugins.onError(t);
            return;
        }
        Object o = NotificationLite.error(t);
        for (BehaviorDisposable<T> bs : terminate(o)) {
            bs.emitNext(o, this.index);
        }
    }

    public void onComplete() {
        if (this.terminalEvent.compareAndSet(null, ExceptionHelper.TERMINATED)) {
            Object o = NotificationLite.complete();
            for (BehaviorDisposable<T> bs : terminate(o)) {
                bs.emitNext(o, this.index);
            }
        }
    }

    public boolean hasObservers() {
        return ((BehaviorDisposable[]) this.subscribers.get()).length != 0;
    }

    /* access modifiers changed from: package-private */
    public int subscriberCount() {
        return ((BehaviorDisposable[]) this.subscribers.get()).length;
    }

    @Nullable
    public Throwable getThrowable() {
        Object o = this.value.get();
        if (NotificationLite.isError(o)) {
            return NotificationLite.getError(o);
        }
        return null;
    }

    @Nullable
    public T getValue() {
        Object o = this.value.get();
        if (NotificationLite.isComplete(o) || NotificationLite.isError(o)) {
            return null;
        }
        return NotificationLite.getValue(o);
    }

    @Deprecated
    public Object[] getValues() {
        T[] b = getValues((Object[]) EMPTY_ARRAY);
        if (b == EMPTY_ARRAY) {
            return new Object[0];
        }
        return b;
    }

    @Deprecated
    public T[] getValues(T[] array) {
        Object o = this.value.get();
        if (o == null || NotificationLite.isComplete(o) || NotificationLite.isError(o)) {
            if (array.length != 0) {
                array[0] = null;
            }
            return array;
        }
        T v = NotificationLite.getValue(o);
        if (array.length != 0) {
            array[0] = v;
            if (array.length != 1) {
                array[1] = null;
            }
        } else {
            array = (Object[]) ((Object[]) Array.newInstance(array.getClass().getComponentType(), 1));
            array[0] = v;
        }
        return array;
    }

    public boolean hasComplete() {
        return NotificationLite.isComplete(this.value.get());
    }

    public boolean hasThrowable() {
        return NotificationLite.isError(this.value.get());
    }

    public boolean hasValue() {
        Object o = this.value.get();
        return o != null && !NotificationLite.isComplete(o) && !NotificationLite.isError(o);
    }

    /* access modifiers changed from: package-private */
    public boolean add(BehaviorDisposable<T> rs) {
        BehaviorDisposable<T>[] a;
        BehaviorDisposable<T>[] b;
        do {
            a = (BehaviorDisposable[]) this.subscribers.get();
            if (a == TERMINATED) {
                return false;
            }
            int len = a.length;
            b = new BehaviorDisposable[(len + 1)];
            System.arraycopy(a, 0, b, 0, len);
            b[len] = rs;
        } while (!this.subscribers.compareAndSet(a, b));
        return true;
    }

    /* access modifiers changed from: package-private */
    public void remove(BehaviorDisposable<T> rs) {
        BehaviorDisposable<T>[] a;
        BehaviorDisposable<T>[] b;
        do {
            a = (BehaviorDisposable[]) this.subscribers.get();
            int len = a.length;
            if (len != 0) {
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
                    b = new BehaviorDisposable[(len - 1)];
                    System.arraycopy(a, 0, b, 0, j);
                    System.arraycopy(a, j + 1, b, j, (len - j) - 1);
                }
            } else {
                return;
            }
        } while (!this.subscribers.compareAndSet(a, b));
    }

    /* access modifiers changed from: package-private */
    public BehaviorDisposable<T>[] terminate(Object terminalValue) {
        BehaviorDisposable<T>[] a = (BehaviorDisposable[]) this.subscribers.getAndSet(TERMINATED);
        if (a != TERMINATED) {
            setCurrent(terminalValue);
        }
        return a;
    }

    /* access modifiers changed from: package-private */
    public void setCurrent(Object o) {
        this.writeLock.lock();
        this.index++;
        this.value.lazySet(o);
        this.writeLock.unlock();
    }

    static final class BehaviorDisposable<T> implements Disposable, AppendOnlyLinkedArrayList.NonThrowingPredicate<Object> {
        volatile boolean cancelled;
        final Observer<? super T> downstream;
        boolean emitting;
        boolean fastPath;
        long index;
        boolean next;
        AppendOnlyLinkedArrayList<Object> queue;
        final BehaviorSubject<T> state;

        BehaviorDisposable(Observer<? super T> actual, BehaviorSubject<T> state2) {
            this.downstream = actual;
            this.state = state2;
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.state.remove(this);
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0032, code lost:
            if (r1 == null) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0038, code lost:
            if (test(r1) != false) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x003a, code lost:
            emitLoop();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitFirst() {
            /*
                r6 = this;
                r3 = 1
                boolean r4 = r6.cancelled
                if (r4 == 0) goto L_0x0006
            L_0x0005:
                return
            L_0x0006:
                monitor-enter(r6)
                boolean r4 = r6.cancelled     // Catch:{ all -> 0x000d }
                if (r4 == 0) goto L_0x0010
                monitor-exit(r6)     // Catch:{ all -> 0x000d }
                goto L_0x0005
            L_0x000d:
                r3 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x000d }
                throw r3
            L_0x0010:
                boolean r4 = r6.next     // Catch:{ all -> 0x000d }
                if (r4 == 0) goto L_0x0016
                monitor-exit(r6)     // Catch:{ all -> 0x000d }
                goto L_0x0005
            L_0x0016:
                io.reactivex.subjects.BehaviorSubject<T> r2 = r6.state     // Catch:{ all -> 0x000d }
                java.util.concurrent.locks.Lock r0 = r2.readLock     // Catch:{ all -> 0x000d }
                r0.lock()     // Catch:{ all -> 0x000d }
                long r4 = r2.index     // Catch:{ all -> 0x000d }
                r6.index = r4     // Catch:{ all -> 0x000d }
                java.util.concurrent.atomic.AtomicReference<java.lang.Object> r4 = r2.value     // Catch:{ all -> 0x000d }
                java.lang.Object r1 = r4.get()     // Catch:{ all -> 0x000d }
                r0.unlock()     // Catch:{ all -> 0x000d }
                if (r1 == 0) goto L_0x003e
            L_0x002c:
                r6.emitting = r3     // Catch:{ all -> 0x000d }
                r3 = 1
                r6.next = r3     // Catch:{ all -> 0x000d }
                monitor-exit(r6)     // Catch:{ all -> 0x000d }
                if (r1 == 0) goto L_0x0005
                boolean r3 = r6.test(r1)
                if (r3 != 0) goto L_0x0005
                r6.emitLoop()
                goto L_0x0005
            L_0x003e:
                r3 = 0
                goto L_0x002c
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.subjects.BehaviorSubject.BehaviorDisposable.emitFirst():void");
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x0035, code lost:
            r6.fastPath = true;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitNext(java.lang.Object r7, long r8) {
            /*
                r6 = this;
                r4 = 1
                boolean r1 = r6.cancelled
                if (r1 == 0) goto L_0x0006
            L_0x0005:
                return
            L_0x0006:
                boolean r1 = r6.fastPath
                if (r1 != 0) goto L_0x0037
                monitor-enter(r6)
                boolean r1 = r6.cancelled     // Catch:{ all -> 0x0011 }
                if (r1 == 0) goto L_0x0014
                monitor-exit(r6)     // Catch:{ all -> 0x0011 }
                goto L_0x0005
            L_0x0011:
                r1 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x0011 }
                throw r1
            L_0x0014:
                long r2 = r6.index     // Catch:{ all -> 0x0011 }
                int r1 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1))
                if (r1 != 0) goto L_0x001c
                monitor-exit(r6)     // Catch:{ all -> 0x0011 }
                goto L_0x0005
            L_0x001c:
                boolean r1 = r6.emitting     // Catch:{ all -> 0x0011 }
                if (r1 == 0) goto L_0x0031
                io.reactivex.internal.util.AppendOnlyLinkedArrayList<java.lang.Object> r0 = r6.queue     // Catch:{ all -> 0x0011 }
                if (r0 != 0) goto L_0x002c
                io.reactivex.internal.util.AppendOnlyLinkedArrayList r0 = new io.reactivex.internal.util.AppendOnlyLinkedArrayList     // Catch:{ all -> 0x0011 }
                r1 = 4
                r0.<init>(r1)     // Catch:{ all -> 0x0011 }
                r6.queue = r0     // Catch:{ all -> 0x0011 }
            L_0x002c:
                r0.add(r7)     // Catch:{ all -> 0x0011 }
                monitor-exit(r6)     // Catch:{ all -> 0x0011 }
                goto L_0x0005
            L_0x0031:
                r1 = 1
                r6.next = r1     // Catch:{ all -> 0x0011 }
                monitor-exit(r6)     // Catch:{ all -> 0x0011 }
                r6.fastPath = r4
            L_0x0037:
                r6.test(r7)
                goto L_0x0005
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.subjects.BehaviorSubject.BehaviorDisposable.emitNext(java.lang.Object, long):void");
        }

        public boolean test(Object o) {
            return this.cancelled || NotificationLite.accept(o, this.downstream);
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0016, code lost:
            r0.forEachWhile(r2);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitLoop() {
            /*
                r2 = this;
            L_0x0000:
                boolean r1 = r2.cancelled
                if (r1 == 0) goto L_0x0005
            L_0x0004:
                return
            L_0x0005:
                monitor-enter(r2)
                io.reactivex.internal.util.AppendOnlyLinkedArrayList<java.lang.Object> r0 = r2.queue     // Catch:{ all -> 0x000f }
                if (r0 != 0) goto L_0x0012
                r1 = 0
                r2.emitting = r1     // Catch:{ all -> 0x000f }
                monitor-exit(r2)     // Catch:{ all -> 0x000f }
                goto L_0x0004
            L_0x000f:
                r1 = move-exception
                monitor-exit(r2)     // Catch:{ all -> 0x000f }
                throw r1
            L_0x0012:
                r1 = 0
                r2.queue = r1     // Catch:{ all -> 0x000f }
                monitor-exit(r2)     // Catch:{ all -> 0x000f }
                r0.forEachWhile(r2)
                goto L_0x0000
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.subjects.BehaviorSubject.BehaviorDisposable.emitLoop():void");
        }
    }
}
