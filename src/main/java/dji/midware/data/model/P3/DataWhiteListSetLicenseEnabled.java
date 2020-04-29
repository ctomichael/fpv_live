package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdADS_B;
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
public class DataWhiteListSetLicenseEnabled extends DataBase implements DJIDataSyncListener {
    private static final int NUMBER_OF_LICENSE = 20;
    private static DataWhiteListSetLicenseEnabled instance;
    private byte[] crc;
    private int enabled = 0;
    private int index = 0;
    private boolean isNetworkAvailable = false;
    private int mLicenseId;
    private int mRepeatTime = -1;

    public DataWhiteListSetLicenseEnabled setCrc(byte[] crc2) {
        this.crc = crc2;
        return this;
    }

    public DataWhiteListSetLicenseEnabled setIndex(int index2) {
        this.index = index2;
        return this;
    }

    public DataWhiteListSetLicenseEnabled setEnabled(boolean enabled2) {
        this.enabled = enabled2 ? 1 : 0;
        return this;
    }

    public DataWhiteListSetLicenseEnabled setNetworkAvailable(boolean networkAvailable) {
        this.isNetworkAvailable = networkAvailable;
        return this;
    }

    public DataWhiteListSetLicenseEnabled setLicenseId(int licenseId) {
        this.mLicenseId = licenseId;
        return this;
    }

    public DataWhiteListSetLicenseEnabled setRepeatTime(int repeatTime) {
        this.mRepeatTime = repeatTime;
        return this;
    }

    public boolean[] getLicensesEnabled() {
        int num = getLicenseNum();
        boolean[] licensesEnabled = new boolean[num];
        for (int i = 0; i < num; i++) {
            licensesEnabled[i] = ((Integer) get(i + 2, 1, Integer.class)).intValue() == 1;
        }
        return licensesEnabled;
    }

    public boolean[] getLicensesValid() {
        int num = getLicenseNum();
        int preLength = num + 2;
        boolean[] licensesValid = new boolean[num];
        for (int i = 0; i < num; i++) {
            licensesValid[i] = ((Integer) get(preLength + i, 1, Integer.class)).intValue() == 0;
        }
        return licensesValid;
    }

    private int getLicenseNum() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public static synchronized DataWhiteListSetLicenseEnabled getInstance() {
        DataWhiteListSetLicenseEnabled dataWhiteListSetLicenseEnabled;
        synchronized (DataWhiteListSetLicenseEnabled.class) {
            if (instance == null) {
                instance = new DataWhiteListSetLicenseEnabled();
            }
            dataWhiteListSetLicenseEnabled = instance;
        }
        return dataWhiteListSetLicenseEnabled;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[6];
        System.arraycopy(BytesUtil.getBytes(this.mLicenseId), 0, this._sendData, 0, 4);
        this._sendData[4] = (byte) this.enabled;
        if (this.isNetworkAvailable) {
            this._sendData[5] = 1;
        } else {
            this._sendData[5] = 0;
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        if (this.mRepeatTime != -1) {
            pack.repeatTimes = this.mRepeatTime;
            this.mRepeatTime = -1;
        }
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.ADS_B.value();
        pack.cmdId = CmdIdADS_B.CmdIdType.SetLicenseEnabled.value();
        start(pack, callBack);
    }
}
