package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdSmartBattery;
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
public class DataSmartBatteryGetPair extends DataBase implements DJIDataSyncListener {
    private static DataSmartBatteryGetPair mInstance = null;
    private int index = 0;

    public static DataSmartBatteryGetPair getInstance() {
        if (mInstance == null) {
            mInstance = new DataSmartBatteryGetPair();
        }
        return mInstance;
    }

    public DataSmartBatteryGetPair setIndex(int index2) {
        this.index = index2;
        return this;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getCheckSumLen() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public byte[] getCheckSum() {
        Log.d("PairBattery", "revData : " + BytesUtil.byte2hex(this._recData));
        int len = getCheckSumLen();
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
        pack.cmdId = CmdIdSmartBattery.CmdIdType.GetPair.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[9];
        this._sendData[0] = (byte) this.index;
    }
}
