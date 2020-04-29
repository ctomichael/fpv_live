package dji.thirdparty.io.reactivex.internal.operators.maybe;

import dji.thirdparty.io.reactivex.Maybe;
import dji.thirdparty.io.reactivex.MaybeSource;
import dji.thirdparty.io.reactivex.functions.Function;

public final class MaybeZipIterable<T, R> extends Maybe<R> {
    final Iterable<? extends MaybeSource<? extends T>> sources;
    final Function<? super Object[], ? extends R> zipper;

    public MaybeZipIterable(Iterable<? extends MaybeSource<? extends T>> sources2, Function<? super Object[], ? extends R> zipper2) {
        this.sources = sources2;
        this.zipper = zipper2;
    }

    /* JADX WARN: Type inference failed for: r9v12, types: [java.lang.Object[]], assign insn: 0x001e: INVOKE  (r9v12 ? I:java.lang.Object[]) = 
      (r1v1 'a' dji.thirdparty.io.reactivex.MaybeSource<? extends T>[] A[D('a' dji.thirdparty.io.reactivex.MaybeSource<? extends T>[])])
      (r9v11 int)
     type: STATIC call: java.util.Arrays.copyOf(java.lang.Object[], int):java.lang.Object[] */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void subscribeActual(dji.thirdparty.io.reactivex.MaybeObserver<? super R> r13) {
        /*
            r12 = this;
            r9 = 8
            dji.thirdparty.io.reactivex.MaybeSource[] r1 = new dji.thirdparty.io.reactivex.MaybeSource[r9]
            r5 = 0
            java.lang.Iterable<? extends dji.thirdparty.io.reactivex.MaybeSource<? extends T>> r9 = r12.sources     // Catch:{ Throwable -> 0x002c }
            java.util.Iterator r4 = r9.iterator()     // Catch:{ Throwable -> 0x002c }
            r6 = r5
        L_0x000c:
            boolean r9 = r4.hasNext()     // Catch:{ Throwable -> 0x0073 }
            if (r9 == 0) goto L_0x0034
            java.lang.Object r8 = r4.next()     // Catch:{ Throwable -> 0x0073 }
            dji.thirdparty.io.reactivex.MaybeSource r8 = (dji.thirdparty.io.reactivex.MaybeSource) r8     // Catch:{ Throwable -> 0x0073 }
            int r9 = r1.length     // Catch:{ Throwable -> 0x0073 }
            if (r6 != r9) goto L_0x0026
            int r9 = r6 >> 2
            int r9 = r9 + r6
            java.lang.Object[] r9 = java.util.Arrays.copyOf(r1, r9)     // Catch:{ Throwable -> 0x0073 }
            r0 = r9
            dji.thirdparty.io.reactivex.MaybeSource[] r0 = (dji.thirdparty.io.reactivex.MaybeSource[]) r0     // Catch:{ Throwable -> 0x0073 }
            r1 = r0
        L_0x0026:
            int r5 = r6 + 1
            r1[r6] = r8     // Catch:{ Throwable -> 0x002c }
            r6 = r5
            goto L_0x000c
        L_0x002c:
            r2 = move-exception
        L_0x002d:
            dji.thirdparty.io.reactivex.exceptions.Exceptions.throwIfFatal(r2)
            dji.thirdparty.io.reactivex.internal.disposables.EmptyDisposable.error(r2, r13)
        L_0x0033:
            return
        L_0x0034:
            if (r6 != 0) goto L_0x003b
            dji.thirdparty.io.reactivex.internal.disposables.EmptyDisposable.complete(r13)
            r5 = r6
            goto L_0x0033
        L_0x003b:
            r9 = 1
            if (r6 != r9) goto L_0x0050
            r9 = 0
            r9 = r1[r9]
            dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeMap$MapMaybeObserver r10 = new dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeMap$MapMaybeObserver
            dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeZipIterable$1 r11 = new dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeZipIterable$1
            r11.<init>()
            r10.<init>(r13, r11)
            r9.subscribe(r10)
            r5 = r6
            goto L_0x0033
        L_0x0050:
            dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeZipArray$ZipCoordinator r7 = new dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeZipArray$ZipCoordinator
            dji.thirdparty.io.reactivex.functions.Function<? super java.lang.Object[], ? extends R> r9 = r12.zipper
            r7.<init>(r13, r6, r9)
            r13.onSubscribe(r7)
            r3 = 0
        L_0x005b:
            if (r3 >= r6) goto L_0x0071
            boolean r9 = r7.isDisposed()
            if (r9 == 0) goto L_0x0065
            r5 = r6
            goto L_0x0033
        L_0x0065:
            r9 = r1[r3]
            dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeZipArray$ZipMaybeObserver<T>[] r10 = r7.observers
            r10 = r10[r3]
            r9.subscribe(r10)
            int r3 = r3 + 1
            goto L_0x005b
        L_0x0071:
            r5 = r6
            goto L_0x0033
        L_0x0073:
            r2 = move-exception
            r5 = r6
            goto L_0x002d
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.internal.operators.maybe.MaybeZipIterable.subscribeActual(dji.thirdparty.io.reactivex.MaybeObserver):void");
    }
}
