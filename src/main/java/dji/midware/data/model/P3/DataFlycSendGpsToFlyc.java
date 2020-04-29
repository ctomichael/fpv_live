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
public class DataFlycSendGpsToFlyc extends DataBase implements DJIDataSyncListener {
    private static DataFlycSendGpsToFlyc instance = null;
    private boolean isGpsValid = true;
    private boolean isNetworkOk = false;
    private boolean isTimeValid = true;
    private int mLat;
    private int mLng;
    private long mTimeStamp;

    public static synchronized DataFlycSendGpsToFlyc getInstance() {
        DataFlycSendGpsToFlyc dataFlycSendGpsToFlyc;
        synchronized (DataFlycSendGpsToFlyc.class) {
            if (instance == null) {
                instance = new DataFlycSendGpsToFlyc();
            }
            dataFlycSendGpsToFlyc = instance;
        }
        return dataFlycSendGpsToFlyc;
    }

    public DataFlycSendGpsToFlyc setLat(double lat) {
        this.mLat = (int) (1000000.0d * lat);
        return this;
    }

    public DataFlycSendGpsToFlyc setLng(double lng) {
        this.mLng = (int) (1000000.0d * lng);
        return this;
    }

    public DataFlycSendGpsToFlyc setTimeStamp(long timeStamp) {
        this.mTimeStamp = timeStamp / 1000;
        return this;
    }

    public DataFlycSendGpsToFlyc setGpsValid(boolean gpsValid) {
        this.isGpsValid = gpsValid;
        return this;
    }

    public DataFlycSendGpsToFlyc setTimeValid(boolean timeValid) {
        this.isTimeValid = timeValid;
        return this;
    }

    public DataFlycSendGpsToFlyc setNetworkOk(boolean networkOk) {
        this.isNetworkOk = networkOk;
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
        pack.cmdId = CmdIdFlyc.CmdIdType.SendGpsToFlyc.value();
        pack.data = getSendData();
        pack.timeOut = 4000;
        pack.repeatTimes = 3;
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[13];
        int flag1 = 0;
        if (this.isGpsValid) {
            flag1 = 0 | 1;
        }
        if (this.isTimeValid) {
            flag1 |= 2;
        }
        if (this.isNetworkOk) {
            flag1 |= 4;
        }
        this._sendData[0] = (byte) flag1;
        System.arraycopy(BytesUtil.getBytes(this.mLat), 0, this._sendData, 1, 4);
        System.arraycopy(BytesUtil.getBytes(this.mLng), 0, this._sendData, 5, 4);
        System.arraycopy(BytesUtil.getBytes(this.mTimeStamp), 0, this._sendData, 9, 4);
    }
}
