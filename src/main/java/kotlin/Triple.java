package kotlin;

import dji.thirdparty.sanselan.formats.tiff.constants.GPSTagConstants;
import java.io.Serializable;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u0001*\u0006\b\u0001\u0010\u0002 \u0001*\u0006\b\u0002\u0010\u0003 \u00012\u00060\u0004j\u0002`\u0005B\u001d\u0012\u0006\u0010\u0006\u001a\u00028\u0000\u0012\u0006\u0010\u0007\u001a\u00028\u0001\u0012\u0006\u0010\b\u001a\u00028\u0002¢\u0006\u0002\u0010\tJ\u000e\u0010\u000f\u001a\u00028\u0000HÆ\u0003¢\u0006\u0002\u0010\u000bJ\u000e\u0010\u0010\u001a\u00028\u0001HÆ\u0003¢\u0006\u0002\u0010\u000bJ\u000e\u0010\u0011\u001a\u00028\u0002HÆ\u0003¢\u0006\u0002\u0010\u000bJ>\u0010\u0012\u001a\u0014\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u0001\u0012\u0004\u0012\u00028\u00020\u00002\b\b\u0002\u0010\u0006\u001a\u00028\u00002\b\b\u0002\u0010\u0007\u001a\u00028\u00012\b\b\u0002\u0010\b\u001a\u00028\u0002HÆ\u0001¢\u0006\u0002\u0010\u0013J\u0013\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017HÖ\u0003J\t\u0010\u0018\u001a\u00020\u0019HÖ\u0001J\b\u0010\u001a\u001a\u00020\u001bH\u0016R\u0013\u0010\u0006\u001a\u00028\u0000¢\u0006\n\n\u0002\u0010\f\u001a\u0004\b\n\u0010\u000bR\u0013\u0010\u0007\u001a\u00028\u0001¢\u0006\n\n\u0002\u0010\f\u001a\u0004\b\r\u0010\u000bR\u0013\u0010\b\u001a\u00028\u0002¢\u0006\n\n\u0002\u0010\f\u001a\u0004\b\u000e\u0010\u000b¨\u0006\u001c"}, d2 = {"Lkotlin/Triple;", GPSTagConstants.GPS_TAG_GPS_STATUS_VALUE_MEASUREMENT_IN_PROGRESS, "B", "C", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "first", "second", "third", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V", "getFirst", "()Ljava/lang/Object;", "Ljava/lang/Object;", "getSecond", "getThird", "component1", "component2", "component3", "copy", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lkotlin/Triple;", "equals", "", "other", "", "hashCode", "", "toString", "", "kotlin-stdlib"}, k = 1, mv = {1, 1, 15})
/* compiled from: Tuples.kt */
public final class Triple<A, B, C> implements Serializable {
    private final A first;
    private final B second;
    private final C third;

    public static /* synthetic */ Triple copy$default(Triple triple, Object obj, Object obj2, Object obj3, int i, Object obj4) {
        if ((i & 1) != 0) {
            obj = triple.first;
        }
        if ((i & 2) != 0) {
            obj2 = triple.second;
        }
        if ((i & 4) != 0) {
            obj3 = triple.third;
        }
        return triple.copy(obj, obj2, obj3);
    }

    public final A component1() {
        return this.first;
    }

    public final B component2() {
        return this.second;
    }

    public final C component3() {
        return this.third;
    }

    @NotNull
    public final Triple<A, B, C> copy(A first2, B second2, C third2) {
        return new Triple<>(first2, second2, third2);
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
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.jvm.internal.Intrinsics.areEqual(java.lang.Object, java.lang.Object):boolean
     arg types: [C, C]
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
            if (obj instanceof Triple) {
                Triple triple = (Triple) obj;
                if (!Intrinsics.areEqual((Object) this.first, (Object) triple.first) || !Intrinsics.areEqual((Object) this.second, (Object) triple.second) || !Intrinsics.areEqual((Object) this.third, (Object) triple.third)) {
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
        int hashCode2 = ((b != null ? b.hashCode() : 0) + hashCode) * 31;
        C c = this.third;
        if (c != null) {
            i = c.hashCode();
        }
        return hashCode2 + i;
    }

    public Triple(A first2, B second2, C third2) {
        this.first = first2;
        this.second = second2;
        this.third = third2;
    }

    public final A getFirst() {
        return this.first;
    }

    public final B getSecond() {
        return this.second;
    }

    public final C getThird() {
        return this.third;
    }

    @NotNull
    public String toString() {
        return '(' + ((Object) this.first) + ", " + ((Object) this.second) + ", " + ((Object) this.third) + ')';
    }
}
