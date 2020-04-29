package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataRcSetGimbalControlMode extends DataBase implements DJIDataSyncListener {
    private static DataRcSetGimbalControlMode instance = null;
    private MODE mode;

    public static synchronized DataRcSetGimbalControlMode getInstance() {
        DataRcSetGimbalControlMode dataRcSetGimbalControlMode;
        synchronized (DataRcSetGimbalControlMode.class) {
            if (instance == null) {
                instance = new DataRcSetGimbalControlMode();
            }
            dataRcSetGimbalControlMode = instance;
        }
        return dataRcSetGimbalControlMode;
    }

    public DataRcSetGimbalControlMode setMode(MODE mode2) {
        this.mode = mode2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.mode.value();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.SetGimbalControlMode.value();
        start(pack, callBack);
    }

    @Keep
    public enum MODE {
        Pitch(0),
        Roll(1),
        Yaw(2),
        OTHER(10);
        
        private int data;

        private MODE(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static MODE find(int b) {
            MODE result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
