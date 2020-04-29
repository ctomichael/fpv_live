package com.mapbox.mapboxsdk.maps;

import android.support.annotation.NonNull;
import android.support.v4.util.LongSparseArray;
import com.mapbox.mapboxsdk.annotations.Annotation;
import java.util.ArrayList;
import java.util.List;

class AnnotationContainer implements Annotations {
    private final LongSparseArray<Annotation> annotations;
    private final NativeMap nativeMap;

    AnnotationContainer(NativeMap nativeMap2, LongSparseArray<Annotation> annotations2) {
        this.nativeMap = nativeMap2;
        this.annotations = annotations2;
    }

    public Annotation obtainBy(long id) {
        return this.annotations.get(id);
    }

    @NonNull
    public List<Annotation> obtainAll() {
        List<Annotation> annotations2 = new ArrayList<>();
        for (int i = 0; i < this.annotations.size(); i++) {
            annotations2.add(this.annotations.get(this.annotations.keyAt(i)));
        }
        return annotations2;
    }

    public void removeBy(long id) {
        if (this.nativeMap != null) {
            this.nativeMap.removeAnnotation(id);
        }
        this.annotations.remove(id);
    }

    public void removeBy(@NonNull Annotation annotation) {
        removeBy(annotation.getId());
    }

    public void removeBy(@NonNull List<? extends Annotation> annotationList) {
        int count = annotationList.size();
        long[] ids = new long[count];
        for (int i = 0; i < count; i++) {
            ids[i] = ((Annotation) annotationList.get(i)).getId();
        }
        removeNativeAnnotations(ids);
        for (long id : ids) {
            this.annotations.remove(id);
        }
    }

    public void removeAll() {
        int count = this.annotations.size();
        long[] ids = new long[count];
        for (int i = 0; i < count; i++) {
            ids[i] = this.annotations.keyAt(i);
        }
        removeNativeAnnotations(ids);
        this.annotations.clear();
    }

    private void removeNativeAnnotations(long[] ids) {
        if (this.nativeMap != null) {
            this.nativeMap.removeAnnotations(ids);
        }
    }
}
