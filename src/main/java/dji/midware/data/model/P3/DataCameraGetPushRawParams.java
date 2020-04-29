package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.base.DJICameraDataBase;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataCameraGetPushRawParams extends DJICameraDataBase {
    private static DataCameraGetPushRawParams instance = null;

    public static synchronized DataCameraGetPushRawParams getInstance() {
        DataCameraGetPushRawParams dataCameraGetPushRawParams;
        synchronized (DataCameraGetPushRawParams.class) {
            if (instance == null) {
                instance = new DataCameraGetPushRawParams();
            }
            dataCameraGetPushRawParams = instance;
        }
        return dataCameraGetPushRawParams;
    }

    public DiskStatus getDiskStatus() {
        if (isDiskConnected()) {
            return DiskStatus.find(((Integer) get(0, 1, Integer.class)).intValue() & 15);
        }
        return DiskStatus.NOTCONNECTED;
    }

    public DiskStatus getDiskStatus(int index) {
        if (isDiskConnected()) {
            return DiskStatus.find(((Integer) get(0, 1, Integer.class, index)).intValue() & 15);
        }
        return DiskStatus.NOTCONNECTED;
    }

    public int getDiskStatusValue() {
        return ((Integer) get(0, 1, Integer.class)).intValue() & 15;
    }

    public PurchasedResolution getPurchasedResolution() {
        return new PurchasedResolution(((Integer) get(14, 4, Integer.class)).intValue());
    }

    public boolean isDiskConnected() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 16) != 0;
    }

    public int getDiskConnectedValue() {
        return (((Integer) get(0, 1, Integer.class)).intValue() >> 4) & 1;
    }

    public int getDiskCapacity() {
        return (((Integer) get(0, 2, Integer.class)).intValue() & 96) >> 5;
    }

    public int getDiskAvailableTime() {
        return ((Integer) get(1, 2, Integer.class)).intValue();
    }

    public long getAvailableCapacity() {
        return (long) ((Integer) get(3, 4, Integer.class)).intValue();
    }

    public int getResolution() {
        return getResolution(-1);
    }

    public int getResolution(int index) {
        return ((Integer) get(7, 1, Integer.class, index)).intValue() & 255;
    }

    public int getFps() {
        return getFps(-1);
    }

    public int getFps(int index) {
        return ((Integer) get(8, 1, Integer.class, index)).intValue() & 255;
    }

    public RawMode getRawMode() {
        return getRawMode(-1);
    }

    public RawMode getRawMode(int index) {
        if (this._recData == null || this._recData.length <= 9) {
            return RawMode.ProrseOFF;
        }
        return RawMode.find(((Integer) get(9, 1, Integer.class, index)).intValue());
    }

    public int getAHCIStatus() {
        if (this._recData == null || this._recData.length <= 9) {
            return -1;
        }
        return ((Integer) get(9, 1, Integer.class)).intValue();
    }

    public int getCurrentRawBurstCount() {
        return ((Integer) get(10, 4, Integer.class)).intValue();
    }

    public char getEquipLabel() {
        return (char) (((Integer) get(18, 4, Integer.class)).intValue() & 255);
    }

    public int getRealName() {
        return (261888 & ((Integer) get(18, 4, Integer.class)).intValue()) >> 8;
    }

    public int getClipId() {
        return (((Integer) get(18, 4, Integer.class)).intValue() & 133955584) >> 18;
    }

    public String getEquipInfoBytes() {
        return BytesUtil.byte2hex(BytesUtil.getBytes(((Integer) get(18, 4, Integer.class)).intValue()));
    }

    public String getRealNameStr() {
        int realName = getRealName();
        if (realName < 100 && realName >= 10) {
            return "0" + realName;
        }
        if (realName < 10) {
            return "00" + realName;
        }
        return "" + realName;
    }

    public String getClipIdStr() {
        int clipId = getClipId();
        if (clipId < 100 && clipId >= 10) {
            return "0" + clipId;
        }
        if (clipId < 10) {
            return "00" + clipId;
        }
        return "" + clipId;
    }

    public String getClipName() {
        return Character.toString(getEquipLabel()) + getRealNameStr() + "_" + getClipIdStr() + ".clip";
    }

    public int getRawProtocolVersion() {
        return ((Integer) get(22, 1, Integer.class)).intValue();
    }

    public int getRawProtocolVersion(int index) {
        return ((Integer) get(22, 1, Integer.class, index)).intValue();
    }

    public int getSSDDigitalFilter() {
        return ((Integer) get(23, 1, Integer.class)).intValue() & 255;
    }

    public UserTips getUserTips() {
        return UserTips.find(((Integer) get(24, 4, Integer.class)).intValue());
    }

    public boolean isVideoSyncEnable() {
        return (((Integer) get(66, 1, Integer.class)).intValue() & 1) == 1;
    }

    public LicenseStatus getLicenseStatus() {
        int intValue = (((Integer) get(57, 4, Integer.class)).intValue() & 30) >> 1;
        return LicenseStatus.find(1);
    }

    public LicenseStatus getLicenseStatus(int index) {
        int intValue = (((Integer) get(57, 4, Integer.class, index)).intValue() & 30) >> 1;
        return LicenseStatus.find(1);
    }

    public int getLooks() {
        return ((Integer) get(61, 1, Integer.class)).intValue() & 255;
    }

    public int getSsdTotalCapacity() {
        return ((Integer) get(62, 2, Integer.class)).intValue();
    }

    public int getSsdAccumulativeData() {
        return ((Integer) get(64, 2, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum DiskStatus {
        NOTCONNECTED(-1),
        NA(0),
        WAITING(1),
        STORING(2),
        SLOW_FORMATING(3),
        FAST_FORMATING(4),
        INITIALIZING(5),
        DEVICE_ERROR(6),
        VERIFY_ERROR(7),
        FULL(8),
        PoorConnection(9),
        ChangingMode(10),
        NeedFormat(11),
        NotInitialized(12),
        FormatNotSupport(13),
        OTHER(255);
        
        private int data;

        private DiskStatus(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static DiskStatus find(int b) {
            DiskStatus result = WAITING;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum RawMode {
        JPEGLossLess(0),
        DRaw(1),
        ProrseRaw(2),
        ProresRawHQ(3),
        ProresHQ422(16),
        ProresHQ444(17),
        ProrseOFF(32),
        LIVEVIEW(48),
        Unknow(255);
        
        private int data;

        private RawMode(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static RawMode find(int b) {
            RawMode result = ProrseOFF;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public static class PurchasedResolution {
        public boolean is3840x2160JpegLosslessSupported;
        public boolean is3840x2160PRORES422HQSupported;
        public boolean is3840x2160PRORES444XQSupported;
        public boolean is4096x2160JpegLosslessSupported;
        public boolean is5280x2160JpegLosslessSupported;
        public boolean is5280x2160PRORES422HQSupported;
        public boolean isMaxResolutionJpegLosslessSupported;
        public boolean isX5SSupportCDNG;
        public boolean isX5SSupportDRAW;
        public boolean isX5SSupportPRORES;
        public boolean isX7SupportCDNG;
        public boolean isX7SupportDRAW;
        public boolean isX7SupportPRORES;
        public boolean isX9SupportCDNG;
        public boolean isX9SupportDRAW;
        public boolean isX9SupportPRORES;
        public boolean sMaxResolutionRawCaptureSupported;
        private int value = 0;

        public PurchasedResolution(int value2) {
            this.value = value2;
            setBooleanData();
        }

        private void setBooleanData() {
            boolean z;
            boolean z2;
            boolean z3;
            boolean z4;
            boolean z5;
            boolean z6;
            boolean z7;
            boolean z8;
            boolean z9;
            boolean z10;
            boolean z11;
            boolean z12;
            boolean z13;
            boolean z14;
            boolean z15;
            boolean z16 = true;
            this.is3840x2160JpegLosslessSupported = (this.value & 1) != 0;
            if ((this.value & 2) != 0) {
                z = true;
            } else {
                z = false;
            }
            this.is4096x2160JpegLosslessSupported = z;
            if ((this.value & 4) != 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            this.is5280x2160JpegLosslessSupported = z2;
            if ((this.value & 8) != 0) {
                z3 = true;
            } else {
                z3 = false;
            }
            this.isMaxResolutionJpegLosslessSupported = z3;
            if ((this.value & 16) != 0) {
                z4 = true;
            } else {
                z4 = false;
            }
            this.is3840x2160PRORES422HQSupported = z4;
            if ((this.value & 32) != 0) {
                z5 = true;
            } else {
                z5 = false;
            }
            this.is5280x2160PRORES422HQSupported = z5;
            if ((this.value & 64) != 0) {
                z6 = true;
            } else {
                z6 = false;
            }
            this.is3840x2160PRORES444XQSupported = z6;
            if ((this.value & 128) != 0) {
                z7 = true;
            } else {
                z7 = false;
            }
            this.sMaxResolutionRawCaptureSupported = z7;
            if ((this.value & 2048) != 0) {
                z8 = true;
            } else {
                z8 = false;
            }
            this.isX7SupportCDNG = z8;
            if ((this.value & 4096) != 0) {
                z9 = true;
            } else {
                z9 = false;
            }
            this.isX7SupportDRAW = z9;
            if ((this.value & 8192) != 0) {
                z10 = true;
            } else {
                z10 = false;
            }
            this.isX7SupportPRORES = z10;
            if ((this.value & 16384) != 0) {
                z11 = true;
            } else {
                z11 = false;
            }
            this.isX9SupportCDNG = z11;
            if ((this.value & 32768) != 0) {
                z12 = true;
            } else {
                z12 = false;
            }
            this.isX9SupportDRAW = z12;
            if ((this.value & 65536) != 0) {
                z13 = true;
            } else {
                z13 = false;
            }
            this.isX9SupportPRORES = z13;
            if ((this.value & 131072) != 0) {
                z14 = true;
            } else {
                z14 = false;
            }
            this.isX5SSupportCDNG = z14;
            if ((this.value & 262144) != 0) {
                z15 = true;
            } else {
                z15 = false;
            }
            this.isX5SSupportDRAW = z15;
            if ((this.value & 524288) == 0) {
                z16 = false;
            }
            this.isX5SSupportPRORES = z16;
        }
    }

    @Keep
    public enum UserTips {
        StopRecord_LoseFrame(1),
        StopRecord_Full(2),
        StopRecord_Unknown(4),
        StartFailed(8),
        OTHER(31);
        
        int mCmd;

        private UserTips(int cmd) {
            this.mCmd = cmd;
        }

        public int value() {
            return this.mCmd;
        }

        public boolean _equals(int b) {
            return this.mCmd == b;
        }

        public static UserTips find(int b) {
            UserTips result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum LicenseStatus {
        UNINITIALIZED(0),
        INITIALIZED(1),
        ERROR(2),
        UNKNOWN(15);
        
        int mCmd;

        private LicenseStatus(int cmd) {
            this.mCmd = cmd;
        }

        public int value() {
            return this.mCmd;
        }

        public boolean _equals(int b) {
            return this.mCmd == b;
        }

        public static LicenseStatus find(int b) {
            LicenseStatus result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
