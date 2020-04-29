package dji.midware.data.config.P3;

import android.support.v4.app.FrameMetricsAggregator;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataGimbalGetPushAbnormalStatus;
import dji.midware.data.model.P3.DataGimbalGetPushAutoCalibrationStatus;
import dji.midware.data.model.P3.DataGimbalGetPushHandheldStickState;
import dji.midware.data.model.P3.DataGimbalGetPushParams;
import dji.midware.data.model.P3.DataGimbalGetPushTimelapseStatus;
import dji.midware.data.model.P3.DataGimbalGetPushTutorialStatus;
import dji.midware.data.model.P3.DataGimbalGetPushType;
import dji.midware.data.model.P3.DataGimbalGetPushUserParams;
import dji.midware.data.model.P3.DataGimbalPushBatteryInfo;
import dji.midware.interfaces.CmdIdInterface;

@EXClassNullAway
public class CmdIdGimbal extends DJICmdSetBase {

    public enum CmdIdType implements CmdIdInterface {
        Control(1),
        GetPushParams(5, false, DataGimbalGetPushParams.class),
        RollFinetune(7),
        AutoCalibration(8),
        SetAngle(10),
        SetOnOrOff(13),
        SpeedControl(12),
        AngleControl(10),
        SetUserParams(15),
        GetUserParams(16),
        SaveUserParams(17),
        ResetUserParams(19),
        AbsAngleControl(20),
        SetGimbalRelease(26),
        GetPushType(28, false, DataGimbalGetPushType.class),
        GetSerialParams(31),
        GetPushUserParams(36, false, DataGimbalGetPushUserParams.class),
        CommonProtocol(37),
        GetPushAbnormalStatus(39, false, DataGimbalGetPushAbnormalStatus.class),
        GetPushTutorialStatus(43, false, DataGimbalGetPushTutorialStatus.class),
        SetTutorialStep(44),
        GetPushAutoCalibrationStatus(48, false, DataGimbalGetPushAutoCalibrationStatus.class),
        RobinSetParams(49),
        RobinGetParams(50),
        RobinPushBattery(51, false, DataGimbalPushBatteryInfo.class),
        SetHandleParams(53),
        GetHandleParams(54),
        SetTimelapseParams(55),
        GetPushTimelapseStatus(56, false, DataGimbalGetPushTimelapseStatus.class),
        NotifyCameraInfo(86),
        GetPushHandheldStickState(87, false, DataGimbalGetPushHandheldStickState.class),
        SetHandheldStickControlEnabled(88),
        ResetAndSetMode(76),
        Other(FrameMetricsAggregator.EVERY_DURATION);
        
        private static CmdIdType[] items = values();
        private Class<? extends DataBase> cls;
        private int data;
        private DataBase dataBase;
        private boolean isBlock = true;

        private CmdIdType(int _data) {
            this.data = _data;
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

        public boolean isBlock() {
            return this.isBlock;
        }

        public Class<? extends DataBase> getDataModel() {
            return this.cls;
        }

        public boolean isMix() {
            return false;
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
