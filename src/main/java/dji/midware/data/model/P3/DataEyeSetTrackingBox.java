package dji.midware.data.model.P3;

import com.dji.cmd.v1.protocol.DataVisionPushTrackingBoxToNav;
import dji.midware.data.config.P3.CmdIdEYE;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

public class DataEyeSetTrackingBox extends DataBase implements DJIDataSyncListener {
    private static DataEyeSetTrackingBox instance = null;
    private DataVisionPushTrackingBoxToNav mTask;

    public static synchronized DataEyeSetTrackingBox getInstance() {
        DataEyeSetTrackingBox dataEyeSetTrackingBox;
        synchronized (DataEyeSetTrackingBox.class) {
            if (instance == null) {
                instance = new DataEyeSetTrackingBox();
            }
            dataEyeSetTrackingBox = instance;
        }
        return dataEyeSetTrackingBox;
    }

    public DataEyeSetTrackingBox setTask(DataVisionPushTrackingBoxToNav task) {
        this.mTask = task;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = this.mTask.getReqBytes();
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
        pack.cmdId = CmdIdEYE.CmdIdType.SetTrackingBox.value();
        start(pack, callBack);
    }
}
