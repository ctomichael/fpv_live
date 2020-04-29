package com.mapbox.mapboxsdk.maps;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mapbox.mapboxsdk.maps.MapFragment;
import com.mapbox.mapboxsdk.utils.MapFragmentUtils;
import java.util.ArrayList;
import java.util.List;

public class SupportMapFragment extends Fragment implements OnMapReadyCallback {
    private MapView map;
    private final List<OnMapReadyCallback> mapReadyCallbackList = new ArrayList();
    private MapFragment.OnMapViewReadyCallback mapViewReadyCallback;
    private MapboxMap mapboxMap;

    public static SupportMapFragment newInstance() {
        return new SupportMapFragment();
    }

    @NonNull
    public static SupportMapFragment newInstance(@Nullable MapboxMapOptions mapboxMapOptions) {
        SupportMapFragment mapFragment = new SupportMapFragment();
        mapFragment.setArguments(MapFragmentUtils.createFragmentArgs(mapboxMapOptions));
        return mapFragment;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MapFragment.OnMapViewReadyCallback) {
            this.mapViewReadyCallback = (MapFragment.OnMapViewReadyCallback) context;
        }
    }

    public void onInflate(@NonNull Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        setArguments(MapFragmentUtils.createFragmentArgs(MapboxMapOptions.createFromAttributes(context, attrs)));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        if (this.map != null) {
            this.map.onSaveInstanceState(outState);
        }
    }

    public void onStop() {
        super.onStop();
        this.map.onStop();
    }

    public void onLowMemory() {
        super.onLowMemory();
        if (this.map != null) {
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
