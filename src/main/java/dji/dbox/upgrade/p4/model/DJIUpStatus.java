package dji.dbox.upgrade.p4.model;

import com.drew.metadata.exif.makernotes.LeicaMakernoteDirectory;
import com.drew.metadata.exif.makernotes.SonyType6MakernoteDirectory;
import dji.dbox.upgrade.collectpacks.UpBaseCollector;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.dbox.upgrade.p4.server.DJIUpServerManager;
import dji.dbox.upgrade.p4.utils.DJIUpStatusHelper;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.link.DJILinkDaemonService;
import dji.midware.link.DJILinkType;
import java.util.List;

@EXClassNullAway
public class DJIUpStatus {
    private final String TAG = getClass().getSimpleName();
    private DJIUpCfgModel cfgModel;
    private DJIUpListElement choiceElement;
    private UpBaseCollector collector;
    private int deviceId = 1;
    private DeviceType deviceType = DeviceType.DM368;
    private boolean isHasUpgrade;
    private boolean isNeedLock = false;
    private boolean isNeedLockGetted = false;
    private boolean isNeedUpgrade = false;
    private DJIUpListElement latestElement;
    private DJIUpDeviceType productId;
    private DJIUpServerManager serverManager;
    private String showVersion = "";
    private List<DJIUpListElement> verList;

    public static DJIUpDeviceType getDeviceTypeByCmd(int dType, int dId, int firmwareType) {
        switch ((dType * 100) + dId) {
            case LeicaMakernoteDirectory.TAG_COLOR_TEMPERATURE /*801*/:
                DJIUpDeviceType deviceType2 = DJIUpStatusHelper.getConnectDeviceType();
                if (DJIUpDeviceType.isRcSeries(deviceType2)) {
                    if (DJIProductManager.getInstance().getType() == ProductType.Orange2) {
                        deviceType2 = DJIUpDeviceType.wm620;
                    } else if (DJIProductManager.getInstance().getType() == ProductType.M200) {
                        deviceType2 = DJIUpDeviceType.pm410;
                    }
                }
                if (deviceType2 == DJIUpDeviceType.rc002 && DJIProductManager.getInstance().getType() == ProductType.Mammoth) {
                    deviceType2 = DJIUpDeviceType.wm100ac;
                }
                if (deviceType2 == DJIUpDeviceType.rc230 && DJIProductManager.getInstance().getType() == ProductType.WM230) {
                    deviceType2 = DJIUpDeviceType.wm230;
                }
                if (deviceType2 == DJIUpDeviceType.rc240 && DJIProductManager.getInstance().getType() == ProductType.WM240) {
                    return DJIUpDeviceType.wm240;
                }
                return deviceType2;
            case SonyType6MakernoteDirectory.TAG_MAKERNOTE_THUMB_LENGTH /*1300*/:
                return DJIUpDeviceType.rc001;
            case 1301:
                DJIUpDeviceType deviceType3 = DJIUpStatusHelper.getConnectDeviceType();
                if (!DJIUpDeviceType.isWM220Series(deviceType3) || firmwareType != 4) {
                    return deviceType3;
                }
                return DJIUpDeviceType.gl811;
            case 2700:
                DJIUpDeviceType deviceType4 = DJIUpDeviceType.rc002;
                if (DJIUpStatusHelper.getConnectDeviceType() == DJIUpDeviceType.rc230) {
                    return DJIUpDeviceType.rc230;
                }
                return deviceType4;
            default:
                return null;
        }
    }

    public static DJIUpStatus factoryRecover(int dType, int dId, int firmwareType) {
        DJIUpDeviceType deviceType2 = getDeviceTypeByCmd(dType, dId, firmwareType);
        DJIUpStatus recoverStatus = new DJIUpStatus(deviceType2);
        DJIUpCfgModel cfgModel2 = new DJIUpCfgModel();
        if (DJIUpDeviceType.isSepRc(deviceType2)) {
            cfgModel2.releaseVersion = DJIUpStatusHelper.getRcVersion();
        } else if (DJIUpDeviceType.isGlassSeries(deviceType2)) {
            cfgModel2.releaseVersion = DJIUpStatusHelper.getGlassVersion();
        } else {
            cfgModel2.releaseVersion = DJIUpStatusHelper.getFlycVersion();
        }
        DJIUpConstants.LOGE("ff", "factoryRecover deviceType = " + deviceType2 + " releaseVersion=" + cfgModel2.releaseVersion);
        recoverStatus.setCfgModel(cfgModel2);
        return recoverStatus;
    }

    public DJIUpStatus(DJIUpDeviceType productId2) {
        this.productId = productId2;
    }

    public boolean isSupportUpgrade() {
        switch (this.productId) {
            case gl811:
            case ag410:
            case wm334:
                return false;
            default:
                return true;
        }
    }

    public boolean isNeedUpgrade() {
        return this.isNeedUpgrade;
    }

    public void setNeedUpgrade(boolean needUpgrade) {
        boolean z = true;
        this.isNeedUpgrade = needUpgrade;
        if (DJIUpDeviceType.isSepRc(this.productId)) {
            if (!this.isNeedUpgrade || !isSupportUpgrade()) {
                z = false;
            }
            DJIUpStatusHelper.setIsNeedUpgradeRC(z);
            return;
        }
        if (!this.isNeedUpgrade || !isSupportUpgrade()) {
            z = false;
        }
        DJIUpStatusHelper.setIsNeedUpgradeMC(z);
    }

    public boolean isNeedLock() {
        return this.isNeedLock;
    }

    public void setNeedLock(boolean needLock) {
        this.isNeedLock = needLock;
    }

    public String getShowVersion() {
        return this.showVersion;
    }

    public void setShowVersion(String showVersion2) {
        this.showVersion = showVersion2;
    }

    public DJIUpListElement getChoiceElement() {
        return this.choiceElement;
    }

    public void setChoiceElement(DJIUpListElement choiceElement2) {
        this.choiceElement = choiceElement2;
    }

    public DJIUpCfgModel getCfgModel() {
        return this.cfgModel;
    }

    public void setCfgModel(DJIUpCfgModel cfgModel2) {
        this.cfgModel = cfgModel2;
        if (cfgModel2 != null) {
            setShowVersion(cfgModel2.releaseVersion);
        } else {
            setShowVersion("");
        }
    }

    public DJIUpDeviceType getProductId() {
        return this.productId;
    }

    public void reset() {
        DJIUpConstants.LOGD(this.TAG, "Status " + this.productId + " reset");
        setNeedLockGetted(false);
        setNeedUpgrade(false);
        setNeedLock(false);
        setShowVersion("");
        setVerList(null);
    }

    public List<DJIUpListElement> getVerList() {
        return this.verList;
    }

    public void setVerList(List<DJIUpListElement> verList2) {
        this.verList = verList2;
    }

    public DJIUpListElement getLatestElement() {
        return this.latestElement;
    }

    public void setLatestElement(DJIUpListElement latestElement2) {
        this.latestElement = latestElement2;
    }

    public void setChoiceElementToLatest() {
        this.choiceElement = this.latestElement;
    }

    public DJIUpServerManager getServerManager() {
        return this.serverManager;
    }

    public void setServerManager(DJIUpServerManager serverManager2) {
        this.serverManager = serverManager2;
    }

    public UpBaseCollector getCollector() {
        return this.collector;
    }

    public void setCollector(UpBaseCollector collector2) {
        this.collector = collector2;
    }

    public DeviceType getDeviceType() {
        return this.deviceType;
    }

    public void setDeviceType(DeviceType deviceType2) {
        this.deviceType = deviceType2;
    }

    public int getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(int deviceId2) {
        this.deviceId = deviceId2;
    }

    public boolean isNeedVerify() {
        return getProductId() == null || getProductId().isNeedVerify();
    }

    public boolean isUseTar() {
        return getProductId() == null || getProductId().isUseTar();
    }

    public boolean isUseMultiFile() {
        boolean isUseMultiFile = false;
        if (getProductId() == DJIUpDeviceType.wm100ac && DJILinkDaemonService.getInstance().getLinkType() == DJILinkType.AOA) {
            isUseMultiFile = true;
        }
        if (getProductId() == null || !getProductId().isUseMultiFile()) {
            return isUseMultiFile;
        }
        return true;
    }

    public boolean isNeedLockGetted() {
        return this.isNeedLockGetted;
    }

    public void setNeedLockGetted(boolean needLockGetted) {
        this.isNeedLockGetted = needLockGetted;
    }

    public String getProductVersion() {
        return this.latestElement == null ? "" : this.latestElement.product_version;
    }

    public int getTotalSize() {
        if (this.latestElement == null || this.latestElement.cfgModel == null) {
            return 0;
        }
        return this.latestElement.cfgModel.totalSize;
    }
}
