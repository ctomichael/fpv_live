package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.config.P3.CmdIdADS_B;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataWhiteListNewRequestLicense;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;
import java.util.ArrayList;
import java.util.List;

@Keep
public class DataWhiteListNewSetLicenseEnabled extends DataBase implements DJIDataSyncListener {
    private static DataWhiteListNewSetLicenseEnabled instance;
    private boolean isNetworkAvailable = false;
    private int mLicenseId;
    private LicenseOpCode mLicenseOpCode = LicenseOpCode.QUERY;
    private List<DataWhiteListNewRequestLicense.LicenseStatus> mLicensesStatus = new ArrayList();
    private int mRepeatTime = -1;

    public static synchronized DataWhiteListNewSetLicenseEnabled getInstance() {
        DataWhiteListNewSetLicenseEnabled dataWhiteListNewSetLicenseEnabled;
        synchronized (DataWhiteListNewSetLicenseEnabled.class) {
            if (instance == null) {
                instance = new DataWhiteListNewSetLicenseEnabled();
            }
            dataWhiteListNewSetLicenseEnabled = instance;
        }
        return dataWhiteListNewSetLicenseEnabled;
    }

    public DataWhiteListNewSetLicenseEnabled setOpCode(LicenseOpCode _opCode) {
        this.mLicenseOpCode = _opCode;
        return this;
    }

    public DataWhiteListNewSetLicenseEnabled setNetworkAvailable(boolean networkAvailable) {
        this.isNetworkAvailable = networkAvailable;
        return this;
    }

    public DataWhiteListNewSetLicenseEnabled setLicenseId(int licenseId) {
        this.mLicenseId = licenseId;
        return this;
    }

    public DataWhiteListNewSetLicenseEnabled setRepeatTime(int repeatTime) {
        this.mRepeatTime = repeatTime;
        return this;
    }

    private int getLicenseNum() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public List<DataWhiteListNewRequestLicense.LicenseStatus> getLicensesStatus() {
        return this.mLicensesStatus;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public void setRecData(byte[] data) {
        super.setRecData(data);
        if (data != null) {
            this.mLicensesStatus.clear();
            int num = getLicenseNum();
            int startIndex = 2;
            for (int i = 0; i < num && startIndex < data.length; i++) {
                DataWhiteListNewRequestLicense.LicenseStatus status = new DataWhiteListNewRequestLicense.LicenseStatus();
                status.parseFromOneByte(((Integer) get(startIndex, 1, Integer.class)).intValue());
                startIndex++;
                this.mLicensesStatus.add(status);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[7];
        this._sendData[0] = 0;
        System.arraycopy(BytesUtil.getBytes(this.mLicenseId), 0, this._sendData, 1, 4);
        this._sendData[5] = (byte) this.mLicenseOpCode.value();
        if (this.isNetworkAvailable) {
            this._sendData[6] = 1;
        } else {
            this._sendData[6] = 0;
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        if (this.mRepeatTime != -1) {
            pack.repeatTimes = this.mRepeatTime;
            this.mRepeatTime = -1;
        }
        pack.timeOut = 5000;
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.ADS_B.value();
        pack.cmdId = CmdIdADS_B.CmdIdType.SetLicenseEnabled.value();
        start(pack, callBack);
    }

    @Keep
    public enum LicenseOpCode {
        QUERY(0),
        OPEN(1),
        CLOSE(2),
        OTHER(100);
        
        private int data;

        private LicenseOpCode(int i) {
            this.data = i;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static LicenseOpCode find(int b) {
            LicenseOpCode result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
