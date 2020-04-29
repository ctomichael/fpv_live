package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.config.P3.CmdIdEYE;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
public class DataEyeSetPOIInitialTarget extends DataBase implements DJIDataSyncListener {
    private static DataEyeSetPOIInitialTarget instance = null;
    private float mHeight;
    private int mTimeStamp;
    private float mWidth;
    private float mX;
    private float mY;

    public static synchronized DataEyeSetPOIInitialTarget getInstance() {
        DataEyeSetPOIInitialTarget dataEyeSetPOIInitialTarget;
        synchronized (DataEyeSetPOIInitialTarget.class) {
            if (instance == null) {
                instance = new DataEyeSetPOIInitialTarget();
            }
            dataEyeSetPOIInitialTarget = instance;
        }
        return dataEyeSetPOIInitialTarget;
    }

    public DataEyeSetPOIInitialTarget setPosition(float x, float y, float width, float height) {
        this.mX = x;
        this.mY = y;
        this.mWidth = width;
        this.mHeight = height;
        return this;
    }

    public DataEyeSetPOIInitialTarget setTimeStamp(int timeStamp) {
        this.mTimeStamp = timeStamp;
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.SINGLE.value();
        pack.receiverId = 7;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.EYE.value();
        pack.cmdId = CmdIdEYE.CmdIdType.SetPOIInitialTarget.value();
        pack.timeOut = 3000;
        pack.repeatTimes = 1;
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[20];
        System.arraycopy(BytesUtil.getBytes(this.mX), 0, this._sendData, 0, 4);
        System.arraycopy(BytesUtil.getBytes(this.mY), 0, this._sendData, 4, 4);
        System.arraycopy(BytesUtil.getBytes(this.mWidth), 0, this._sendData, 8, 4);
        System.arraycopy(BytesUtil.getBytes(this.mHeight), 0, this._sendData, 12, 4);
        System.arraycopy(BytesUtil.getBytes(this.mTimeStamp), 0, this._sendData, 16, 4);
    }
}
