package dji.thirdparty.io.reactivex.internal.operators.observable;

import dji.thirdparty.io.reactivex.ObservableSource;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.Function;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.internal.util.AtomicThrowable;
import dji.thirdparty.io.reactivex.internal.util.HalfSerializer;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

public final class ObservableWithLatestFromMany<T, R> extends AbstractObservableWithUpstream<T, R> {
    final Function<? super Object[], R> combiner;
    final ObservableSource<?>[] otherArray;
    final Iterable<? extends ObservableSource<?>> otherIterable;

    public ObservableWithLatestFromMany(ObservableSource observableSource, ObservableSource<?>[] otherArray2, Function function) {
        super(observableSource);
        this.otherArray = otherArray2;
        this.otherIterable = null;
        this.combiner = function;
    }

    public ObservableWithLatestFromMany(ObservableSource observableSource, Iterable<? extends ObservableSource<?>> otherIterable2, Function function) {
        super(observableSource);
        this.otherArray = null;
        this.otherIterable = otherIterable2;
        this.combiner = function;
    }

    /* JADX WARN: Type inference failed for: r8v9, types: [java.lang.Object[]], assign insn: 0x0022: INVOKE  (r8v9 ? I:java.lang.Object[]) = 
      (r5v3 'others' dji.thirdparty.io.reactivex.ObservableSource<?>[] A[D('others' dji.thirdparty.io.reactivex.ObservableSource<?>[])])
      (r8v8 int)
     type: STATIC call: java.util.Arrays.copyOf(java.lang.Object[], int):java.lang.Object[] */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void subscribeActual(dji.thirdparty.io.reactivex.Observer<? super R> r12) {
        /*
            r11 = this;
            dji.thirdparty.io.reactivex.ObservableSource<?>[] r5 = r11.otherArray
            r3 = 0
            if (r5 != 0) goto L_0x004b
            r8 = 8
            dji.thirdparty.io.reactivex.ObservableSource[] r5 = new dji.thirdparty.io.reactivex.ObservableSource[r8]
            java.lang.Iterable<? extends dji.thirdparty.io.reactivex.ObservableSource<?>> r8 = r11.otherIterable     // Catch:{ Throwable -> 0x0043 }
            java.util.Iterator r2 = r8.iterator()     // Catch:{ Throwable -> 0x0043 }
            r4 = r3
        L_0x0010:
            boolean r8 = r2.hasNext()     // Catch:{ Throwable -> 0x0060 }
            if (r8 == 0) goto L_0x0030
            java.lang.Object r6 = r2.next()     // Catch:{ Throwable -> 0x0060 }
            dji.thirdparty.io.reactivex.ObservableSource r6 = (dji.thirdparty.io.reactivex.ObservableSource) r6     // Catch:{ Throwable -> 0x0060 }
            int r8 = r5.length     // Catch:{ Throwable -> 0x0060 }
            if (r4 != r8) goto L_0x002a
            int r8 = r4 >> 1
            int r8 = r8 + r4
            java.lang.Object[] r8 = java.util.Arrays.copyOf(r5, r8)     // Catch:{ Throwable -> 0x0060 }
            r0 = r8
            dji.thirdparty.io.reactivex.ObservableSource[] r0 = (dji.thirdparty.io.reactivex.ObservableSource[]) r0     // Catch:{ Throwable -> 0x0060 }
            r5 = r0
        L_0x002a:
            int r3 = r4 + 1
            r5[r4] = r6     // Catch:{ Throwable -> 0x0043 }
            r4 = r3
            goto L_0x0010
        L_0x0030:
            r3 = r4
        L_0x0031:
            if (r3 != 0) goto L_0x004d
            dji.thirdparty.io.reactivex.internal.operators.observable.ObservableMap r8 = new dji.thirdparty.io.reactivex.internal.operators.observable.ObservableMap
            dji.thirdparty.io.reactivex.ObservableSource r9 = r11.source
            dji.thirdparty.io.reactivex.internal.operators.observable.ObservableWithLatestFromMany$1 r10 = new dji.thirdparty.io.reactivex.internal.operators.observable.ObservableWithLatestFromMany$1
            r10.<init>()
            r8.<init>(r9, r10)
            r8.subscribeActual(r12)
        L_0x0042:
            return
        L_0x0043:
            r1 = move-exception
        L_0x0044:
            dji.thirdparty.io.reactivex.exceptions.Exceptions.throwIfFatal(r1)
            dji.thirdparty.io.reactivex.internal.disposables.EmptyDisposable.error(r1, r12)
            goto L_0x0042
        L_0x004b:
            int r3 = r5.length
            goto L_0x0031
        L_0x004d:
            dji.thirdparty.io.reactivex.internal.operators.observable.ObservableWithLatestFromMany$WithLatestFromObserver r7 = new dji.thirdparty.io.reactivex.internal.operators.observable.ObservableWithLatestFromMany$WithLatestFromObserver
            dji.thirdparty.io.reactivex.functions.Function<? super java.lang.Object[], R> r8 = r11.combiner
            r7.<init>(r12, r8, r3)
            r12.onSubscribe(r7)
            r7.subscribe(r5, r3)
            dji.thirdparty.io.reactivex.ObservableSource r8 = r11.source
            r8.subscribe(r7)
            goto L_0x0042
        L_0x0060:
            r1 = move-exception
            r3 = r4
            goto L_0x0044
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.internal.operators.observable.ObservableWithLatestFromMany.subscribeActual(dji.thirdparty.io.reactivex.Observer):void");
    }

    static final class WithLatestFromObserver<T, R> extends AtomicInteger implements Observer<T>, Disposable {
        private static final long serialVersionUID = 1577321883966341961L;
        final Observer<? super R> actual;
        final Function<? super Object[], R> combiner;
        final AtomicReference<Disposable> d;
        volatile boolean done;
        final AtomicThrowable error;
        final WithLatestInnerObserver[] observers;
        final AtomicReferenceArray<Object> values;

        WithLatestFromObserver(Observer<? super R> actual2, Function<? super Object[], R> combiner2, int n) {
            this.actual = actual2;
            this.combiner = combiner2;
            WithLatestInnerObserver[] s = new WithLatestInnerObserver[n];
            for (int i = 0; i < n; i++) {
                s[i] = new WithLatestInnerObserver(this, i);
            }
            this.observers = s;
            this.values = new AtomicReferenceArray<>(n);
            this.d = new AtomicReference<>();
            this.error = new AtomicThrowable();
        }

        /* access modifiers changed from: package-private */
        public void subscribe(ObservableSource<?>[] others, int n) {
            WithLatestInnerObserver[] observers2 = this.observers;
            AtomicReference<Disposable> s = this.d;
            for (int i = 0; i < n && !DisposableHelper.isDisposed(s.get()) && !this.done; i++) {
                others[i].subscribe(observers2[i]);
            }
        }

        public void onSubscribe(Disposable d2) {
            DisposableHelper.setOnce(this.d, d2);
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
                    HalfSerializer.onNext(this.actual, ObjectHelper.requireNonNull(this.combiner.apply(objects), "combiner returned a null value"), this, this.error);
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
            HalfSerializer.onError(this.actual, t, this, this.error);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                cancelAllBut(-1);
                HalfSerializer.onComplete(this.actual, this, this.error);
            }
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed(this.d.get());
        }

        public void dispose() {
            DisposableHelper.dispose(this.d);
            for (WithLatestInnerObserver s : this.observers) {
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
            DisposableHelper.dispose(this.d);
            cancelAllBut(index);
            HalfSerializer.onError(this.actual, t, this, this.error);
        }

        /* access modifiers changed from: package-private */
        public void innerComplete(int index, boolean nonEmpty) {
            if (!nonEmpty) {
                this.done = true;
                cancelAllBut(index);
                HalfSerializer.onComplete(this.actual, this, this.error);
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
}
