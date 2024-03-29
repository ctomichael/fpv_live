package com.mapbox.mapboxsdk;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mapbox.mapboxsdk.http.HttpRequest;
import com.mapbox.mapboxsdk.maps.TelemetryDefinition;
import com.mapbox.mapboxsdk.module.http.HttpRequestImpl;
import com.mapbox.mapboxsdk.module.loader.LibraryLoaderProviderImpl;
import com.mapbox.mapboxsdk.module.telemetry.TelemetryImpl;

public class ModuleProviderImpl implements ModuleProvider {
    @NonNull
    public HttpRequest createHttpRequest() {
        return new HttpRequestImpl();
    }

    @Nullable
    public TelemetryDefinition obtainTelemetry() {
        return new TelemetryImpl();
    }

    @NonNull
    public LibraryLoaderProvider createLibraryLoaderProvider() {
        return new LibraryLoaderProviderImpl();
    }
}
