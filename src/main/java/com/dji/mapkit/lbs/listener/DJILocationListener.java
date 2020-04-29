package com.dji.mapkit.lbs.listener;

import android.os.Bundle;
import com.dji.mapkit.core.models.DJILatLng;

public interface DJILocationListener {
    void onLocationChanged(DJILatLng dJILatLng);

    void onLocationFailed(int i);

    void onProcessTypeChanged(int i);

    void onProviderDisabled(String str);

    void onProviderEnabled(String str);

    void onStatusChanged(String str, int i, Bundle bundle);
}
