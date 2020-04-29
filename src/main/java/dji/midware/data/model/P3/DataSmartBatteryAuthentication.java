package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdSmartBattery;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataSmartBatteryAuthentication extends DataBase implements DJIDataSyncListener {
    private static DataSmartBatteryAuthentication mInstance = null;
    private byte[] data;
    private int index = 0;
    private boolean random = false;

    public static DataSmartBatteryAuthentication getInstance() {
        if (mInstance == null) {
            mInstance = new DataSmartBatteryAuthentication();
        }
        return mInstance;
    }

    public DataSmartBatteryAuthentication setIndex(int index2) {
        this.index = index2;
        return this;
    }

    public DataSmartBatteryAuthentication setType(boolean random2) {
        this.random = random2;
        return this;
    }

    public DataSmartBatteryAuthentication setData(byte[] data2) {
        this.data = data2;
        return this;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getIndex() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public boolean isRandom() {
        return ((Integer) get(2, 1, Integer.class)).intValue() == 0;
    }

    public byte[] getData() {
        if (this._sendData == null || this._sendData.length != 23) {
            return null;
        }
        byte[] res = new byte[20];
        System.arraycopy(this._sendData, 2, res, 0, 20);
        return res;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.BATTERY.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.SMARTBATTERY.value();
        pack.cmdId = CmdIdSmartBattery.CmdIdType.Authentication.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i;
        this._sendData = new byte[22];
        this._sendData[0] = (byte) this.index;
        byte[] bArr = this._sendData;
        if (this.random) {
            i = 0;
        } else {
            i = 1;
        }
        bArr[1] = (byte) i;
        if (this.data != null && this.data.length <= 20) {
            System.arraycopy(this.data, 0, this._sendData, 2, this.data.length);
        }
    }
}
