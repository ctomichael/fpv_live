package com.loc;

import com.amap.location.offline.IOfflineCloudConfig;

/* compiled from: OfflineCloudConfig */
public final class cn implements IOfflineCloudConfig {
    public final boolean clearAll() {
        return false;
    }

    public final long getConfigTime() {
        return 0;
    }

    public final String[] getContentProviderList() {
        return null;
    }

    public final int getMaxNonWifiRequestTimes() {
        return 5;
    }

    public final int getMaxNumPerRequest() {
        return 100;
    }

    public final int getMaxRequestTimes() {
        return 10;
    }

    public final int getMinWifiNum() {
        return 8;
    }

    public final boolean getNeedFirstDownload() {
        return false;
    }

    public final int getTrainingThreshold() {
        return 6;
    }

    public final boolean isEnable() {
        return true;
    }
}
