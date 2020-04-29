package com.amap.location.offline;

/* compiled from: OfflineCloudConfig */
public class a implements IOfflineCloudConfig {
    public IOfflineCloudConfig a;

    public boolean clearAll() {
        if (this.a != null) {
            return this.a.clearAll();
        }
        return false;
    }

    public long getConfigTime() {
        if (this.a != null) {
            return this.a.getConfigTime();
        }
        return 0;
    }

    public String[] getContentProviderList() {
        if (this.a != null) {
            return this.a.getContentProviderList();
        }
        return null;
    }

    public int getMaxNonWifiRequestTimes() {
        if (this.a != null) {
            return this.a.getMaxNonWifiRequestTimes();
        }
        return 5;
    }

    public int getMaxNumPerRequest() {
        if (this.a != null) {
            return this.a.getMaxNumPerRequest();
        }
        return 100;
    }

    public int getMaxRequestTimes() {
        if (this.a != null) {
            return this.a.getMaxRequestTimes();
        }
        return 10;
    }

    public int getMinWifiNum() {
        if (this.a != null) {
            return this.a.getMinWifiNum();
        }
        return 8;
    }

    public boolean getNeedFirstDownload() {
        if (this.a != null) {
            return this.a.getNeedFirstDownload();
        }
        return false;
    }

    public int getTrainingThreshold() {
        if (this.a != null) {
            return this.a.getTrainingThreshold();
        }
        return 6;
    }

    public boolean isEnable() {
        if (this.a != null) {
            return this.a.isEnable();
        }
        return true;
    }
}
