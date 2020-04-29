package dji.midware.data.model.P3;

import android.util.Log;
import dji.midware.data.config.P3.CmdIdOnBoardSDK;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

public class DataOnBoardSdkSetPPSEnabledState extends DataBase implements DJIDataSyncListener {
    private boolean powerOn = true;

    public DataOnBoardSdkSetPPSEnabledState setEnabledState(boolean switchOn) {
        this.powerOn = switchOn;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        this._sendData[0] = 0;
        if (this.powerOn) {
            this._sendData[1] = 1;
        } else {
            this._sendData[1] = 0;
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.receiverId = 7;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.OnboardSDK.value();
        pack.cmdId = CmdIdOnBoardSDK.CmdIdType.setPPSEnabled.value();
        pack.timeOut = 1000;
        pack.repeatTimes = 2;
        Log.d("setPPSEnableState", "start cmdSet:" + pack.cmdSet + " cmdId: " + pack.cmdId);
        start(pack, callBack);
    }
}
