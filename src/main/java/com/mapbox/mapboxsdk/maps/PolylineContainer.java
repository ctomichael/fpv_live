package com.mapbox.mapboxsdk.maps;

import android.support.annotation.NonNull;
import android.support.v4.util.LongSparseArray;
import com.mapbox.mapboxsdk.annotations.Annotation;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import java.util.ArrayList;
import java.util.List;

class PolylineContainer implements Polylines {
    private final LongSparseArray<Annotation> annotations;
    private final NativeMap nativeMap;

    PolylineContainer(NativeMap nativeMap2, LongSparseArray<Annotation> annotations2) {
        this.nativeMap = nativeMap2;
        this.annotations = annotations2;
    }

    public Polyline addBy(@NonNull PolylineOptions polylineOptions, @NonNull MapboxMap mapboxMap) {
        Polyline polyline = polylineOptions.getPolyline();
        if (!polyline.getPoints().isEmpty()) {
            long id = this.nativeMap != null ? this.nativeMap.addPolyline(polyline) : 0;
            polyline.setMapboxMap(mapboxMap);
            polyline.setId(id);
            this.annotations.put(id, polyline);
        }
        return polyline;
    }

    @NonNull
    public List<Polyline> addBy(@NonNull List<PolylineOptions> polylineOptionsList, @NonNull MapboxMap mapboxMap) {
        int count = polylineOptionsList.size();
        List<Polyline> polylines = new ArrayList<>(count);
        if (this.nativeMap != null && count > 0) {
            for (PolylineOptions options : polylineOptionsList) {
                Polyline polyline = options.getPolyline();
                if (!polyline.getPoints().isEmpty()) {
                    polylines.add(polyline);
                }
            }
            long[] ids = this.nativeMap.addPolylines(polylines);
            for (int i = 0; i < ids.length; i++) {
                Polyline polylineCreated = (Polyline) polylines.get(i);
                polylineCreated.setMapboxMap(mapboxMap);
                polylineCreated.setId(ids[i]);
                this.annotations.put(ids[i], polylineCreated);
            }
        }
        return polylines;
    }

    public void update(@NonNull Polyline polyline) {
        this.nativeMap.updatePolyline(polyline);
        this.annotations.setValueAt(this.annotations.indexOfKey(polyline.getId()), polyline);
    }

    @NonNull
    public List<Polyline> obtainAll() {
        List<Polyline> polylines = new ArrayList<>();
        for (int i = 0; i < this.annotations.size(); i++) {
            Annotation annotation = this.annotations.get(this.annotations.keyAt(i));
            if (annotation instanceof Polyline) {
                polylines.add((Polyline) annotation);
            }
        }
        return polylines;
    }
}
