package com.mapbox.mapboxsdk.annotations;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.mapbox.mapboxsdk.R;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

@Deprecated
public class Marker extends Annotation {
    @Nullable
    private Icon icon;
    @Keep
    @Nullable
    private String iconId;
    @Nullable
    private InfoWindow infoWindow;
    private boolean infoWindowShown;
    @Keep
    private LatLng position;
    private int rightOffsetPixels;
    private String snippet;
    private String title;
    private int topOffsetPixels;

    Marker() {
    }

    public Marker(BaseMarkerOptions baseMarkerOptions) {
        this(baseMarkerOptions.position, baseMarkerOptions.icon, baseMarkerOptions.title, baseMarkerOptions.snippet);
    }

    Marker(LatLng position2, Icon icon2, String title2, String snippet2) {
        this.position = position2;
        this.title = title2;
        this.snippet = snippet2;
        setIcon(icon2);
    }

    public LatLng getPosition() {
        return this.position;
    }

    public String getSnippet() {
        return this.snippet;
    }

    public String getTitle() {
        return this.title;
    }

    public void hideInfoWindow() {
        if (this.infoWindow != null) {
            this.infoWindow.close();
        }
        this.infoWindowShown = false;
    }

    public boolean isInfoWindowShown() {
        return this.infoWindowShown;
    }

    public void setPosition(LatLng position2) {
        this.position = position2;
        MapboxMap map = getMapboxMap();
        if (map != null) {
            map.updateMarker(this);
        }
    }

    public void setSnippet(String snippet2) {
        this.snippet = snippet2;
        refreshInfoWindowContent();
    }

    public void setIcon(@Nullable Icon icon2) {
        this.icon = icon2;
        this.iconId = icon2 != null ? icon2.getId() : null;
        MapboxMap map = getMapboxMap();
        if (map != null) {
            map.updateMarker(this);
        }
    }

    @Nullable
    public Icon getIcon() {
        return this.icon;
    }

    public void setTitle(String title2) {
        this.title = title2;
        refreshInfoWindowContent();
    }

    @Nullable
    public InfoWindow getInfoWindow() {
        return this.infoWindow;
    }

    private void refreshInfoWindowContent() {
        if (isInfoWindowShown() && this.mapView != null && this.mapboxMap != null && this.mapboxMap.getInfoWindowAdapter() == null) {
            InfoWindow infoWindow2 = getInfoWindow(this.mapView);
            if (this.mapView.getContext() != null) {
                infoWindow2.adaptDefaultMarker(this, this.mapboxMap, this.mapView);
            }
            MapboxMap map = getMapboxMap();
            if (map != null) {
                map.updateMarker(this);
            }
            infoWindow2.onContentUpdate();
        }
    }

    @Nullable
    public InfoWindow showInfoWindow(@NonNull MapboxMap mapboxMap, @NonNull MapView mapView) {
        View content;
        setMapboxMap(mapboxMap);
        setMapView(mapView);
        MapboxMap.InfoWindowAdapter infoWindowAdapter = getMapboxMap().getInfoWindowAdapter();
        if (infoWindowAdapter == null || (content = infoWindowAdapter.getInfoWindow(this)) == null) {
            InfoWindow infoWindow2 = getInfoWindow(mapView);
            if (mapView.getContext() != null) {
                infoWindow2.adaptDefaultMarker(this, mapboxMap, mapView);
            }
            return showInfoWindow(infoWindow2, mapView);
        }
        this.infoWindow = new InfoWindow(content, mapboxMap);
        showInfoWindow(this.infoWindow, mapView);
        return this.infoWindow;
    }

    @NonNull
    private InfoWindow showInfoWindow(InfoWindow iw, MapView mapView) {
        iw.open(mapView, this, getPosition(), this.rightOffsetPixels, this.topOffsetPixels);
        this.infoWindowShown = true;
        return iw;
    }

    @Nullable
    private InfoWindow getInfoWindow(@NonNull MapView mapView) {
        if (this.infoWindow == null && mapView.getContext() != null) {
            this.infoWindow = new InfoWindow(mapView, R.layout.mapbox_infowindow_content, getMapboxMap());
        }
        return this.infoWindow;
    }

    public void setTopOffsetPixels(int topOffsetPixels2) {
        this.topOffsetPixels = topOffsetPixels2;
    }

    public void setRightOffsetPixels(int rightOffsetPixels2) {
        this.rightOffsetPixels = rightOffsetPixels2;
    }

    public String toString() {
        return "Marker [position[" + getPosition() + "]]";
    }
}
