package dji.midware.data.config.P3;

import android.support.v4.app.FrameMetricsAggregator;
import com.adobe.xmp.XMPError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataCameraDeleteFile;
import dji.midware.data.model.P3.DataCameraGetPushFile;
import dji.midware.data.model.P3.DataCameraGetPushFiles;
import dji.midware.data.model.P3.DataCommonDataLockerCommonRequest;
import dji.midware.data.model.P3.DataCommonGetPushAppGpsConfig;
import dji.midware.data.model.P3.DataCommonGetPushUpgradeStatus;
import dji.midware.data.model.P3.DataCommonSetStartModuleChecking;
import dji.midware.data.model.P3.DataCommonTransferFileData;
import dji.midware.data.model.P3.DataNotifyDisconnect;
import dji.midware.data.model.P3.DataUpgradeSelfRequest;
import dji.midware.data.model.common.DataHeartBeat;
import dji.midware.interfaces.CmdIdInterface;

@EXClassNullAway
public class CmdIdCommon extends DJICmdSetBase {

    public enum CmdIdType implements CmdIdInterface {
        GetVersion(1),
        SetMultiParam(5),
        RequestUpgrade(7),
        RequestReceiveData(8),
        TranslateData(9),
        TranslateComplete(10),
        RestartDevice(11),
        GetDeviceStatus(12),
        HeartBeat(14, false, DataHeartBeat.class),
        UpgradeSelfRequest(15, false, DataUpgradeSelfRequest.class),
        GetPushLog(240, false, null),
        RequestSendFiles(34),
        AckReceiveFiles(35, false, null),
        GetPushFiles(36, false, DataCameraGetPushFiles.class),
        SetResendFiles(37),
        RequestFile(38),
        GetPushFile(39, false, (Class<? extends DataBase>) DataCameraGetPushFile.class, (DataBase) DataCameraGetPushFile.getInstance()),
        DeleteFile(40, false, true, (Class<? extends DataBase>) DataCameraDeleteFile.class),
        TransferFile(42, false, true, (Class<? extends DataBase>) DataCommonTransferFileData.class),
        ActiveStatus(50, false, true, (Class<? extends DataBase>) null),
        LockLicense(52),
        GetPushRequestUpgrade(64, false, null),
        ControlUpgrade(65),
        GetPushUpgradeStatus(66, false, DataCommonGetPushUpgradeStatus.class),
        AckUpgradeStop(67, false, null),
        GetSerialNumberOfMavicRC(81),
        GetPushAppGpsConfig(82, false, DataCommonGetPushAppGpsConfig.class),
        SetAppGpsCyclic(83),
        SetGetTipsAudio(90),
        NotifyDisconnect(71, false, DataNotifyDisconnect.class),
        SetDate(74),
        GetCfgFile(79),
        GetSerialNum(81),
        SetNewestVersions(97),
        CameraDebugCmd(112),
        DataLocker(116, false, true, (Class<? extends DataBase>) DataCommonDataLockerCommonRequest.class),
        CmdNeedEncrypt(XMPError.BADSTREAM),
        GetPushCheckStatus(241, false, null),
        SetStartModuleChecking(244, false, true, (Class<? extends DataBase>) DataCommonSetStartModuleChecking.class),
        GetPushCheckStatusV2(251, false, null),
        GetDeviceInfo(255, false),
        Other(FrameMetricsAggregator.EVERY_DURATION);
        
        private static CmdIdType[] items = values();
        private Class<? extends DataBase> cls;
        private int data;
        private DataBase dataBase;
        private boolean isBlock = true;
        private boolean isMix = false;
        private boolean isNeedCcode = true;

        private CmdIdType(int _data) {
            this.data = _data;
        }

        private CmdIdType(int _data, boolean isBlock2, Class<? extends DataBase> cls2) {
            this.data = _data;
            this.isBlock = isBlock2;
            this.cls = cls2;
        }

        private CmdIdType(int _data, boolean isNeedCcode2) {
            this.data = _data;
            this.isNeedCcode = isNeedCcode2;
        }

        private CmdIdType(int _data, boolean isBlock2, boolean isMix2, Class<? extends DataBase> cls2) {
            this.data = _data;
            this.isMix = isMix2;
            this.isBlock = isBlock2;
            this.cls = cls2;
            this.dataBase = DataUtil.getDataBaseInstRefl(cls2);
        }

        private CmdIdType(int _data, boolean isBlock2, Class<? extends DataBase> cls2, DataBase dataBase2) {
            this.data = _data;
            this.isBlock = isBlock2;
            this.cls = cls2;
            this.dataBase = dataBase2;
        }

        public int value() {
            return this.data;
        }

        public boolean isMix() {
            return this.isMix;
        }

        public boolean isBlock() {
            return this.isBlock;
        }

        public Class<? extends DataBase> getDataModel() {
            return this.cls;
        }

        public boolean isNeedCcode() {
            return this.isNeedCcode;
        }

        public DataBase getDataBase() {
            return this.dataBase;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static CmdIdType find(int b) {
            CmdIdType result = Other;
            for (int i = 0; i < items.length; i++) {
                if (items[i]._equals(b)) {
                    return items[i];
                }
            }
            return result;
        }
    }

    /* access modifiers changed from: protected */
    public CmdIdInterface[] getCurCmdIdValues() {
        return CmdIdType.values();
    }

    public String getDataModelName(int deviceType, int cmdId) {
        String deviceName;
        if (cmdId == CmdIdType.GetPushCheckStatus.value() || cmdId == CmdIdType.GetPushCheckStatusV2.value()) {
            cmdId = CmdIdType.GetPushCheckStatus.value();
            if (deviceType == DeviceType.OFDM.value()) {
                deviceName = "OFDM";
            } else {
                deviceName = DeviceType.find(deviceType).toString();
            }
        } else {
            deviceName = DeviceType.find(deviceType).toString();
        }
        return DataUtil.getDataModelName(deviceName, CmdIdType.find(cmdId).toString());
    }

    public String getDataModelName(int deviceType, int deviceId, int cmdId) {
        if (cmdId == CmdIdType.GetPushLog.value()) {
            if (deviceType == DeviceType.WIFI_G.value()) {
                return DataUtil.getDataModelNameNon("Rc", CmdIdType.find(cmdId).toString());
            }
            if (deviceType == DeviceType.DOUBLE.value()) {
                return DataUtil.getDataModelNameNon("Eye", "GetPushLog");
            }
        } else if (cmdId == CmdIdType.GetPushCheckStatus.value() || cmdId == CmdIdType.GetPushCheckStatusV2.value()) {
            cmdId = CmdIdType.GetPushCheckStatus.value();
            if (deviceType == DeviceType.DM368.value()) {
                if (1 == deviceId) {
                    return DataUtil.getDataModelNameNon("1860", CmdIdType.find(cmdId).toString());
                }
            } else if (deviceType == DeviceType.DM368_G.value()) {
                if (deviceId == 1) {
                    return "";
                }
            } else if (deviceType == DeviceType.DOUBLE.value()) {
                if (deviceId == 0) {
                    if (DJIProductManager.getInstance().getType() == ProductType.Mammoth) {
                        return DataUtil.getDataModelNameNon("2150", CmdIdType.find(cmdId).toString());
                    }
                    if (DJIProductManager.getInstance().getType() == ProductType.WM160) {
                        return DataUtil.getDataModelNameNon("WM160Vision", CmdIdType.find(cmdId).toString());
                    }
                    return DataUtil.getDataModelNameNon("2100", CmdIdType.find(cmdId).toString());
                }
            } else if (deviceType == DeviceType.RC.value() && deviceId == 0) {
                return DataUtil.getDataModelNameNon("Rc", CmdIdType.find(cmdId).toString());
            }
        } else if (cmdId == CmdIdType.ActiveStatus.value() && (deviceType == DeviceType.DM368.value() || deviceType == DeviceType.FLYC.value())) {
            return DataUtil.getDataModelNameNon("Eagle", CmdIdType.find(cmdId).toString());
        }
        return getDataModelName(deviceType, cmdId);
    }
}
