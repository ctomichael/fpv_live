package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdGimbal;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataGimbalCommonProtocol extends DataBase implements DJIDataSyncListener {
    private int commandId;
    private int commandLength;
    private int commandType;
    private int commandValue;

    public DataGimbalCommonProtocol setCommandType(boolean isGetter) {
        this.commandType = isGetter ? 1 : 0;
        return this;
    }

    public DataGimbalCommonProtocol setCommandId(int commandId2) {
        this.commandId = commandId2;
        switch (commandId2) {
            case 0:
                this.commandLength = 1;
                break;
        }
        return this;
    }

    public DataGimbalCommonProtocol setCommandValue(int commandValue2) {
        this.commandValue = commandValue2;
        return this;
    }

    public boolean isLimitationEnabled() {
        return ((Integer) get(2, 1, Integer.class)).intValue() == 1;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.GIMBAL.value();
        pack.cmdId = CmdIdGimbal.CmdIdType.CommonProtocol.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.commandLength != 0) {
            this._sendData = new byte[(this.commandLength + 3)];
            this._sendData[0] = (byte) this.commandType;
            this._sendData[1] = (byte) this.commandId;
            this._sendData[2] = (byte) this.commandLength;
            System.arraycopy(BytesUtil.getBytes(this.commandValue), 0, this._sendData, 3, this.commandLength);
        }
    }
}
