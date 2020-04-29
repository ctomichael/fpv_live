package dji.midware.data.config.P3;

import android.support.v4.app.FrameMetricsAggregator;
import com.drew.metadata.exif.makernotes.NikonType2MakernoteDirectory;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataOsdGetHdvtPushException;
import dji.midware.data.model.P3.DataOsdGetPushChannalStatus;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataOsdGetPushConfig;
import dji.midware.data.model.P3.DataOsdGetPushDebugInfo;
import dji.midware.data.model.P3.DataOsdGetPushDevicesState;
import dji.midware.data.model.P3.DataOsdGetPushHome;
import dji.midware.data.model.P3.DataOsdGetPushMaxMcs;
import dji.midware.data.model.P3.DataOsdGetPushMicInfo;
import dji.midware.data.model.P3.DataOsdGetPushPowerStatus;
import dji.midware.data.model.P3.DataOsdGetPushSDRBarInterference;
import dji.midware.data.model.P3.DataOsdGetPushSDRNfParams;
import dji.midware.data.model.P3.DataOsdGetPushSdrConfigInfo;
import dji.midware.data.model.P3.DataOsdGetPushSdrLinkMode;
import dji.midware.data.model.P3.DataOsdGetPushSdrLteStatus;
import dji.midware.data.model.P3.DataOsdGetPushSdrStatusGroundInfo;
import dji.midware.data.model.P3.DataOsdGetPushSdrStatusInfo;
import dji.midware.data.model.P3.DataOsdGetPushSdrSweepFrequency;
import dji.midware.data.model.P3.DataOsdGetPushSdrUpwardSelectChannel;
import dji.midware.data.model.P3.DataOsdGetPushSdrUpwardSweepFrequency;
import dji.midware.data.model.P3.DataOsdGetPushSignalQuality;
import dji.midware.data.model.P3.DataOsdGetPushSweepFrequency;
import dji.midware.data.model.P3.DataOsdGetPushWirelessState;
import dji.midware.data.model.P3.DataOsdGetSDRPushCustomCodeRate;
import dji.midware.data.model.P3.DataOsdOsmoPushCalibration;
import dji.midware.interfaces.CmdIdInterface;

@EXClassNullAway
public class CmdIdOsd extends DJICmdSetBase {

    public enum CmdIdType implements CmdIdInterface {
        GetPushCommon(1, false, DataOsdGetPushCommon.class),
        GetPushHome(2, false, DataOsdGetPushHome.class),
        GetPushBasebandState(3, false, null),
        SetFPGA(4),
        GetFPGA(5),
        Set9363(6),
        Get9363(7),
        GetPushSignalQuality(8, false, DataOsdGetPushSignalQuality.class),
        SetSweepFrequency(9),
        GetPushSweepFrequency(10, false, DataOsdGetPushSweepFrequency.class),
        GetPushDevicesState(11, false, DataOsdGetPushDevicesState.class),
        GetPushConfig(12, false, true, DataOsdGetPushConfig.class),
        SetConfig(13),
        SetUsbTransform(14),
        SetUpgradeTip(16),
        GetPushChannelStatus(17, false, DataOsdGetPushChannalStatus.class),
        SetMaxMcs(20),
        GetPushMaxMcs(21, false, DataOsdGetPushMaxMcs.class),
        GetPushDebugInfo(22, false, DataOsdGetPushDebugInfo.class),
        GetPushSdrSweepFrequency(32, false, DataOsdGetPushSdrSweepFrequency.class),
        GetSdrConfig(33),
        GetPushSdrConfigInfo(34, false, DataOsdGetPushSdrConfigInfo.class),
        SetSdrStatus(35),
        GetPushSdrStatusInfo(36, false, DataOsdGetPushSdrStatusInfo.class),
        GetPushSdrStatusGroundInfo(37, false, DataOsdGetPushSdrStatusGroundInfo.class),
        SetSdrAssitantRead(38),
        SetSdrAssitantWrite(39),
        SetSdrStartLog(40),
        GetPushSdrUpwardSweepFrequency(41, false, DataOsdGetPushSdrUpwardSweepFrequency.class),
        GetPushSdrUpwardSelectChannel(42, false, DataOsdGetPushSdrUpwardSelectChannel.class),
        SetReverseLink(43),
        calibrationAmt(44),
        GetSdrLBT(45),
        SetSdrLBT(46),
        GetPushWirelessState(48, false, DataOsdGetPushWirelessState.class),
        SetSDRImageTransmissionMode(52),
        GetSDRImageTransmissionMode(53),
        GetSDRPushCustomCodeRate(54, false, DataOsdGetSDRPushCustomCodeRate.class),
        GetHdtvPushException(55, false, DataOsdGetHdvtPushException.class),
        SetSDRConfigInfo(57),
        GetPushSDRNfParams(58, false, DataOsdGetPushSDRNfParams.class),
        GetPushSDRBarDisturb(59, false, DataOsdGetPushSDRBarInterference.class),
        SetSDRForceBoost(60),
        GetPushLinkMode(67, false, DataOsdGetPushSdrLinkMode.class),
        SetSdrLinkMode(68),
        SetLED(80),
        SetPower(81),
        GetPushPowerStatus(82, false, DataOsdGetPushPowerStatus.class),
        OsmoCalibration(83),
        OsmoPushCalibration(84, false, DataOsdOsmoPushCalibration.class),
        SetMicGain(87),
        GetMicGain(88),
        GetPushMicInfo(89, false, DataOsdGetPushMicInfo.class),
        GetMicEnable(98),
        SetMicEnable(99),
        SendRtcmPack(101),
        GetSdrUplinkBandwidth(105),
        SetMainCameraBandwidthPercent(113),
        GetMainCameraBandwidthPercent(114),
        GetPushSdrLteStatus(129, false, DataOsdGetPushSdrLteStatus.class),
        SwitchSetSdrOrLte(NikonType2MakernoteDirectory.TAG_ADAPTER),
        SetDebugVideo(240),
        Other(FrameMetricsAggregator.EVERY_DURATION);
        
        private static CmdIdType[] items = values();
        private Class<? extends DataBase> cls;
        private int data;
        private DataBase dataBase;
        private boolean isBlock = true;
        private boolean isMix = false;

        private CmdIdType(int _data) {
            this.data = _data;
        }

        private CmdIdType(int _data, boolean isBlock2, boolean isMix2, Class<? extends DataBase> cls2) {
            this.data = _data;
            this.isBlock = isBlock2;
            this.isMix = isMix2;
            this.cls = cls2;
            this.dataBase = DataUtil.getDataBaseInstRefl(cls2);
        }

        private CmdIdType(int _data, boolean isBlock2, Class<? extends DataBase> cls2) {
            this.data = _data;
            this.isBlock = isBlock2;
            this.cls = cls2;
            this.dataBase = DataUtil.getDataBaseInstRefl(cls2);
        }

        public int value() {
            return this.data;
        }

        public boolean isMix() {
            return this.isMix;
        }

        public boolean isBlock() {
            return this.isBlock;
        }

        public Class<? extends DataBase> getDataModel() {
            return this.cls;
        }

        public boolean isNeedCcode() {
            return true;
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
