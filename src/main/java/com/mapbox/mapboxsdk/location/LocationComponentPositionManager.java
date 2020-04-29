package com.mapbox.mapboxsdk.location;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.Layer;

class LocationComponentPositionManager {
    @Nullable
    private String layerAbove;
    @Nullable
    private String layerBelow;
    @NonNull
    private final Style style;

    LocationComponentPositionManager(@NonNull Style style2, @Nullable String layerAbove2, @Nullable String layerBelow2) {
        this.style = style2;
        this.layerAbove = layerAbove2;
        this.layerBelow = layerBelow2;
    }

    /* access modifiers changed from: package-private */
    public boolean update(@Nullable String layerAbove2, @Nullable String layerBelow2) {
        boolean requiresUpdate = (this.layerAbove != layerAbove2 && (this.layerAbove == null || !this.layerAbove.equals(layerAbove2))) || (this.layerBelow != layerBelow2 && (this.layerBelow == null || !this.layerBelow.equals(layerBelow2)));
        this.layerAbove = layerAbove2;
        this.layerBelow = layerBelow2;
        return requiresUpdate;
    }

    /* access modifiers changed from: package-private */
    public void addLayerToMap(@NonNull Layer layer) {
        if (this.layerAbove != null) {
            this.style.addLayerAbove(layer, this.layerAbove);
        } else if (this.layerBelow != null) {
            this.style.addLayerBelow(layer, this.layerBelow);
        } else {
            this.style.addLayer(layer);
        }
    }
}
