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
public class DataSmartBatteryGetBarCode extends DataBase implements DJIDataSyncListener {
    private static DataSmartBatteryGetBarCode mInstance = null;
    private int index = 0;

    public static DataSmartBatteryGetBarCode getInstance() {
        if (mInstance == null) {
            mInstance = new DataSmartBatteryGetBarCode();
        }
        return mInstance;
    }

    public DataSmartBatteryGetBarCode setIndex(int index2) {
        this.index = index2;
        return this;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getIndex() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getLength() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public String getBarCode() {
        int len = ((Integer) get(2, 1, Integer.class)).intValue();
        if (len > 0) {
            return get(3, len);
        }
        return null;
    }

    public byte[] getBarCodeBytes() {
        int len = ((Integer) get(2, 1, Integer.class)).intValue();
        if (len <= 0) {
            return null;
        }
        byte[] tmp = new byte[len];
        System.arraycopy(this._recData, 3, tmp, 0, len);
        return tmp;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.BATTERY.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.SMARTBATTERY.value();
        pack.cmdId = CmdIdSmartBattery.CmdIdType.GetBarCode.value();
        pack.data = getSendData();
        pack.timeOut = 4000;
        pack.repeatTimes = 4;
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[9];
        this._sendData[0] = (byte) this.index;
    }
}
