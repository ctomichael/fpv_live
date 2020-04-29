package kotlin.jvm.internal;

import java.lang.annotation.Annotation;
import java.util.List;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.SinceKotlin;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.reflect.KClass;
import kotlin.reflect.KClassifier;
import kotlin.reflect.KType;
import kotlin.reflect.KTypeProjection;
import kotlin.reflect.KVariance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u001b\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B#\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\b\u0010\u0017\u001a\u00020\u0013H\u0002J\u0013\u0010\u0018\u001a\u00020\b2\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0002J\b\u0010\u001b\u001a\u00020\u001cH\u0016J\b\u0010\u001d\u001a\u00020\u0013H\u0016J\f\u0010\u0017\u001a\u00020\u0013*\u00020\u0006H\u0002R\u001a\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u00058VX\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u001a\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\rR\u0014\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0014\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\u0011R\u001c\u0010\u0012\u001a\u00020\u0013*\u0006\u0012\u0002\b\u00030\u00148BX\u0004¢\u0006\u0006\u001a\u0004\b\u0015\u0010\u0016¨\u0006\u001e"}, d2 = {"Lkotlin/jvm/internal/TypeReference;", "Lkotlin/reflect/KType;", "classifier", "Lkotlin/reflect/KClassifier;", "arguments", "", "Lkotlin/reflect/KTypeProjection;", "isMarkedNullable", "", "(Lkotlin/reflect/KClassifier;Ljava/util/List;Z)V", "annotations", "", "getAnnotations", "()Ljava/util/List;", "getArguments", "getClassifier", "()Lkotlin/reflect/KClassifier;", "()Z", "arrayClassName", "", "Ljava/lang/Class;", "getArrayClassName", "(Ljava/lang/Class;)Ljava/lang/String;", "asString", "equals", "other", "", "hashCode", "", "toString", "kotlin-stdlib"}, k = 1, mv = {1, 1, 15})
@SinceKotlin(version = "1.4")
/* compiled from: TypeReference.kt */
public final class TypeReference implements KType {
    @NotNull
    private final List<KTypeProjection> arguments;
    @NotNull
    private final KClassifier classifier;
    private final boolean isMarkedNullable;

    public TypeReference(@NotNull KClassifier classifier2, @NotNull List<KTypeProjection> arguments2, boolean isMarkedNullable2) {
        Intrinsics.checkParameterIsNotNull(classifier2, "classifier");
        Intrinsics.checkParameterIsNotNull(arguments2, "arguments");
        this.classifier = classifier2;
        this.arguments = arguments2;
        this.isMarkedNullable = isMarkedNullable2;
    }

    @NotNull
    public KClassifier getClassifier() {
        return this.classifier;
    }

    @NotNull
    public List<KTypeProjection> getArguments() {
        return this.arguments;
    }

    public boolean isMarkedNullable() {
        return this.isMarkedNullable;
    }

    @NotNull
    public List<Annotation> getAnnotations() {
        return CollectionsKt.emptyList();
    }

    public boolean equals(@Nullable Object other) {
        return (other instanceof TypeReference) && Intrinsics.areEqual(getClassifier(), ((TypeReference) other).getClassifier()) && Intrinsics.areEqual(getArguments(), ((TypeReference) other).getArguments()) && isMarkedNullable() == ((TypeReference) other).isMarkedNullable();
    }

    public int hashCode() {
        return (((getClassifier().hashCode() * 31) + getArguments().hashCode()) * 31) + Boolean.valueOf(isMarkedNullable()).hashCode();
    }

    @NotNull
    public String toString() {
        return asString() + " (Kotlin reflection is not available)";
    }

    private final String asString() {
        Class javaClass;
        String klass;
        String args;
        KClassifier classifier2 = getClassifier();
        if (!(classifier2 instanceof KClass)) {
            classifier2 = null;
        }
        KClass kClass = (KClass) classifier2;
        if (kClass != null) {
            javaClass = JvmClassMappingKt.getJavaClass(kClass);
        } else {
            javaClass = null;
        }
        if (javaClass == null) {
            klass = getClassifier().toString();
        } else if (javaClass.isArray()) {
            klass = getArrayClassName(javaClass);
        } else {
            klass = javaClass.getName();
        }
        if (getArguments().isEmpty()) {
            args = "";
        } else {
            args = CollectionsKt.joinToString$default(getArguments(), ", ", "<", ">", 0, null, new TypeReference$asString$args$1(this), 24, null);
        }
        return klass + args + (isMarkedNullable() ? "?" : "");
    }

    private final String getArrayClassName(@NotNull Class<?> $this$arrayClassName) {
        if (Intrinsics.areEqual($this$arrayClassName, boolean[].class)) {
            return "kotlin.BooleanArray";
        }
        if (Intrinsics.areEqual($this$arrayClassName, char[].class)) {
            return "kotlin.CharArray";
        }
        if (Intrinsics.areEqual($this$arrayClassName, byte[].class)) {
            return "kotlin.ByteArray";
        }
        if (Intrinsics.areEqual($this$arrayClassName, short[].class)) {
            return "kotlin.ShortArray";
        }
        if (Intrinsics.areEqual($this$arrayClassName, int[].class)) {
            return "kotlin.IntArray";
        }
        if (Intrinsics.areEqual($this$arrayClassName, float[].class)) {
            return "kotlin.FloatArray";
        }
        if (Intrinsics.areEqual($this$arrayClassName, long[].class)) {
            return "kotlin.LongArray";
        }
        if (Intrinsics.areEqual($this$arrayClassName, double[].class)) {
            return "kotlin.DoubleArray";
        }
        return "kotlin.Array";
    }

    /* access modifiers changed from: private */
    public final String asString(@NotNull KTypeProjection $this$asString) {
        String typeString;
        if ($this$asString.getVariance() == null) {
            return "*";
        }
        KType type = $this$asString.getType();
        if (!(type instanceof TypeReference)) {
            type = null;
        }
        TypeReference typeReference = (TypeReference) type;
        if (typeReference == null || (typeString = typeReference.asString()) == null) {
            typeString = String.valueOf($this$asString.getType());
        }
        KVariance variance = $this$asString.getVariance();
        if (variance != null) {
            switch (variance) {
                case INVARIANT:
                    return typeString;
                case IN:
                    return "in " + typeString;
                case OUT:
                    return "out " + typeString;
            }
        }
        throw new NoWhenBranchMatchedException();
    }
}
