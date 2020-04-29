package com.mapbox.mapboxsdk.maps;

import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v4.util.LongSparseArray;
import com.mapbox.mapboxsdk.annotations.Annotation;
import com.mapbox.mapboxsdk.annotations.BaseMarkerOptions;
import com.mapbox.mapboxsdk.annotations.Marker;
import java.util.ArrayList;
import java.util.List;

class MarkerContainer implements Markers {
    private final LongSparseArray<Annotation> annotations;
    private final IconManager iconManager;
    private final NativeMap nativeMapView;

    MarkerContainer(NativeMap nativeMapView2, LongSparseArray<Annotation> annotations2, IconManager iconManager2) {
        this.nativeMapView = nativeMapView2;
        this.annotations = annotations2;
        this.iconManager = iconManager2;
    }

    public Marker addBy(@NonNull BaseMarkerOptions markerOptions, @NonNull MapboxMap mapboxMap) {
        Marker marker = prepareMarker(markerOptions);
        long id = this.nativeMapView != null ? this.nativeMapView.addMarker(marker) : 0;
        marker.setMapboxMap(mapboxMap);
        marker.setId(id);
        this.annotations.put(id, marker);
        return marker;
    }

    @NonNull
    public List<Marker> addBy(@NonNull List<? extends BaseMarkerOptions> markerOptionsList, @NonNull MapboxMap mapboxMap) {
        int count = markerOptionsList.size();
        List<Marker> markers = new ArrayList<>(count);
        if (this.nativeMapView != null && count > 0) {
            for (int i = 0; i < count; i++) {
                markers.add(prepareMarker((BaseMarkerOptions) markerOptionsList.get(i)));
            }
            if (markers.size() > 0) {
                long[] ids = this.nativeMapView.addMarkers(markers);
                for (int i2 = 0; i2 < ids.length; i2++) {
                    Marker createdMarker = (Marker) markers.get(i2);
                    createdMarker.setMapboxMap(mapboxMap);
                    createdMarker.setId(ids[i2]);
                    this.annotations.put(ids[i2], createdMarker);
                }
            }
        }
        return markers;
    }

    public void update(@NonNull Marker updatedMarker, @NonNull MapboxMap mapboxMap) {
        ensureIconLoaded(updatedMarker, mapboxMap);
        this.nativeMapView.updateMarker(updatedMarker);
        this.annotations.setValueAt(this.annotations.indexOfKey(updatedMarker.getId()), updatedMarker);
    }

    @NonNull
    public List<Marker> obtainAll() {
        List<Marker> markers = new ArrayList<>();
        for (int i = 0; i < this.annotations.size(); i++) {
            Annotation annotation = this.annotations.get(this.annotations.keyAt(i));
            if (annotation instanceof Marker) {
                markers.add((Marker) annotation);
            }
        }
        return markers;
    }

    @NonNull
    public List<Marker> obtainAllIn(@NonNull RectF rectangle) {
        long[] ids = this.nativeMapView.queryPointAnnotations(this.nativeMapView.getDensityDependantRectangle(rectangle));
        List<Long> idsList = new ArrayList<>(ids.length);
        for (long id : ids) {
            idsList.add(Long.valueOf(id));
        }
        List<Marker> annotations2 = new ArrayList<>(ids.length);
        List<Annotation> annotationList = obtainAnnotations();
        int count = annotationList.size();
        for (int i = 0; i < count; i++) {
            Annotation annotation = annotationList.get(i);
            if ((annotation instanceof Marker) && idsList.contains(Long.valueOf(annotation.getId()))) {
                annotations2.add((Marker) annotation);
            }
        }
        return new ArrayList(annotations2);
    }

    public void reload() {
        this.iconManager.reloadIcons();
        int count = this.annotations.size();
        for (int i = 0; i < count; i++) {
            Annotation annotation = this.annotations.get((long) i);
            if (annotation instanceof Marker) {
                Marker marker = (Marker) annotation;
                this.nativeMapView.removeAnnotation(annotation.getId());
                marker.setId(this.nativeMapView.addMarker(marker));
            }
        }
    }

    private Marker prepareMarker(BaseMarkerOptions markerOptions) {
        Marker marker = markerOptions.getMarker();
        marker.setTopOffsetPixels(this.iconManager.getTopOffsetPixelsForIcon(this.iconManager.loadIconForMarker(marker)));
        return marker;
    }

    private void ensureIconLoaded(Marker marker, @NonNull MapboxMap mapboxMap) {
        this.iconManager.ensureIconLoaded(marker, mapboxMap);
    }

    @NonNull
    private List<Annotation> obtainAnnotations() {
        List<Annotation> annotations2 = new ArrayList<>();
        for (int i = 0; i < this.annotations.size(); i++) {
            annotations2.add(this.annotations.get(this.annotations.keyAt(i)));
        }
        return annotations2;
    }
}
