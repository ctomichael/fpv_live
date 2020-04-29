package com.dji.mapkit.core.maps;

import android.support.annotation.Nullable;
import android.view.View;
import com.dji.mapkit.core.callback.MapScreenShotListener;
import com.dji.mapkit.core.callback.OnCameraChangeListener;
import com.dji.mapkit.core.camera.DJICameraUpdate;
import com.dji.mapkit.core.models.DJICameraPosition;
import com.dji.mapkit.core.models.DJILatLng;
import com.dji.mapkit.core.models.annotations.DJICircle;
import com.dji.mapkit.core.models.annotations.DJICircleOptions;
import com.dji.mapkit.core.models.annotations.DJIMarker;
import com.dji.mapkit.core.models.annotations.DJIMarkerOptions;
import com.dji.mapkit.core.models.annotations.DJIPolygon;
import com.dji.mapkit.core.models.annotations.DJIPolygonOptions;
import com.dji.mapkit.core.models.annotations.DJIPolyline;
import com.dji.mapkit.core.models.annotations.DJIPolylineOptions;

public interface DJIMap {
    public static final int MAP_TYPE_HYBRID = 4;
    public static final int MAP_TYPE_NIGHT = 3;
    public static final int MAP_TYPE_NONE = 0;
    public static final int MAP_TYPE_NORMAL = 1;
    public static final int MAP_TYPE_SATELLITE = 2;

    public interface OnInfoWindowClickListener {
        void onInfoWindowClick(DJIMarker dJIMarker);
    }

    public interface InfoWindowAdapter {
        @Nullable
        View getInfoContents(DJIMarker dJIMarker);

        @Nullable
        DJIInfoWindow getInfoWindow(DJIMarker dJIMarker);
    }

    public interface OnMarkerClickListener {
        boolean onMarkerClick(DJIMarker dJIMarker);
    }

    public interface OnMapLongClickListener {
        void onMapLongClick(DJILatLng dJILatLng);
    }

    public interface OnMarkerDragListener {
        void onMarkerDrag(DJIMarker dJIMarker);

        void onMarkerDragEnd(DJIMarker dJIMarker);

        void onMarkerDragStart(DJIMarker dJIMarker);
    }

    public interface OnMapClickListener {
        void onMapClick(DJILatLng dJILatLng);
    }

    DJIMarker addMarker(DJIMarkerOptions dJIMarkerOptions);

    DJIPolygon addPolygon(DJIPolygonOptions dJIPolygonOptions);

    DJIPolyline addPolyline(DJIPolylineOptions dJIPolylineOptions);

    DJICircle addSingleCircle(DJICircleOptions dJICircleOptions);

    void animateCamera(DJICameraUpdate dJICameraUpdate);

    void clear();

    DJICameraPosition getCameraPosition();

    Object getMap();

    DJIProjection getProjection();

    DJIUiSettings getUiSettings();

    void moveCamera(DJICameraUpdate dJICameraUpdate);

    void removeAllOnCameraChangeListeners();

    void removeAllOnMapClickListener();

    void removeAllOnMapLongClickListener();

    void removeAllOnMarkerClickListener();

    void removeAllOnMarkerDragListener();

    void removeOnCameraChangeListener(OnCameraChangeListener onCameraChangeListener);

    void removeOnMapClickListener(OnMapClickListener onMapClickListener);

    void removeOnMapLongClickListener(OnMapLongClickListener onMapLongClickListener);

    void removeOnMarkerClickListener(OnMarkerClickListener onMarkerClickListener);

    void removeOnMarkerDragListener(OnMarkerDragListener onMarkerDragListener);

    void setCameraPosition(DJICameraPosition dJICameraPosition);

    void setInfoWindowAdapter(InfoWindowAdapter infoWindowAdapter);

    void setMapType(int i);

    void setMapType(MapType mapType);

    void setOnCameraChangeListener(OnCameraChangeListener onCameraChangeListener);

    void setOnInfoWindowClickListener(OnInfoWindowClickListener onInfoWindowClickListener);

    void setOnMapClickListener(OnMapClickListener onMapClickListener);

    void setOnMapLongClickListener(OnMapLongClickListener onMapLongClickListener);

    void setOnMarkerClickListener(OnMarkerClickListener onMarkerClickListener);

    void setOnMarkerDragListener(OnMarkerDragListener onMarkerDragListener);

    void snapshot(MapScreenShotListener mapScreenShotListener);

    public enum MapType {
        Normal(1),
        Satellite(2),
        Night(3),
        Hybrid(4);
        
        int value;

        private MapType(int value2) {
            this.value = value2;
        }

        public int getValue() {
            return this.value;
        }
    }
}
