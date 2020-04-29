package com.dji.mapkit.core.maps;

import com.google.android.gms.common.internal.ServiceSpecificExtraArgs;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\b\u0010\u0004\u001a\u00020\u0003H&J\u0016\u0010\u0005\u001a\u00020\u00032\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00030\u0007H&¨\u0006\b"}, d2 = {"Lcom/dji/mapkit/core/maps/DJIInfoWindow;", "", "onCreate", "", "onDestroy", "setOnViewChangedListener", ServiceSpecificExtraArgs.CastExtraArgs.LISTENER, "Lkotlin/Function0;", "DJI-Mapkit-Core_debug"}, k = 1, mv = {1, 1, 15})
/* compiled from: DJIInfoWindow.kt */
public interface DJIInfoWindow {
    void onCreate();

    void onDestroy();

    void setOnViewChangedListener(@NotNull Function0<Unit> function0);
}
