package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdRc;
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
public class DataRcSetMCU407 extends DataBase implements DJIDataSyncListener {
    private byte[] data;
    private int len;
    private MODE mode;
    private int offset;

    @Keep
    public enum MODE {
        GET(0),
        SET(1);
        
        /* access modifiers changed from: private */
        public byte data;

        private MODE(int data2) {
            this.data = (byte) data2;
        }
    }

    public void setMode(MODE mode2) {
        this.mode = mode2;
    }

    public void setOffset(short offset2) {
        this.offset = offset2;
    }

    public void setLen(short len2) {
        this.len = len2;
    }

    public void setData(byte[] _data) {
        this.data = _data;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[205];
        this._sendData[0] = this.mode.data;
        System.arraycopy(BytesUtil.getBytes(this.offset), 0, this._sendData, 1, 2);
        System.arraycopy(BytesUtil.getBytes(this.len), 0, this._sendData, 3, 2);
        if (this.data != null && this.mode == MODE.SET) {
            System.arraycopy(this.data, 0, this._sendData, 5, this.len);
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.RC.value();
        pack.receiverId = 4;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.SetMCU407.value();
        pack.data = getSendData();
        pack.timeOut = 500;
        doPack();
        start(pack, callBack);
    }

    public byte[] getBuf() {
        if (isGetted()) {
            return BytesUtil.readBytes(this._recData, 0, this.len);
        }
        return new byte[this.len];
    }

    public String getSendDataForTest() {
        return BytesUtil.byte2hex(BytesUtil.readBytes(this._sendData, 5, this.len));
    }

    public String getSendPackForTest() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.RC.value();
        pack.receiverId = 4;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.SetMCU407.value();
        pack.data = getSendData();
        pack.doPack();
        return pack.toString();
    }

    public byte[] getRecDataFromMCU() {
        if (getRecData() == null) {
            return new byte[this.len];
        }
        return BytesUtil.readBytes(getRecData(), 0, this.len);
    }
}
