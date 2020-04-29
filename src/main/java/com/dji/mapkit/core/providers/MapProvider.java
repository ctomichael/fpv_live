package com.dji.mapkit.core.providers;

import android.content.Context;
import android.support.annotation.NonNull;
import com.dji.mapkit.core.Mapkit;
import com.dji.mapkit.core.MapkitOptions;
import com.dji.mapkit.core.maps.DJIMapViewInternal;
import com.dji.mapkit.core.places.IInternalPlacesClient;

public abstract class MapProvider {
    protected static final String TAG = MapProvider.class.getSimpleName();
    protected MapProvider nextProvider;
    protected int providerType;

    /* access modifiers changed from: protected */
    public abstract IInternalPlacesClient requestGeocodingClient(Context context, MapkitOptions mapkitOptions);

    /* access modifiers changed from: protected */
    public abstract DJIMapViewInternal requestMapView(@NonNull Context context, MapkitOptions mapkitOptions);

    @Mapkit.MapProviderConstant
    public int getProviderType() {
        return this.providerType;
    }

    public MapProvider getNextProvider() {
        return this.nextProvider;
    }

    public DJIMapViewInternal dispatchMapViewRequest(@NonNull Context context, MapkitOptions mapkitOptions) {
        DJIMapViewInternal mapView = requestMapView(context, mapkitOptions);
        if (mapView != null) {
            return mapView;
        }
        if (this.nextProvider != null) {
            mapView = this.nextProvider.dispatchMapViewRequest(context, mapkitOptions);
        }
        return mapView;
    }

    public IInternalPlacesClient dispatchGeocodingClientRequest(@NonNull Context context, MapkitOptions mapkitOptions) {
        IInternalPlacesClient client = requestGeocodingClient(context, mapkitOptions);
        if (client != null) {
            return client;
        }
        if (this.nextProvider != null) {
            client = this.nextProvider.dispatchGeocodingClientRequest(context, mapkitOptions);
        }
        return client;
    }

    public MapProvider next(MapProvider nextProvider2) {
        this.nextProvider = nextProvider2;
        return this;
    }
}
