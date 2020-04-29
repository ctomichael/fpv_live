package com.dji.mapkit.core.maps;

import android.os.Bundle;
import com.dji.mapkit.core.maps.DJIMapView;

public interface DJIMapViewInternal {
    void getDJIMapAsync(DJIMapView.OnDJIMapReadyCallback onDJIMapReadyCallback);

    void onCreate(Bundle bundle);

    void onDestroy();

    void onLowMemory();

    void onPause();

    void onResume();

    void onSaveInstanceState(Bundle bundle);

    void onStart();

    void onStop();
}
