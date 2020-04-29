package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.Pack;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataFlycGetPushFlightRecord extends DataBase {
    private static DataFlycGetPushFlightRecord instance;
    private CmdType mCmdType = CmdType.Write;
    private int mReceId = 0;
    private int mReceType = 3;
    private int mSeq;
    private int mSessionIndex;
    public int result = 0;

    public static synchronized DataFlycGetPushFlightRecord getInstance() {
        DataFlycGetPushFlightRecord dataFlycGetPushFlightRecord;
        synchronized (DataFlycGetPushFlightRecord.class) {
            if (instance == null) {
                instance = new DataFlycGetPushFlightRecord();
            }
            dataFlycGetPushFlightRecord = instance;
        }
        return dataFlycGetPushFlightRecord;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    public DataFlycGetPushFlightRecord setCmdType(CmdType cmdType) {
        this.mCmdType = cmdType;
        return this;
    }

    public DataFlycGetPushFlightRecord setReceType(int mReceType2) {
        this.mReceType = mReceType2;
        return this;
    }

    public DataFlycGetPushFlightRecord setReceId(int mReceId2) {
        this.mReceId = mReceId2;
        return this;
    }

    public DataFlycGetPushFlightRecord setSeqs(int mSeq2) {
        this.mSeq = mSeq2;
        return this;
    }

    public int getSendType() {
        if (this.pack != null) {
            return this.pack.senderType;
        }
        return 8;
    }

    public int getSendId() {
        if (this.pack != null) {
            return this.pack.senderId;
        }
        return 1;
    }

    public int getSeqs() {
        if (this.pack != null) {
            return this.pack.seq;
        }
        return 0;
    }

    public Pack getPacks() {
        return this.pack;
    }

    public DataFlycGetPushFlightRecord setSessionIndex(int index) {
        this.mSessionIndex = index;
        return this;
    }

    public CmdType getRPCCommandId() {
        return CmdType.find(((Integer) get(1, 1, Integer.class)).intValue());
    }

    public String getFileName() {
        byte[] data = new byte[10];
        if (this._recData != null && this._recData.length > 3) {
            System.arraycopy(this._recData, 3, data, 0, 10);
        }
        return BytesUtil.getString(data);
    }

    public byte[] getFirstPackage() {
        if (this._recData == null || this._recData.length <= 13) {
            return null;
        }
        byte[] data = new byte[(this._recData.length - 13)];
        System.arraycopy(this._recData, 13, data, 0, this._recData.length - 13);
        return data;
    }

    public byte[] getConfigPackage() {
        if (this._recData == null || this._recData.length <= 10) {
            return null;
        }
        byte[] data = new byte[(this._recData.length - 10)];
        System.arraycopy(this._recData, 10, data, 0, this._recData.length - 10);
        return data;
    }

    public boolean isConfigFinish() {
        return (((Integer) get(9, 1, Integer.class)).intValue() & 1) == 0;
    }

    public byte[] getCompressPackage() {
        if (this._recData == null || this._recData.length <= 3) {
            return null;
        }
        byte[] data = new byte[(this._recData.length - 3)];
        System.arraycopy(this._recData, 3, data, 0, this._recData.length - 3);
        return data;
    }

    public int getSessionId() {
        return ((Integer) get(5, 4, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.mCmdType == CmdType.CreateFile) {
            this._sendData = new byte[4];
            this._sendData[0] = 1;
            this._sendData[1] = 1;
            this._sendData[2] = 0;
            this._sendData[3] = 0;
        } else if (this.mCmdType == CmdType.WriteConfig) {
            this._sendData = new byte[8];
            this._sendData[0] = 1;
            this._sendData[1] = (byte) this.mCmdType.data;
            this._sendData[2] = 0;
            this._sendData[3] = 0;
            byte[] session = BytesUtil.getBytes(this.mSessionIndex);
            System.arraycopy(session, 0, this._sendData, 4, session.length);
        } else if (this.mCmdType == CmdType.AppRequest) {
            this._sendData = new byte[8];
            this._sendData[0] = 1;
            this._sendData[1] = (byte) this.mCmdType.data;
            this._sendData[2] = 0;
            this._sendData[3] = 1;
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        getPacks();
        if (this.mCmdType == CmdType.AppRequest) {
            pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
            pack.isNeedAck = DataConfig.NEEDACK.YES_BY_PUSH.value();
        } else {
            pack.cmdType = DataConfig.CMDTYPE.ACK.value();
            pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        }
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.GetPushFlightRecord.value();
        pack.senderType = DeviceType.APP.value();
        pack.senderId = 7;
        pack.receiverType = this.mReceType;
        pack.receiverId = this.mReceId;
        pack.seq = this.mSeq;
        pack.timeOut = 3000;
        start(pack, callBack);
    }

    public DataFlycGetPushFlightRecord configRequest() {
        if (DJIProductManager.getInstance().getType() == ProductType.Mammoth) {
            this.mReceType = 8;
            this.mReceId = 1;
        } else {
            this.mReceType = 3;
            this.mReceId = 6;
        }
        this.mSeq = 0;
        return this;
    }

    public DataFlycGetPushFlightRecord configRequest1860() {
        this.mReceType = 8;
        this.mReceId = 1;
        this.mSeq = 0;
        return this;
    }

    @Keep
    public enum CmdType {
        CreateFile(1),
        Write(2),
        Read(3),
        WriteConfig(4),
        AppRequest(7);
        
        int data;

        private CmdType(int data2) {
            this.data = data2;
        }

        public static CmdType find(int b) {
            CmdType[] items = values();
            CmdType result = Write;
            for (CmdType item : items) {
                if (item.data == b) {
                    return item;
                }
            }
            return result;
        }
    }
}
