package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdGimbal;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataGimbalControl;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataGimbalNewResetAndSetMode extends DataBase implements DJIDataSyncListener {
    private DataGimbalControl.MODE gimbalMode = DataGimbalControl.MODE.OTHER;
    private boolean resetGimbal = false;
    private boolean validBothYawAndPitch = false;

    public DataGimbalNewResetAndSetMode setGimbalMode(DataGimbalControl.MODE mode) {
        this.gimbalMode = mode;
        return this;
    }

    public DataGimbalNewResetAndSetMode setReset(boolean needReset) {
        this.resetGimbal = needReset;
        return this;
    }

    public DataGimbalNewResetAndSetMode setValidBothYawAndPitch(boolean isValidBothYawAndPitch) {
        this.validBothYawAndPitch = isValidBothYawAndPitch;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        this._sendData[0] = (byte) this.gimbalMode.value();
        if (this.resetGimbal && !this.validBothYawAndPitch) {
            this._sendData[1] = 1;
        } else if (!this.resetGimbal || !this.validBothYawAndPitch) {
            this._sendData[1] = 0;
        } else {
            this._sendData[0] = -2;
            this._sendData[1] = 3;
        }
    }

    public void start() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.GIMBAL.value();
        pack.cmdId = CmdIdGimbal.CmdIdType.ResetAndSetMode.value();
        start(pack);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.GIMBAL.value();
        pack.cmdId = CmdIdGimbal.CmdIdType.ResetAndSetMode.value();
        pack.timeOut = 1000;
        pack.repeatTimes = 2;
        start(pack, callBack);
    }
}
