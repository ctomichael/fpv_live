package dji.common.util;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.util.BackgroundLooper;
import dji.publics.protocol.ResponseBase;

@EXClassNullAway
public class MobileGPSLocationUtil {
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private static final long MIN_TIME_BW_UPDATES = 1000;
    private static final String TAG = "MobileGPSLocationUtil";
    private Context activityContext;
    private LocationListener locationListener;
    private LocationManager locationManager;

    public MobileGPSLocationUtil(Context context, LocationListener listener) {
        this.activityContext = context;
        this.locationListener = listener;
    }

    public void startUpdateLocation() {
        if (this.activityContext != null && this.locationListener != null) {
            try {
                this.locationManager = (LocationManager) this.activityContext.getSystemService(ResponseBase.STRING_LOCATION);
                if (this.locationManager.isProviderEnabled("gps")) {
                    this.locationManager.requestLocationUpdates("gps", 1000, 1.0f, this.locationListener, BackgroundLooper.getLooper());
                }
            } catch (SecurityException e) {
                DJILog.e(TAG, e.getMessage(), new Object[0]);
            }
        }
    }

    public void stopUpdateLocation() {
        if (this.locationManager != null) {
            try {
                this.locationManager.removeUpdates(this.locationListener);
            } catch (SecurityException e) {
                DJILog.e(TAG, e.getMessage(), new Object[0]);
            }
        }
    }
}
