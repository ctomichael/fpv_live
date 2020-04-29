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

@Keep
@EXClassNullAway
public class DataFlycUploadUnlimitAreas extends DataBase implements DJIDataSyncListener {
    private static DataFlycUploadUnlimitAreas instance = null;
    private byte[] data = null;
    private int packetIndex = 0;

    public static synchronized DataFlycUploadUnlimitAreas getInstance() {
        DataFlycUploadUnlimitAreas dataFlycUploadUnlimitAreas;
        synchronized (DataFlycUploadUnlimitAreas.class) {
            if (instance == null) {
                instance = new DataFlycUploadUnlimitAreas();
            }
            dataFlycUploadUnlimitAreas = instance;
        }
        return dataFlycUploadUnlimitAreas;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public DataFlycUploadUnlimitAreas setIndex(int index) {
        this.packetIndex = index;
        return this;
    }

    public DataFlycUploadUnlimitAreas setData(byte[] data2) {
        this.data = data2;
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.UploadUnlimitAreas.value();
        pack.timeOut = 3000;
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[133];
        if (this.data == null) {
            this._sendData[0] = 0;
        } else {
            this._sendData[0] = (byte) this.data.length;
            int i = 0;
            while (i < this.data.length && i < 128) {
                if (i < this.data.length) {
                    this._sendData[i + 5] = this.data[i];
                } else {
                    this._sendData[i + 5] = 0;
                }
                i++;
            }
        }
        this._sendData[1] = (byte) this.packetIndex;
    }
}
