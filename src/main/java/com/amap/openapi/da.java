package com.amap.openapi;

import android.content.Context;
import android.location.GnssStatus;
import android.location.GpsStatus;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.OnNmeaMessageListener;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.RequiresPermission;
import com.amap.location.common.log.ALLog;
import com.dji.permission.Permission;
import dji.publics.protocol.ResponseBase;
import java.util.List;

/* compiled from: SystemGpsProvider */
public class da implements cz {
    private LocationManager a;

    public da(Context context) {
        this.a = (LocationManager) context.getSystemService(ResponseBase.STRING_LOCATION);
    }

    public GpsStatus a(GpsStatus gpsStatus) {
        if (this.a == null) {
            return null;
        }
        try {
            return this.a.getGpsStatus(gpsStatus);
        } catch (SecurityException e) {
            ALLog.trace("@_24_1_@", "@_24_1_5_@");
            return null;
        }
    }

    public List<String> a() {
        if (this.a == null) {
            return null;
        }
        return this.a.getAllProviders();
    }

    public void a(GpsStatus.NmeaListener nmeaListener) {
        if (this.a != null) {
            this.a.removeNmeaListener(nmeaListener);
        }
    }

    public void a(LocationListener locationListener) {
        if (this.a != null) {
            try {
                this.a.removeUpdates(locationListener);
            } catch (Exception e) {
                ALLog.trace("@_24_1_@", "@_24_1_6_@");
            }
        }
    }

    public void a(OnNmeaMessageListener onNmeaMessageListener) {
        if (Build.VERSION.SDK_INT >= 24 && this.a != null) {
            this.a.removeNmeaListener(onNmeaMessageListener);
        }
    }

    public void a(String str, long j, float f, LocationListener locationListener, Looper looper) {
        try {
            if (this.a != null) {
                this.a.requestLocationUpdates(str, j, f, locationListener, looper);
            }
        } catch (SecurityException e) {
            ALLog.trace("@_24_1_@", "@_24_2_1_@");
        }
    }

    @RequiresPermission(Permission.ACCESS_FINE_LOCATION)
    public boolean a(GnssStatus.Callback callback) {
        if (this.a == null || Build.VERSION.SDK_INT < 24) {
            return false;
        }
        try {
            return this.a.registerGnssStatusCallback(callback);
        } catch (SecurityException e) {
            ALLog.trace("@_24_1_@", "@_24_1_7_@", e);
            return false;
        }
    }

    public boolean a(GpsStatus.Listener listener) {
        if (this.a == null) {
            return false;
        }
        try {
            return this.a.addGpsStatusListener(listener);
        } catch (SecurityException e) {
            ALLog.trace("@_24_1_@", "@_24_1_3_@");
            return false;
        }
    }

    public boolean a(GpsStatus.NmeaListener nmeaListener, Looper looper) {
        if (this.a == null) {
            return false;
        }
        try {
            return this.a.addNmeaListener(nmeaListener);
        } catch (SecurityException e) {
            ALLog.trace("@_24_1_@", "@_24_1_2_@");
            return false;
        }
    }

    public boolean a(OnNmeaMessageListener onNmeaMessageListener, Looper looper) {
        if (this.a == null) {
            return false;
        }
        try {
            if (Build.VERSION.SDK_INT >= 24) {
                return this.a.addNmeaListener(onNmeaMessageListener);
            }
            return false;
        } catch (SecurityException e) {
            ALLog.trace("@_24_1_@", "@_24_1_2_@");
            return false;
        }
    }

    public boolean a(String str) {
        if (this.a == null) {
            return false;
        }
        try {
            return this.a.isProviderEnabled(str);
        } catch (Exception e) {
            ALLog.trace("@_24_1_@", "@_24_1_4_@", e);
            return false;
        }
    }

    public void b(GnssStatus.Callback callback) {
        if (this.a != null && Build.VERSION.SDK_INT >= 24) {
            this.a.unregisterGnssStatusCallback(callback);
        }
    }

    public void b(GpsStatus.Listener listener) {
        if (this.a != null) {
            this.a.removeGpsStatusListener(listener);
        }
    }
}
