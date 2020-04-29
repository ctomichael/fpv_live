package kotlin.collections;

import dji.thirdparty.sanselan.formats.tiff.constants.GPSTagConstants;
import java.util.Map;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.internal.InlineOnly;
import kotlin.internal.LowPriorityInOverloadResolution;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KProperty;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\u001aK\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0002\"\b\b\u0001\u0010\u0001*\u0002H\u0002*\u0015\u0012\u0006\b\u0000\u0012\u00020\u0004\u0012\t\u0012\u0007H\u0002¢\u0006\u0002\b\u00050\u00032\b\u0010\u0006\u001a\u0004\u0018\u00010\u00072\n\u0010\b\u001a\u0006\u0012\u0002\b\u00030\tH\n¢\u0006\u0002\u0010\n\u001a@\u0010\u0000\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0006\b\u0000\u0012\u00020\u0004\u0012\u0006\b\u0000\u0012\u0002H\u00020\u000b2\b\u0010\u0006\u001a\u0004\u0018\u00010\u00072\n\u0010\b\u001a\u0006\u0012\u0002\b\u00030\tH\b¢\u0006\u0004\b\f\u0010\n\u001aO\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0002\"\b\b\u0001\u0010\u0001*\u0002H\u0002*\u0017\u0012\u0006\b\u0000\u0012\u00020\u0004\u0012\u000b\b\u0001\u0012\u0007H\u0002¢\u0006\u0002\b\u00050\u000b2\b\u0010\u0006\u001a\u0004\u0018\u00010\u00072\n\u0010\b\u001a\u0006\u0012\u0002\b\u00030\tH\n¢\u0006\u0004\b\r\u0010\n\u001aF\u0010\u000e\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0006\b\u0000\u0012\u00020\u0004\u0012\u0006\b\u0000\u0012\u0002H\u00020\u000b2\b\u0010\u0006\u001a\u0004\u0018\u00010\u00072\n\u0010\b\u001a\u0006\u0012\u0002\b\u00030\t2\u0006\u0010\u0010\u001a\u0002H\u0002H\n¢\u0006\u0002\u0010\u0011¨\u0006\u0012"}, d2 = {"getValue", "V1", GPSTagConstants.GPS_TAG_GPS_STATUS_VALUE_MEASUREMENT_INTEROPERABILITY, "", "", "Lkotlin/internal/Exact;", "thisRef", "", "property", "Lkotlin/reflect/KProperty;", "(Ljava/util/Map;Ljava/lang/Object;Lkotlin/reflect/KProperty;)Ljava/lang/Object;", "", "getVarContravariant", "getVar", "setValue", "", "value", "(Ljava/util/Map;Ljava/lang/Object;Lkotlin/reflect/KProperty;Ljava/lang/Object;)V", "kotlin-stdlib"}, k = 2, mv = {1, 1, 15})
@JvmName(name = "MapAccessorsKt")
/* compiled from: MapAccessors.kt */
public final class MapAccessorsKt {
    @InlineOnly
    private static final <V, V1 extends V> V1 getValue(@NotNull Map<? super String, ? extends V> $this$getValue, Object thisRef, KProperty<?> property) {
        Intrinsics.checkParameterIsNotNull($this$getValue, "$this$getValue");
        return MapsKt.getOrImplicitDefaultNullable($this$getValue, property.getName());
    }

    @InlineOnly
    @JvmName(name = "getVar")
    private static final <V, V1 extends V> V1 getVar(@NotNull Map<? super String, ? extends V> $this$getValue, Object thisRef, KProperty<?> property) {
        Intrinsics.checkParameterIsNotNull($this$getValue, "$this$getValue");
        return MapsKt.getOrImplicitDefaultNullable($this$getValue, property.getName());
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Use getValue() with two type parameters instead")
    @InlineOnly
    @LowPriorityInOverloadResolution
    @JvmName(name = "getVarContravariant")
    private static final <V> V getVarContravariant(@NotNull Map<? super String, ? super V> $this$getValue, Object thisRef, KProperty<?> property) {
        return MapsKt.getOrImplicitDefaultNullable($this$getValue, property.getName());
    }

    @InlineOnly
    private static final <V> void setValue(@NotNull Map<? super String, ? super V> $this$setValue, Object thisRef, KProperty<?> property, V value) {
        Intrinsics.checkParameterIsNotNull($this$setValue, "$this$setValue");
        $this$setValue.put(property.getName(), value);
    }
}
