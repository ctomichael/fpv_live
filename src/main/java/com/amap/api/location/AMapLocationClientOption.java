package com.amap.api.location;

import android.os.Parcel;
import android.os.Parcelable;
import com.loc.es;

public class AMapLocationClientOption implements Parcelable, Cloneable {
    public static final Parcelable.Creator<AMapLocationClientOption> CREATOR = new Parcelable.Creator<AMapLocationClientOption>() {
        /* class com.amap.api.location.AMapLocationClientOption.AnonymousClass1 */

        public final /* synthetic */ Object createFromParcel(Parcel parcel) {
            return new AMapLocationClientOption(parcel);
        }

        public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
            return new AMapLocationClientOption[i];
        }
    };
    public static boolean OPEN_ALWAYS_SCAN_WIFI = true;
    public static long SCAN_WIFI_INTERVAL = 30000;
    static String a = "";
    private static AMapLocationProtocol j = AMapLocationProtocol.HTTP;
    private static boolean u = true;
    private long b = 2000;
    private long c = ((long) es.f);
    private boolean d = false;
    private boolean e = true;
    private boolean f = true;
    private boolean g = true;
    private boolean h = true;
    private AMapLocationMode i = AMapLocationMode.Hight_Accuracy;
    private boolean k = false;
    private boolean l = false;
    private boolean m = true;
    private boolean n = true;
    private boolean o = false;
    private boolean p = false;
    private boolean q = true;
    private long r = 30000;
    private long s = 30000;
    private GeoLanguage t = GeoLanguage.DEFAULT;
    private float v = 0.0f;
    private AMapLocationPurpose w = null;

    public enum AMapLocationMode {
        Battery_Saving,
        Device_Sensors,
        Hight_Accuracy
    }

    public enum AMapLocationProtocol {
        HTTP(0),
        HTTPS(1);
        
        private int a;

        private AMapLocationProtocol(int i) {
            this.a = i;
        }

        public final int getValue() {
            return this.a;
        }
    }

    public enum AMapLocationPurpose {
        SignIn,
        Transport,
        Sport
    }

    public enum GeoLanguage {
        DEFAULT,
        ZH,
        EN
    }

    public AMapLocationClientOption() {
    }

    protected AMapLocationClientOption(Parcel parcel) {
        boolean z = true;
        this.b = parcel.readLong();
        this.c = parcel.readLong();
        this.d = parcel.readByte() != 0;
        this.e = parcel.readByte() != 0;
        this.f = parcel.readByte() != 0;
        this.g = parcel.readByte() != 0;
        this.h = parcel.readByte() != 0;
        int readInt = parcel.readInt();
        this.i = readInt == -1 ? AMapLocationMode.Hight_Accuracy : AMapLocationMode.values()[readInt];
        this.k = parcel.readByte() != 0;
        this.l = parcel.readByte() != 0;
        this.m = parcel.readByte() != 0;
        this.n = parcel.readByte() != 0;
        this.o = parcel.readByte() != 0;
        this.p = parcel.readByte() != 0;
        this.q = parcel.readByte() != 0;
        this.r = parcel.readLong();
        int readInt2 = parcel.readInt();
        j = readInt2 == -1 ? AMapLocationProtocol.HTTP : AMapLocationProtocol.values()[readInt2];
        int readInt3 = parcel.readInt();
        this.t = readInt3 == -1 ? GeoLanguage.DEFAULT : GeoLanguage.values()[readInt3];
        u = parcel.readByte() != 0;
        this.v = parcel.readFloat();
        int readInt4 = parcel.readInt();
        this.w = readInt4 == -1 ? null : AMapLocationPurpose.values()[readInt4];
        OPEN_ALWAYS_SCAN_WIFI = parcel.readByte() == 0 ? false : z;
        this.s = parcel.readLong();
    }

    public static String getAPIKEY() {
        return a;
    }

    public static boolean isDownloadCoordinateConvertLibrary() {
        return u;
    }

    public static boolean isOpenAlwaysScanWifi() {
        return OPEN_ALWAYS_SCAN_WIFI;
    }

    public static void setDownloadCoordinateConvertLibrary(boolean z) {
        u = z;
    }

    public static void setLocationProtocol(AMapLocationProtocol aMapLocationProtocol) {
        j = aMapLocationProtocol;
    }

    public static void setOpenAlwaysScanWifi(boolean z) {
        OPEN_ALWAYS_SCAN_WIFI = z;
    }

    public static void setScanWifiInterval(long j2) {
        SCAN_WIFI_INTERVAL = j2;
    }

    public AMapLocationClientOption clone() {
        try {
            super.clone();
        } catch (Throwable th) {
            th.printStackTrace();
        }
        AMapLocationClientOption aMapLocationClientOption = new AMapLocationClientOption();
        aMapLocationClientOption.b = this.b;
        aMapLocationClientOption.d = this.d;
        aMapLocationClientOption.i = this.i;
        aMapLocationClientOption.e = this.e;
        aMapLocationClientOption.k = this.k;
        aMapLocationClientOption.l = this.l;
        aMapLocationClientOption.f = this.f;
        aMapLocationClientOption.g = this.g;
        aMapLocationClientOption.c = this.c;
        aMapLocationClientOption.m = this.m;
        aMapLocationClientOption.n = this.n;
        aMapLocationClientOption.o = this.o;
        aMapLocationClientOption.p = isSensorEnable();
        aMapLocationClientOption.q = isWifiScan();
        aMapLocationClientOption.r = this.r;
        setLocationProtocol(getLocationProtocol());
        aMapLocationClientOption.t = this.t;
        setDownloadCoordinateConvertLibrary(isDownloadCoordinateConvertLibrary());
        aMapLocationClientOption.v = this.v;
        aMapLocationClientOption.w = this.w;
        setOpenAlwaysScanWifi(isOpenAlwaysScanWifi());
        setScanWifiInterval(getScanWifiInterval());
        aMapLocationClientOption.s = this.s;
        return aMapLocationClientOption;
    }

    public int describeContents() {
        return 0;
    }

    public float getDeviceModeDistanceFilter() {
        return this.v;
    }

    public GeoLanguage getGeoLanguage() {
        return this.t;
    }

    public long getGpsFirstTimeout() {
        return this.s;
    }

    public long getHttpTimeOut() {
        return this.c;
    }

    public long getInterval() {
        return this.b;
    }

    public long getLastLocationLifeCycle() {
        return this.r;
    }

    public AMapLocationMode getLocationMode() {
        return this.i;
    }

    public AMapLocationProtocol getLocationProtocol() {
        return j;
    }

    public AMapLocationPurpose getLocationPurpose() {
        return this.w;
    }

    public long getScanWifiInterval() {
        return SCAN_WIFI_INTERVAL;
    }

    public boolean isGpsFirst() {
        return this.l;
    }

    public boolean isKillProcess() {
        return this.k;
    }

    public boolean isLocationCacheEnable() {
        return this.n;
    }

    public boolean isMockEnable() {
        return this.e;
    }

    public boolean isNeedAddress() {
        return this.f;
    }

    public boolean isOffset() {
        return this.m;
    }

    public boolean isOnceLocation() {
        return this.d;
    }

    public boolean isOnceLocationLatest() {
        return this.o;
    }

    public boolean isSensorEnable() {
        return this.p;
    }

    public boolean isWifiActiveScan() {
        return this.g;
    }

    public boolean isWifiScan() {
        return this.q;
    }

    public AMapLocationClientOption setDeviceModeDistanceFilter(float f2) {
        this.v = f2;
        return this;
    }

    public AMapLocationClientOption setGeoLanguage(GeoLanguage geoLanguage) {
        this.t = geoLanguage;
        return this;
    }

    public AMapLocationClientOption setGpsFirst(boolean z) {
        this.l = z;
        return this;
    }

    public AMapLocationClientOption setGpsFirstTimeout(long j2) {
        long j3 = 30000;
        long j4 = 5000;
        if (j2 >= 5000) {
            j4 = j2;
        }
        if (j4 <= 30000) {
            j3 = j4;
        }
        this.s = j3;
        return this;
    }

    public AMapLocationClientOption setHttpTimeOut(long j2) {
        this.c = j2;
        return this;
    }

    public AMapLocationClientOption setInterval(long j2) {
        if (j2 <= 800) {
            j2 = 800;
        }
        this.b = j2;
        return this;
    }

    public AMapLocationClientOption setKillProcess(boolean z) {
        this.k = z;
        return this;
    }

    public AMapLocationClientOption setLastLocationLifeCycle(long j2) {
        this.r = j2;
        return this;
    }

    public AMapLocationClientOption setLocationCacheEnable(boolean z) {
        this.n = z;
        return this;
    }

    public AMapLocationClientOption setLocationMode(AMapLocationMode aMapLocationMode) {
        this.i = aMapLocationMode;
        return this;
    }

    public AMapLocationClientOption setLocationPurpose(AMapLocationPurpose aMapLocationPurpose) {
        this.w = aMapLocationPurpose;
        if (aMapLocationPurpose != null) {
            switch (aMapLocationPurpose) {
                case SignIn:
                    this.i = AMapLocationMode.Hight_Accuracy;
                    this.d = true;
                    this.o = true;
                    this.l = false;
                    this.e = false;
                    this.q = true;
                    break;
                case Transport:
                case Sport:
                    this.i = AMapLocationMode.Hight_Accuracy;
                    this.d = false;
                    this.o = false;
                    this.l = true;
                    this.e = false;
                    this.q = true;
                    break;
            }
        }
        return this;
    }

    public AMapLocationClientOption setMockEnable(boolean z) {
        this.e = z;
        return this;
    }

    public AMapLocationClientOption setNeedAddress(boolean z) {
        this.f = z;
        return this;
    }

    public AMapLocationClientOption setOffset(boolean z) {
        this.m = z;
        return this;
    }

    public AMapLocationClientOption setOnceLocation(boolean z) {
        this.d = z;
        return this;
    }

    public AMapLocationClientOption setOnceLocationLatest(boolean z) {
        this.o = z;
        return this;
    }

    public AMapLocationClientOption setSensorEnable(boolean z) {
        this.p = z;
        return this;
    }

    public AMapLocationClientOption setWifiActiveScan(boolean z) {
        this.g = z;
        this.h = z;
        return this;
    }

    public AMapLocationClientOption setWifiScan(boolean z) {
        this.q = z;
        if (this.q) {
            this.g = this.h;
        } else {
            this.g = false;
        }
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("interval:").append(String.valueOf(this.b)).append("#");
        sb.append("isOnceLocation:").append(String.valueOf(this.d)).append("#");
        sb.append("locationMode:").append(String.valueOf(this.i)).append("#");
        sb.append("locationProtocol:").append(String.valueOf(j)).append("#");
        sb.append("isMockEnable:").append(String.valueOf(this.e)).append("#");
        sb.append("isKillProcess:").append(String.valueOf(this.k)).append("#");
        sb.append("isGpsFirst:").append(String.valueOf(this.l)).append("#");
        sb.append("isNeedAddress:").append(String.valueOf(this.f)).append("#");
        sb.append("isWifiActiveScan:").append(String.valueOf(this.g)).append("#");
        sb.append("wifiScan:").append(String.valueOf(this.q)).append("#");
        sb.append("httpTimeOut:").append(String.valueOf(this.c)).append("#");
        sb.append("isLocationCacheEnable:").append(String.valueOf(this.n)).append("#");
        sb.append("isOnceLocationLatest:").append(String.valueOf(this.o)).append("#");
        sb.append("sensorEnable:").append(String.valueOf(this.p)).append("#");
        sb.append("geoLanguage:").append(String.valueOf(this.t)).append("#");
        sb.append("locationPurpose:").append(String.valueOf(this.w)).append("#");
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i2) {
        int i3 = -1;
        int i4 = 1;
        parcel.writeLong(this.b);
        parcel.writeLong(this.c);
        parcel.writeByte(this.d ? (byte) 1 : 0);
        parcel.writeByte(this.e ? (byte) 1 : 0);
        parcel.writeByte(this.f ? (byte) 1 : 0);
        parcel.writeByte(this.g ? (byte) 1 : 0);
        parcel.writeByte(this.h ? (byte) 1 : 0);
        parcel.writeInt(this.i == null ? -1 : this.i.ordinal());
        parcel.writeByte(this.k ? (byte) 1 : 0);
        parcel.writeByte(this.l ? (byte) 1 : 0);
        parcel.writeByte(this.m ? (byte) 1 : 0);
        parcel.writeByte(this.n ? (byte) 1 : 0);
        parcel.writeByte(this.o ? (byte) 1 : 0);
        parcel.writeByte(this.p ? (byte) 1 : 0);
        parcel.writeByte(this.q ? (byte) 1 : 0);
        parcel.writeLong(this.r);
        parcel.writeInt(j == null ? -1 : getLocationProtocol().ordinal());
        parcel.writeInt(this.t == null ? -1 : this.t.ordinal());
        parcel.writeByte(u ? (byte) 1 : 0);
        parcel.writeFloat(this.v);
        if (this.w != null) {
            i3 = this.w.ordinal();
        }
        parcel.writeInt(i3);
        if (!OPEN_ALWAYS_SCAN_WIFI) {
            i4 = 0;
        }
        parcel.writeInt(i4);
        parcel.writeLong(this.s);
    }
}
