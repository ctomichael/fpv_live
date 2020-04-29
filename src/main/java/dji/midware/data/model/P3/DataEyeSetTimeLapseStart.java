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
public class DataEyeSetTimeLapseStart extends DataBase implements DJIDataSyncListener {
    private static DataEyeSetTimeLapseStart instance = null;
    private int deltTime;
    private int direction;
    private int duration;
    private int frameNumber;
    private int submodeType;
    private long trajId;

    public DataEyeSetTimeLapseStart setSubmodeType(int subModeType) {
        this.submodeType = subModeType;
        return this;
    }

    public DataEyeSetTimeLapseStart setDuration(int duration2) {
        this.duration = duration2 * 10;
        return this;
    }

    public DataEyeSetTimeLapseStart setDeltTime(int deltTime2) {
        this.deltTime = deltTime2;
        return this;
    }

    public DataEyeSetTimeLapseStart setTrajId(long trajId2) {
        this.trajId = trajId2;
        return this;
    }

    public DataEyeSetTimeLapseStart setDirection(int direction2) {
        this.direction = direction2;
        return this;
    }

    public DataEyeSetTimeLapseStart setFrameNumber(int frameNumber2) {
        this.frameNumber = frameNumber2;
        return this;
    }

    public static synchronized DataEyeSetTimeLapseStart getInstance() {
        DataEyeSetTimeLapseStart dataEyeSetTimeLapseStart;
        synchronized (DataEyeSetTimeLapseStart.class) {
            if (instance == null) {
                instance = new DataEyeSetTimeLapseStart();
            }
            dataEyeSetTimeLapseStart = instance;
        }
        return dataEyeSetTimeLapseStart;
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
        pack.cmdId = CmdIdEYE.CmdIdType.SetTimeLapseStart.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[16];
        this._sendData[0] = (byte) this.submodeType;
        System.arraycopy(BytesUtil.getBytes(this.duration), 0, this._sendData, 1, 2);
        System.arraycopy(BytesUtil.getBytes(this.deltTime), 0, this._sendData, 3, 2);
        System.arraycopy(BytesUtil.getBytes(this.trajId), 0, this._sendData, 5, 8);
        this._sendData[13] = (byte) this.direction;
        this._sendData[14] = (byte) this.frameNumber;
    }
}
