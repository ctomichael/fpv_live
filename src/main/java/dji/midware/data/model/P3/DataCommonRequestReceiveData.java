package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataCommonRequestUpgrade;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataCommonRequestReceiveData extends DataBase implements DJIDataSyncListener {
    private static DataCommonRequestReceiveData instance = null;
    private DataCommonRequestUpgrade.DJIUpgradeFileMethod fileMethod;
    private long mDataLength = 0;
    private int mEncrypt = 0;
    private int mReceiveId = 0;
    private DeviceType mReceiveType = DeviceType.RC;
    private DataCommonRequestUpgrade.DJIUpgradeTranMethod tranMethod;

    public static synchronized DataCommonRequestReceiveData getInstance() {
        DataCommonRequestReceiveData dataCommonRequestReceiveData;
        synchronized (DataCommonRequestReceiveData.class) {
            if (instance == null) {
                instance = new DataCommonRequestReceiveData();
            }
            dataCommonRequestReceiveData = instance;
        }
        return dataCommonRequestReceiveData;
    }

    public void clear() {
    }

    public DataCommonRequestReceiveData setDataLength(long length) {
        this.mDataLength = length;
        return this;
    }

    public DataCommonRequestReceiveData setReceiveType(DeviceType type) {
        this.mReceiveType = type;
        return this;
    }

    public DataCommonRequestReceiveData setReceiveId(int id) {
        this.mReceiveId = id;
        return this;
    }

    public DataCommonRequestReceiveData setTranMethod(DataCommonRequestUpgrade.DJIUpgradeTranMethod tranMethod2) {
        this.tranMethod = tranMethod2;
        return this;
    }

    public DataCommonRequestReceiveData setFileMethod(DataCommonRequestUpgrade.DJIUpgradeFileMethod fileMethod2) {
        this.fileMethod = fileMethod2;
        return this;
    }

    public int getReceiveDataLength() {
        if (this._recData == null || this._recData.length < 2) {
            return 300;
        }
        return ((Integer) get(0, 2, Integer.class)).intValue();
    }

    public String getFtpIP() {
        if (this._recData == null || this._recData.length < 2) {
            return "0.0.0.0";
        }
        return (((((int) BytesUtil.getInt(this._recData[3])) + ".") + ((int) BytesUtil.getInt(this._recData[2])) + ".") + ((int) BytesUtil.getInt(this._recData[1])) + ".") + ((int) BytesUtil.getInt(this._recData[0]));
    }

    public int getFtpPort() {
        if (this._recData == null || this._recData.length < 2) {
            return 0;
        }
        return ((Integer) get(4, 2, Integer.class)).intValue();
    }

    public String getDir() {
        if (this._recData == null || this._recData.length < 2) {
            return "/upgrade/dji_system.bin";
        }
        return getUTF8(6, this._recData.length - 6);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[13];
        this._sendData[0] = (byte) this.mEncrypt;
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mDataLength), 0, this._sendData, 1, 4);
        if (this.tranMethod != null) {
            this._sendData[11] = this.tranMethod.getBuffer();
        }
        if (this.fileMethod != null) {
            this._sendData[12] = this.fileMethod.getBuffer();
        }
    }

    public void start(DJIDataCallBack callBack) {
        start(callBack, 15000, 2);
    }

    public void start(DJIDataCallBack callBack, int timeOut, int repeatTimes) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.mReceiveType.value();
        pack.receiverId = this.mReceiveId;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.RequestReceiveData.value();
        pack.timeOut = timeOut;
        pack.repeatTimes = repeatTimes;
        start(pack, callBack);
    }
}
