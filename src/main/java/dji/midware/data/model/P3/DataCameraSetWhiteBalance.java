package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataCameraSetWhiteBalance extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetWhiteBalance instance = null;
    private int colorTemp;
    protected int mRepeatTime = 2;
    protected int mTimeOut = 1000;
    private int type;

    public static synchronized DataCameraSetWhiteBalance getInstance() {
        DataCameraSetWhiteBalance dataCameraSetWhiteBalance;
        synchronized (DataCameraSetWhiteBalance.class) {
            if (instance == null) {
                instance = new DataCameraSetWhiteBalance();
            }
            dataCameraSetWhiteBalance = instance;
        }
        return dataCameraSetWhiteBalance;
    }

    public DataCameraSetWhiteBalance setAll() {
        setType(DataCameraGetPushShotParams.getInstance().getWhiteBalance(this.receiverID));
        setColorTemp(DataCameraGetPushShotParams.getInstance().getColorTemp(this.receiverID));
        return this;
    }

    public DataCameraSetWhiteBalance setType(int type2) {
        this.type = type2;
        return this;
    }

    public DataCameraSetWhiteBalance setColorTemp(int colorTemp2) {
        this.colorTemp = colorTemp2;
        return this;
    }

    public DataCameraSetWhiteBalance setPackParam(int timeout, int repeat) {
        if (timeout > 0) {
            this.mTimeOut = timeout;
        }
        if (repeat > 0 && repeat <= 3) {
            this.mRepeatTime = repeat;
        }
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        this._sendData[0] = (byte) this.type;
        this._sendData[1] = (byte) this.colorTemp;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetWhiteBalance.value();
        pack.timeOut = this.mTimeOut;
        pack.repeatTimes = this.mRepeatTime;
        start(pack, callBack);
    }
}
