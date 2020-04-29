package com.amap.location;

import android.content.Context;
import android.support.annotation.NonNull;
import com.amap.location.common.a;
import com.amap.openapi.b;
import com.amap.openapi.d;

public class BasicLocateManager {
    private static volatile BasicLocateManager mInstance;
    private b mCloudManager;

    private BasicLocateManager() {
    }

    private void destroyCloud() {
        if (this.mCloudManager != null) {
            this.mCloudManager.b();
        }
    }

    public static BasicLocateManager getInstance() {
        if (mInstance == null) {
            synchronized (BasicLocateManager.class) {
                if (mInstance == null) {
                    mInstance = new BasicLocateManager();
                }
            }
        }
        return mInstance;
    }

    private void initCloud(Context context, BasicLocateConfig basicLocateConfig) {
        if (this.mCloudManager == null) {
            this.mCloudManager = b.a();
            d dVar = new d();
            dVar.a(basicLocateConfig.getProductId());
            dVar.a(basicLocateConfig.getProductVersion());
            dVar.c(basicLocateConfig.getLicense());
            dVar.b(basicLocateConfig.getMapkey());
            dVar.d(basicLocateConfig.getUtdid());
            dVar.e(basicLocateConfig.getAdiu());
            dVar.a(basicLocateConfig.getHttpClient());
            this.mCloudManager.a(context, dVar);
        }
        a.a(context, basicLocateConfig.getUtdid());
    }

    public synchronized void destroy() {
        destroyCloud();
    }

    public synchronized void init(@NonNull Context context, @NonNull BasicLocateConfig basicLocateConfig) {
        initCloud(context, basicLocateConfig);
    }
}
