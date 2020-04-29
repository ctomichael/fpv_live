package kotlin.sequences;

import dji.publics.LogReport.base.Fields;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0004H@¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "T", "R", "Lkotlin/sequences/SequenceScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
@DebugMetadata(c = "kotlin.sequences.SequencesKt___SequencesKt$zipWithNext$2", f = "_Sequences.kt", i = {0, 0, 0, 0}, l = {1702}, m = "invokeSuspend", n = {"$this$result", "iterator", Fields.Dgo_in2_battery.current, "next"}, s = {"L$0", "L$1", "L$2", "L$3"})
/* compiled from: _Sequences.kt */
final class SequencesKt___SequencesKt$zipWithNext$2 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super R>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Sequence $this_zipWithNext;
    final /* synthetic */ Function2 $transform;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    int label;
    private SequenceScope p$;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    SequencesKt___SequencesKt$zipWithNext$2(Sequence sequence, Function2 function2, Continuation continuation) {
        super(2, continuation);
        this.$this_zipWithNext = sequence;
        this.$transform = function2;
    }

    @NotNull
    public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> completion) {
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        SequencesKt___SequencesKt$zipWithNext$2 sequencesKt___SequencesKt$zipWithNext$2 = new SequencesKt___SequencesKt$zipWithNext$2(this.$this_zipWithNext, this.$transform, completion);
        SequenceScope sequenceScope = (SequenceScope) value;
        sequencesKt___SequencesKt$zipWithNext$2.p$ = (SequenceScope) value;
        return sequencesKt___SequencesKt$zipWithNext$2;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((SequencesKt___SequencesKt$zipWithNext$2) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0030  */
    @org.jetbrains.annotations.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object invokeSuspend(@org.jetbrains.annotations.NotNull java.lang.Object r8) {
        /*
            r7 = this;
            java.lang.Object r4 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r5 = r7.label
            switch(r5) {
                case 0: goto L_0x0012;
                case 1: goto L_0x004d;
                default: goto L_0x0009;
            }
        L_0x0009:
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException
            java.lang.String r5 = "call to 'resume' before 'invoke' with coroutine"
            r4.<init>(r5)
            throw r4
        L_0x0012:
            kotlin.ResultKt.throwOnFailure(r8)
            kotlin.sequences.SequenceScope r0 = r7.p$
            kotlin.sequences.Sequence r5 = r7.$this_zipWithNext
            java.util.Iterator r2 = r5.iterator()
            boolean r5 = r2.hasNext()
            if (r5 != 0) goto L_0x0026
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
        L_0x0025:
            return r4
        L_0x0026:
            java.lang.Object r1 = r2.next()
        L_0x002a:
            boolean r5 = r2.hasNext()
            if (r5 == 0) goto L_0x005d
            java.lang.Object r3 = r2.next()
            kotlin.jvm.functions.Function2 r5 = r7.$transform
            java.lang.Object r5 = r5.invoke(r1, r3)
            r7.L$0 = r0
            r7.L$1 = r2
            r7.L$2 = r1
            r7.L$3 = r3
            r6 = 1
            r7.label = r6
            java.lang.Object r5 = r0.yield(r5, r7)
            if (r5 == r4) goto L_0x0025
        L_0x004b:
            r1 = r3
            goto L_0x002a
        L_0x004d:
            java.lang.Object r3 = r7.L$3
            java.lang.Object r1 = r7.L$2
            java.lang.Object r2 = r7.L$1
            java.util.Iterator r2 = (java.util.Iterator) r2
            java.lang.Object r0 = r7.L$0
            kotlin.sequences.SequenceScope r0 = (kotlin.sequences.SequenceScope) r0
            kotlin.ResultKt.throwOnFailure(r8)
            goto L_0x004b
        L_0x005d:
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
            goto L_0x0025
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.sequences.SequencesKt___SequencesKt$zipWithNext$2.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
