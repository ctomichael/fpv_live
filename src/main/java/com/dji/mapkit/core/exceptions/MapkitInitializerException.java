package com.dji.mapkit.core.exceptions;

import com.dji.mapkit.core.Mapkit;

public class MapkitInitializerException extends IllegalStateException {
    public MapkitInitializerException(@Mapkit.MapProviderConstant int provider) {
        super("Error initializing map for provider type " + provider);
    }

    public MapkitInitializerException(String s) {
        super(s);
    }
}
