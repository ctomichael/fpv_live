package dji.midware.data.config.P3;

import android.support.v4.app.FrameMetricsAggregator;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataGetPushMultiRcPairingStatus;
import dji.midware.data.model.P3.DataRcAckGimbalCtrPermission;
import dji.midware.data.model.P3.DataRcGetFDPushConnectStatus;
import dji.midware.data.model.P3.DataRcGetPushBatteryInfo;
import dji.midware.data.model.P3.DataRcGetPushConnectStatus;
import dji.midware.data.model.P3.DataRcGetPushFlowControl;
import dji.midware.data.model.P3.DataRcGetPushFollowFocus;
import dji.midware.data.model.P3.DataRcGetPushFollowFocus2;
import dji.midware.data.model.P3.DataRcGetPushGpsInfo;
import dji.midware.data.model.P3.DataRcGetPushParams;
import dji.midware.data.model.P3.DataRcGetPushRcCustomButtonsStatus;
import dji.midware.data.model.P3.DataRcGetPushRcProCustomButtonsStatus;
import dji.midware.data.model.P3.DataRcHandShake;
import dji.midware.data.model.P3.DataRcSetAppSpecialControl;
import dji.midware.data.model.P3.DataRcSimPushParams;
import dji.midware.interfaces.CmdIdInterface;

@EXClassNullAway
public class CmdIdRc extends DJICmdSetBase {

    public enum CmdIdType implements CmdIdInterface {
        GetChannelParams(1),
        SetChannelParams(2),
        SetCalibration(3),
        GetHardwareParams(4),
        GetPushParams(5, false, DataRcGetPushParams.class),
        SetMaster(6),
        GetMaster(7, false),
        SetName(8),
        GetName(9, false),
        SetPassword(10),
        GetPassword(11, false),
        SetConnectMaster(12),
        GetConnectMaster(13, false),
        GetSearchMasters(14, false),
        SetSearchMode(15),
        GetSearchMode(16, false),
        SetToggle(17),
        GetToggle(18, false),
        RequestSlaveJoin(19),
        GetSlaveList(20, false),
        DeleteSlave(21),
        DeleteMaster(22),
        SetSlavePermission(23, false),
        GetSlavePermission(24, false),
        SetControlMode(25),
        GetControlMode(26),
        GetPushGpsInfo(27, false, DataRcGetPushGpsInfo.class),
        GetPushBatteryInfo(30, false, DataRcGetPushBatteryInfo.class),
        GetPushConnectStatus(31, false, DataRcGetPushConnectStatus.class),
        SetPowerMode(32),
        RequestGimbalCtrPermission(34),
        AckGimbalCtrPermission(35, false, DataRcAckGimbalCtrPermission.class),
        SetSimulation(36),
        GetSimFlyStatus(37),
        GetSimPushParams(38, false, DataRcSimPushParams.class),
        SetSlaveMode(41),
        GetSlaveMode(42),
        SetGimbalSpeed(43),
        GetGimbalSpeed(44),
        SetCustomFuction(45),
        GetCustomFuction(46),
        SetFrequency(47),
        SetRTC(49),
        GetRTC(50),
        SetWheelGain(51),
        GetWheelGain(52),
        SetGimbalControlMode(53),
        GetGimbalControlMode(54),
        CoachMode(60),
        MultiRcPairing(61),
        GetPushMultiRcPairingStatus(62, false, DataGetPushMultiRcPairingStatus.class),
        MaterSlaveId(63),
        GetPushFollowFocus(66, false, DataRcGetPushFollowFocus.class),
        AppSpecailControl(71, false, true, DataRcSetAppSpecialControl.class),
        GetFreqModeInfo(72),
        EnableBaseStationRTK(73),
        GetPushRcProCustomButtonsStatus(76, false, DataRcGetPushRcProCustomButtonsStatus.class),
        GetRCParam(72),
        GetPushFollowFocus2(152, false, DataRcGetPushFollowFocus2.class),
        SetFollowFocusInfo(153),
        SetMCU407(80),
        GetPushRcCustomButtonsStatus(81, false, DataRcGetPushRcCustomButtonsStatus.class),
        GetRcUnitNLang(83),
        SetRcUnitNLang(84),
        GetRcRole(86),
        GetFDPushConnectStatus(87, false, DataRcGetFDPushConnectStatus.class),
        SetNewControlFunction(88),
        SetFlightChannel(89),
        GetPushRcFlowControl(171, false, DataRcGetPushFlowControl.class),
        HandShake(239, false, DataRcHandShake.class),
        GetFDRcCalibrationStatue(248),
        Other(FrameMetricsAggregator.EVERY_DURATION);
        
        private static CmdIdType[] items = values();
        private Class<? extends DataBase> cls;
        private int data;
        private DataBase dataBase;
        private boolean isBlock = true;
        private boolean isMix = false;
        private boolean isNeedCcode = true;

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

        private CmdIdType(int _data, boolean isNeedCcode2) {
            this.data = _data;
            this.isNeedCcode = isNeedCcode2;
        }

        public boolean isMix() {
            return this.isMix;
        }

        public int value() {
            return this.data;
        }

        public boolean isBlock() {
            return this.isBlock;
        }

        public boolean isNeedCcode() {
            return this.isNeedCcode;
        }

        public Class<? extends DataBase> getDataModel() {
            return this.cls;
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
