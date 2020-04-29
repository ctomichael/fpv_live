package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataFlycExecFly extends DataBase implements DJIDataSyncListener {
    private TYPE type;

    public static DataFlycExecFly factory() {
        return new DataFlycExecFly();
    }

    public DataFlycExecFly setType(TYPE type2) {
        this.type = type2;
        return this;
    }

    public int getAckStatus() {
        return BytesUtil.getInt(this._recData);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = BytesUtil.getBytes(this.type.value());
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.ExecFly.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    @Keep
    public enum TYPE {
        PAUSE_FLY(0),
        RESUME_FLY(1),
        AUTO_LANDING(3),
        ENTER_SINGAL(4),
        OUT_SINGAL(5),
        START_FLY(7),
        START_TURN(8),
        OTHER(100);
        
        private int data;

        private TYPE(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static TYPE find(int b) {
            TYPE result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
