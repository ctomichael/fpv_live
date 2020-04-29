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
public class DataEyeGetTimeLapseKeyFrameInfoByIndex extends DataBase implements DJIDataSyncListener {
    private static DataEyeGetTimeLapseKeyFrameInfoByIndex sInstance = null;
    private long mIndex;

    public static synchronized DataEyeGetTimeLapseKeyFrameInfoByIndex getInstance() {
        DataEyeGetTimeLapseKeyFrameInfoByIndex dataEyeGetTimeLapseKeyFrameInfoByIndex;
        synchronized (DataEyeGetTimeLapseKeyFrameInfoByIndex.class) {
            if (sInstance == null) {
                sInstance = new DataEyeGetTimeLapseKeyFrameInfoByIndex();
            }
            dataEyeGetTimeLapseKeyFrameInfoByIndex = sInstance;
        }
        return dataEyeGetTimeLapseKeyFrameInfoByIndex;
    }

    public DataEyeGetTimeLapseKeyFrameInfoByIndex setIndex(long index) {
        this.mIndex = index;
        return this;
    }

    public int getReturnType() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public long getIndex() {
        return ((Long) get(1, 8, Long.class)).longValue();
    }

    public float getX() {
        return ((Float) get(9, 4, Float.class)).floatValue();
    }

    public float getY() {
        return ((Float) get(13, 4, Float.class)).floatValue();
    }

    public float getZ() {
        return ((Float) get(17, 4, Float.class)).floatValue();
    }

    public float getHeight() {
        return ((Float) get(21, 4, Float.class)).floatValue();
    }

    public float getGimbalPitch() {
        return ((Float) get(25, 4, Float.class)).floatValue();
    }

    public float getGimbalRoll() {
        return ((Float) get(29, 4, Float.class)).floatValue();
    }

    public float getGimbalYaw() {
        return ((Float) get(33, 4, Float.class)).floatValue();
    }

    public double getLongitude() {
        return (((Double) get(37, 8, Double.class)).doubleValue() * 180.0d) / 3.141592653589793d;
    }

    public Double getLatitude() {
        return Double.valueOf((((Double) get(45, 8, Double.class)).doubleValue() * 180.0d) / 3.141592653589793d);
    }

    public float getAltitude() {
        return ((Float) get(53, 4, Float.class)).floatValue();
    }

    public byte[] getReceiverData() {
        if (this._recData == null || this._recData.length <= 65) {
            return null;
        }
        byte[] res = new byte[64];
        System.arraycopy(this._recData, 1, res, 0, 64);
        return res;
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
        pack.cmdId = CmdIdEYE.CmdIdType.GetTimeLapseKeyFrameInfoByIndex.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[8];
        System.arraycopy(BytesUtil.getBytes(this.mIndex), 0, this._sendData, 0, 8);
    }
}
