package com.amap.location.offline;

import com.amap.location.common.network.IHttpClient;
import com.amap.location.offline.upload.UploadConfig;

public class OfflineConfig {
    public static final byte PRODUCT_FLP = 1;
    public static final byte PRODUCT_NLP = 2;
    public static final byte PRODUCT_OPEN_SDK = 4;
    public static final byte PRODUCT_SDK_AMAP = 0;
    public static final byte PRODUCT_SDK_AUTO = 3;
    public static final byte PRODUCT_UNKNOWN = -1;
    public static boolean sUseTestNet = false;
    public String adiu = "";
    public String[] contentProviderList = null;
    public ICoordinateConverter coordinateConverter = null;
    public IHttpClient httpClient = null;
    public String imei = "";
    public String imsi = "";
    public String license = "";
    public boolean locEnable = true;
    public ILocateLogRecorder mLocateLogRecorder = null;
    public String mapKey = "";
    public String packageName = "";
    public byte productId = -1;
    public String productVersion = "";
    public UploadConfig uploadConfig = null;
    public String uuid = "";

    public interface ICoordinateConverter {
        double[] wgsToGcj(double[] dArr);
    }

    public interface ILocateLogRecorder {
        void onLocateSuccess(byte[] bArr);
    }
}
