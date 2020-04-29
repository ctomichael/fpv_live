package com.amap.location.offline.upload;

import com.mapzen.android.lost.internal.FusionEngine;

public class UploadConfig {
    public long bufferSize = 100;
    public long expireTimeInDb = 864000000;
    public long maxDbSize = 100000;
    public long maxSizePerDay = 5000;
    public boolean nonWifiEnable = false;
    public long sizePerRequest = 1000;
    public long storePeriod = FusionEngine.RECENT_UPDATE_THRESHOLD_IN_MILLIS;
    public long uploadPeriod = FusionEngine.RECENT_UPDATE_THRESHOLD_IN_MILLIS;
}
