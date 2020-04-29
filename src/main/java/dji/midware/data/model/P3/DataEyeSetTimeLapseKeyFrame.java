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
public class DataEyeSetTimeLapseKeyFrame extends DataBase implements DJIDataSyncListener {
    private static DataEyeSetTimeLapseKeyFrame sInstance = null;
    private int mAction;
    private long mIndex;

    @Keep
    public enum Action {
        ADD,
        DELETE
    }

    public static synchronized DataEyeSetTimeLapseKeyFrame getInstance() {
        DataEyeSetTimeLapseKeyFrame dataEyeSetTimeLapseKeyFrame;
        synchronized (DataEyeSetTimeLapseKeyFrame.class) {
            if (sInstance == null) {
                sInstance = new DataEyeSetTimeLapseKeyFrame();
            }
            dataEyeSetTimeLapseKeyFrame = sInstance;
        }
        return dataEyeSetTimeLapseKeyFrame;
    }

    public DataEyeSetTimeLapseKeyFrame setIndex(long index) {
        this.mIndex = index;
        return this;
    }

    public DataEyeSetTimeLapseKeyFrame setAction(Action action) {
        if (Action.ADD.equals(action)) {
            this.mAction = 0;
        } else if (Action.DELETE.equals(action)) {
            this.mAction = 1;
        }
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
        pack.cmdId = CmdIdEYE.CmdIdType.SetTimeLapseKeyFrame.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[17];
        System.arraycopy(BytesUtil.getBytes(this.mIndex), 0, this._sendData, 0, 8);
        this._sendData[8] = (byte) this.mAction;
        System.arraycopy(BytesUtil.getBytes(System.currentTimeMillis()), 0, this._sendData, 9, 8);
    }
}
