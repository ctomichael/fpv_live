package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdEYE;
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
public class DataSingleSetFlyYaw extends DataBase implements DJIDataSyncListener {
    private short mCmdCnt = 0;
    private short mPitchValue = 0;
    private short mRollValue = 0;
    private short mThrottleValue = 0;
    private short mYawValue = 0;

    public DataSingleSetFlyYaw setCmdCnt(short cmdCnt) {
        this.mCmdCnt = cmdCnt;
        return this;
    }

    public DataSingleSetFlyYaw setYawValue(short yaw) {
        this.mYawValue = yaw;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[10];
        System.arraycopy(BytesUtil.getBytes(this.mCmdCnt), 0, this._sendData, 0, 2);
        System.arraycopy(BytesUtil.getBytes(this.mRollValue), 0, this._sendData, 2, 2);
        System.arraycopy(BytesUtil.getBytes(this.mPitchValue), 0, this._sendData, 4, 2);
        System.arraycopy(BytesUtil.getBytes(this.mYawValue), 0, this._sendData, 6, 2);
        System.arraycopy(BytesUtil.getBytes(this.mThrottleValue), 0, this._sendData, 8, 2);
    }

    public void start() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.SINGLE.value();
        pack.receiverId = 7;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.EYE.value();
        pack.cmdId = CmdIdEYE.CmdIdType.SetFlyYaw.value();
        start(pack);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.SINGLE.value();
        pack.receiverId = 7;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.EYE.value();
        pack.cmdId = CmdIdEYE.CmdIdType.SetFlyYaw.value();
        pack.timeOut = 1000;
        pack.repeatTimes = 1;
        start(pack, callBack);
    }
}
