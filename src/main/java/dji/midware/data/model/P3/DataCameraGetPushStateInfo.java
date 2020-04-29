package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataCameraGetMode;
import dji.midware.data.model.P3.DataCameraGetStateInfo;
import dji.midware.data.model.P3.DataCameraSetFileIndexMode;
import dji.midware.data.model.P3.DataCameraSetNDFilter;
import dji.midware.data.model.P3.DataCameraSetStorageInfo;
import dji.midware.data.model.base.DJICameraDataBase;
import dji.midware.data.packages.P3.Pack;
import dji.thirdparty.ciphersql.database.SQLiteDatabase;
import org.bouncycastle.asn1.eac.CertificateBody;
import org.greenrobot.eventbus.EventBus;

@Keep
@EXClassNullAway
public class DataCameraGetPushStateInfo extends DJICameraDataBase {
    private static final String TAG = "DataCameraGetPushStateInfo";
    private static DataCameraGetPushStateInfo instance = null;
    private DataCameraSetStorageInfo.Storage mStorage = DataCameraSetStorageInfo.Storage.SDCARD;

    @Keep
    public enum Event {
        ConectChanged
    }

    public static synchronized DataCameraGetPushStateInfo getInstance() {
        DataCameraGetPushStateInfo dataCameraGetPushStateInfo;
        synchronized (DataCameraGetPushStateInfo.class) {
            if (instance == null) {
                instance = new DataCameraGetPushStateInfo();
                instance.isNeedPushLosed = true;
                instance.isRemoteModel = true;
            }
            dataCameraGetPushStateInfo = instance;
        }
        return dataCameraGetPushStateInfo;
    }

    /* access modifiers changed from: protected */
    public void setPushLose() {
        this.isPushLosed = true;
        if (this.isRegist && ServiceManager.getInstance().isConnected()) {
            if (!this.isRemoteModel || (this.isRemoteModel && ServiceManager.getInstance().isRemoteOK())) {
                post();
            }
        }
    }

    public boolean isCameraOK() {
        return getFirmUpgradeErrorState() != DataCameraGetStateInfo.FirmErrorType.NO || getSensorState() || getHotState() || getIsInternalError();
    }

    public CameraProtocolType getCameraProtocolType() {
        return getCameraProtocolType(-1);
    }

    public CameraProtocolType getCameraProtocolType(int index) {
        return CameraProtocolType.find(((Integer) get(36, 1, Integer.class, index)).intValue());
    }

    public boolean getConnectState() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 1) == 1;
    }

    public boolean getUsbState() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 1) & 1) == 1;
    }

    public boolean getTimeSyncState() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 2) & 1) == 1;
    }

    public DataCameraGetStateInfo.PhotoState getPhotoState() {
        return getPhotoState(-1);
    }

    public DataCameraGetStateInfo.PhotoState getPhotoState(int index) {
        return DataCameraGetStateInfo.PhotoState.find((((Integer) get(0, 4, Integer.class, index)).intValue() >> 3) & 7);
    }

    public RecordType getRecordState() {
        return getRecordState(-1);
    }

    public RecordType getRecordState(int index) {
        return RecordType.find((((Integer) get(0, 4, Integer.class, index)).intValue() >> 6) & 3);
    }

    public boolean getSensorState() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 8) & 1) == 1;
    }

    public boolean getSDCardInsertState() {
        return getSDCardInsertState(-1);
    }

    public boolean getSDCardInsertState(int index) {
        return ((((Integer) get(0, 4, Integer.class, index)).intValue() >> 9) & 1) == 1;
    }

    public DataCameraGetStateInfo.SDCardState getSDCardState() {
        return getSDCardState(-1);
    }

    public DataCameraGetStateInfo.SDCardState getSDCardState(int index) {
        return DataCameraGetStateInfo.SDCardState.find((((Integer) get(0, 4, Integer.class, index)).intValue() >> 10) & 15);
    }

    public DataCameraGetStateInfo.SDCardState getSDCardState(boolean checkUsbConnected) {
        if (getUsbState()) {
            return DataCameraGetStateInfo.SDCardState.USBConnected;
        }
        return DataCameraGetStateInfo.SDCardState.find((((Integer) get(0, 4, Integer.class)).intValue() >> 10) & 15);
    }

    public boolean getFirmUpgradeState() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 14) & 1) == 1;
    }

    public DataCameraGetStateInfo.FirmErrorType getFirmUpgradeErrorState() {
        return DataCameraGetStateInfo.FirmErrorType.find((((Integer) get(0, 4, Integer.class)).intValue() >> 15) & 3);
    }

    public boolean getHotState() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 17) & 1) == 1;
    }

    public boolean getEnabledPhoto() {
        return getEnabledPhoto(-1);
    }

    public boolean getEnabledPhoto(int index) {
        return ((((Integer) get(0, 4, Integer.class, index)).intValue() >> 18) & 1) == 0;
    }

    public boolean getIsStoring() {
        return getIsStoring(-1);
    }

    public boolean getIsStoring(int index) {
        return ((((Integer) get(0, 4, Integer.class, index)).intValue() >> 19) & 1) == 1;
    }

    public boolean getIsTimePhotoing() {
        return getIsTimePhotoing(-1);
    }

    public boolean getIsTimePhotoing(int index) {
        return ((((Integer) get(0, 4, Integer.class, index)).intValue() >> 20) & 1) == 1;
    }

    public EncryptStatus getEncryptStatus() {
        return EncryptStatus.find((((Integer) get(0, 4, Integer.class)).intValue() >> 22) & 3);
    }

    public boolean getIsGimbalBusy() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 27) & 1) == 1;
    }

    public boolean beInTrackingMode() {
        return beInTrackingMode(-1);
    }

    public boolean beInTrackingMode(int index) {
        return (((Integer) get(0, 4, Integer.class, index)).intValue() & SQLiteDatabase.CREATE_IF_NECESSARY) != 0;
    }

    public boolean beInHyperLapseMode() {
        return beInHyperLapseMode(-1);
    }

    public boolean beInHyperLapseMode(int index) {
        return (((Integer) get(0, 4, Integer.class, index)).intValue() & 536870912) != 0;
    }

    public DataCameraGetMode.MODE getMode() {
        return getMode(-1);
    }

    public DataCameraGetMode.MODE getMode(int index) {
        DataCameraGetMode.MODE mode = DataCameraGetMode.MODE.find(((Integer) get(4, 1, Integer.class, index)).intValue());
        if (mode == DataCameraGetMode.MODE.NEW_PLAYBACK) {
            return DataCameraGetMode.MODE.PLAYBACK;
        }
        return mode;
    }

    public int getSDCardTotalSize() {
        return ((Integer) get(5, 4, Integer.class)).intValue();
    }

    public int getSDCardFreeSize() {
        return ((Integer) get(9, 4, Integer.class)).intValue();
    }

    public long getRemainedShots() {
        return ((Long) get(13, 4, Long.class)).longValue();
    }

    public int getRemainedTime() {
        return ((Integer) get(17, 4, Integer.class)).intValue();
    }

    public DataCameraSetFileIndexMode.FileIndexMode getFileIndexMode() {
        if (this._recData == null) {
            return DataCameraSetFileIndexMode.FileIndexMode.RESET;
        }
        return DataCameraSetFileIndexMode.FileIndexMode.find(((Integer) get(21, 1, Integer.class)).intValue());
    }

    public boolean getFastPlayBackEnabled() {
        return (((Integer) get(22, 1, Integer.class)).intValue() >> 7) == 1;
    }

    public int getFastPlayBackTime() {
        return ((Integer) get(22, 1, Integer.class)).intValue() & CertificateBody.profileType;
    }

    public boolean getPhotoOsdTimeIsShow() {
        return (((Integer) get(23, 2, Integer.class)).intValue() & 1) == 1;
    }

    public boolean getPhotoOsdApertureIsShow() {
        return ((((Integer) get(23, 2, Integer.class)).intValue() >> 1) & 1) == 1;
    }

    public boolean getPhotoOsdShutterIsShow() {
        return ((((Integer) get(23, 2, Integer.class)).intValue() >> 2) & 1) == 1;
    }

    public boolean getPhotoOsdIsoIsShow() {
        return ((((Integer) get(23, 2, Integer.class)).intValue() >> 3) & 1) == 1;
    }

    public boolean getPhotoOsdExposureIsShow() {
        return ((((Integer) get(23, 2, Integer.class)).intValue() >> 4) & 1) == 1;
    }

    public boolean getPhotoOsdSharpeIsShow() {
        return ((((Integer) get(23, 2, Integer.class)).intValue() >> 5) & 1) == 1;
    }

    public boolean getPhotoOsdContrastIsShow() {
        return ((((Integer) get(23, 2, Integer.class)).intValue() << 6) & 1) == 1;
    }

    public boolean getPhotoOsdSaturationIsShow() {
        return ((((Integer) get(23, 2, Integer.class)).intValue() << 7) & 1) == 1;
    }

    public int getWm620CameraProtocolVersion() {
        return getWm620CameraProtocolVersion(-1);
    }

    public int getWm620CameraProtocolVersion(int index) {
        return ((Integer) get(25, 2, Integer.class, index)).intValue();
    }

    public boolean beInDebugMode() {
        return ((Integer) get(27, 1, Integer.class)).intValue() != 0;
    }

    public int getVideoRecordTime() {
        return ((Integer) get(29, 2, Integer.class)).intValue();
    }

    public int getMaxPhotoNum() {
        return ((Integer) get(31, 1, Integer.class)).intValue();
    }

    public boolean isHistogramEnable() {
        return (((Integer) get(32, 1, Integer.class)).intValue() & 1) == 1;
    }

    public CameraType getCameraType() {
        return getCameraType(-1);
    }

    public CameraType getCameraType(int index) {
        return CameraType.find(((Integer) get(33, 1, Integer.class, index)).intValue());
    }

    public int getRecordSplitTime() {
        return ((Integer) get(34, 2, Integer.class)).intValue();
    }

    public int getVerstion() {
        return getVerstion(-1);
    }

    public int getVerstion(int index) {
        return ((Integer) get(36, 1, Integer.class, index)).intValue();
    }

    public DataCameraSetNDFilter.NDFilterMode getNDFilter() {
        return DataCameraSetNDFilter.NDFilterMode.find(((Integer) get(41, 1, Integer.class)).intValue());
    }

    public DataCameraSetNDFilter.NDFilterMode getNDFilter(int index) {
        return DataCameraSetNDFilter.NDFilterMode.find(((Integer) get(41, 1, Integer.class, index)).intValue());
    }

    public long getInternalError() {
        return ((Long) get(42, 2, Long.class)).longValue() & 65535;
    }

    public boolean getIsInternalError() {
        return InternalError.find(((Long) get(42, 2, Long.class)).longValue() & 65535) != null;
    }

    public boolean isCameraAbnormalReboot() {
        return ((Integer) get(44, 2, Integer.class)).intValue() == 1;
    }

    public float getHyperLapseMarginX() {
        return ((Float) get(49, 4, Float.class)).floatValue();
    }

    public float getHyperLapseMarginY() {
        return ((Float) get(53, 4, Float.class)).floatValue();
    }

    public int getCameraTemp() {
        return ((Byte) get(61, 1, Byte.class)).byteValue();
    }

    public CameraTemperatureState getTempAlarmState() {
        return CameraTemperatureState.find(((Short) get(62, 1, Short.class)).shortValue());
    }

    public void clear() {
        super.clear();
        DJILog.d(TAG, "clear data in DataCameraGetPushStateInfo : recDatas" + this.recDatas, new Object[0]);
    }

    public void setStorageLocation(DataCameraSetStorageInfo.Storage storage) {
        this.mStorage = storage;
    }

    /* access modifiers changed from: protected */
    public void notifyPushLoseIndexChanged(boolean fromGetPush) {
        EventBus.getDefault().post(Event.ConectChanged);
        post();
    }

    /* access modifiers changed from: protected */
    public int getPushLoseDelayTime() {
        return 3000;
    }

    /* access modifiers changed from: protected */
    public void setPushRecPack(Pack pack) {
        if (pack == null || pack.data == null || pack.data.length != 21) {
            super.setPushRecPack(pack);
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) (this.mStorage != null ? this.mStorage.value() : DataCameraSetStorageInfo.Storage.SDCARD.value());
    }

    @Keep
    public enum EncryptStatus {
        NON_ENCRYPT(0),
        CHECK_FAILED(1),
        CHECK_SUCCESS(2),
        OTHER(7);
        
        private int data;

        private EncryptStatus(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static EncryptStatus find(int b) {
            EncryptStatus result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum RecordType {
        NO(0),
        START(1),
        STARTING(2),
        STOP(3),
        OTHER(7);
        
        private int data;

        private RecordType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static RecordType find(int b) {
            RecordType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum CameraType {
        DJICameraTypeFC350(0),
        DJICameraTypeFC550(1),
        DJICameraTypeFC260(2),
        DJICameraTypeFC300S(3),
        DJICameraTypeFC300X(4),
        DJICameraTypeFC550Raw(5),
        DJICameraTypeFC330X(6),
        DJICameraTypeTau640(7),
        DJICameraTypeTau336(8),
        DJICameraTypeFC220(9),
        DJICameraTypeFC300XW(10),
        DJICameraTypeCV600(11),
        DJICameraTypeFC65XXUnknown(12),
        DJICameraTypeFC6310(13),
        DJICameraTypeFC6510(14),
        DJICameraTypeFC6520(15),
        DJICameraTypeFC6532(16),
        DJICameraTypeFC6540(17),
        DJICameraTypeFC220S(18),
        DJICameraTypeFC1102(19),
        DJICameraTypeGD600(20),
        DJICameraTypeFC6310A(21),
        DJICameraTypeP3SE(22),
        DJICameraTypeFC230(23),
        DJICameraTypeFC240(25),
        DJICameraTypeFC1705(26),
        DJICameraTypeHG330(27),
        DJICameraTypeFC6310S(28),
        DJICameraTypeFC240_1(29),
        DJIPayloadCamera(31),
        DJICameraTypeFC245_IMX477(38),
        DJICameraFPV(39),
        DJICameraTypeFC2403(40),
        DJICameraTypeFC160(44),
        DJICameraTypeHasselH6D50C(166),
        DJICameraTypeHasselH6D100C(167),
        OTHER(255);
        
        private static volatile CameraType[] sValues = null;
        private final int data;

        private CameraType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        private boolean _equals(int b) {
            return this.data == b;
        }

        public static CameraType find(int b) {
            if (sValues == null) {
                sValues = values();
            }
            CameraType result = OTHER;
            for (int i = 0; i < sValues.length; i++) {
                if (sValues[i]._equals(b)) {
                    return sValues[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum CameraProtocolType {
        Default(0),
        APP2_LIB(1),
        P3SSupport2_7k(2),
        FC350Support1080_120(3),
        FC350SupportDigiZoomAndOSMONO368(4),
        FC330XTureColor(7);
        
        private int data;

        private CameraProtocolType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static CameraProtocolType find(int b) {
            CameraProtocolType result = Default;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum InternalError {
        UNENCRYPTED(1),
        UNCALIBRATION(2),
        PARTCALIBRATION(3),
        CALIBRATIONEXCEPTION(4);
        
        private static volatile InternalError[] sValues = null;
        private final long data;

        private InternalError(long _data) {
            this.data = _data;
        }

        public long value() {
            return this.data;
        }

        private boolean _equals(long b) {
            return this.data == b;
        }

        public static InternalError find(long b) {
            if (sValues == null) {
                sValues = values();
            }
            for (int i = 0; i < sValues.length; i++) {
                if (sValues[i]._equals(b)) {
                    return sValues[i];
                }
            }
            return null;
        }
    }

    public boolean hasSecondaryCameraRecData() {
        return (this.recDatas == null || this.recDatas.get(2) == null) ? false : true;
    }

    public boolean hasPrimaryCameraRecData() {
        return (this.recDatas == null || this.recDatas.get(0) == null) ? false : true;
    }

    @Keep
    public enum CameraTemperatureState {
        NORMAL(0),
        OVER_HEAT_LEVEL_1(1),
        OVER_HEAT_LEVEL_2(2),
        OVER_HEAT_LEVEL_3(3),
        UNKNOWN(255);
        
        private int data;

        private CameraTemperatureState(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        private boolean _equals(int b) {
            return this.data == b;
        }

        public static CameraTemperatureState find(int b) {
            CameraTemperatureState result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
