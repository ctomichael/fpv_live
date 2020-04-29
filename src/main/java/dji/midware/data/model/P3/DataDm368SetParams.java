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
public class DataDm368SetParams extends DataBase implements DJIDataSyncListener {
    private DM368CmdId cmdId;
    private int value;

    public DataDm368SetParams set(DM368CmdId cmdId2, int value2) {
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
        start(pack, callBack);
    }

    @Keep
    public enum DM368CmdId {
        DisableLiveStream(1),
        SetEncodeFormat(2),
        SetTransmissionMode(3),
        EncodeMode(6),
        BandwidthPercentage(7),
        RevertImage(9),
        OTHER(100);
        
        private int data;

        private DM368CmdId(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static DM368CmdId find(int b) {
            DM368CmdId result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
