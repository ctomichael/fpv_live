package com.amap.openapi;

import android.location.GnssStatus;
import android.location.GpsStatus;
import android.location.LocationListener;
import android.location.OnNmeaMessageListener;
import android.os.Looper;
import android.support.annotation.RequiresPermission;
import com.dji.permission.Permission;
import java.util.List;

/* compiled from: IGpsProvider */
public interface cz {
    GpsStatus a(GpsStatus gpsStatus);

    List<String> a();

    @Deprecated
    void a(GpsStatus.NmeaListener nmeaListener);

    void a(LocationListener locationListener);

    void a(OnNmeaMessageListener onNmeaMessageListener);

    @RequiresPermission(Permission.ACCESS_FINE_LOCATION)
    void a(String str, long j, float f, LocationListener locationListener, Looper looper);

    @RequiresPermission(Permission.ACCESS_FINE_LOCATION)
    boolean a(GnssStatus.Callback callback);

    @RequiresPermission(Permission.ACCESS_FINE_LOCATION)
    @Deprecated
    boolean a(GpsStatus.Listener listener);

    @RequiresPermission(Permission.ACCESS_FINE_LOCATION)
    @Deprecated
    boolean a(GpsStatus.NmeaListener nmeaListener, Looper looper);

    @RequiresPermission(Permission.ACCESS_FINE_LOCATION)
    boolean a(OnNmeaMessageListener onNmeaMessageListener, Looper looper);

    boolean a(String str);

    void b(GnssStatus.Callback callback);

    @Deprecated
    void b(GpsStatus.Listener listener);
}
