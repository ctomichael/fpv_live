package com.mapbox.mapboxsdk.maps;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mapbox.mapboxsdk.utils.MapFragmentUtils;
import java.util.ArrayList;
import java.util.List;

public final class MapFragment extends Fragment implements OnMapReadyCallback {
    private MapView map;
    private final List<OnMapReadyCallback> mapReadyCallbackList = new ArrayList();
    private OnMapViewReadyCallback mapViewReadyCallback;
    private MapboxMap mapboxMap;

    public interface OnMapViewReadyCallback {
        void onMapViewReady(MapView mapView);
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @NonNull
    public static MapFragment newInstance(@Nullable MapboxMapOptions mapboxMapOptions) {
        MapFragment mapFragment = new MapFragment();
        mapFragment.setArguments(MapFragmentUtils.createFragmentArgs(mapboxMapOptions));
        return mapFragment;
    }

    public void onInflate(@NonNull Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        setArguments(MapFragmentUtils.createFragmentArgs(MapboxMapOptions.createFromAttributes(context, attrs)));
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMapViewReadyCallback) {
            this.mapViewReadyCallback = (OnMapViewReadyCallback) context;
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Context context = inflater.getContext();
        this.map = new MapView(context, MapFragmentUtils.resolveArgs(context, getArguments()));
        return this.map;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.map.onCreate(savedInstanceState);
        this.map.getMapAsync(this);
        if (this.mapViewReadyCallback != null) {
            this.mapViewReadyCallback.onMapViewReady(this.map);
        }
    }

    public void onMapReady(@NonNull MapboxMap mapboxMap2) {
        this.mapboxMap = mapboxMap2;
        for (OnMapReadyCallback onMapReadyCallback : this.mapReadyCallbackList) {
            onMapReadyCallback.onMapReady(mapboxMap2);
        }
    }

    public void onStart() {
        super.onStart();
        this.map.onStart();
    }

    public void onResume() {
        super.onResume();
        this.map.onResume();
    }

    public void onPause() {
        super.onPause();
        this.map.onPause();
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (this.map != null && !this.map.isDestroyed()) {
            this.map.onSaveInstanceState(outState);
        }
    }

    public void onStop() {
        super.onStop();
        this.map.onStop();
    }

    public void onLowMemory() {
        super.onLowMemory();
        if (this.map != null && !this.map.isDestroyed()) {
            this.map.onLowMemory();
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.map.onDestroy();
    }

    public void onDestroy() {
        super.onDestroy();
        this.mapReadyCallbackList.clear();
    }

    public void getMapAsync(@NonNull OnMapReadyCallback onMapReadyCallback) {
        if (this.mapboxMap == null) {
            this.mapReadyCallbackList.add(onMapReadyCallback);
        } else {
            onMapReadyCallback.onMapReady(this.mapboxMap);
        }
    }
}
