package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataFlycHotPointMissionSwitch extends DataBase implements DJIDataSyncListener {
    private static DataFlycHotPointMissionSwitch instance = null;
    private HOTPOINTMISSIONSWITCH missionSwitch;

    public static synchronized DataFlycHotPointMissionSwitch getInstance() {
        DataFlycHotPointMissionSwitch dataFlycHotPointMissionSwitch;
        synchronized (DataFlycHotPointMissionSwitch.class) {
            if (instance == null) {
                instance = new DataFlycHotPointMissionSwitch();
            }
            dataFlycHotPointMissionSwitch = instance;
        }
        return dataFlycHotPointMissionSwitch;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public DataFlycHotPointMissionSwitch setSwitch(HOTPOINTMISSIONSWITCH missionSwitch2) {
        this.missionSwitch = missionSwitch2;
        return this;
    }

    public void printResult() {
        DJILogHelper.getInstance().LOGD("Inspire", "Msg: " + get(0, 1, Integer.class), true, true);
    }

    @Keep
    public enum HOTPOINTMISSIONSWITCH {
        PAUSE(0),
        RESUME(1);
        
        private int data;

        private HOTPOINTMISSIONSWITCH(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static HOTPOINTMISSIONSWITCH find(int b) {
            HOTPOINTMISSIONSWITCH result = PAUSE;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.HotPointMissionSwitch.value();
        super.start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.missionSwitch.value();
    }
}
