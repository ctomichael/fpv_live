package com.mapbox.mapboxsdk.maps;

import android.support.annotation.NonNull;
import com.mapbox.mapboxsdk.annotations.Annotation;
import java.util.List;

interface Annotations {
    List<Annotation> obtainAll();

    Annotation obtainBy(long j);

    void removeAll();

    void removeBy(long j);

    void removeBy(@NonNull Annotation annotation);

    void removeBy(@NonNull List<? extends Annotation> list);
}
