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
public class DataEyeSetPOIStartWithGPS extends DataBase implements DJIDataSyncListener {
    private static DataEyeSetPOIStartWithGPS instance = null;
    private float mHeight;
    private double mLatitude;
    private double mLongitude;
    private float mRadius;
    private float mVelocity;

    public DataEyeSetPOIStartWithGPS setInformation(double latitude, double longitude, float radius, float height, float velocity) {
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mRadius = radius;
        this.mHeight = height;
        this.mVelocity = velocity;
        return this;
    }

    public static synchronized DataEyeSetPOIStartWithGPS getInstance() {
        DataEyeSetPOIStartWithGPS dataEyeSetPOIStartWithGPS;
        synchronized (DataEyeSetPOIStartWithGPS.class) {
            if (instance == null) {
                instance = new DataEyeSetPOIStartWithGPS();
            }
            dataEyeSetPOIStartWithGPS = instance;
        }
        return dataEyeSetPOIStartWithGPS;
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
        pack.cmdId = CmdIdEYE.CmdIdType.SetPOIStartWithGPS.value();
        pack.timeOut = 3000;
        pack.repeatTimes = 1;
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[28];
        System.arraycopy(BytesUtil.getBytes(this.mLatitude), 0, this._sendData, 0, 8);
        System.arraycopy(BytesUtil.getBytes(this.mLongitude), 0, this._sendData, 8, 8);
        System.arraycopy(BytesUtil.getBytes(this.mRadius), 0, this._sendData, 16, 4);
        System.arraycopy(BytesUtil.getBytes(this.mHeight), 0, this._sendData, 20, 4);
        System.arraycopy(BytesUtil.getBytes(this.mVelocity), 0, this._sendData, 24, 4);
    }
}
