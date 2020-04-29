package com.mapbox.mapboxsdk.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mapbox.mapboxsdk.constants.MapboxConstants;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;

public class MapFragmentUtils {
    @NonNull
    public static Bundle createFragmentArgs(MapboxMapOptions options) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(MapboxConstants.FRAG_ARG_MAPBOXMAPOPTIONS, options);
        return bundle;
    }

    @Nullable
    public static MapboxMapOptions resolveArgs(@NonNull Context context, @Nullable Bundle args) {
        if (args == null || !args.containsKey(MapboxConstants.FRAG_ARG_MAPBOXMAPOPTIONS)) {
            return MapboxMapOptions.createFromAttributes(context);
        }
        return (MapboxMapOptions) args.getParcelable(MapboxConstants.FRAG_ARG_MAPBOXMAPOPTIONS);
    }
}
