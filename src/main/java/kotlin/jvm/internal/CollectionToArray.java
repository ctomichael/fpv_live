package kotlin.jvm.internal;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a#\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\bH\u0007¢\u0006\u0004\b\t\u0010\n\u001a5\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\b2\u0010\u0010\u000b\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0002\u0018\u00010\u0001H\u0007¢\u0006\u0004\b\t\u0010\f\u001a~\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\b2\u0014\u0010\u000e\u001a\u0010\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00010\u000f2\u001a\u0010\u0010\u001a\u0016\u0012\u0004\u0012\u00020\u0005\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00010\u00112(\u0010\u0012\u001a$\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001\u0012\u0004\u0012\u00020\u0005\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00010\u0013H\b¢\u0006\u0002\u0010\u0014\"\u0018\u0010\u0000\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001X\u0004¢\u0006\u0004\n\u0002\u0010\u0003\"\u000e\u0010\u0004\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"EMPTY", "", "", "[Ljava/lang/Object;", "MAX_SIZE", "", "collectionToArray", "collection", "", "toArray", "(Ljava/util/Collection;)[Ljava/lang/Object;", "a", "(Ljava/util/Collection;[Ljava/lang/Object;)[Ljava/lang/Object;", "toArrayImpl", "empty", "Lkotlin/Function0;", "alloc", "Lkotlin/Function1;", "trim", "Lkotlin/Function2;", "(Ljava/util/Collection;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;)[Ljava/lang/Object;", "kotlin-stdlib"}, k = 2, mv = {1, 1, 15})
@JvmName(name = "CollectionToArray")
/* compiled from: CollectionToArray.kt */
public final class CollectionToArray {
    private static final Object[] EMPTY = new Object[0];
    private static final int MAX_SIZE = 2147483645;

    @NotNull
    @JvmName(name = "toArray")
    public static final Object[] toArray(@NotNull Collection<?> collection) {
        Intrinsics.checkParameterIsNotNull(collection, "collection");
        int size$iv = collection.size();
        if (size$iv == 0) {
            return EMPTY;
        }
        Iterator iter$iv = collection.iterator();
        if (!iter$iv.hasNext()) {
            return EMPTY;
        }
        Object[] result$iv = new Object[size$iv];
        int i$iv = 0;
        while (true) {
            int i$iv2 = i$iv + 1;
            result$iv[i$iv] = iter$iv.next();
            if (i$iv2 >= result$iv.length) {
                if (!iter$iv.hasNext()) {
                    return result$iv;
                }
                int newSize$iv = ((i$iv2 * 3) + 1) >>> 1;
                if (newSize$iv <= i$iv2) {
                    if (i$iv2 >= MAX_SIZE) {
                        throw new OutOfMemoryError();
                    }
                    newSize$iv = MAX_SIZE;
                }
                result$iv = Arrays.copyOf(result$iv, newSize$iv);
                Intrinsics.checkExpressionValueIsNotNull(result$iv, "Arrays.copyOf(result, newSize)");
            } else if (!iter$iv.hasNext()) {
                Object[] result$iv2 = Arrays.copyOf(result$iv, i$iv2);
                Intrinsics.checkExpressionValueIsNotNull(result$iv2, "Arrays.copyOf(result, size)");
                return result$iv2;
            }
            i$iv = i$iv2;
        }
    }

    @NotNull
    @JvmName(name = "toArray")
    public static final Object[] toArray(@NotNull Collection<?> collection, @Nullable Object[] a) {
        Object[] result$iv;
        Intrinsics.checkParameterIsNotNull(collection, "collection");
        if (a == null) {
            throw new NullPointerException();
        }
        int size$iv = collection.size();
        if (size$iv != 0) {
            Iterator iter$iv = collection.iterator();
            if (iter$iv.hasNext()) {
                int size = size$iv;
                if (size <= a.length) {
                    result$iv = a;
                } else {
                    Object newInstance = Array.newInstance(a.getClass().getComponentType(), size);
                    if (newInstance == null) {
                        throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<kotlin.Any?>");
                    }
                    result$iv = (Object[]) newInstance;
                }
                int i$iv = 0;
                while (true) {
                    int i$iv2 = i$iv + 1;
                    result$iv[i$iv] = iter$iv.next();
                    if (i$iv2 >= result$iv.length) {
                        if (!iter$iv.hasNext()) {
                            return result$iv;
                        }
                        int newSize$iv = ((i$iv2 * 3) + 1) >>> 1;
                        if (newSize$iv <= i$iv2) {
                            if (i$iv2 >= MAX_SIZE) {
                                throw new OutOfMemoryError();
                            }
                            newSize$iv = MAX_SIZE;
                        }
                        result$iv = Arrays.copyOf(result$iv, newSize$iv);
                        Intrinsics.checkExpressionValueIsNotNull(result$iv, "Arrays.copyOf(result, newSize)");
                    } else if (!iter$iv.hasNext()) {
                        Object[] result = result$iv;
                        if (result == a) {
                            a[i$iv2] = null;
                            return a;
                        }
                        Object[] a2 = Arrays.copyOf(result, i$iv2);
                        Intrinsics.checkExpressionValueIsNotNull(a2, "Arrays.copyOf(result, size)");
                        return a2;
                    }
                    i$iv = i$iv2;
                }
            } else if (a.length <= 0) {
                return a;
            } else {
                a[0] = null;
                return a;
            }
        } else if (a.length <= 0) {
            return a;
        } else {
            a[0] = null;
            return a;
        }
    }

    private static final Object[] toArrayImpl(Collection<?> collection, Function0<Object[]> empty, Function1<? super Integer, Object[]> alloc, Function2<? super Object[], ? super Integer, Object[]> trim) {
        int size = collection.size();
        if (size == 0) {
            return empty.invoke();
        }
        Iterator iter = collection.iterator();
        if (!iter.hasNext()) {
            return empty.invoke();
        }
        Object[] result = alloc.invoke(Integer.valueOf(size));
        int i = 0;
        while (true) {
            int i2 = i + 1;
            result[i] = iter.next();
            if (i2 >= result.length) {
                if (!iter.hasNext()) {
                    return result;
                }
                int newSize = ((i2 * 3) + 1) >>> 1;
                if (newSize <= i2) {
                    if (i2 >= MAX_SIZE) {
                        throw new OutOfMemoryError();
                    }
                    newSize = MAX_SIZE;
                }
                result = Arrays.copyOf(result, newSize);
                Intrinsics.checkExpressionValueIsNotNull(result, "Arrays.copyOf(result, newSize)");
            } else if (!iter.hasNext()) {
                return trim.invoke(result, Integer.valueOf(i2));
            }
            i = i2;
        }
    }
}
