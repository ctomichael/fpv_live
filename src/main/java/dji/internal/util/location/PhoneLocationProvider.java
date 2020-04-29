package dji.internal.util.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import com.dji.permission.Permission;
import com.mapzen.android.lost.internal.FusionEngine;
import dji.component.flysafe.util.NFZLogUtil;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.publics.protocol.ResponseBase;

@EXClassNullAway
public class PhoneLocationProvider {
    private static final String TAG = "LocationManager";
    private Context mContext;
    /* access modifiers changed from: private */
    public CustomLocationListener mCustomLocationListener;
    LocationListener[] mLocationListeners = {new LocationListener("gps"), new LocationListener("network")};
    private LocationManager mLocationManager;
    private boolean mRecordLocation;

    public interface CustomLocationListener {
        void onLocationChanged(Location location);
    }

    public void setCustomLocationListener(CustomLocationListener customLocationListener) {
        this.mCustomLocationListener = customLocationListener;
    }

    public PhoneLocationProvider(Context context) {
        this.mContext = context;
    }

    public Location getCurrentLocation() {
        if (!this.mRecordLocation) {
            return null;
        }
        for (int i = 0; i < this.mLocationListeners.length; i++) {
            Location l = this.mLocationListeners[i].current();
            if (l != null) {
                return l;
            }
        }
        Log.d(TAG, "No location received yet.");
        return null;
    }

    public void recordLocation(boolean recordLocation) {
        if (this.mRecordLocation != recordLocation) {
            this.mRecordLocation = recordLocation;
            if (recordLocation) {
                startReceivingLocationUpdates();
            } else {
                stopReceivingLocationUpdates();
            }
        }
    }

    private void startReceivingLocationUpdates() {
        if (this.mLocationManager == null) {
            this.mLocationManager = (LocationManager) this.mContext.getSystemService(ResponseBase.STRING_LOCATION);
        }
        if (this.mLocationManager != null) {
            try {
                this.mLocationManager.requestLocationUpdates("network", (long) FusionEngine.RECENT_UPDATE_THRESHOLD_IN_MILLIS, 0.0f, this.mLocationListeners[1], Looper.getMainLooper());
            } catch (SecurityException ex) {
                NFZLogUtil.savedLOGD("fail to request location update, ignore: " + ex.getMessage());
            } catch (IllegalArgumentException ex2) {
                NFZLogUtil.savedLOGD("provider does not exist " + ex2.getMessage());
            } catch (NullPointerException e) {
                NFZLogUtil.savedLOGD("android LocationManager NPE");
            }
            try {
                this.mLocationManager.requestLocationUpdates("gps", (long) FusionEngine.RECENT_UPDATE_THRESHOLD_IN_MILLIS, 0.0f, this.mLocationListeners[0], Looper.getMainLooper());
            } catch (SecurityException ex3) {
                NFZLogUtil.savedLOGD("fail to request location update, ignore: " + ex3.getMessage());
            } catch (IllegalArgumentException ex4) {
                NFZLogUtil.savedLOGD("provider does not exist " + ex4.getMessage());
            } catch (NullPointerException e2) {
                NFZLogUtil.savedLOGD("android LocationManager NPE");
            }
            NFZLogUtil.LOGD("startReceivingLocationUpdates");
        }
    }

    private void stopReceivingLocationUpdates() {
        if (this.mLocationManager != null) {
            int i = 0;
            while (i < this.mLocationListeners.length) {
                try {
                    if (Build.VERSION.SDK_INT < 23 || this.mContext.checkSelfPermission(Permission.ACCESS_FINE_LOCATION) == 0 || this.mContext.checkSelfPermission(Permission.ACCESS_COARSE_LOCATION) == 0) {
                        this.mLocationManager.removeUpdates(this.mLocationListeners[i]);
                        i++;
                    } else {
                        return;
                    }
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
            Log.d(TAG, "stopReceivingLocationUpdates");
        }
    }

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;
        String mProvider;
        boolean mValid = false;

        public LocationListener(String provider) {
            this.mProvider = provider;
            this.mLastLocation = new Location(this.mProvider);
        }

        public void onLocationChanged(Location newLocation) {
            if (newLocation.getLatitude() != 0.0d || newLocation.getLongitude() != 0.0d) {
                if (!this.mValid) {
                    DJILog.d(PhoneLocationProvider.TAG, "Got first location." + newLocation, new Object[0]);
                    DJILogHelper.getInstance().LOGD(PhoneLocationProvider.TAG, "Got first location." + newLocation, false, true);
                }
                this.mLastLocation.set(newLocation);
                this.mValid = true;
                if (PhoneLocationProvider.this.mCustomLocationListener != null) {
                    PhoneLocationProvider.this.mCustomLocationListener.onLocationChanged(newLocation);
                }
            }
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
            this.mValid = false;
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case 0:
                case 1:
                    this.mValid = false;
                    return;
                default:
                    return;
            }
        }

        public Location current() {
            DJILogHelper.getInstance().LOGD(PhoneLocationProvider.TAG, "Got first location." + this.mValid, false, true);
            if (this.mValid) {
                return this.mLastLocation;
            }
            return null;
        }
    }
}
