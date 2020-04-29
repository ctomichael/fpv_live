package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
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
@EXClassNullAway
public class DataSingleCheckPointPosValid extends DataBase implements DJIDataSyncListener {
    private static DataSingleCheckPointPosValid instance = null;
    private float mPosX = 0.0f;
    private float mPosY = 0.0f;
    private short mSessionId = 0;

    public static DataSingleCheckPointPosValid getInstance() {
        if (instance == null) {
            instance = new DataSingleCheckPointPosValid();
        }
        return instance;
    }

    private DataSingleCheckPointPosValid() {
    }

    public DataSingleCheckPointPosValid setPosXY(float x, float y) {
        this.mPosX = x;
        this.mPosY = y;
        return this;
    }

    public DataSingleCheckPointPosValid setSessionId(short id) {
        this.mSessionId = id;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[11];
        System.arraycopy(BytesUtil.getBytes(this.mPosX), 0, this._sendData, 0, 4);
        System.arraycopy(BytesUtil.getBytes(this.mPosY), 0, this._sendData, 4, 4);
        System.arraycopy(BytesUtil.getBytes(this.mSessionId), 0, this._sendData, 8, 2);
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
        pack.cmdId = CmdIdEYE.CmdIdType.CheckPointPosValid.value();
        pack.timeOut = 5000;
        pack.repeatTimes = 1;
        start(pack, callBack);
    }
}
