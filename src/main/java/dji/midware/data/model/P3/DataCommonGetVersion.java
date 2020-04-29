package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.util.SparseArray;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.component.rc.DJIRcDetectHelper;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.packages.P3.Pack;
import dji.midware.data.packages.P3.RecvPack;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import java.util.Locale;

@Keep
@EXClassNullAway
public class DataCommonGetVersion extends DataBase implements DJIDataSyncListener {
    private static final SparseArray<RecvPack> versionList = new SparseArray<>();
    private DeviceType deviceType;
    private boolean isClearCameraLose = true;
    private int modelId;

    @Keep
    public static class DJIVersionInfo {
        public boolean isProduction = true;
        public boolean isSupportSafeUpgrade = true;
    }

    public DataCommonGetVersion() {
        super(true);
    }

    public void setClearCameraLose(boolean clearCameraLose) {
        this.isClearCameraLose = clearCameraLose;
    }

    public void clear() {
        if (this.isClearCameraLose) {
            super.clear();
            synchronized (versionList) {
                versionList.clear();
            }
        }
    }

    public DeviceType getWhoamI() {
        Pack pack = versionList.get(getKey());
        if (pack == null) {
            return DeviceType.OTHER;
        }
        return DeviceType.find(pack.senderType);
    }

    public DeviceType getDeviceType() {
        return this.deviceType;
    }

    public int getModelId() {
        return this.modelId;
    }

    public DataCommonGetVersion setDeviceModel(int modelId2) {
        this.modelId = modelId2;
        return this;
    }

    public DataCommonGetVersion setDeviceType(DeviceType deviceType2) {
        this.deviceType = deviceType2;
        this.modelId = 0;
        return this;
    }

    public byte[] getCacheData() {
        RecvPack pack = versionList.get(getKey());
        if (pack == null) {
            return null;
        }
        return pack.data;
    }

    public String getHardwareVer() {
        byte[] bytes = null;
        RecvPack pack = versionList.get(getKey());
        if (!(pack == null || pack.data == null)) {
            bytes = pack.data;
        }
        if (bytes != null) {
            this._recData = bytes;
        }
        return get(1, 16);
    }

    public int getLoaderByte(int which) {
        byte[] bytes = null;
        RecvPack pack = versionList.get(getKey());
        if (!(pack == null || pack.data == null)) {
            bytes = pack.data;
        }
        if (bytes != null) {
            this._recData = bytes;
        }
        return ((Integer) get(21 - which, 1, Integer.class)).intValue();
    }

    public int getFirmByte(int which) {
        byte[] bytes = null;
        RecvPack pack = versionList.get(getKey());
        if (!(pack == null || pack.data == null)) {
            bytes = pack.data;
        }
        if (bytes != null) {
            this._recData = bytes;
        }
        return ((Integer) get(25 - which, 1, Integer.class)).intValue();
    }

    public String getLoader(String separator) {
        byte[] bytes;
        RecvPack pack = versionList.get(getKey());
        if (pack != null) {
            bytes = pack.data != null ? pack.data : null;
        } else {
            bytes = null;
        }
        if (bytes != null) {
            this._recData = bytes;
        }
        if (this.deviceType == DeviceType.CAMERA) {
            int firstNum = ((Integer) get(20, 1, Integer.class)).intValue();
            if (DJIProductManager.getInstance().getType() == ProductType.litchiC && firstNum == 2) {
                return String.format(Locale.US, "%02d" + separator + "%02d" + separator + "%02d" + separator + "%02d", get(20, 1, Integer.class), get(19, 1, Integer.class), get(18, 1, Integer.class), get(17, 1, Integer.class));
            }
            int last = ((Integer) get(17, 2, Integer.class)).intValue();
            return String.format(Locale.US, "%02d" + separator + "%02d" + separator + "%02d" + separator + "%02d", get(20, 1, Integer.class), get(19, 1, Integer.class), Integer.valueOf(last / 100), Integer.valueOf(last % 100));
        }
        return String.format(Locale.US, "%02d" + separator + "%02d" + separator + "%02d" + separator + "%02d", get(20, 1, Integer.class), get(19, 1, Integer.class), get(18, 1, Integer.class), get(17, 1, Integer.class));
    }

    public String getFirmVer(String separator) {
        byte[] bytes;
        RecvPack pack = versionList.get(getKey());
        if (pack != null) {
            bytes = pack.data != null ? pack.data : null;
        } else {
            bytes = null;
        }
        if (bytes != null) {
            this._recData = bytes;
        }
        if (this.deviceType == DeviceType.CAMERA) {
            int firstNum = ((Integer) get(20, 1, Integer.class)).intValue();
            if (DJIProductManager.getInstance().getType() == ProductType.litchiC && firstNum == 2) {
                return String.format(Locale.US, "%02d" + separator + "%02d" + separator + "%02d" + separator + "%02d", get(24, 1, Integer.class), get(23, 1, Integer.class), get(22, 1, Integer.class), get(21, 1, Integer.class));
            } else if (this.modelId == 6) {
                return String.format(Locale.US, "%02d" + separator + "%02d" + separator + "%02d" + separator + "%02d", get(24, 1, Integer.class), get(23, 1, Integer.class), get(22, 1, Integer.class), get(21, 1, Integer.class));
            } else {
                int last = ((Integer) get(21, 2, Integer.class)).intValue();
                return String.format(Locale.US, "%02d" + separator + "%02d" + separator + "%02d" + separator + "%02d", get(24, 1, Integer.class), get(23, 1, Integer.class), Integer.valueOf(last / 100), Integer.valueOf(last % 100));
            }
        } else {
            return String.format(Locale.US, "%02d" + separator + "%02d" + separator + "%02d" + separator + "%02d", get(24, 1, Integer.class), get(23, 1, Integer.class), get(22, 1, Integer.class), get(21, 1, Integer.class));
        }
    }

    public DJIVersionInfo getInfo() {
        boolean z;
        boolean z2 = true;
        RecvPack pack = versionList.get(getKey());
        byte[] bytes = pack != null ? pack.data : null;
        if (bytes != null) {
            this._recData = bytes;
        }
        DJIVersionInfo info = new DJIVersionInfo();
        if ((((Integer) get(25, 4, Integer.class)).intValue() >> 31) == 1) {
            z = true;
        } else {
            z = false;
        }
        info.isProduction = z;
        if (((((Integer) get(25, 4, Integer.class)).intValue() >> 30) & 1) != 1) {
            z2 = false;
        }
        info.isSupportSafeUpgrade = z2;
        return info;
    }

    public String getLoaderSimple(String separator) {
        RecvPack pack = versionList.get(getKey());
        byte[] bytes = pack != null ? pack.data : null;
        if (bytes != null) {
            this._recData = bytes;
        }
        return String.format(Locale.US, "%02d" + separator + "%02d", get(20, 1, Integer.class), get(19, 1, Integer.class));
    }

    public String getFirmVerSimple(String separator) {
        RecvPack pack = versionList.get(getKey());
        byte[] bytes = pack != null ? pack.data != null ? pack.data : null : null;
        if (bytes != null) {
            this._recData = bytes;
        }
        if (this.deviceType == DeviceType.CAMERA) {
            return String.format(Locale.US, "%02d" + separator + "%02d", get(24, 1, Integer.class), get(23, 1, Integer.class));
        }
        return String.format(Locale.US, "%02d" + separator + "%02d", get(24, 1, Integer.class), get(23, 1, Integer.class));
    }

    public void onRecvPackSeted(RecvPack recvPack) {
        boolean success;
        ProductType rcType;
        super.onRecvPackSeted(recvPack);
        RecvPack pk = versionList.get(getKey());
        if (recvPack.data != null) {
            success = true;
        } else {
            success = false;
        }
        if (success && this.deviceType == DeviceType.FPGA_G) {
            if (((Integer) get(17, 4, Integer.class)).intValue() != 0) {
                success = true;
            } else {
                success = false;
            }
        }
        if (success) {
            if (pk == null && this.deviceType == DeviceType.OSD && !DJIProductManager.getInstance().isRcSeted() && (rcType = DJIRcDetectHelper.getRcType(this)) != null) {
                DJIProductManager.getInstance().setRcSeted(true);
                DJIProductManager.getInstance().updateRcType(rcType);
                if (!DJIProductManager.getInstance().isRemoteSeted()) {
                    DJIProductManager.getInstance().setType(rcType);
                    if (ProductType.Grape2 == rcType) {
                        DJIProductManager.getInstance().setRemoteSeted(true);
                    } else if (ProductType.P34K == rcType && ServiceManager.getInstance().isRemoteOK()) {
                        DJIProductManager.getInstance().setRemoteSeted(true);
                    }
                }
                if (!(rcType != ProductType.Orange2 || DJIProductManager.getInstance().getType() == ProductType.Orange2 || DJIProductManager.getInstance().getType() == ProductType.M200 || DJIProductManager.getInstance().getType() == ProductType.M210 || DJIProductManager.getInstance().getType() == ProductType.M210RTK || DJIProductManager.getInstance().getType() == ProductType.PM420 || DJIProductManager.getInstance().getType() == ProductType.PM420PRO || DJIProductManager.getInstance().getType() == ProductType.PM420PRO_RTK || DJIProductManager.getInstance().isRemoteSeted())) {
                    DJIProductManager.getInstance().setType(ProductType.Orange2);
                }
            }
            synchronized (versionList) {
                versionList.put(getKey(), recvPack);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    private int getKey() {
        return (this.deviceType.value() * 100) + this.modelId;
    }

    public void start(DJIDataCallBack callBack) {
        start(callBack, 500, 1);
    }

    public void startForce(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverId = this.modelId;
        pack.receiverType = this.deviceType.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.GetVersion.value();
        pack.timeOut = 500;
        pack.repeatTimes = 1;
        start(pack, callBack);
    }

    public void startForce(DJIDataCallBack callBack, int timeOut, int repeat, boolean isForce) {
        byte[] bytes;
        if (isForce || versionList.get(getKey()) == null || (bytes = versionList.get(getKey()).data) == null) {
            SendPack pack = new SendPack();
            pack.senderType = DeviceType.APP.value();
            pack.receiverId = this.modelId;
            pack.receiverType = this.deviceType.value();
            pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
            pack.isNeedAck = DataConfig.NEEDACK.YES.value();
            pack.encryptType = DataConfig.EncryptType.NO.value();
            pack.cmdSet = CmdSet.COMMON.value();
            pack.cmdId = CmdIdCommon.CmdIdType.GetVersion.value();
            pack.timeOut = timeOut;
            pack.repeatTimes = repeat;
            start(pack, callBack);
            return;
        }
        this._recData = bytes;
        callBack.onSuccess(this);
    }

    public void start(DJIDataCallBack callBack, int timeOut, int repeat) {
        byte[] bytes;
        if (versionList.get(getKey()) == null || (bytes = versionList.get(getKey()).data) == null) {
            SendPack pack = new SendPack();
            pack.senderType = DeviceType.APP.value();
            pack.receiverId = this.modelId;
            pack.receiverType = this.deviceType.value();
            pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
            pack.isNeedAck = DataConfig.NEEDACK.YES.value();
            pack.encryptType = DataConfig.EncryptType.NO.value();
            pack.cmdSet = CmdSet.COMMON.value();
            pack.cmdId = CmdIdCommon.CmdIdType.GetVersion.value();
            pack.timeOut = timeOut;
            pack.repeatTimes = repeat;
            start(pack, callBack);
            return;
        }
        this._recData = bytes;
        callBack.onSuccess(this);
    }
}
