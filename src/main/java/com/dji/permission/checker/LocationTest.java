package com.dji.permission.checker;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import com.dji.permission.Permission;
import dji.publics.protocol.ResponseBase;
import java.util.List;

class LocationTest implements PermissionTest {
    private LocationManager mManager;

    LocationTest(Context context) {
        this.mManager = (LocationManager) context.getSystemService(ResponseBase.STRING_LOCATION);
    }

    @RequiresPermission(anyOf = {Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION})
    public boolean test() throws Throwable {
        List<String> list = this.mManager.getProviders(true);
        if (!list.contains("gps") && !list.contains("network")) {
            this.mManager.requestLocationUpdates("gps", 0, 0.0f, new MLocationListener(this.mManager));
        }
        return true;
    }

    private static class MLocationListener implements LocationListener {
        private LocationManager mManager;

        public MLocationListener(LocationManager manager) {
            this.mManager = manager;
        }

        public void onLocationChanged(Location location) {
            this.mManager.removeUpdates(this);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            this.mManager.removeUpdates(this);
        }

        public void onProviderEnabled(String provider) {
            this.mManager.removeUpdates(this);
        }

        public void onProviderDisabled(String provider) {
            this.mManager.removeUpdates(this);
        }
    }
}
