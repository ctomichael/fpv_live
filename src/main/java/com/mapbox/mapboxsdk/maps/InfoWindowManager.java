package com.mapbox.mapboxsdk.maps;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.mapbox.mapboxsdk.annotations.InfoWindow;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import java.util.ArrayList;
import java.util.List;

class InfoWindowManager {
    private boolean allowConcurrentMultipleInfoWindows;
    @Nullable
    private MapboxMap.InfoWindowAdapter infoWindowAdapter;
    private final List<InfoWindow> infoWindows = new ArrayList();
    @Nullable
    private MapboxMap.OnInfoWindowClickListener onInfoWindowClickListener;
    @Nullable
    private MapboxMap.OnInfoWindowCloseListener onInfoWindowCloseListener;
    @Nullable
    private MapboxMap.OnInfoWindowLongClickListener onInfoWindowLongClickListener;

    InfoWindowManager() {
    }

    /* access modifiers changed from: package-private */
    public void update() {
        if (!this.infoWindows.isEmpty()) {
            for (InfoWindow infoWindow : this.infoWindows) {
                infoWindow.update();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setInfoWindowAdapter(@Nullable MapboxMap.InfoWindowAdapter infoWindowAdapter2) {
        this.infoWindowAdapter = infoWindowAdapter2;
    }

    /* access modifiers changed from: package-private */
    @Nullable
    public MapboxMap.InfoWindowAdapter getInfoWindowAdapter() {
        return this.infoWindowAdapter;
    }

    /* access modifiers changed from: package-private */
    public void setAllowConcurrentMultipleOpenInfoWindows(boolean allow) {
        this.allowConcurrentMultipleInfoWindows = allow;
    }

    /* access modifiers changed from: package-private */
    public boolean isAllowConcurrentMultipleOpenInfoWindows() {
        return this.allowConcurrentMultipleInfoWindows;
    }

    /* access modifiers changed from: package-private */
    public boolean isInfoWindowValidForMarker(@Nullable Marker marker) {
        return marker != null && (!TextUtils.isEmpty(marker.getTitle()) || !TextUtils.isEmpty(marker.getSnippet()));
    }

    /* access modifiers changed from: package-private */
    public void setOnInfoWindowClickListener(@Nullable MapboxMap.OnInfoWindowClickListener listener) {
        this.onInfoWindowClickListener = listener;
    }

    /* access modifiers changed from: package-private */
    @Nullable
    public MapboxMap.OnInfoWindowClickListener getOnInfoWindowClickListener() {
        return this.onInfoWindowClickListener;
    }

    /* access modifiers changed from: package-private */
    public void setOnInfoWindowLongClickListener(@Nullable MapboxMap.OnInfoWindowLongClickListener listener) {
        this.onInfoWindowLongClickListener = listener;
    }

    /* access modifiers changed from: package-private */
    @Nullable
    public MapboxMap.OnInfoWindowLongClickListener getOnInfoWindowLongClickListener() {
        return this.onInfoWindowLongClickListener;
    }

    /* access modifiers changed from: package-private */
    public void setOnInfoWindowCloseListener(@Nullable MapboxMap.OnInfoWindowCloseListener listener) {
        this.onInfoWindowCloseListener = listener;
    }

    /* access modifiers changed from: package-private */
    @Nullable
    public MapboxMap.OnInfoWindowCloseListener getOnInfoWindowCloseListener() {
        return this.onInfoWindowCloseListener;
    }

    public void add(InfoWindow infoWindow) {
        this.infoWindows.add(infoWindow);
    }
}
