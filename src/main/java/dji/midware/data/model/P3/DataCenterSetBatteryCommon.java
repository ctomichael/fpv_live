package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCenter;
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
public class DataCenterSetBatteryCommon extends DataBase implements DJIDataSyncListener {
    private static DataCenterSetBatteryCommon instance = null;
    private int rate;
    private int timeOut = 10;

    public static synchronized DataCenterSetBatteryCommon getInstance() {
        DataCenterSetBatteryCommon dataCenterSetBatteryCommon;
        synchronized (DataCenterSetBatteryCommon.class) {
            if (instance == null) {
                instance = new DataCenterSetBatteryCommon();
            }
            dataCenterSetBatteryCommon = instance;
        }
        return dataCenterSetBatteryCommon;
    }

    public DataCenterSetBatteryCommon setRate(int rate2) {
        this.rate = rate2;
        return this;
    }

    public DataCenterSetBatteryCommon setTimeOut(int timeOut2) {
        this.timeOut = timeOut2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[3];
        this._sendData[0] = (byte) this.rate;
        System.arraycopy(BytesUtil.getUnsignedBytes(this.timeOut), 0, this._sendData, 1, 2);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CENTER.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CENTER.value();
        pack.cmdId = CmdIdCenter.CmdIdType.SetBatteryCommon.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
