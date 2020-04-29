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
public class DataEyeSendUserLocation extends DataBase implements DJIDataSyncListener {
    private static DataEyeSendUserLocation instance = null;
    private short mAccuracy = 0;
    private float mEastSpeed = 0.0f;
    private double mLantitue = 0.0d;
    private double mLongtitue = 0.0d;
    private float mNorthSpeed = 0.0f;

    public static synchronized DataEyeSendUserLocation getInstance() {
        DataEyeSendUserLocation dataEyeSendUserLocation;
        synchronized (DataEyeSendUserLocation.class) {
            if (instance == null) {
                instance = new DataEyeSendUserLocation();
            }
            dataEyeSendUserLocation = instance;
        }
        return dataEyeSendUserLocation;
    }

    public DataEyeSendUserLocation setLocation(double longtitue, double lantitue) {
        this.mLongtitue = longtitue;
        this.mLantitue = lantitue;
        return this;
    }

    public DataEyeSendUserLocation setSpeed(float north, float east) {
        this.mNorthSpeed = north;
        this.mEastSpeed = east;
        return this;
    }

    public DataEyeSendUserLocation setAccuracy(short accuracy) {
        this.mAccuracy = accuracy;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this._sendData == null) {
            this._sendData = new byte[26];
        }
        System.arraycopy(BytesUtil.getBytes(this.mLongtitue), 0, this._sendData, 0, 8);
        System.arraycopy(BytesUtil.getBytes(this.mLantitue), 0, this._sendData, 8, 8);
        System.arraycopy(BytesUtil.getBytes(this.mNorthSpeed), 0, this._sendData, 16, 4);
        System.arraycopy(BytesUtil.getBytes(this.mEastSpeed), 0, this._sendData, 20, 4);
        System.arraycopy(BytesUtil.getBytes(this.mAccuracy), 0, this._sendData, 24, 2);
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
        pack.cmdId = CmdIdEYE.CmdIdType.SendTrackingUserLocation.value();
        pack.timeOut = 3000;
        pack.repeatTimes = 1;
        start(pack, callBack);
    }
}
