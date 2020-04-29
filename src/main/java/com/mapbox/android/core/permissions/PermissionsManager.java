package com.mapbox.android.core.permissions;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import java.util.ArrayList;

public class PermissionsManager {
    private static final String COARSE_LOCATION_PERMISSION = "android.permission.ACCESS_COARSE_LOCATION";
    private static final String FINE_LOCATION_PERMISSION = "android.permission.ACCESS_FINE_LOCATION";
    private final int REQUEST_PERMISSIONS_CODE = 0;
    private PermissionsListener listener;

    public PermissionsManager(PermissionsListener listener2) {
        this.listener = listener2;
    }

    public PermissionsListener getListener() {
        return this.listener;
    }

    public void setListener(PermissionsListener listener2) {
        this.listener = listener2;
    }

    private static boolean isPermissionGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == 0;
    }

    private static boolean isCoarseLocationPermissionGranted(Context context) {
        return isPermissionGranted(context, "android.permission.ACCESS_COARSE_LOCATION");
    }

    private static boolean isFineLocationPermissionGranted(Context context) {
        return isPermissionGranted(context, "android.permission.ACCESS_FINE_LOCATION");
    }

    public static boolean areLocationPermissionsGranted(Context context) {
        return isCoarseLocationPermissionGranted(context) || isFineLocationPermissionGranted(context);
    }

    public static boolean areRuntimePermissionsRequired() {
        return Build.VERSION.SDK_INT >= 23;
    }

    public void requestLocationPermissions(Activity activity) {
        requestLocationPermissions(activity, true);
    }

    private void requestLocationPermissions(Activity activity, boolean requestFineLocation) {
        requestPermissions(activity, requestFineLocation ? new String[]{"android.permission.ACCESS_FINE_LOCATION"} : new String[]{"android.permission.ACCESS_COARSE_LOCATION"});
    }

    private void requestPermissions(Activity activity, String[] permissions) {
        ArrayList<String> permissionsToExplain = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                permissionsToExplain.add(permission);
            }
        }
        if (this.listener != null && permissionsToExplain.size() > 0) {
            this.listener.onExplanationNeeded(permissionsToExplain);
        }
        ActivityCompat.requestPermissions(activity, permissions, 0);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean granted = false;
        switch (requestCode) {
            case 0:
                if (this.listener != null) {
                    if (grantResults.length > 0 && grantResults[0] == 0) {
                        granted = true;
                    }
                    this.listener.onPermissionResult(granted);
                    return;
                }
                return;
            default:
                return;
        }
    }
}
