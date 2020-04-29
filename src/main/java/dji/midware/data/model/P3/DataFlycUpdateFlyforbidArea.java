package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdFlyc;
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
public class DataFlycUpdateFlyforbidArea extends DataBase implements DJIDataSyncListener {
    private static DataFlycUpdateFlyforbidArea instance = null;
    private byte[] mData = new byte[0];
    private short mPkgSeq = 0;
    private int mPkgTotalSize = 0;
    private int mType = 0;

    public static synchronized DataFlycUpdateFlyforbidArea getInstance() {
        DataFlycUpdateFlyforbidArea dataFlycUpdateFlyforbidArea;
        synchronized (DataFlycUpdateFlyforbidArea.class) {
            if (instance == null) {
                instance = new DataFlycUpdateFlyforbidArea();
            }
            dataFlycUpdateFlyforbidArea = instance;
        }
        return dataFlycUpdateFlyforbidArea;
    }

    public DataFlycUpdateFlyforbidArea setType(int type) {
        this.mType = type;
        return this;
    }

    public DataFlycUpdateFlyforbidArea setPkgSeq(short pkgSeq) {
        this.mPkgSeq = pkgSeq;
        return this;
    }

    public DataFlycUpdateFlyforbidArea setData(byte[] data) {
        this.mData = data;
        return this;
    }

    public DataFlycUpdateFlyforbidArea setPkgTotalSize(int pkgTotalSize) {
        this.mPkgTotalSize = pkgTotalSize;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[(this.mData.length + 8)];
        this._sendData[0] = (byte) this.mType;
        System.arraycopy(BytesUtil.getBytes(this.mPkgSeq), 0, this._sendData, 1, 2);
        System.arraycopy(BytesUtil.getBytes(this.mPkgTotalSize), 0, this._sendData, 3, 4);
        this._sendData[7] = (byte) this.mData.length;
        System.arraycopy(this.mData, 0, this._sendData, 8, this.mData.length);
    }

    public int getAckCode() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getAckType() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getVerifyResult() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public int getNexSeqNum() {
        return ((Integer) get(2, 2, Integer.class)).intValue();
    }

    public String getRemoteUuid() {
        byte[] res = new byte[16];
        System.arraycopy(this._recData, 2, res, 0, 16);
        return BytesUtil.byte2hex(res, "");
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.timeOut = 5000;
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.SINGLE.value();
        pack.receiverId = 5;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.UpdateFlyforbidArea.value();
        start(pack, callBack);
    }
}
