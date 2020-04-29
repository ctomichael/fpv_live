package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;

public final class MaybeZipIterable<T, R> extends Maybe<R> {
    final Iterable<? extends MaybeSource<? extends T>> sources;
    final Function<? super Object[], ? extends R> zipper;

    public MaybeZipIterable(Iterable<? extends MaybeSource<? extends T>> sources2, Function<? super Object[], ? extends R> zipper2) {
        this.sources = sources2;
        this.zipper = zipper2;
    }

    /* JADX WARN: Type inference failed for: r8v13, types: [java.lang.Object[]], assign insn: 0x002d: INVOKE  (r8v13 ? I:java.lang.Object[]) = (r1v1 'a' io.reactivex.MaybeSource<? extends T>[] A[D('a' io.reactivex.MaybeSource<? extends T>[])]), (r8v12 int) type: STATIC call: java.util.Arrays.copyOf(java.lang.Object[], int):java.lang.Object[] */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void subscribeActual(io.reactivex.MaybeObserver<? super R> r12) {
        /*
            r11 = this;
            r8 = 8
            io.reactivex.MaybeSource[] r1 = new io.reactivex.MaybeSource[r8]
            r4 = 0
            java.lang.Iterable<? extends io.reactivex.MaybeSource<? extends T>> r8 = r11.sources     // Catch:{ Throwable -> 0x003b }
            java.util.Iterator r9 = r8.iterator()     // Catch:{ Throwable -> 0x003b }
            r5 = r4
        L_0x000c:
            boolean r8 = r9.hasNext()     // Catch:{ Throwable -> 0x0082 }
            if (r8 == 0) goto L_0x0043
            java.lang.Object r7 = r9.next()     // Catch:{ Throwable -> 0x0082 }
            io.reactivex.MaybeSource r7 = (io.reactivex.MaybeSource) r7     // Catch:{ Throwable -> 0x0082 }
            if (r7 != 0) goto L_0x0027
            java.lang.NullPointerException r8 = new java.lang.NullPointerException     // Catch:{ Throwable -> 0x0082 }
            java.lang.String r9 = "One of the sources is null"
            r8.<init>(r9)     // Catch:{ Throwable -> 0x0082 }
            io.reactivex.internal.disposables.EmptyDisposable.error(r8, r12)     // Catch:{ Throwable -> 0x0082 }
            r4 = r5
        L_0x0026:
            return
        L_0x0027:
            int r8 = r1.length     // Catch:{ Throwable -> 0x0082 }
            if (r5 != r8) goto L_0x0035
            int r8 = r5 >> 2
            int r8 = r8 + r5
            java.lang.Object[] r8 = java.util.Arrays.copyOf(r1, r8)     // Catch:{ Throwable -> 0x0082 }
            r0 = r8
            io.reactivex.MaybeSource[] r0 = (io.reactivex.MaybeSource[]) r0     // Catch:{ Throwable -> 0x0082 }
            r1 = r0
        L_0x0035:
            int r4 = r5 + 1
            r1[r5] = r7     // Catch:{ Throwable -> 0x003b }
            r5 = r4
            goto L_0x000c
        L_0x003b:
            r2 = move-exception
        L_0x003c:
            io.reactivex.exceptions.Exceptions.throwIfFatal(r2)
            io.reactivex.internal.disposables.EmptyDisposable.error(r2, r12)
            goto L_0x0026
        L_0x0043:
            if (r5 != 0) goto L_0x004a
            io.reactivex.internal.disposables.EmptyDisposable.complete(r12)
            r4 = r5
            goto L_0x0026
        L_0x004a:
            r8 = 1
            if (r5 != r8) goto L_0x005f
            r8 = 0
            r8 = r1[r8]
            io.reactivex.internal.operators.maybe.MaybeMap$MapMaybeObserver r9 = new io.reactivex.internal.operators.maybe.MaybeMap$MapMaybeObserver
            io.reactivex.internal.operators.maybe.MaybeZipIterable$SingletonArrayFunc r10 = new io.reactivex.internal.operators.maybe.MaybeZipIterable$SingletonArrayFunc
            r10.<init>()
            r9.<init>(r12, r10)
            r8.subscribe(r9)
            r4 = r5
            goto L_0x0026
        L_0x005f:
            io.reactivex.internal.operators.maybe.MaybeZipArray$ZipCoordinator r6 = new io.reactivex.internal.operators.maybe.MaybeZipArray$ZipCoordinator
            io.reactivex.functions.Function<? super java.lang.Object[], ? extends R> r8 = r11.zipper
            r6.<init>(r12, r5, r8)
            r12.onSubscribe(r6)
            r3 = 0
        L_0x006a:
            if (r3 >= r5) goto L_0x0080
            boolean r8 = r6.isDisposed()
            if (r8 == 0) goto L_0x0074
            r4 = r5
            goto L_0x0026
        L_0x0074:
            r8 = r1[r3]
            io.reactivex.internal.operators.maybe.MaybeZipArray$ZipMaybeObserver<T>[] r9 = r6.observers
            r9 = r9[r3]
            r8.subscribe(r9)
            int r3 = r3 + 1
            goto L_0x006a
        L_0x0080:
            r4 = r5
            goto L_0x0026
        L_0x0082:
            r2 = move-exception
            r4 = r5
            goto L_0x003c
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.maybe.MaybeZipIterable.subscribeActual(io.reactivex.MaybeObserver):void");
    }

    final class SingletonArrayFunc implements Function<T, R> {
        SingletonArrayFunc() {
        }

        public R apply(T t) throws Exception {
            return ObjectHelper.requireNonNull(MaybeZipIterable.this.zipper.apply(new Object[]{t}), "The zipper returned a null value");
        }
    }
}
