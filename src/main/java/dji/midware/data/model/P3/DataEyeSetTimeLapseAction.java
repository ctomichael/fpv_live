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

@Keep
public class DataEyeSetTimeLapseAction extends DataBase implements DJIDataSyncListener {
    public static final int CLEAR = 4;
    public static final int PAUSE = 2;
    public static final int RESUME = 3;
    public static final int STOP = 1;
    private static DataEyeSetTimeLapseAction instance = null;
    private boolean isVideoGet;
    private int type;

    public static synchronized DataEyeSetTimeLapseAction getInstance() {
        DataEyeSetTimeLapseAction dataEyeSetTimeLapseAction;
        synchronized (DataEyeSetTimeLapseAction.class) {
            if (instance == null) {
                instance = new DataEyeSetTimeLapseAction();
            }
            dataEyeSetTimeLapseAction = instance;
        }
        return dataEyeSetTimeLapseAction;
    }

    public DataEyeSetTimeLapseAction setType(int type2) {
        this.type = type2;
        return this;
    }

    public DataEyeSetTimeLapseAction setVideoGet(boolean isVideoGet2) {
        this.isVideoGet = isVideoGet2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i = 0;
        this._sendData = new byte[2];
        this._sendData[0] = (byte) this.type;
        byte[] bArr = this._sendData;
        if (this.isVideoGet) {
            i = 1;
        }
        bArr[1] = (byte) i;
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
        pack.cmdId = CmdIdEYE.CmdIdType.SetTimeLapseAction.value();
        start(pack, callBack);
    }
}
