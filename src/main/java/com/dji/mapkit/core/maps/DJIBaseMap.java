package com.dji.mapkit.core.maps;

import com.dji.mapkit.core.callback.OnCameraChangeListener;
import com.dji.mapkit.core.maps.DJIMap;
import com.dji.mapkit.core.models.DJICameraPosition;
import com.dji.mapkit.core.models.DJILatLng;
import com.dji.mapkit.core.models.annotations.DJIMarker;
import java.util.LinkedList;
import java.util.List;

public abstract class DJIBaseMap implements DJIMap {
    protected List<OnCameraChangeListener> onCameraChangeListeners = new LinkedList();
    protected List<DJIMap.OnInfoWindowClickListener> onInfoWindowClickListeners = new LinkedList();
    protected List<DJIMap.OnMapClickListener> onMapClickListeners = new LinkedList();
    protected List<DJIMap.OnMapLongClickListener> onMapLongClickListeners = new LinkedList();
    protected List<DJIMap.OnMarkerClickListener> onMarkerClickListeners = new LinkedList();
    protected List<DJIMap.OnMarkerDragListener> onMarkerDragListeners = new LinkedList();

    /* access modifiers changed from: protected */
    public boolean onMarkerClick(DJIMarker marker) {
        for (DJIMap.OnMarkerClickListener listener : this.onMarkerClickListeners) {
            listener.onMarkerClick(marker);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onMapClick(DJILatLng latLng) {
        for (DJIMap.OnMapClickListener listener : this.onMapClickListeners) {
            listener.onMapClick(latLng);
        }
    }

    /* access modifiers changed from: protected */
    public void onMapLongClick(DJILatLng latLng) {
        for (DJIMap.OnMapLongClickListener listener : this.onMapLongClickListeners) {
            listener.onMapLongClick(latLng);
        }
    }

    /* access modifiers changed from: protected */
    public void onInfoWindowClick(DJIMarker marker) {
        for (DJIMap.OnInfoWindowClickListener listener : this.onInfoWindowClickListeners) {
            listener.onInfoWindowClick(marker);
        }
    }

    /* access modifiers changed from: protected */
    public void onMarkerDragStart(DJIMarker marker) {
        for (DJIMap.OnMarkerDragListener listener : this.onMarkerDragListeners) {
            listener.onMarkerDragStart(marker);
        }
    }

    /* access modifiers changed from: protected */
    public void onMarkerDrag(DJIMarker marker) {
        for (DJIMap.OnMarkerDragListener listener : this.onMarkerDragListeners) {
            listener.onMarkerDrag(marker);
        }
    }

    /* access modifiers changed from: protected */
    public void onMarkerDragEnd(DJIMarker marker) {
        for (DJIMap.OnMarkerDragListener listener : this.onMarkerDragListeners) {
            listener.onMarkerDragEnd(marker);
        }
    }

    /* access modifiers changed from: protected */
    public void onCameraChange(DJICameraPosition cameraPosition) {
        for (OnCameraChangeListener listener : this.onCameraChangeListeners) {
            listener.onCameraChange(cameraPosition);
        }
    }

    /* access modifiers changed from: protected */
    public void onCameraChangeFinish(DJICameraPosition cameraPosition) {
        for (OnCameraChangeListener listener : this.onCameraChangeListeners) {
            listener.onCameraChange(cameraPosition);
        }
    }

    public void setOnMarkerClickListener(DJIMap.OnMarkerClickListener listener) {
        if (listener != null && !this.onMarkerClickListeners.contains(listener)) {
            this.onMarkerClickListeners.add(listener);
        }
    }

    public void removeOnMarkerClickListener(DJIMap.OnMarkerClickListener listener) {
        this.onMarkerClickListeners.remove(listener);
    }

    public void setOnMapClickListener(DJIMap.OnMapClickListener listener) {
        if (listener != null && !this.onMapClickListeners.contains(listener)) {
            this.onMapClickListeners.add(listener);
        }
    }

    public void removeOnMapClickListener(DJIMap.OnMapClickListener listener) {
        this.onMapClickListeners.clear();
    }

    public void setOnInfoWindowClickListener(DJIMap.OnInfoWindowClickListener listener) {
        if (listener != null && !this.onInfoWindowClickListeners.contains(listener)) {
            this.onInfoWindowClickListeners.add(listener);
        }
    }

    public void setOnMarkerDragListener(DJIMap.OnMarkerDragListener listener) {
        if (listener != null && !this.onMarkerDragListeners.contains(listener)) {
            this.onMarkerDragListeners.add(listener);
        }
    }

    public void removeOnMarkerDragListener(DJIMap.OnMarkerDragListener listener) {
        this.onMarkerDragListeners.remove(listener);
    }

    public void removeAllOnMarkerDragListener() {
        this.onMarkerDragListeners.clear();
    }

    public void setOnMapLongClickListener(DJIMap.OnMapLongClickListener listener) {
        if (listener != null && !this.onMapLongClickListeners.contains(listener)) {
            this.onMapLongClickListeners.add(listener);
        }
    }

    public void removeOnMapLongClickListener(DJIMap.OnMapLongClickListener listener) {
        this.onMapLongClickListeners.remove(listener);
    }

    public void removeAllOnMapLongClickListener() {
        this.onMapLongClickListeners.clear();
    }

    public void setOnCameraChangeListener(OnCameraChangeListener listener) {
        if (listener != null && !this.onCameraChangeListeners.contains(listener)) {
            this.onCameraChangeListeners.add(listener);
        }
    }

    public void removeOnCameraChangeListener(OnCameraChangeListener listener) {
        this.onCameraChangeListeners.remove(listener);
    }

    public void removeAllOnCameraChangeListeners() {
        this.onCameraChangeListeners.clear();
    }

    public void removeAllOnMarkerClickListener() {
        this.onMarkerDragListeners.clear();
    }

    public void removeAllOnMapClickListener() {
        this.onMapClickListeners.clear();
    }
}
