package com.mapbox.android.core.location;

import android.support.annotation.NonNull;

public interface LocationEngineCallback<T> {
    void onFailure(@NonNull Exception exc);

    void onSuccess(Object obj);
}
