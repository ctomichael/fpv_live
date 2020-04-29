package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.flysafe.v3.License;
import dji.flysafe.v3.LicenseGroupInfo;
import dji.midware.data.config.P3.CmdIdADS_B;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import java.io.IOException;

@Keep
public class DataWhiteListNewRequestLicense extends DataBase implements DJIDataSyncListener {
    private static final int LENGTH_GROUP_INFO_BYTE = 16;
    private static final int LENGTH_LICENSE_DATA_BYTE = 74;
    private static DataWhiteListNewRequestLicense instance;
    private boolean isRequestGroupInfo = false;
    private LicenseGroupInfo mLicenseGroupInfo;
    private License mLicenseInfo;
    private LicenseStatus mLicenseStatus = new LicenseStatus();
    private int mRequestId = -1;

    public static synchronized DataWhiteListNewRequestLicense getInstance() {
        DataWhiteListNewRequestLicense dataWhiteListNewRequestLicense;
        synchronized (DataWhiteListNewRequestLicense.class) {
            if (instance == null) {
                instance = new DataWhiteListNewRequestLicense();
            }
            dataWhiteListNewRequestLicense = instance;
        }
        return dataWhiteListNewRequestLicense;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public DataWhiteListNewRequestLicense setRequestId(int requestId) {
        this.mRequestId = requestId;
        return this;
    }

    public DataWhiteListNewRequestLicense setRequestGroupInfo(boolean requestGroupInfo) {
        this.isRequestGroupInfo = requestGroupInfo;
        return this;
    }

    public License getLicenseInfo() {
        return this.mLicenseInfo;
    }

    public LicenseGroupInfo getLicenseGroupInfo() {
        return this.mLicenseGroupInfo;
    }

    public LicenseStatus getLicenseStatus() {
        return this.mLicenseStatus;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        this._sendData[0] = 0;
        if (this.isRequestGroupInfo) {
            this._sendData[1] = (byte) ((this.mRequestId << 1) + 1);
        } else {
            this._sendData[1] = (byte) (this.mRequestId << 1);
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.ADS_B.value();
        pack.cmdId = CmdIdADS_B.CmdIdType.RequestLicense.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    public void setRecData(byte[] data) {
        super.setRecData(data);
        if (data != null && getResult() == 0) {
            if (!this.isRequestGroupInfo) {
                this.mLicenseStatus.parseFromOneByte(((Integer) get(1, 1, Integer.class)).intValue());
                byte[] licenseBytes = new byte[(data.length - 2)];
                System.arraycopy(data, 2, licenseBytes, 0, licenseBytes.length);
                try {
                    this.mLicenseInfo = License.ADAPTER.decode(licenseBytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                byte[] licenseBytes2 = new byte[(data.length - 1)];
                System.arraycopy(data, 1, licenseBytes2, 0, licenseBytes2.length);
                try {
                    this.mLicenseGroupInfo = LicenseGroupInfo.ADAPTER.decode(licenseBytes2);
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    @Keep
    public static class LicenseStatus {
        public boolean isCurTakeEffect;
        public boolean isInValidDate;
        public boolean isOpen;
        public LicenseUserVerifyStatus mVerifyStatus = LicenseUserVerifyStatus.OTHER;

        public void parseFromOneByte(int data) {
            boolean z;
            boolean z2;
            boolean z3 = true;
            if ((data & 1) == 1) {
                z = true;
            } else {
                z = false;
            }
            this.isCurTakeEffect = z;
            if (((data >> 1) & 1) == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            this.isOpen = z2;
            if (((data >> 2) & 1) != 1) {
                z3 = false;
            }
            this.isInValidDate = z3;
            this.mVerifyStatus = LicenseUserVerifyStatus.find((data >> 3) & 3);
        }
    }

    @Keep
    public enum LicenseUserVerifyStatus {
        NOT_NEED(0),
        FAIL(1),
        Access(2),
        OTHER(100);
        
        private int data;

        private LicenseUserVerifyStatus(int i) {
            this.data = i;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static LicenseUserVerifyStatus find(int b) {
            LicenseUserVerifyStatus result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
