package com.amap.location.offline;

public interface IOfflineCloudConfig {
    boolean clearAll();

    long getConfigTime();

    String[] getContentProviderList();

    int getMaxNonWifiRequestTimes();

    int getMaxNumPerRequest();

    int getMaxRequestTimes();

    int getMinWifiNum();

    boolean getNeedFirstDownload();

    int getTrainingThreshold();

    boolean isEnable();
}
