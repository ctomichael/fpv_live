package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.transfer.base.ITransferFileObject;

@Keep
@EXClassNullAway
public abstract class DataCommonTransferFile extends DataBase implements DJIDataSyncListener {
    protected final String TAG = DataCommonTransferFile.class.getSimpleName();
    protected final ITransferFileObject.TransferCmdType mCmdType;
    protected int mReceiveId = 0;
    protected DeviceType mReceiveType = DeviceType.CAMERA;

    protected DataCommonTransferFile(ITransferFileObject.TransferCmdType type) {
        this.mCmdType = type;
    }

    public DataCommonTransferFile setReceiveType(DeviceType type) {
        this.mReceiveType = type;
        return this;
    }

    public DataCommonTransferFile setReceiveId(int id) {
        this.mReceiveId = id;
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.mReceiveType.value();
        pack.receiverId = this.mReceiveId;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.TransferFile.value();
        pack.repeatTimes = 4;
        pack.timeOut = 5000;
        start(pack, callBack);
    }
}
