package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataCameraGetPushUpgradeStatus;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataAsync2Listener;
import dji.midware.util.BytesUtil;
import java.util.ArrayList;
import java.util.Locale;

@Keep
@EXClassNullAway
public class DataCommonGetPushUpgradeStatus extends DataBase implements DJIDataAsync2Listener {
    private static DataCommonGetPushUpgradeStatus instance = null;
    private DataCommonGetPushUpgradeStatusInfo info;
    private int receiverId;
    private int receiverType;
    private DataCameraGetPushUpgradeStatus.UpgradeStep upgradeStep;

    public static synchronized DataCommonGetPushUpgradeStatus getInstance() {
        DataCommonGetPushUpgradeStatus dataCommonGetPushUpgradeStatus;
        synchronized (DataCommonGetPushUpgradeStatus.class) {
            if (instance == null) {
                instance = new DataCommonGetPushUpgradeStatus();
            }
            dataCommonGetPushUpgradeStatus = instance;
        }
        return dataCommonGetPushUpgradeStatus;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    public DataCommonGetPushUpgradeStatusInfo getDescList() {
        if (this.info == null) {
            this.info = new DataCommonGetPushUpgradeStatusInfo();
        }
        this.info.parse(this._recData);
        return this.info;
    }

    public byte[] getBytes() {
        return this._recData;
    }

    public DeviceType getSenderDeviceType() {
        if (this.pack != null) {
            return DeviceType.find(this.pack.senderType);
        }
        return null;
    }

    public int getSenderDeviceId() {
        if (this.pack != null) {
            return this.pack.senderId;
        }
        return 1;
    }

    public boolean isBatteryFailed() {
        DataCommonGetPushUpgradeStatusInfo info2 = getDescList();
        if (info2 == null || info2.completeReason != DJIUpgradeCompleteReason.Failure || info2.mUpgradeDescList == null || info2.mUpgradeDescList.size() <= 0 || info2.mUpgradeDescList.get(0).mDeviceType != DeviceType.BATTERY.value()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = BytesUtil.getByte(this.upgradeStep.value());
    }

    public void pushStatus(DataCameraGetPushUpgradeStatus.UpgradeStep upgradeStep2, int receiverType2, int receiverId2) {
        this.receiverType = receiverType2;
        this.receiverId = receiverId2;
        this.upgradeStep = upgradeStep2;
        start();
    }

    public void start() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.receiverType;
        pack.receiverId = this.receiverId;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.GetPushUpgradeStatus.value();
        start(pack);
    }

    @Keep
    public static class DataUpgradeStatusInfo {
        public int mUpgradeProcess;
        public DataUpgradeStatus mUpgradeState;

        public void parse(byte[] data) {
            if (data != null) {
                this.mUpgradeState = DataUpgradeStatus.find(data[2]);
                this.mUpgradeProcess = data[1] & 255;
            }
        }
    }

    @Keep
    public static class DataCommonGetPushUpgradeStatusInfo {
        public DJIUpgradeCompleteReason completeReason;
        public DataUpgradeStatusInfo dataUpgradeStatusInfo;
        public int mCurUpgradeIndex;
        public ArrayList<DataCommonGetPushUpgradeStatusDescInfo> mUpgradeDescList;
        public int mUpgradeProcess;
        public int mUpgradeResult;
        public int mUpgradeState;
        public DJIUpgradeStep mUpgradeStep;
        public int mUpgradeTimes;
        public int mUserReserve;
        public int mUserTime;

        public void parse(byte[] data) {
            if (data != null) {
                this.mUpgradeState = data[0] & 255;
                this.mUpgradeStep = DJIUpgradeStep.find(this.mUpgradeState);
                reset();
                switch (this.mUpgradeStep) {
                    case UserConfirm:
                        this.mUserTime = BytesUtil.getInt(data[1]);
                        this.mUserReserve = BytesUtil.getInt(data[2]);
                        break;
                    case Upgrading:
                        this.mCurUpgradeIndex = (data[2] >> 5) & 7;
                        this.mUpgradeTimes = data[2] & 31;
                        this.mUpgradeProcess = data[1] & 255;
                        break;
                    case Complete:
                        this.mUpgradeTimes = data[2] & 255;
                        this.mUpgradeResult = data[1] & 255;
                        this.completeReason = DJIUpgradeCompleteReason.find(this.mUpgradeResult);
                        break;
                    case DataUpgrading:
                        if (this.dataUpgradeStatusInfo == null) {
                            this.dataUpgradeStatusInfo = new DataUpgradeStatusInfo();
                        }
                        this.dataUpgradeStatusInfo.parse(data);
                        break;
                }
                this.mUpgradeDescList = new ArrayList<>();
                for (int i = 0; i < this.mUpgradeTimes; i++) {
                    this.mUpgradeDescList.add(DataCommonGetPushUpgradeStatusDescInfo.parseItem(data, (i * 8) + 3));
                }
            }
        }

        private void reset() {
            this.mUpgradeTimes = 0;
        }
    }

    @Keep
    public static class DataCommonGetPushUpgradeStatusDescInfo {
        public int mDeviceId;
        public int mDeviceType;
        public int mFirmwareType;
        public int mFirmwareVersion;
        public int mUpgradeProcess;
        public int mUpgradeStatus;

        static DataCommonGetPushUpgradeStatusDescInfo parseItem(byte[] data, int offset) {
            DataCommonGetPushUpgradeStatusDescInfo item = new DataCommonGetPushUpgradeStatusDescInfo();
            item.mDeviceId = BytesUtil.getInt(data[offset]) >> 5;
            item.mDeviceType = BytesUtil.getInt(data[offset]) & 31;
            item.mFirmwareType = BytesUtil.getInt(data[offset + 1]);
            item.mFirmwareVersion = BytesUtil.getInt(data, 2);
            item.mUpgradeStatus = BytesUtil.getInt(data[offset + 6]);
            item.mUpgradeProcess = BytesUtil.getInt(data[offset + 7]);
            return item;
        }

        public String toString() {
            return String.format(Locale.US, "mDeviceType = %d, mDeviceId = %d, mFirmwareType = %d, mFirmwareVersion = %d, mUpgradeStatus = %d, mUpgradeProcess = %d", Integer.valueOf(this.mDeviceType), Integer.valueOf(this.mDeviceId), Integer.valueOf(this.mFirmwareType), Integer.valueOf(this.mFirmwareVersion), Integer.valueOf(this.mUpgradeStatus), Integer.valueOf(this.mUpgradeProcess));
        }
    }

    @Keep
    public enum DJIUpgradeStep {
        Verify(1),
        UserConfirm(2),
        Upgrading(3),
        Complete(4),
        DataUpgrading(5),
        OTHER(100);
        
        private int data;

        private DJIUpgradeStep(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static DJIUpgradeStep find(int b) {
            DJIUpgradeStep result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum DJIUpgradeCompleteReason {
        Success(1),
        Failure(2),
        FirmwareError(3),
        SameVersion(4),
        UserCancel(5),
        TimeOut(6),
        MotorWorking(7),
        FirmNotMatch(8),
        IllegalDegrade(9),
        NoConnectRC(10),
        UavSDRErr(11),
        RcSDRErr(12),
        SendDataToUAVErr(13),
        CtrlChannelErr(14),
        PackForUavErr(15),
        CfgNotFound(16),
        PackForGlsErr(17),
        RcGlsAftRevertErr(18),
        RcGlsAftRevertBackErr(19),
        SendDataToGlsErr(20),
        PackageExtraErr(21),
        PackForRtkErr(22),
        RcRtkAftRevertErr(23),
        RcRtkRevertBackErr(24),
        SendDataToRtkErr(25),
        OTHER(100);
        
        private int data;

        private DJIUpgradeCompleteReason(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static DJIUpgradeCompleteReason find(int b) {
            DJIUpgradeCompleteReason result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum DataUpgradeStatus {
        Success(0),
        Failure(1),
        FirmwareErr(2),
        TimeOut(3),
        AirDataLinkError(4),
        AirSdrError(5),
        GdSdrError(6),
        TransFromAirToGdError(7),
        AirCtrLinkError(8),
        ReqAirToRecvError(9),
        DataParseError(10),
        verifyError(11),
        DataCopyError(12),
        FtpError(13),
        OTHER(100);
        
        private int data;

        private DataUpgradeStatus(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static DataUpgradeStatus find(int b) {
            DataUpgradeStatus result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
