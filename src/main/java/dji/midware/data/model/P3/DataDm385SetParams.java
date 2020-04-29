package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.CmdIdDm368;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.Pack;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataDm385SetParams extends DataBase implements DJIDataSyncListener {
    private DM385CmdId cmdId;
    private int value;

    public DataDm385SetParams set(DM385CmdId cmdId2, int value2) {
        this.cmdId = cmdId2;
        this.value = value2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void setPushRecPack(Pack pack) {
        DJILogHelper.getInstance().LOGD("", pack.receiverType + "", false, true);
        super.setPushRecPack(pack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[3];
        this._sendData[0] = (byte) this.cmdId.value();
        this._sendData[1] = 1;
        this._sendData[2] = (byte) this.value;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverId = 1;
        pack.receiverType = DeviceType.DM368.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.DM368.value();
        pack.cmdId = CmdIdDm368.CmdIdType.SetParams.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    @Keep
    public enum DM385CmdId {
        SetTransmissionMode(3),
        OTHER(100);
        
        private int data;

        private DM385CmdId(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static DM385CmdId find(int b) {
            DM385CmdId result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
