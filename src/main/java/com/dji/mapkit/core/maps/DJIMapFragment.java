package com.dji.mapkit.core.maps;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dji.mapkit.core.MapkitOptions;
import com.dji.mapkit.core.constants.MapkitConstants;
import com.dji.mapkit.core.maps.DJIMapView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DJIMapFragment extends Fragment implements DJIMapView.OnDJIMapReadyCallback {
    private DJIMap djiMap;
    private List<DJIMapView.OnDJIMapReadyCallback> mapReadyCallbackList = new ArrayList();
    private DJIMapView mapView;

    public static DJIMapFragment newInstance(MapkitOptions mapkitOptions) {
        DJIMapFragment fragment = new DJIMapFragment();
        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList(MapkitConstants.KEY_SUPPORT_PROVIDER_LIST, (ArrayList) mapkitOptions.getProviderList());
        bundle.putInt(MapkitConstants.KEY_SUPPORT_MAP_TYPE, mapkitOptions.getMapType());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.mapView = new DJIMapView((Activity) inflater.getContext(), parseBundle(getArguments()));
        return this.mapView;
    }

    private MapkitOptions parseBundle(Bundle arguments) {
        int mapType = arguments.getInt(MapkitConstants.KEY_SUPPORT_MAP_TYPE);
        ArrayList<Integer> supportList = arguments.getIntegerArrayList(MapkitConstants.KEY_SUPPORT_PROVIDER_LIST);
        MapkitOptions.Builder builder = new MapkitOptions.Builder();
        builder.mapType(mapType);
        if (supportList != null && !supportList.isEmpty()) {
            Iterator<Integer> it2 = supportList.iterator();
            while (it2.hasNext()) {
                builder.addMapProvider(it2.next().intValue());
            }
        }
        return builder.build();
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mapView.onCreate(savedInstanceState);
        this.mapView.getDJIMapAsync(this);
    }

    public void onDJIMapReady(DJIMap map) {
        this.djiMap = map;
        for (DJIMapView.OnDJIMapReadyCallback onMapReadyCallback : this.mapReadyCallbackList) {
            onMapReadyCallback.onDJIMapReady(this.djiMap);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.mapView.onSaveInstanceState(outState);
    }

    public void onStart() {
        super.onStart();
        this.mapView.onStart();
    }

    public void onResume() {
        super.onResume();
        this.mapView.onResume();
    }

    public void onPause() {
        super.onPause();
        this.mapView.onPause();
    }

    public void onStop() {
        super.onStop();
        this.mapView.onStop();
    }

    public void onLowMemory() {
        super.onLowMemory();
        this.mapView.onLowMemory();
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.mapView.onDestroy();
        this.mapReadyCallbackList.clear();
    }

    public void getDJIMapAsync(DJIMapView.OnDJIMapReadyCallback onDJIMapReadyCallback) {
        if (this.djiMap == null) {
            this.mapReadyCallbackList.add(onDJIMapReadyCallback);
        } else {
            onDJIMapReadyCallback.onDJIMapReady(this.djiMap);
        }
    }
}
