package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataFlycGetFsAction;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataFlycSetFsAction extends DataBase implements DJIDataSyncListener {
    private static DataFlycSetFsAction instance = null;
    private DataFlycGetFsAction.FS_ACTION mFsAction = DataFlycGetFsAction.FS_ACTION.GoHome;

    public static synchronized DataFlycSetFsAction getInstance() {
        DataFlycSetFsAction dataFlycSetFsAction;
        synchronized (DataFlycSetFsAction.class) {
            if (instance == null) {
                instance = new DataFlycSetFsAction();
            }
            dataFlycSetFsAction = instance;
        }
        return dataFlycSetFsAction;
    }

    public DataFlycSetFsAction setFsAction(DataFlycGetFsAction.FS_ACTION action) {
        this.mFsAction = action;
        return this;
    }

    public DataFlycGetFsAction.FS_ACTION getReturnFsAction() {
        DataFlycGetFsAction.FS_ACTION fs_action = DataFlycGetFsAction.FS_ACTION.GoHome;
        if (this._recData == null || this._recData.length == 0) {
            return this.mFsAction == DataFlycGetFsAction.FS_ACTION.Landing ? DataFlycGetFsAction.FS_ACTION.GoHome : DataFlycGetFsAction.FS_ACTION.Landing;
        }
        return DataFlycGetFsAction.FS_ACTION.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.mFsAction.value();
        DJILogHelper.getInstance().LOGD("", "send fail safe: " + ((int) this._sendData[0]), true, true);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.SetFsAction.value();
        start(pack, callBack);
    }
}
