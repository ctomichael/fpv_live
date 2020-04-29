package kotlin.internal;

import java.lang.reflect.Method;
import java.util.regex.MatchResult;
import kotlin.Metadata;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.FallbackThreadLocalRandom;
import kotlin.random.Random;
import kotlin.text.MatchGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0010\u0018\u00002\u00020\u0001:\u0001\u0010B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0016J\b\u0010\b\u001a\u00020\tH\u0016J\u001a\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016¨\u0006\u0011"}, d2 = {"Lkotlin/internal/PlatformImplementations;", "", "()V", "addSuppressed", "", "cause", "", "exception", "defaultPlatformRandom", "Lkotlin/random/Random;", "getMatchResultNamedGroup", "Lkotlin/text/MatchGroup;", "matchResult", "Ljava/util/regex/MatchResult;", "name", "", "ReflectAddSuppressedMethod", "kotlin-stdlib"}, k = 1, mv = {1, 1, 15})
/* compiled from: PlatformImplementations.kt */
public class PlatformImplementations {

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bÂ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0012\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lkotlin/internal/PlatformImplementations$ReflectAddSuppressedMethod;", "", "()V", "method", "Ljava/lang/reflect/Method;", "kotlin-stdlib"}, k = 1, mv = {1, 1, 15})
    /* compiled from: PlatformImplementations.kt */
    private static final class ReflectAddSuppressedMethod {
        public static final ReflectAddSuppressedMethod INSTANCE = new ReflectAddSuppressedMethod();
        @Nullable
        @JvmField
        public static final Method method;

        /* JADX WARNING: Removed duplicated region for block: B:11:0x004e A[LOOP:0: B:1:0x0017->B:11:0x004e, LOOP_END] */
        /* JADX WARNING: Removed duplicated region for block: B:13:0x0048 A[EDGE_INSN: B:13:0x0048->B:8:0x0048 ?: BREAK  , SYNTHETIC] */
        static {
            /*
                r5 = 0
                kotlin.internal.PlatformImplementations$ReflectAddSuppressedMethod r4 = new kotlin.internal.PlatformImplementations$ReflectAddSuppressedMethod
                r4.<init>()
                kotlin.internal.PlatformImplementations.ReflectAddSuppressedMethod.INSTANCE = r4
                java.lang.Class<java.lang.Throwable> r3 = java.lang.Throwable.class
                r1 = 0
                java.lang.reflect.Method[] r7 = r3.getMethods()
                java.lang.String r4 = "throwableClass.methods"
                kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r7, r4)
                int r8 = r7.length
                r6 = r5
            L_0x0017:
                if (r6 >= r8) goto L_0x0052
                r2 = r7[r6]
                r0 = 0
                java.lang.String r4 = "it"
                kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r2, r4)
                java.lang.String r4 = r2.getName()
                java.lang.String r9 = "addSuppressed"
                boolean r4 = kotlin.jvm.internal.Intrinsics.areEqual(r4, r9)
                if (r4 == 0) goto L_0x004c
                java.lang.Class[] r4 = r2.getParameterTypes()
                java.lang.String r9 = "it.parameterTypes"
                kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r4, r9)
                java.lang.Object r4 = kotlin.collections.ArraysKt.singleOrNull(r4)
                java.lang.Class r4 = (java.lang.Class) r4
                boolean r4 = kotlin.jvm.internal.Intrinsics.areEqual(r4, r3)
                if (r4 == 0) goto L_0x004c
                r4 = 1
            L_0x0046:
                if (r4 == 0) goto L_0x004e
            L_0x0048:
                kotlin.internal.PlatformImplementations.ReflectAddSuppressedMethod.method = r2
                return
            L_0x004c:
                r4 = r5
                goto L_0x0046
            L_0x004e:
                int r4 = r6 + 1
                r6 = r4
                goto L_0x0017
            L_0x0052:
                r2 = 0
                goto L_0x0048
            */
            throw new UnsupportedOperationException("Method not decompiled: kotlin.internal.PlatformImplementations.ReflectAddSuppressedMethod.<clinit>():void");
        }

        private ReflectAddSuppressedMethod() {
        }
    }

    public void addSuppressed(@NotNull Throwable cause, @NotNull Throwable exception) {
        Intrinsics.checkParameterIsNotNull(cause, "cause");
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        Method method = ReflectAddSuppressedMethod.method;
        if (method != null) {
            method.invoke(cause, exception);
        }
    }

    @Nullable
    public MatchGroup getMatchResultNamedGroup(@NotNull MatchResult matchResult, @NotNull String name) {
        Intrinsics.checkParameterIsNotNull(matchResult, "matchResult");
        Intrinsics.checkParameterIsNotNull(name, "name");
        throw new UnsupportedOperationException("Retrieving groups by name is not supported on this platform.");
    }

    @NotNull
    public Random defaultPlatformRandom() {
        return new FallbackThreadLocalRandom();
    }
}
