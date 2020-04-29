package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DJIFlycParamInfoManager;
import dji.midware.data.model.base.DJICommonDataBase;
import org.bouncycastle.asn1.eac.CertificateBody;

@Keep
@EXClassNullAway
public class DataFlycGetPushSmartBattery extends DJICommonDataBase {
    public static final int MaskBatteryCellError = 64;
    public static final int MaskBatteryCommunicateError = 128;
    public static final int MaskBatteryDangerous = 8192;
    public static final int MaskBatteryDangerousWarning = 16384;
    public static final int MaskBatteryFakeSingleBattery = 65536;
    public static final int MaskBatteryFirstChargeNotFull = 2048;
    public static final int MaskBatteryLimitOutputMax = 4096;
    public static final int MaskBatteryNotReady = 1024;
    public static final int MaskBatterySingleBattery = 32768;
    public static final int MaskBatteryTempVoltageLow = 512;
    public static final int MaskMainVoltageLowGoHOme = 16;
    public static final int MaskMainVoltageLowLand = 32;
    public static final int MaskSmartBatteryReqGoHome = 4;
    public static final int MaskSmartBatteryReqLand = 8;
    public static final int MaskUserBatteryReqGoHome = 1;
    public static final int MaskUserBatteryReqLand = 2;
    public static final int MaskVoltageLowNeedLand = 256;
    private static DataFlycGetPushSmartBattery instance = null;

    public static synchronized DataFlycGetPushSmartBattery getInstance() {
        DataFlycGetPushSmartBattery dataFlycGetPushSmartBattery;
        synchronized (DataFlycGetPushSmartBattery.class) {
            if (instance == null) {
                instance = new DataFlycGetPushSmartBattery();
            }
            dataFlycGetPushSmartBattery = instance;
        }
        return dataFlycGetPushSmartBattery;
    }

    public DataFlycGetPushSmartBattery() {
    }

    public DataFlycGetPushSmartBattery(boolean isRegist) {
        super(isRegist);
    }

    public int getUsefulTime() {
        return ((Integer) get(0, 2, Integer.class)).intValue();
    }

    public int getGoHomeTime() {
        return ((Integer) get(2, 2, Integer.class)).intValue();
    }

    public int getLandTime() {
        return ((Integer) get(4, 2, Integer.class)).intValue();
    }

    public int getGoHomeBattery() {
        return ((Integer) get(6, 2, Integer.class)).intValue();
    }

    public int getLandBattery() {
        return ((Integer) get(8, 2, Integer.class)).intValue();
    }

    public float getSafeFlyRadius() {
        return ((Float) get(10, 4, Float.class)).floatValue();
    }

    public float getVolumeComsume() {
        return ((Float) get(14, 4, Float.class)).floatValue();
    }

    public int getStatus() {
        return ((Integer) get(18, 4, Integer.class)).intValue();
    }

    public SmartGoHomeStatus getGoHomeStatus() {
        return SmartGoHomeStatus.find((byte) ((Integer) get(22, 1, Integer.class)).intValue());
    }

    public int getGoHomeCountDown() {
        return ((Integer) get(23, 1, Integer.class)).intValue();
    }

    public int getVoltage() {
        return ((Integer) get(24, 2, Integer.class)).intValue();
    }

    public int getBattery() {
        if (DJIFlycParamInfoManager.isNewForOsd()) {
            return ((Integer) get(26, 1, Integer.class)).intValue();
        }
        return DataOsdGetPushCommon.getInstance().getBattery();
    }

    public int getBatteryPercent() {
        return ((Integer) get(26, 1, Integer.class)).intValue();
    }

    public int getLowWarning() {
        return ((Integer) get(27, 1, Integer.class)).intValue() & CertificateBody.profileType;
    }

    public boolean getLowWarningGoHome() {
        return (((Integer) get(27, 1, Integer.class)).intValue() & 128) != 0;
    }

    public int getSeriousLowWarning() {
        return ((Integer) get(28, 1, Integer.class)).intValue() & CertificateBody.profileType;
    }

    public boolean getSeriousLowWarningLanding() {
        return (((Integer) get(28, 1, Integer.class)).intValue() & 128) != 0;
    }

    public int getVoltagePercent() {
        return ((Integer) get(29, 1, Integer.class)).intValue();
    }

    public boolean getIsSingleBatteryMode() {
        return (32768 & ((Integer) get(18, 4, Integer.class)).intValue()) != 0;
    }

    public boolean getIsFakeSingleBatteryMode() {
        return (65536 & ((Integer) get(18, 4, Integer.class)).intValue()) != 0;
    }

    public boolean isFirstChargeNotFull() {
        return (getStatus() & 2048) != 0;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum SmartGoHomeStatus {
        NON_GOHOME((byte) 0),
        GOHOME((byte) 1),
        GOHOME_ALREADY((byte) 2);
        
        private byte _value = 0;

        private SmartGoHomeStatus(byte value) {
            this._value = value;
        }

        public final byte value() {
            return this._value;
        }

        public boolean _equals(byte value) {
            return this._value == value;
        }

        public static SmartGoHomeStatus find(byte value) {
            SmartGoHomeStatus[] values = values();
            for (SmartGoHomeStatus ss : values) {
                if (ss._equals(value)) {
                    return ss;
                }
            }
            return NON_GOHOME;
        }
    }
}
