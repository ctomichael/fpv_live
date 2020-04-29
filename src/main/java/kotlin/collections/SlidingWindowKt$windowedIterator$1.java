package kotlin.collections;

import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequenceScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00040\u0003H@¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "T", "Lkotlin/sequences/SequenceScope;", "", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
@DebugMetadata(c = "kotlin.collections.SlidingWindowKt$windowedIterator$1", f = "SlidingWindow.kt", i = {0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 4, 4, 4}, l = {33, 39, 46, 52, 55}, m = "invokeSuspend", n = {"$this$iterator", "gap", "buffer", "skip", "e", "$this$iterator", "gap", "buffer", "skip", "$this$iterator", "gap", "buffer", "e", "$this$iterator", "gap", "buffer", "$this$iterator", "gap", "buffer"}, s = {"L$0", "I$0", "L$1", "I$1", "L$2", "L$0", "I$0", "L$1", "I$1", "L$0", "I$0", "L$1", "L$2", "L$0", "I$0", "L$1", "L$0", "I$0", "L$1"})
/* compiled from: SlidingWindow.kt */
final class SlidingWindowKt$windowedIterator$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super List<? extends T>>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Iterator $iterator;
    final /* synthetic */ boolean $partialWindows;
    final /* synthetic */ boolean $reuseBuffer;
    final /* synthetic */ int $size;
    final /* synthetic */ int $step;
    int I$0;
    int I$1;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    int label;
    private SequenceScope p$;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    SlidingWindowKt$windowedIterator$1(int i, int i2, Iterator it2, boolean z, boolean z2, Continuation continuation) {
        super(2, continuation);
        this.$step = i;
        this.$size = i2;
        this.$iterator = it2;
        this.$reuseBuffer = z;
        this.$partialWindows = z2;
    }

    @NotNull
    public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> completion) {
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        SlidingWindowKt$windowedIterator$1 slidingWindowKt$windowedIterator$1 = new SlidingWindowKt$windowedIterator$1(this.$step, this.$size, this.$iterator, this.$reuseBuffer, this.$partialWindows, completion);
        SequenceScope sequenceScope = (SequenceScope) value;
        slidingWindowKt$windowedIterator$1.p$ = (SequenceScope) value;
        return slidingWindowKt$windowedIterator$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((SlidingWindowKt$windowedIterator$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:52:0x0120, code lost:
        if (r11.$partialWindows == false) goto L_0x00bc;
     */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00a9  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00ab  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00ce  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x011e  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x012a  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0031  */
    @org.jetbrains.annotations.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object invokeSuspend(@org.jetbrains.annotations.NotNull java.lang.Object r12) {
        /*
            r11 = this;
            r10 = 0
            r9 = 1
            java.lang.Object r8 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r5 = r11.label
            switch(r5) {
                case 0: goto L_0x0014;
                case 1: goto L_0x005b;
                case 2: goto L_0x00ad;
                case 3: goto L_0x0104;
                case 4: goto L_0x014f;
                case 5: goto L_0x0182;
                default: goto L_0x000b;
            }
        L_0x000b:
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r6 = "call to 'resume' before 'invoke' with coroutine"
            r5.<init>(r6)
            throw r5
        L_0x0014:
            kotlin.ResultKt.throwOnFailure(r12)
            kotlin.sequences.SequenceScope r0 = r11.p$
            int r5 = r11.$step
            int r6 = r11.$size
            int r3 = r5 - r6
            if (r3 < 0) goto L_0x00bf
            java.util.ArrayList r1 = new java.util.ArrayList
            int r5 = r11.$size
            r1.<init>(r5)
            r4 = 0
            java.util.Iterator r5 = r11.$iterator
        L_0x002b:
            boolean r6 = r5.hasNext()
            if (r6 == 0) goto L_0x0081
            java.lang.Object r2 = r5.next()
            if (r4 <= 0) goto L_0x003a
            int r4 = r4 + -1
            goto L_0x002b
        L_0x003a:
            r1.add(r2)
            int r6 = r1.size()
            int r7 = r11.$size
            if (r6 != r7) goto L_0x002b
            r11.L$0 = r0
            r11.I$0 = r3
            r11.L$1 = r1
            r11.I$1 = r4
            r11.L$2 = r2
            r11.L$3 = r5
            r11.label = r9
            java.lang.Object r6 = r0.yield(r1, r11)
            if (r6 != r8) goto L_0x0070
            r5 = r8
        L_0x005a:
            return r5
        L_0x005b:
            java.lang.Object r5 = r11.L$3
            java.util.Iterator r5 = (java.util.Iterator) r5
            java.lang.Object r2 = r11.L$2
            int r4 = r11.I$1
            java.lang.Object r1 = r11.L$1
            java.util.ArrayList r1 = (java.util.ArrayList) r1
            int r3 = r11.I$0
            java.lang.Object r0 = r11.L$0
            kotlin.sequences.SequenceScope r0 = (kotlin.sequences.SequenceScope) r0
            kotlin.ResultKt.throwOnFailure(r12)
        L_0x0070:
            boolean r6 = r11.$reuseBuffer
            if (r6 == 0) goto L_0x0079
            r1.clear()
        L_0x0077:
            r4 = r3
            goto L_0x002b
        L_0x0079:
            java.util.ArrayList r1 = new java.util.ArrayList
            int r6 = r11.$size
            r1.<init>(r6)
            goto L_0x0077
        L_0x0081:
            r5 = r1
            java.util.Collection r5 = (java.util.Collection) r5
            boolean r5 = r5.isEmpty()
            if (r5 != 0) goto L_0x00ab
        L_0x008a:
            if (r9 == 0) goto L_0x00bc
            boolean r5 = r11.$partialWindows
            if (r5 != 0) goto L_0x0098
            int r5 = r1.size()
            int r6 = r11.$size
            if (r5 != r6) goto L_0x00bc
        L_0x0098:
            r11.L$0 = r0
            r11.I$0 = r3
            r11.L$1 = r1
            r11.I$1 = r4
            r5 = 2
            r11.label = r5
            java.lang.Object r5 = r0.yield(r1, r11)
            if (r5 != r8) goto L_0x00bc
            r5 = r8
            goto L_0x005a
        L_0x00ab:
            r9 = r10
            goto L_0x008a
        L_0x00ad:
            int r4 = r11.I$1
            java.lang.Object r1 = r11.L$1
            java.util.ArrayList r1 = (java.util.ArrayList) r1
            int r3 = r11.I$0
            java.lang.Object r0 = r11.L$0
            kotlin.sequences.SequenceScope r0 = (kotlin.sequences.SequenceScope) r0
            kotlin.ResultKt.throwOnFailure(r12)
        L_0x00bc:
            kotlin.Unit r5 = kotlin.Unit.INSTANCE
            goto L_0x005a
        L_0x00bf:
            kotlin.collections.RingBuffer r1 = new kotlin.collections.RingBuffer
            int r5 = r11.$size
            r1.<init>(r5)
            java.util.Iterator r7 = r11.$iterator
        L_0x00c8:
            boolean r5 = r7.hasNext()
            if (r5 == 0) goto L_0x011e
            java.lang.Object r2 = r7.next()
            r1.add(r2)
            boolean r5 = r1.isFull()
            if (r5 == 0) goto L_0x0193
            boolean r5 = r11.$reuseBuffer
            if (r5 == 0) goto L_0x00f8
            r5 = r1
            java.util.List r5 = (java.util.List) r5
        L_0x00e2:
            r11.L$0 = r0
            r11.I$0 = r3
            r11.L$1 = r1
            r11.L$2 = r2
            r11.L$3 = r7
            r6 = 3
            r11.label = r6
            java.lang.Object r5 = r0.yield(r5, r11)
            if (r5 != r8) goto L_0x0191
            r5 = r8
            goto L_0x005a
        L_0x00f8:
            java.util.ArrayList r6 = new java.util.ArrayList
            r5 = r1
            java.util.Collection r5 = (java.util.Collection) r5
            r6.<init>(r5)
            r5 = r6
            java.util.List r5 = (java.util.List) r5
            goto L_0x00e2
        L_0x0104:
            java.lang.Object r5 = r11.L$3
            java.util.Iterator r5 = (java.util.Iterator) r5
            java.lang.Object r2 = r11.L$2
            java.lang.Object r1 = r11.L$1
            kotlin.collections.RingBuffer r1 = (kotlin.collections.RingBuffer) r1
            int r3 = r11.I$0
            java.lang.Object r0 = r11.L$0
            kotlin.sequences.SequenceScope r0 = (kotlin.sequences.SequenceScope) r0
            kotlin.ResultKt.throwOnFailure(r12)
        L_0x0117:
            int r6 = r11.$step
            r1.removeFirst(r6)
        L_0x011c:
            r7 = r5
            goto L_0x00c8
        L_0x011e:
            boolean r5 = r11.$partialWindows
            if (r5 == 0) goto L_0x00bc
        L_0x0122:
            int r5 = r1.size()
            int r6 = r11.$step
            if (r5 <= r6) goto L_0x0162
            boolean r5 = r11.$reuseBuffer
            if (r5 == 0) goto L_0x0143
            r5 = r1
            java.util.List r5 = (java.util.List) r5
        L_0x0131:
            r11.L$0 = r0
            r11.I$0 = r3
            r11.L$1 = r1
            r6 = 4
            r11.label = r6
            java.lang.Object r5 = r0.yield(r5, r11)
            if (r5 != r8) goto L_0x015c
            r5 = r8
            goto L_0x005a
        L_0x0143:
            java.util.ArrayList r6 = new java.util.ArrayList
            r5 = r1
            java.util.Collection r5 = (java.util.Collection) r5
            r6.<init>(r5)
            r5 = r6
            java.util.List r5 = (java.util.List) r5
            goto L_0x0131
        L_0x014f:
            java.lang.Object r1 = r11.L$1
            kotlin.collections.RingBuffer r1 = (kotlin.collections.RingBuffer) r1
            int r3 = r11.I$0
            java.lang.Object r0 = r11.L$0
            kotlin.sequences.SequenceScope r0 = (kotlin.sequences.SequenceScope) r0
            kotlin.ResultKt.throwOnFailure(r12)
        L_0x015c:
            int r5 = r11.$step
            r1.removeFirst(r5)
            goto L_0x0122
        L_0x0162:
            r5 = r1
            java.util.Collection r5 = (java.util.Collection) r5
            boolean r5 = r5.isEmpty()
            if (r5 != 0) goto L_0x0180
            r5 = r9
        L_0x016c:
            if (r5 == 0) goto L_0x00bc
            r11.L$0 = r0
            r11.I$0 = r3
            r11.L$1 = r1
            r5 = 5
            r11.label = r5
            java.lang.Object r5 = r0.yield(r1, r11)
            if (r5 != r8) goto L_0x00bc
            r5 = r8
            goto L_0x005a
        L_0x0180:
            r5 = r10
            goto L_0x016c
        L_0x0182:
            java.lang.Object r1 = r11.L$1
            kotlin.collections.RingBuffer r1 = (kotlin.collections.RingBuffer) r1
            int r3 = r11.I$0
            java.lang.Object r0 = r11.L$0
            kotlin.sequences.SequenceScope r0 = (kotlin.sequences.SequenceScope) r0
            kotlin.ResultKt.throwOnFailure(r12)
            goto L_0x00bc
        L_0x0191:
            r5 = r7
            goto L_0x0117
        L_0x0193:
            r5 = r7
            goto L_0x011c
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.collections.SlidingWindowKt$windowedIterator$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
