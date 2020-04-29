package dji.midware.data.config.P3;

import android.support.v4.app.FrameMetricsAggregator;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataSimulatorGetPushConnectHeartPacket;
import dji.midware.data.model.P3.DataSimulatorGetPushFlightStatusParams;
import dji.midware.data.model.P3.DataSimulatorGetPushMainControllerReturnParams;
import dji.midware.data.model.P3.DataSimulatorSetGetWind;
import dji.midware.interfaces.CmdIdInterface;

@EXClassNullAway
public class CmdIdSimulator extends DJICmdSetBase {

    public enum CmdIdType implements CmdIdInterface {
        GetPushConnectHeartPacket(1, false, DataSimulatorGetPushConnectHeartPacket.class),
        RequestMainControllerParams(2),
        GetPushMainControllerReturnParams(3, false, DataSimulatorGetPushMainControllerReturnParams.class),
        SimulateFlightCommend(4),
        GetPushFlightStatusParams(6, false, DataSimulatorGetPushFlightStatusParams.class),
        SetGetWind(7, false, DataSimulatorSetGetWind.class),
        SetGetArea(8),
        SetGetAirParams(9),
        ForceMoment(10),
        SetGetTemperature(11),
        SetGetGravity(12),
        CrashShutDown(13),
        CtrlMotor(14),
        Momentum(15),
        SetGetArmLength(16),
        SetGetMassInertia(17),
        SetGetMotorSetting(18),
        SetGetBatterySetting(19),
        GetFrequency(20),
        ResetAll(255),
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

        public boolean isNeedCcode() {
            return this.isNeedCcode;
        }

        public Class<? extends DataBase> getDataModel() {
            return this.cls;
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

    public String getDataModelName(int deviceType, int cmdId) {
        return DataUtil.getDataModelName(DeviceType.find(deviceType).toString(), CmdIdType.find(cmdId).toString());
    }

    public String getDataModelName(int deviceType, int deviceId, int cmdId) {
        return getDataModelName(deviceType, cmdId);
    }

    /* access modifiers changed from: protected */
    public CmdIdInterface[] getCurCmdIdValues() {
        return CmdIdType.values();
    }
}
