package com.mapbox.mapboxsdk.maps;

import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v4.util.LongSparseArray;
import com.mapbox.mapboxsdk.annotations.Annotation;
import java.util.ArrayList;
import java.util.List;

class ShapeAnnotationContainer implements ShapeAnnotations {
    private final LongSparseArray<Annotation> annotations;
    private final NativeMap nativeMapView;

    ShapeAnnotationContainer(NativeMap nativeMapView2, LongSparseArray<Annotation> annotations2) {
        this.nativeMapView = nativeMapView2;
        this.annotations = annotations2;
    }

    @NonNull
    public List<Annotation> obtainAllIn(@NonNull RectF rectangle) {
        return getAnnotationsFromIds(this.nativeMapView.queryShapeAnnotations(this.nativeMapView.getDensityDependantRectangle(rectangle)));
    }

    @NonNull
    private List<Annotation> getAnnotationsFromIds(long[] annotationIds) {
        List<Annotation> shapeAnnotations = new ArrayList<>();
        for (long annotationId : annotationIds) {
            Annotation annotation = this.annotations.get(annotationId);
            if (annotation != null) {
                shapeAnnotations.add(annotation);
            }
        }
        return shapeAnnotations;
    }
}
