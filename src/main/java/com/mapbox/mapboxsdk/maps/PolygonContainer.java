package com.mapbox.mapboxsdk.maps;

import android.support.annotation.NonNull;
import android.support.v4.util.LongSparseArray;
import com.mapbox.mapboxsdk.annotations.Annotation;
import com.mapbox.mapboxsdk.annotations.Polygon;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import java.util.ArrayList;
import java.util.List;

class PolygonContainer implements Polygons {
    private final LongSparseArray<Annotation> annotations;
    private final NativeMap nativeMap;

    PolygonContainer(NativeMap nativeMap2, LongSparseArray<Annotation> annotations2) {
        this.nativeMap = nativeMap2;
        this.annotations = annotations2;
    }

    public Polygon addBy(@NonNull PolygonOptions polygonOptions, @NonNull MapboxMap mapboxMap) {
        Polygon polygon = polygonOptions.getPolygon();
        if (!polygon.getPoints().isEmpty()) {
            long id = this.nativeMap != null ? this.nativeMap.addPolygon(polygon) : 0;
            polygon.setId(id);
            polygon.setMapboxMap(mapboxMap);
            this.annotations.put(id, polygon);
        }
        return polygon;
    }

    @NonNull
    public List<Polygon> addBy(@NonNull List<PolygonOptions> polygonOptionsList, @NonNull MapboxMap mapboxMap) {
        int count = polygonOptionsList.size();
        List<Polygon> polygons = new ArrayList<>(count);
        if (this.nativeMap != null && count > 0) {
            for (PolygonOptions polygonOptions : polygonOptionsList) {
                Polygon polygon = polygonOptions.getPolygon();
                if (!polygon.getPoints().isEmpty()) {
                    polygons.add(polygon);
                }
            }
            long[] ids = this.nativeMap.addPolygons(polygons);
            for (int i = 0; i < ids.length; i++) {
                Polygon polygon2 = (Polygon) polygons.get(i);
                polygon2.setMapboxMap(mapboxMap);
                polygon2.setId(ids[i]);
                this.annotations.put(ids[i], polygon2);
            }
        }
        return polygons;
    }

    public void update(@NonNull Polygon polygon) {
        this.nativeMap.updatePolygon(polygon);
        this.annotations.setValueAt(this.annotations.indexOfKey(polygon.getId()), polygon);
    }

    @NonNull
    public List<Polygon> obtainAll() {
        List<Polygon> polygons = new ArrayList<>();
        for (int i = 0; i < this.annotations.size(); i++) {
            Annotation annotation = this.annotations.get(this.annotations.keyAt(i));
            if (annotation instanceof Polygon) {
                polygons.add((Polygon) annotation);
            }
        }
        return polygons;
    }
}
