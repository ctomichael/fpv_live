package com.amap.location.offline;

import android.content.Context;
import android.support.annotation.NonNull;
import com.amap.location.common.model.AmapLoc;
import com.amap.location.common.model.FPS;
import com.amap.location.offline.upload.a;
import com.amap.openapi.bo;
import com.amap.openapi.bp;
import com.amap.openapi.co;

public class OfflineManager {
    private static final String TAG = "OfflineManager";
    private static volatile OfflineManager mInstance;
    private a mCloudConfig;
    private bo mCloudWrapper;
    private OfflineConfig mConfig;
    private Context mContext;
    private bp mOfflineCore;
    private b mOfflineRemoteProxy;

    private OfflineManager() {
    }

    public static OfflineManager getInstance() {
        if (mInstance == null) {
            synchronized (OfflineManager.class) {
                if (mInstance == null) {
                    mInstance = new OfflineManager();
                }
            }
        }
        return mInstance;
    }

    private void initOfflineCore() {
        this.mOfflineCore = new bp(this.mContext, this.mConfig, this.mCloudConfig);
        this.mOfflineCore.a();
    }

    public synchronized void correctLocation(@NonNull FPS fps, AmapLoc amapLoc) {
        correctLocation(fps, amapLoc, this.mContext.getPackageName());
    }

    /* access modifiers changed from: protected */
    public synchronized void correctLocation(@NonNull FPS fps, AmapLoc amapLoc, String str) {
        if (isEnable()) {
            if (this.mOfflineCore != null) {
                this.mOfflineCore.a(fps, amapLoc);
            } else if (!this.mOfflineRemoteProxy.a(fps, amapLoc, str)) {
                initOfflineCore();
            }
        }
    }

    public synchronized void destroy() {
        OfflineConfig offlineConfig = this.mConfig;
        this.mConfig = null;
        this.mCloudConfig = null;
        this.mOfflineRemoteProxy = null;
        if (this.mCloudWrapper != null) {
            this.mCloudWrapper.b();
            this.mCloudWrapper = null;
        }
        if (this.mOfflineCore != null) {
            this.mOfflineCore.b();
            this.mOfflineCore = null;
        }
        a.a(offlineConfig);
    }

    /* access modifiers changed from: protected */
    public synchronized AmapLoc getLocation(@NonNull FPS fps, int i, boolean z, String str) {
        AmapLoc a;
        if (!isEnable()) {
            a = null;
        } else {
            if (this.mOfflineCore == null) {
                co.a a2 = this.mOfflineRemoteProxy.a(fps, i, str);
                if (a2.a) {
                    a = a2.b;
                } else {
                    initOfflineCore();
                }
            }
            a = this.mOfflineCore.a(fps, i, z);
        }
        return a;
    }

    public synchronized AmapLoc getLocation(@NonNull FPS fps, boolean z) {
        AmapLoc location;
        int i = 0;
        synchronized (this) {
            if (z) {
                if (this.mCloudConfig != null) {
                    i = this.mCloudConfig.getMinWifiNum();
                }
            }
            if (z) {
                a.a(100033);
            } else {
                a.a(100034);
            }
            location = getLocation(fps, i, false, this.mContext.getPackageName());
        }
        return location;
    }

    public synchronized void init(@NonNull Context context, @NonNull OfflineConfig offlineConfig, @NonNull IOfflineCloudConfig iOfflineCloudConfig) {
        if (this.mConfig == null) {
            if (offlineConfig != null) {
                this.mConfig = offlineConfig;
            } else {
                this.mConfig = new OfflineConfig();
            }
        }
        if (this.mCloudConfig == null) {
            this.mCloudConfig = new a();
            if (iOfflineCloudConfig != null) {
                this.mCloudConfig.a = iOfflineCloudConfig;
            }
        }
        if (this.mCloudWrapper == null) {
            this.mCloudWrapper = new bo(context, this.mConfig, this.mCloudConfig);
            this.mCloudWrapper.a();
        }
        if (this.mOfflineRemoteProxy == null) {
            this.mContext = context.getApplicationContext();
            a.a(this.mContext, this.mConfig, this.mCloudConfig);
            this.mOfflineRemoteProxy = new b(context, this.mConfig, this.mCloudConfig);
            if (!this.mOfflineRemoteProxy.a(this.mContext.getPackageName()) && this.mOfflineCore == null) {
                initOfflineCore();
            }
        }
    }

    public synchronized boolean isEnable() {
        return (this.mOfflineRemoteProxy == null || this.mConfig == null || !this.mConfig.locEnable || this.mCloudConfig == null || !this.mCloudConfig.isEnable()) ? false : true;
    }

    public synchronized void trainingFps(@NonNull FPS fps) {
        getLocation(fps, 0, true, this.mContext.getPackageName());
    }

    public synchronized void updateConfig(OfflineConfig offlineConfig) {
        if (offlineConfig != null) {
            if (this.mOfflineRemoteProxy != null) {
                this.mConfig = offlineConfig;
                this.mOfflineRemoteProxy.a(this.mConfig);
                if (this.mOfflineCore != null) {
                    this.mOfflineCore.a(this.mConfig);
                }
            }
        }
    }
}
