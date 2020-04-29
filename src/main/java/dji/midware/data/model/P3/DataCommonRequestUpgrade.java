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
import dji.thirdparty.afinal.core.Arrays;

@Keep
@EXClassNullAway
public class DataCommonRequestUpgrade extends DataBase implements DJIDataSyncListener {
    private static DataCommonRequestUpgrade instance = null;
    private int mEncrypt = 0;
    private int mReceiveId = 0;
    private DeviceType mReceiveType = DeviceType.RC;
    private DJIUpgradeFileMethod upgradeFileMethod = new DJIUpgradeFileMethod();
    private DJIUpgradeTranMethod upgradeTranMethod = new DJIUpgradeTranMethod();

    public static synchronized DataCommonRequestUpgrade getInstance() {
        DataCommonRequestUpgrade dataCommonRequestUpgrade;
        synchronized (DataCommonRequestUpgrade.class) {
            if (instance == null) {
                instance = new DataCommonRequestUpgrade();
            }
            dataCommonRequestUpgrade = instance;
        }
        return dataCommonRequestUpgrade;
    }

    public DataCommonRequestUpgrade setReceiveType(DeviceType type) {
        this.mReceiveType = type;
        return this;
    }

    public DeviceType getmReceiveType() {
        return this.mReceiveType;
    }

    public DataCommonRequestUpgrade setReceiveId(int id) {
        this.mReceiveId = id;
        return this;
    }

    public DJIUpgradeTranMethod getTranMethodEntry() {
        this.upgradeTranMethod.parse(this._recData[0]);
        return this.upgradeTranMethod;
    }

    public DJIUpgradeFileMethod getTranFileEntry() {
        this.upgradeFileMethod.parse(this._recData[1]);
        return this.upgradeFileMethod;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[9];
        Arrays.fill(this._sendData, (byte) 0);
        this._sendData[0] = (byte) this.mEncrypt;
    }

    public void start(DJIDataCallBack callBack) {
        start(callBack, 15000, 1);
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
        pack.cmdId = CmdIdCommon.CmdIdType.RequestUpgrade.value();
        pack.timeOut = timeOut;
        pack.repeatTimes = repeatTimes;
        start(pack, callBack);
    }

    @Keep
    public static class DJIUpgradeTranMethod implements Cloneable {
        public boolean isSupportFTP = false;
        public boolean isSupportV1 = false;

        public void parse(byte me) {
            boolean z;
            boolean z2 = true;
            if ((me & 1) == 1) {
                z = true;
            } else {
                z = false;
            }
            this.isSupportV1 = z;
            if (((me >> 1) & 1) != 1) {
                z2 = false;
            }
            this.isSupportFTP = z2;
        }

        public byte getBuffer() {
            int i = 1;
            int i2 = this.isSupportV1 ? 1 : 0;
            if (!this.isSupportFTP) {
                i = 0;
            }
            return (byte) (i2 | (i << 1));
        }

        public void reset() {
            this.isSupportV1 = false;
            this.isSupportFTP = false;
        }

        public Object clone() {
            try {
                return super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Keep
    public static class DJIUpgradeFileMethod implements Cloneable {
        public boolean isSupportBigPackage = false;
        public boolean isSupportCopyData = false;
        public boolean isSupportMultiFile = false;
        public boolean isSupportSingalFileSerial = false;

        public void parse(byte me) {
            boolean z;
            boolean z2;
            boolean z3;
            boolean z4 = true;
            if ((me & 1) == 1) {
                z = true;
            } else {
                z = false;
            }
            this.isSupportSingalFileSerial = z;
            if (((me >> 1) & 1) == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            this.isSupportMultiFile = z2;
            if (((me >> 2) & 1) == 1) {
                z3 = true;
            } else {
                z3 = false;
            }
            this.isSupportBigPackage = z3;
            if (((me >> 3) & 1) != 1) {
                z4 = false;
            }
            this.isSupportCopyData = z4;
        }

        public byte getBuffer() {
            int i = 1;
            int i2 = ((this.isSupportBigPackage ? 1 : 0) << 2) | ((this.isSupportMultiFile ? 1 : 0) << 1) | (this.isSupportSingalFileSerial ? 1 : 0);
            if (!this.isSupportCopyData) {
                i = 0;
            }
            return (byte) (i2 | (i << 3));
        }

        public void reset() {
            this.isSupportSingalFileSerial = false;
            this.isSupportMultiFile = false;
            this.isSupportBigPackage = false;
            this.isSupportCopyData = false;
        }

        public Object clone() {
            try {
                return super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
