package dji.midware.data.model.common;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataAsync2Listener;

@EXClassNullAway
public class DataHeartBeat extends DataBase implements DJIDataAsync2Listener {
    private static DataHeartBeat instance = null;

    public static synchronized DataHeartBeat getInstance() {
        DataHeartBeat dataHeartBeat;
        synchronized (DataHeartBeat.class) {
            if (instance == null) {
                instance = new DataHeartBeat();
            }
            dataHeartBeat = instance;
        }
        return dataHeartBeat;
    }

    /* access modifiers changed from: protected */
    public void setPushRecData(byte[] data) {
        super.setPushRecData(data);
        start();
    }

    /* access modifiers changed from: protected */
    public boolean isWantPush() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) Ccode.OK.value();
    }

    public void start() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.pack.senderType;
        pack.receiverId = this.pack.senderId;
        pack.cmdType = DataConfig.CMDTYPE.ACK.value();
        pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = this.pack.cmdSet;
        pack.cmdId = this.pack.cmdId;
        pack.seq = this.pack.seq;
        super.start(pack);
    }
}
