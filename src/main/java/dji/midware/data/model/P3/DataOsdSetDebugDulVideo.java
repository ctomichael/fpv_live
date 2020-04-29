package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdOsd;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

public class DataOsdSetDebugDulVideo extends DataBase implements DJIDataSyncListener {
    public static final byte SOURCE_MAIN = 1;
    public static final byte SOURCE_SECOND = 2;
    public static final byte START = 1;
    public static final byte STATUS = 0;
    public static final byte STOP = 2;
    private int mReceiverType;
    private byte mType;
    private byte mVideoSource;

    public DataOsdSetDebugDulVideo setType(byte type) {
        this.mType = type;
        return this;
    }

    public DataOsdSetDebugDulVideo setReceiverType(DeviceType type) {
        this.mReceiverType = type.value();
        return this;
    }

    public DataOsdSetDebugDulVideo setVideoSource(byte videoSource) {
        this.mVideoSource = videoSource;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[3];
        this._sendData[0] = this.mType;
        this._sendData[1] = -1;
        this._sendData[2] = this.mVideoSource;
    }

    public boolean isOpen() {
        return ((Integer) get(1, 1, Integer.class)).intValue() == 1;
    }

    public int getVideoSource() {
        return ((Integer) get(3, 1, Integer.class)).intValue();
    }

    public boolean isFromRc() {
        return ((Integer) get(2, 1, Integer.class)).intValue() == 1;
    }

    public boolean isFromUav() {
        return ((Integer) get(2, 1, Integer.class)).intValue() == 2;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.mReceiverType;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.OSD.value();
        pack.cmdId = CmdIdOsd.CmdIdType.SetDebugVideo.value();
        pack.repeatTimes = 3;
        pack.timeOut = 3000;
        start(pack, callBack);
    }
}
