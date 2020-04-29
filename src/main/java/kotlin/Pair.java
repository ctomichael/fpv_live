package kotlin;

import dji.thirdparty.sanselan.formats.tiff.constants.GPSTagConstants;
import java.io.Serializable;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u0001*\u0006\b\u0001\u0010\u0002 \u00012\u00060\u0003j\u0002`\u0004B\u0015\u0012\u0006\u0010\u0005\u001a\u00028\u0000\u0012\u0006\u0010\u0006\u001a\u00028\u0001¢\u0006\u0002\u0010\u0007J\u000e\u0010\f\u001a\u00028\u0000HÆ\u0003¢\u0006\u0002\u0010\tJ\u000e\u0010\r\u001a\u00028\u0001HÆ\u0003¢\u0006\u0002\u0010\tJ.\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u00002\b\b\u0002\u0010\u0005\u001a\u00028\u00002\b\b\u0002\u0010\u0006\u001a\u00028\u0001HÆ\u0001¢\u0006\u0002\u0010\u000fJ\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013HÖ\u0003J\t\u0010\u0014\u001a\u00020\u0015HÖ\u0001J\b\u0010\u0016\u001a\u00020\u0017H\u0016R\u0013\u0010\u0005\u001a\u00028\u0000¢\u0006\n\n\u0002\u0010\n\u001a\u0004\b\b\u0010\tR\u0013\u0010\u0006\u001a\u00028\u0001¢\u0006\n\n\u0002\u0010\n\u001a\u0004\b\u000b\u0010\t¨\u0006\u0018"}, d2 = {"Lkotlin/Pair;", GPSTagConstants.GPS_TAG_GPS_STATUS_VALUE_MEASUREMENT_IN_PROGRESS, "B", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "first", "second", "(Ljava/lang/Object;Ljava/lang/Object;)V", "getFirst", "()Ljava/lang/Object;", "Ljava/lang/Object;", "getSecond", "component1", "component2", "copy", "(Ljava/lang/Object;Ljava/lang/Object;)Lkotlin/Pair;", "equals", "", "other", "", "hashCode", "", "toString", "", "kotlin-stdlib"}, k = 1, mv = {1, 1, 15})
/* compiled from: Tuples.kt */
public final class Pair<A, B> implements Serializable {
    private final A first;
    private final B second;

    public static /* synthetic */ Pair copy$default(Pair pair, Object obj, Object obj2, int i, Object obj3) {
        if ((i & 1) != 0) {
            obj = pair.first;
        }
        if ((i & 2) != 0) {
            obj2 = pair.second;
        }
        return pair.copy(obj, obj2);
    }

    public final A component1() {
        return this.first;
    }

    public final B component2() {
        return this.second;
    }

    @NotNull
    public final Pair<A, B> copy(A first2, B second2) {
        return new Pair<>(first2, second2);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.jvm.internal.Intrinsics.areEqual(java.lang.Object, java.lang.Object):boolean
     arg types: [A, A]
     candidates:
      kotlin.jvm.internal.Intrinsics.areEqual(double, java.lang.Double):boolean
      kotlin.jvm.internal.Intrinsics.areEqual(float, java.lang.Float):boolean
      kotlin.jvm.internal.Intrinsics.areEqual(java.lang.Double, double):boolean
      kotlin.jvm.internal.Intrinsics.areEqual(java.lang.Double, java.lang.Double):boolean
      kotlin.jvm.internal.Intrinsics.areEqual(java.lang.Float, float):boolean
      kotlin.jvm.internal.Intrinsics.areEqual(java.lang.Float, java.lang.Float):boolean
      kotlin.jvm.internal.Intrinsics.areEqual(java.lang.Object, java.lang.Object):boolean */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.jvm.internal.Intrinsics.areEqual(java.lang.Object, java.lang.Object):boolean
     arg types: [B, B]
     candidates:
      kotlin.jvm.internal.Intrinsics.areEqual(double, java.lang.Double):boolean
      kotlin.jvm.internal.Intrinsics.areEqual(float, java.lang.Float):boolean
      kotlin.jvm.internal.Intrinsics.areEqual(java.lang.Double, double):boolean
      kotlin.jvm.internal.Intrinsics.areEqual(java.lang.Double, java.lang.Double):boolean
      kotlin.jvm.internal.Intrinsics.areEqual(java.lang.Float, float):boolean
      kotlin.jvm.internal.Intrinsics.areEqual(java.lang.Float, java.lang.Float):boolean
      kotlin.jvm.internal.Intrinsics.areEqual(java.lang.Object, java.lang.Object):boolean */
    public boolean equals(@Nullable Object obj) {
        if (this != obj) {
            if (obj instanceof Pair) {
                Pair pair = (Pair) obj;
                if (!Intrinsics.areEqual((Object) this.first, (Object) pair.first) || !Intrinsics.areEqual((Object) this.second, (Object) pair.second)) {
                    return false;
                }
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i = 0;
        A a = this.first;
        int hashCode = (a != null ? a.hashCode() : 0) * 31;
        B b = this.second;
        if (b != null) {
            i = b.hashCode();
        }
        return hashCode + i;
    }

    public Pair(A first2, B second2) {
        this.first = first2;
        this.second = second2;
    }

    public final A getFirst() {
        return this.first;
    }

    public final B getSecond() {
        return this.second;
    }

    @NotNull
    public String toString() {
        return '(' + ((Object) this.first) + ", " + ((Object) this.second) + ')';
    }
}
