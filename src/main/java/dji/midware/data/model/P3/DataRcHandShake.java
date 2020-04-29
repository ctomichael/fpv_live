package dji.midware.data.model.P3;

import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataAsync2Listener;

public class DataRcHandShake extends DataBase implements DJIDataAsync2Listener {
    private static DataRcHandShake instance = null;

    public static synchronized DataRcHandShake getInstance() {
        DataRcHandShake dataRcHandShake;
        synchronized (DataRcHandShake.class) {
            if (instance == null) {
                instance = new DataRcHandShake();
            }
            dataRcHandShake = instance;
        }
        return dataRcHandShake;
    }

    /* access modifiers changed from: protected */
    public void setPushRecData(byte[] data) {
        super.setPushRecData(data);
        start();
    }

    /* access modifiers changed from: protected */
    public boolean isWantPush() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    private int getDataLength() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public String getRCInfo() {
        return get(1, getDataLength());
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
