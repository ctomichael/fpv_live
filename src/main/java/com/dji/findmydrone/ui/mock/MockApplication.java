package com.dji.findmydrone.ui.mock;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Keep;
import android.support.multidex.MultiDex;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.MidWare;
import dji.sdksharedlib.DJISDKCache;

@Keep
@EXClassNullAway
public class MockApplication extends Application {
    public final String TAG = "MockApplication";

    public void onCreate() {
        super.onCreate();
        MidWare.init(this);
        DJISDKCache.getInstance().init();
    }

    /* access modifiers changed from: protected */
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
