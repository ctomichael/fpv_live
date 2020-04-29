package com.dji.mapkit.core.places;

import com.dji.mapkit.core.models.DJILatLng;
import com.dji.mapkit.core.places.DJIPlacesClient;

public interface IInternalPlacesClient {
    public static final int POI_RADIUS = 300;

    void regeocodeSearchAsyn(DJILatLng dJILatLng);

    void searchPOIAsyn(DJILatLng dJILatLng);

    void searchPOIAsyn(DJILatLng dJILatLng, int i);

    void setOnPoiSearchListener(DJIPlacesClient.OnPoiSearchListener onPoiSearchListener);

    void setOnRegeocodeSearchListener(DJIPlacesClient.OnRegeocodeSearchListener onRegeocodeSearchListener);

    void setPoiSearchQuery(DJIPoiSearchQuery dJIPoiSearchQuery);
}
