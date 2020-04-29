package com.dji.mapkit.core.places;

import android.content.Context;
import android.support.annotation.NonNull;
import com.dji.mapkit.core.Mapkit;
import com.dji.mapkit.core.MapkitOptions;
import com.dji.mapkit.core.models.DJILatLng;
import com.dji.mapkit.core.providers.MapProvider;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class DJIPlacesClient {
    private static final int AMAP_SEARCH_SUCCESS_CODE = 1000;
    private static final int POI_RADIUS = 300;
    private IInternalPlacesClient client;

    public interface OnRegeocodeSearchListener {
        void onSearched(DJIRegeocodeResult dJIRegeocodeResult);
    }

    public interface OnPoiSearchListener {
        void onPoiSearchFailed();

        void onPoiSearched(List<DJIPoiItem> list);
    }

    public DJIPlacesClient(@NonNull Context context, @NonNull MapkitOptions mapkitOptions) {
        List<Integer> providerList = mapkitOptions.getProviderList();
        int i = 0;
        while (i < providerList.size()) {
            try {
                this.client = ((MapProvider) Class.forName(Mapkit.getMapProviderClassName(providerList.get(i).intValue())).getConstructor(new Class[0]).newInstance(new Object[0])).dispatchGeocodingClientRequest(context, mapkitOptions);
                if (this.client == null) {
                    i++;
                } else {
                    return;
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoClassDefFoundError | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public void setOnPoiSearchListener(OnPoiSearchListener onPoiSearchListener) {
        this.client.setOnPoiSearchListener(onPoiSearchListener);
    }

    public void setPoiSearchQuery(DJIPoiSearchQuery poiSearchQuery) {
        this.client.setPoiSearchQuery(poiSearchQuery);
    }

    public void searchPOIAsyn(DJILatLng latLng) {
        this.client.searchPOIAsyn(latLng);
    }

    public void searchPOIAsyn(DJILatLng latLng, int radius) {
        this.client.searchPOIAsyn(latLng, radius);
    }

    public void regeocodeSearchAsyn(DJILatLng latLng) {
        this.client.regeocodeSearchAsyn(latLng);
    }

    public void setOnRegeocodeSearchListener(OnRegeocodeSearchListener onRegeocodeSearchListener) {
        this.client.setOnRegeocodeSearchListener(onRegeocodeSearchListener);
    }
}
