package dji.midware.data.config.P3;

import android.support.v4.app.FrameMetricsAggregator;
import com.drew.metadata.exif.makernotes.NikonType2MakernoteDirectory;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataWifiGetPushElecSignal;
import dji.midware.data.model.P3.DataWifiGetPushFirstAppMac;
import dji.midware.data.model.P3.DataWifiGetPushLog;
import dji.midware.data.model.P3.DataWifiGetPushMSErrorInfo;
import dji.midware.data.model.P3.DataWifiGetPushMasterSlaveStatus;
import dji.midware.data.model.P3.DataWifiGetPushSignal;
import dji.midware.data.model.P3.DataWifiGetPushSweepFrequency;
import dji.midware.data.model.P3.DataWifiGetPushWifiComplexInfo;
import dji.midware.interfaces.CmdIdInterface;

@EXClassNullAway
public class CmdIdWifi extends DJICmdSetBase {

    public enum CmdIdType implements CmdIdInterface {
        SetPower(5),
        GetPower(6),
        GetSSID(7),
        SetSSID(8),
        GetSignalPush(9, false, DataWifiGetPushSignal.class),
        SetWifiFrequency(16),
        GetPassword(14),
        SetPassword(13),
        GetPushFirstAppMac(17, false, DataWifiGetPushFirstAppMac.class),
        GetPushElectricSignal(18, false, DataWifiGetPushElecSignal.class),
        SetPowerMode(19),
        RestartWifi(21),
        SetSelectionMode(22),
        GetSelectionMode(23),
        GetCountryCode(25),
        GetWifiFrequency(32),
        SetNoiseCheckAdapt(38),
        SwitchSDR(39),
        GetChannelList(40),
        SetSweepFrequency(41),
        GetPushSweepFrequency(42, false, DataWifiGetPushSweepFrequency.class),
        SetWifiModeChannel(43),
        SetWifiCodeRate(44),
        GetWifiCurCodeRate(45),
        SetWifiFreq5GMode(46),
        GetWifiFreqMode(47),
        SetWiFiCountryCode(48),
        IsSupportCountryCode(51),
        RequestSnrPush(41),
        GetPushLog(128, false, DataWifiGetPushLog.class),
        GetPushMasterSlaveStatus(NikonType2MakernoteDirectory.TAG_ADAPTER, false, DataWifiGetPushMasterSlaveStatus.class),
        SetMasterSlaveAuthCode(131),
        ScanMasterList(132),
        ConnectMasterWithIdAuthCode(133),
        GetAuthCode(137),
        GetPushMSErrorInfo(139, false, DataWifiGetPushMSErrorInfo.class),
        GetPushWifiComplexInfo(170, false, DataWifiGetPushWifiComplexInfo.class),
        Other(FrameMetricsAggregator.EVERY_DURATION);
        
        private static CmdIdType[] items = values();
        private Class<? extends DataBase> cls;
        private int data;
        private DataBase dataBase;
        private boolean isBlock = true;
        private boolean isNeedCcode = true;

        private CmdIdType(int _data) {
            this.data = _data;
        }

        private CmdIdType(int _data, boolean isBlock2, Class<? extends DataBase> cls2) {
            this.data = _data;
            this.isBlock = isBlock2;
            this.cls = cls2;
            this.dataBase = DataUtil.getDataBaseInstRefl(cls2);
        }

        private CmdIdType(int _data, boolean isNeedCcode2) {
            this.data = _data;
            this.isNeedCcode = isNeedCcode2;
        }

        public int value() {
            return this.data;
        }

        public boolean isBlock() {
            return this.isBlock;
        }

        public Class<? extends DataBase> getDataModel() {
            return this.cls;
        }

        public boolean isNeedCcode() {
            return this.isNeedCcode;
        }

        public boolean isMix() {
            return false;
        }

        public DataBase getDataBase() {
            return this.dataBase;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static CmdIdType find(int b) {
            CmdIdType result = Other;
            for (int i = 0; i < items.length; i++) {
                if (items[i]._equals(b)) {
                    return items[i];
                }
            }
            return result;
        }
    }

    /* access modifiers changed from: protected */
    public CmdIdInterface[] getCurCmdIdValues() {
        return CmdIdType.values();
    }

    public String getDataModelName(int deviceType, int cmdId) {
        return DataUtil.getDataModelName(DeviceType.find(deviceType).toString(), CmdIdType.find(cmdId).toString());
    }

    public String getDataModelName(int deviceType, int deviceId, int cmdId) {
        return getDataModelName(deviceType, cmdId);
    }
}
