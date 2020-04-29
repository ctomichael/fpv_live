package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import com.dji.cmd.v1.protocol.DataVisionSetTimeLapseLoadTask;
import dji.midware.data.config.P3.CmdIdEYE;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
public class DataEyeSetTimeLapseLoadTask extends DataBase implements DJIDataSyncListener {
    private static DataEyeSetTimeLapseLoadTask instance = null;
    DataVisionSetTimeLapseLoadTask task;

    public static synchronized DataEyeSetTimeLapseLoadTask getInstance() {
        DataEyeSetTimeLapseLoadTask dataEyeSetTimeLapseLoadTask;
        synchronized (DataEyeSetTimeLapseLoadTask.class) {
            if (instance == null) {
                instance = new DataEyeSetTimeLapseLoadTask();
            }
            dataEyeSetTimeLapseLoadTask = instance;
        }
        return dataEyeSetTimeLapseLoadTask;
    }

    public DataEyeSetTimeLapseLoadTask set(DataVisionSetTimeLapseLoadTask subMode) {
        this.task = subMode;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = this.task.getReqBytes();
    }

    public void start(DJIDataCallBack callBack) {
        start(configSendPack(), callBack);
    }

    @NonNull
    private SendPack configSendPack() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.SINGLE.value();
        pack.receiverId = 7;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.EYE.value();
        pack.cmdId = CmdIdEYE.CmdIdType.SetTimeLapseLoadTask.value();
        return pack;
    }

    public void syncStart() {
        syncStart(configSendPack());
    }
}
