package com.mapbox.mapboxsdk.location;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.mapboxsdk.maps.Style;

public class LocationComponentActivationOptions {
    private final Context context;
    private final LocationComponentOptions locationComponentOptions;
    private final LocationEngine locationEngine;
    private final LocationEngineRequest locationEngineRequest;
    private final Style style;
    private final int styleRes;
    private final boolean useDefaultLocationEngine;

    private LocationComponentActivationOptions(@NonNull Context context2, @NonNull Style style2, @Nullable LocationEngine locationEngine2, @Nullable LocationEngineRequest locationEngineRequest2, @Nullable LocationComponentOptions locationComponentOptions2, int styleRes2, boolean useDefaultLocationEngine2) {
        this.context = context2;
        this.style = style2;
        this.locationEngine = locationEngine2;
        this.locationEngineRequest = locationEngineRequest2;
        this.locationComponentOptions = locationComponentOptions2;
        this.styleRes = styleRes2;
        this.useDefaultLocationEngine = useDefaultLocationEngine2;
    }

    @NonNull
    public static Builder builder(@NonNull Context context2, @NonNull Style fullyLoadedMapStyle) {
        return new Builder(context2, fullyLoadedMapStyle);
    }

    @NonNull
    public Context context() {
        return this.context;
    }

    @NonNull
    public Style style() {
        return this.style;
    }

    @Nullable
    public LocationEngine locationEngine() {
        return this.locationEngine;
    }

    @Nullable
    public LocationEngineRequest locationEngineRequest() {
        return this.locationEngineRequest;
    }

    @Nullable
    public LocationComponentOptions locationComponentOptions() {
        return this.locationComponentOptions;
    }

    public int styleRes() {
        return this.styleRes;
    }

    public boolean useDefaultLocationEngine() {
        return this.useDefaultLocationEngine;
    }

    public static class Builder {
        private final Context context;
        private LocationComponentOptions locationComponentOptions;
        private LocationEngine locationEngine;
        private LocationEngineRequest locationEngineRequest;
        private final Style style;
        private int styleRes;
        private boolean useDefaultLocationEngine = true;

        public Builder(@NonNull Context context2, @NonNull Style style2) {
            this.context = context2;
            this.style = style2;
        }

        @NonNull
        public Builder locationEngine(@Nullable LocationEngine locationEngine2) {
            this.locationEngine = locationEngine2;
            return this;
        }

        public Builder locationEngineRequest(LocationEngineRequest locationEngineRequest2) {
            this.locationEngineRequest = locationEngineRequest2;
            return this;
        }

        public Builder locationComponentOptions(LocationComponentOptions locationComponentOptions2) {
            this.locationComponentOptions = locationComponentOptions2;
            return this;
        }

        public Builder styleRes(int styleRes2) {
            this.styleRes = styleRes2;
            return this;
        }

        public Builder useDefaultLocationEngine(boolean useDefaultLocationEngine2) {
            this.useDefaultLocationEngine = useDefaultLocationEngine2;
            return this;
        }

        public LocationComponentActivationOptions build() {
            if (this.styleRes != 0 && this.locationComponentOptions != null) {
                throw new IllegalArgumentException("You've provided both a style resource and a LocationComponentOptions object to the LocationComponentActivationOptions builder. You can't use both and you must choose one of the two to style the LocationComponent.");
            } else if (this.context == null) {
                throw new NullPointerException("Context in LocationComponentActivationOptions is null.");
            } else if (this.style == null) {
                throw new NullPointerException("Style in LocationComponentActivationOptions is null. Make sure the Style object isn't null. Wait for the map to fully load before passing the Style object to LocationComponentActivationOptions.");
            } else if (this.style.isFullyLoaded()) {
                return new LocationComponentActivationOptions(this.context, this.style, this.locationEngine, this.locationEngineRequest, this.locationComponentOptions, this.styleRes, this.useDefaultLocationEngine);
            } else {
                throw new IllegalArgumentException("Style in LocationComponentActivationOptions isn't fully loaded. Wait for the map to fully load before passing the Style object to LocationComponentActivationOptions.");
            }
        }
    }
}
