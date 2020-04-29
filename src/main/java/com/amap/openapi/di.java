package com.amap.openapi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import com.amap.openapi.df;
import java.util.List;

/* compiled from: IWifiProvider */
public interface di {
    @RequiresPermission("android.permission.ACCESS_WIFI_STATE")
    List<ScanResult> a();

    void a(@NonNull Context context, @NonNull df.a aVar);

    @RequiresPermission("android.permission.CHANGE_WIFI_STATE")
    boolean b();

    boolean c();
}
