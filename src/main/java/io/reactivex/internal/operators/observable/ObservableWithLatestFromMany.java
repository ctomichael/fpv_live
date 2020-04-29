package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.HalfSerializer;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

public final class ObservableWithLatestFromMany<T, R> extends AbstractObservableWithUpstream<T, R> {
    @NonNull
    final Function<? super Object[], R> combiner;
    @Nullable
    final ObservableSource<?>[] otherArray;
    @Nullable
    final Iterable<? extends ObservableSource<?>> otherIterable;

    public ObservableWithLatestFromMany(@NonNull ObservableSource observableSource, @NonNull ObservableSource<?>[] otherArray2, @NonNull Function function) {
        super(observableSource);
        this.otherArray = otherArray2;
        this.otherIterable = null;
        this.combiner = function;
    }

    public ObservableWithLatestFromMany(@NonNull ObservableSource observableSource, @NonNull Iterable<? extends ObservableSource<?>> otherIterable2, @NonNull Function function) {
        super(observableSource);
        this.otherArray = null;
        this.otherIterable = otherIterable2;
        this.combiner = function;
    }

    /* JADX WARN: Type inference failed for: r7v9, types: [java.lang.Object[]], assign insn: 0x0022: INVOKE  (r7v9 ? I:java.lang.Object[]) = (r4v3 'others' io.reactivex.ObservableSource<?>[] A[D('others' io.reactivex.ObservableSource<?>[])]), (r7v8 int) type: STATIC call: java.util.Arrays.copyOf(java.lang.Object[], int):java.lang.Object[] */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void subscribeActual(io.reactivex.Observer<? super R> r11) {
        /*
            r10 = this;
            io.reactivex.ObservableSource<?>[] r4 = r10.otherArray
            r2 = 0
            if (r4 != 0) goto L_0x004b
            r7 = 8
            io.reactivex.ObservableSource[] r4 = new io.reactivex.ObservableSource[r7]
            java.lang.Iterable<? extends io.reactivex.ObservableSource<?>> r7 = r10.otherIterable     // Catch:{ Throwable -> 0x0043 }
            java.util.Iterator r8 = r7.iterator()     // Catch:{ Throwable -> 0x0043 }
            r3 = r2
        L_0x0010:
            boolean r7 = r8.hasNext()     // Catch:{ Throwable -> 0x0060 }
            if (r7 == 0) goto L_0x0030
            java.lang.Object r5 = r8.next()     // Catch:{ Throwable -> 0x0060 }
            io.reactivex.ObservableSource r5 = (io.reactivex.ObservableSource) r5     // Catch:{ Throwable -> 0x0060 }
            int r7 = r4.length     // Catch:{ Throwable -> 0x0060 }
            if (r3 != r7) goto L_0x002a
            int r7 = r3 >> 1
            int r7 = r7 + r3
            java.lang.Object[] r7 = java.util.Arrays.copyOf(r4, r7)     // Catch:{ Throwable -> 0x0060 }
            r0 = r7
            io.reactivex.ObservableSource[] r0 = (io.reactivex.ObservableSource[]) r0     // Catch:{ Throwable -> 0x0060 }
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
            io.reactivex.internal.operators.observable.ObservableMap r7 = new io.reactivex.internal.operators.observable.ObservableMap
            io.reactivex.ObservableSource r8 = r10.source
            io.reactivex.internal.operators.observable.ObservableWithLatestFromMany$SingletonArrayFunc r9 = new io.reactivex.internal.operators.observable.ObservableWithLatestFromMany$SingletonArrayFunc
            r9.<init>()
            r7.<init>(r8, r9)
            r7.subscribeActual(r11)
        L_0x0042:
            return
        L_0x0043:
            r1 = move-exception
        L_0x0044:
            io.reactivex.exceptions.Exceptions.throwIfFatal(r1)
            io.reactivex.internal.disposables.EmptyDisposable.error(r1, r11)
            goto L_0x0042
        L_0x004b:
            int r2 = r4.length
            goto L_0x0031
        L_0x004d:
            io.reactivex.internal.operators.observable.ObservableWithLatestFromMany$WithLatestFromObserver r6 = new io.reactivex.internal.operators.observable.ObservableWithLatestFromMany$WithLatestFromObserver
            io.reactivex.functions.Function<? super java.lang.Object[], R> r7 = r10.combiner
            r6.<init>(r11, r7, r2)
            r11.onSubscribe(r6)
            r6.subscribe(r4, r2)
            io.reactivex.ObservableSource r7 = r10.source
            r7.subscribe(r6)
            goto L_0x0042
        L_0x0060:
            r1 = move-exception
            r2 = r3
            goto L_0x0044
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.observable.ObservableWithLatestFromMany.subscribeActual(io.reactivex.Observer):void");
    }

    static final class WithLatestFromObserver<T, R> extends AtomicInteger implements Observer<T>, Disposable {
        private static final long serialVersionUID = 1577321883966341961L;
        final Function<? super Object[], R> combiner;
        volatile boolean done;
        final Observer<? super R> downstream;
        final AtomicThrowable error;
        final WithLatestInnerObserver[] observers;
        final AtomicReference<Disposable> upstream;
        final AtomicReferenceArray<Object> values;

        WithLatestFromObserver(Observer<? super R> actual, Function<? super Object[], R> combiner2, int n) {
            this.downstream = actual;
            this.combiner = combiner2;
            WithLatestInnerObserver[] s = new WithLatestInnerObserver[n];
            for (int i = 0; i < n; i++) {
                s[i] = new WithLatestInnerObserver(this, i);
            }
            this.observers = s;
            this.values = new AtomicReferenceArray<>(n);
            this.upstream = new AtomicReference<>();
            this.error = new AtomicThrowable();
        }

        /* access modifiers changed from: package-private */
        public void subscribe(ObservableSource<?>[] others, int n) {
            WithLatestInnerObserver[] observers2 = this.observers;
            AtomicReference<Disposable> upstream2 = this.upstream;
            for (int i = 0; i < n && !DisposableHelper.isDisposed(upstream2.get()) && !this.done; i++) {
                others[i].subscribe(observers2[i]);
            }
        }

        public void onSubscribe(Disposable d) {
            DisposableHelper.setOnce(this.upstream, d);
        }

        public void onNext(T t) {
            if (!this.done) {
                AtomicReferenceArray<Object> ara = this.values;
                int n = ara.length();
                Object[] objects = new Object[(n + 1)];
                objects[0] = t;
                int i = 0;
                while (i < n) {
                    Object o = ara.get(i);
                    if (o != null) {
                        objects[i + 1] = o;
                        i++;
                    } else {
                        return;
                    }
                }
                try {
                    HalfSerializer.onNext(this.downstream, ObjectHelper.requireNonNull(this.combiner.apply(objects), "combiner returned a null value"), this, this.error);
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    dispose();
                    onError(ex);
                }
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

        public boolean isDisposed() {
            return DisposableHelper.isDisposed(this.upstream.get());
        }

        public void dispose() {
            DisposableHelper.dispose(this.upstream);
            for (WithLatestInnerObserver observer : this.observers) {
                observer.dispose();
            }
        }

        /* access modifiers changed from: package-private */
        public void innerNext(int index, Object o) {
            this.values.set(index, o);
        }

        /* access modifiers changed from: package-private */
        public void innerError(int index, Throwable t) {
            this.done = true;
            DisposableHelper.dispose(this.upstream);
            cancelAllBut(index);
            HalfSerializer.onError(this.downstream, t, this, this.error);
        }

        /* access modifiers changed from: package-private */
        public void innerComplete(int index, boolean nonEmpty) {
            if (!nonEmpty) {
                this.done = true;
                cancelAllBut(index);
                HalfSerializer.onComplete(this.downstream, this, this.error);
            }
        }

        /* access modifiers changed from: package-private */
        public void cancelAllBut(int index) {
            WithLatestInnerObserver[] observers2 = this.observers;
            for (int i = 0; i < observers2.length; i++) {
                if (i != index) {
                    observers2[i].dispose();
                }
            }
        }
    }

    static final class WithLatestInnerObserver extends AtomicReference<Disposable> implements Observer<Object> {
        private static final long serialVersionUID = 3256684027868224024L;
        boolean hasValue;
        final int index;
        final WithLatestFromObserver<?, ?> parent;

        WithLatestInnerObserver(WithLatestFromObserver<?, ?> parent2, int index2) {
            this.parent = parent2;
            this.index = index2;
        }

        public void onSubscribe(Disposable d) {
            DisposableHelper.setOnce(this, d);
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

        public void dispose() {
            DisposableHelper.dispose(this);
        }
    }

    final class SingletonArrayFunc implements Function<T, R> {
        SingletonArrayFunc() {
        }

        public R apply(T t) throws Exception {
            return ObjectHelper.requireNonNull(ObservableWithLatestFromMany.this.combiner.apply(new Object[]{t}), "The combiner returned a null value");
        }
    }
}
