package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataCameraIRCEnable extends DataBase implements DJIDataSyncListener {
    private boolean enable = false;
    private boolean setMode = true;

    public DataCameraIRCEnable setMode(boolean setMode2) {
        this.setMode = setMode2;
        return this;
    }

    public void setEnable(boolean enable2) {
        this.enable = enable2;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        byte b = 0;
        if (this.setMode) {
            this._sendData = new byte[2];
            this._sendData[0] = 1;
            byte[] bArr = this._sendData;
            if (this.enable) {
                b = 1;
            }
            bArr[1] = b;
            return;
        }
        this._sendData = new byte[1];
        this._sendData[0] = 0;
    }

    public boolean isEnabled() {
        return (((Short) get(0, 1, Short.class)).shortValue() & 1) != 0;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.IRCEnable.value();
        pack.timeOut = 1000;
        pack.repeatTimes = 2;
        start(pack, callBack);
    }
}
