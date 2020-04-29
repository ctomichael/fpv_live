package dji.midware.data.model.litchis;

import android.support.annotation.NonNull;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataRcGetFDPushConnectStatus;
import dji.midware.data.model.P3.DataRcGetRcRole;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@EXClassNullAway
public abstract class DataAppRequest extends DataBase implements DJIDataSyncListener {
    protected DeviceType deviceType = DeviceType.CAMERA;

    public DataAppRequest setDeviceType(@NonNull DeviceType deviceType2) {
        if (deviceType2 != null) {
            this.deviceType = deviceType2;
        }
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        DataRcGetRcRole.RcRole rcRole = DataRcGetFDPushConnectStatus.getInstance().getRole();
        if (rcRole == DataRcGetRcRole.RcRole.SLAVE_CONTROL || rcRole == DataRcGetRcRole.RcRole.SLAVE_CONTROL_SUB) {
            pack.senderId = 2;
        }
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.deviceType.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.RequestFile.value();
        start(pack);
    }
}
