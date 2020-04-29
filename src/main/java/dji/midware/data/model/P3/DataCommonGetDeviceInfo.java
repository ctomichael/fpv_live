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

@Keep
@EXClassNullAway
public class DataCommonGetDeviceInfo extends DataBase implements DJIDataSyncListener {
    private int mReceiveId = 0;
    private DeviceType mReceiveType = DeviceType.DM368;

    public DataCommonGetDeviceInfo setReceiveType(DeviceType type) {
        this.mReceiveType = type;
        return this;
    }

    public DataCommonGetDeviceInfo setReceiveId(int id) {
        this.mReceiveId = id;
        return this;
    }

    public String getInfo() {
        if (this._recData == null || this._recData.length == 0) {
            return "";
        }
        if (isHaveCcode()) {
            return get(1, this._recData.length - 1);
        }
        return get(0, this._recData.length);
    }

    public boolean isHaveCcode() {
        if (this._recData == null || this._recData[0] != 0) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        start(callBack, 1000, 2);
    }

    public void start(DJIDataCallBack callBack, int timeout, int times) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.mReceiveType.value();
        pack.receiverId = this.mReceiveId;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.GetDeviceInfo.value();
        pack.timeOut = timeout;
        pack.repeatTimes = times;
        start(pack, callBack);
    }
}
