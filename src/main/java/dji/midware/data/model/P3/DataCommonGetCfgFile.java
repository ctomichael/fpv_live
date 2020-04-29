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
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataCommonGetCfgFile extends DataBase implements DJIDataSyncListener {
    private static DataCommonGetCfgFile instance = null;
    private long length = 0;
    private int mReceiveId = 0;
    private DeviceType mReceiveType = DeviceType.DM368;
    private long offset = 0;
    private DJIUpgradeFileType type;

    public static synchronized DataCommonGetCfgFile getInstance() {
        DataCommonGetCfgFile dataCommonGetCfgFile;
        synchronized (DataCommonGetCfgFile.class) {
            if (instance == null) {
                instance = new DataCommonGetCfgFile();
            }
            dataCommonGetCfgFile = instance;
        }
        return dataCommonGetCfgFile;
    }

    public DataCommonGetCfgFile setReceiveType(DeviceType type2) {
        this.mReceiveType = type2;
        return this;
    }

    public DataCommonGetCfgFile setReceiveId(int id) {
        this.mReceiveId = id;
        return this;
    }

    public DataCommonGetCfgFile setType(DJIUpgradeFileType type2) {
        this.type = type2;
        return this;
    }

    public DataCommonGetCfgFile setOffset(long offset2) {
        this.offset = offset2;
        return this;
    }

    public DataCommonGetCfgFile setLength(long length2) {
        this.length = length2;
        return this;
    }

    public long getRelLength() {
        return ((Long) get(0, 4, Long.class)).longValue();
    }

    public long getRemainLength() {
        return ((Long) get(4, 4, Long.class)).longValue();
    }

    public int getBuffer(byte[] buffer) {
        if (this._recData == null) {
            return 0;
        }
        int size = this._recData.length - 8;
        System.arraycopy(this._recData, 8, buffer, 0, size);
        return size;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[9];
        this._sendData[0] = (byte) this.type.value();
        System.arraycopy(BytesUtil.getUnsignedBytes(this.offset), 0, this._sendData, 1, 4);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.length), 0, this._sendData, 5, 4);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.mReceiveType.value();
        pack.receiverId = this.mReceiveId;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.GetCfgFile.value();
        pack.timeOut = 1000;
        pack.repeatTimes = 6;
        start(pack, callBack);
    }

    @Keep
    public enum DJIUpgradeFileType {
        CFG(1),
        LOG(2),
        LICENSE(3),
        OTHER(100);
        
        private int data;

        private DJIUpgradeFileType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static DJIUpgradeFileType find(int b) {
            DJIUpgradeFileType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
