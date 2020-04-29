package dji.dbox.upgrade.p4.utils;

import android.content.Context;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.dbox.upgrade.p4.model.DJIUpCfgModel;
import dji.dbox.upgrade.p4.model.DJIUpListElement;
import dji.dbox.upgrade.p4.model.DJIUpStatus;
import dji.dbox.upgrade.p4.server.DJIUpServerManager;
import dji.dbox.upgrade.p4.statemachine.DJIUpTarManager;
import dji.dbox.upgrade.p4.statemachine.DJIUpgradeService;
import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.midware.component.DJIComponentManager;
import dji.midware.component.rc.DJIRcDetectHelper;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataGlassGetPushCheckStatus;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.link.DJILinkDaemonService;
import dji.midware.link.DJILinkType;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@EXClassNullAway
public class DJIUpStatusHelper {
    private static final String TAG = "DJIUpStatusHelper";
    private static DJIUpStatus aircraftStatus;
    private static boolean allowRedo = true;
    private static DJIUpDeviceType connectDeviceType;
    private static DJIUpgradeService.DJIUpP4Event curEvent;
    private static long enforceTime;
    private static String flycVersion = "";
    private static String glassVersion;
    public static int initTimes;
    private static boolean isAllowRollBack;
    private static boolean isChecking = true;
    private static boolean isCheckingSetted = false;
    private static boolean isNeedUpgradeMC;
    private static boolean isNeedUpgradeRC;
    private static boolean isRecovered;
    private static boolean isUnderSyncUpgradeContext;
    private static boolean isUpDownloading = false;
    private static boolean isUpProgressing = false;
    private static boolean mIsNeedLock;
    private static boolean mIsNeedLockGetted;
    private static List<DJIUpStatus> mainPageStatus;
    private static String rcVersion = "";
    private static DJIUpStatus rollBackStatus;
    private static DJIUpStatus upgradingStatus;

    public static void setCurEvent(DJIUpgradeService.DJIUpP4Event curEvent2) {
        curEvent = curEvent2;
    }

    public static DJIUpgradeService.DJIUpP4Event getCurEvent() {
        return curEvent;
    }

    public static boolean isNewUpgradeSystem() {
        return curEvent == DJIUpgradeService.DJIUpP4Event.ConnectP4MC || curEvent == DJIUpgradeService.DJIUpP4Event.ConnectP4RC;
    }

    public static boolean isConnectRC() {
        return curEvent == DJIUpgradeService.DJIUpP4Event.Disconnect || curEvent == DJIUpgradeService.DJIUpP4Event.ConnectOther || curEvent == DJIUpgradeService.DJIUpP4Event.ConnectP4RC;
    }

    public static boolean isConnectMC() {
        return curEvent == DJIUpgradeService.DJIUpP4Event.ConnectP4MC;
    }

    public static boolean isSpecialMode() {
        return curEvent == DJIUpgradeService.DJIUpP4Event.ConnectP4MC && DJILinkDaemonService.getInstance().getLinkType() != DJILinkType.WIFI;
    }

    public static boolean isAllowRollBack(boolean isMainPage) {
        return isAllowRollBack;
    }

    public static boolean isProductUpFromMC(DataOsdGetPushCommon.DroneType type) {
        return type == DataOsdGetPushCommon.DroneType.P4 || type == DataOsdGetPushCommon.DroneType.Pomato || type == DataOsdGetPushCommon.DroneType.Orange2;
    }

    public static boolean isProductUpFromMC(boolean isMainPage) {
        DJIUpDeviceType device;
        if (isMainPage) {
            device = getMainPageStatus() == null ? null : getMainPageStatus().getProductId();
        } else {
            device = getRollBackStatus().getProductId();
        }
        return DJIUpDeviceType.wm330 == device || DJIUpDeviceType.wm331 == device || DJIUpDeviceType.wm332 == device || DJIUpDeviceType.wm620 == device || DJIUpDeviceType.pm410 == device;
    }

    public static boolean isProductUpFromMC() {
        DJIUpDeviceType device = connectDeviceType;
        return DJIUpDeviceType.wm330 == device || DJIUpDeviceType.wm331 == device || DJIUpDeviceType.rc001 == device || DJIUpDeviceType.rc003 == device || DJIUpDeviceType.wm332 == device;
    }

    public static boolean isProductUpOnlyMC() {
        DJIUpDeviceType device = getMainPageStatus() == null ? null : getMainPageStatus().getProductId();
        return DJIUpDeviceType.wm330 == device || DJIUpDeviceType.wm331 == device || DJIUpDeviceType.wm332 == device;
    }

    /* JADX WARNING: Removed duplicated region for block: B:5:0x0011  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isNeedUpgrade() {
        /*
            r0 = 0
            java.util.List<dji.dbox.upgrade.p4.model.DJIUpStatus> r2 = dji.dbox.upgrade.p4.utils.DJIUpStatusHelper.mainPageStatus
            if (r2 == 0) goto L_0x001d
            java.util.List<dji.dbox.upgrade.p4.model.DJIUpStatus> r2 = dji.dbox.upgrade.p4.utils.DJIUpStatusHelper.mainPageStatus
            java.util.Iterator r2 = r2.iterator()
        L_0x000b:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x001d
            java.lang.Object r1 = r2.next()
            dji.dbox.upgrade.p4.model.DJIUpStatus r1 = (dji.dbox.upgrade.p4.model.DJIUpStatus) r1
            boolean r0 = isNeedUpgrade(r1)
            if (r0 == 0) goto L_0x000b
        L_0x001d:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.dbox.upgrade.p4.utils.DJIUpStatusHelper.isNeedUpgrade():boolean");
    }

    public static boolean isNeedUpgrade(DJIUpStatus status) {
        return status != null && !isNeedShieldUpgrade() && status.isSupportUpgrade() && status.isNeedUpgrade();
    }

    public static boolean isNeedUpgradeRC() {
        return isNeedUpgradeRC && !isNeedShieldUpgrade();
    }

    public static boolean isNeedUpgradeMC() {
        return isNeedUpgradeMC && !isNeedShieldUpgrade();
    }

    public static void setIsNeedLock(boolean isNeedLock) {
        mIsNeedLock = isNeedLock;
    }

    public static void setIsNeedLockGetted(boolean isNeedLockGetted) {
        mIsNeedLockGetted = isNeedLockGetted;
    }

    public static boolean isNeedLock() {
        DJIUpConstants.LOGD("", " isNeedLock =" + mIsNeedLock);
        return mIsNeedLock;
    }

    public static boolean isNeedLockGetted() {
        DJIUpConstants.LOGD("", " isNeedLockGetted =" + mIsNeedLockGetted);
        return mIsNeedLockGetted;
    }

    public static String getFlycVersion() {
        return flycVersion;
    }

    public static void setFlycVersion(String flycVersion2) {
        flycVersion = flycVersion2;
    }

    public static String getRcVersion() {
        return rcVersion;
    }

    public static void setRcVersion(String rcVersion2) {
        rcVersion = rcVersion2;
    }

    public static void setMainPageStatus(DJIUpStatus... status) {
        DJIUpConstants.LOGE_WIFI(TAG, "固件升级状态设置");
        if (mainPageStatus == null) {
            mainPageStatus = new CopyOnWriteArrayList();
        } else {
            mainPageStatus.clear();
        }
        mainPageStatus.addAll(Arrays.asList(status));
    }

    public static DJIUpStatus getMainPageStatus() {
        DJIUpStatus status = getCurrentNeedUpStatus();
        if (status != null || mainPageStatus == null || mainPageStatus.size() <= 0) {
            return status;
        }
        return mainPageStatus.get(0);
    }

    private static DJIUpStatus getCurrentNeedUpStatus() {
        if (mainPageStatus == null) {
            return null;
        }
        for (DJIUpStatus status : mainPageStatus) {
            if (status.isNeedUpgrade()) {
                return status;
            }
        }
        return null;
    }

    public static boolean isHasNextOne() {
        return getCurrentNeedUpStatus() != null;
    }

    public static boolean isNeedUpTogether() {
        boolean isTogether = true;
        if (mainPageStatus == null || mainPageStatus.size() <= 1) {
            isTogether = false;
        }
        if (mainPageStatus == null) {
            return false;
        }
        for (DJIUpStatus status : mainPageStatus) {
            if (!status.isNeedUpgrade()) {
                return false;
            }
        }
        return isTogether;
    }

    public static int getUpStatusIndex() {
        if (getUpgradingStatus() == null || mainPageStatus == null) {
            return 0;
        }
        return mainPageStatus.indexOf(getUpgradingStatus());
    }

    public static int getFirmwareTotalSize() {
        int size = 0;
        if (!(mainPageStatus == null || mainPageStatus.size() == 0)) {
            for (DJIUpStatus status : mainPageStatus) {
                size += status.getCfgModel().totalSize;
            }
        }
        return size;
    }

    public static List<DJIUpStatus> getMainPageStatusTogether() {
        return mainPageStatus;
    }

    public static DJIUpStatus getUpgradingStatus() {
        return upgradingStatus == null ? getMainPageStatus() : upgradingStatus;
    }

    public static DJIUpDeviceType getUpgradingProductId() {
        if (getUpgradingStatus() == null) {
            return null;
        }
        return getUpgradingStatus().getProductId();
    }

    public static void setUpgradingStatus(boolean isMainPage) {
        if (isMainPage) {
            upgradingStatus = getCurrentNeedUpStatus();
        } else {
            upgradingStatus = rollBackStatus;
        }
    }

    public static void setUpgradingStatus(DJIUpStatus recoverUpStatus) {
        upgradingStatus = recoverUpStatus;
    }

    public static DJIUpStatus getRollBackStatus() {
        return rollBackStatus;
    }

    public static boolean isSameStatus() {
        return getMainPageStatus() == rollBackStatus;
    }

    public static void setRollBackStatus(DJIUpStatus rollBackStatus2) {
        rollBackStatus = rollBackStatus2;
    }

    public static boolean isUpProgressing() {
        return isUpProgressing;
    }

    public static void setIsUpProgressing(boolean isUpProgressing2) {
        setIsUpProgressing(isUpProgressing2, false);
    }

    public static void setIsUpProgressing(boolean isUpProgressing2, boolean isRecovered2) {
        isUpProgressing = isUpProgressing2;
        isRecovered = isRecovered2;
    }

    public static boolean isUpDownloading() {
        return isUpDownloading;
    }

    public static void setIsUpDownloading(boolean isUpDownloading2) {
        isUpDownloading = isUpDownloading2;
    }

    public static void resetStatus() {
        if (isOfflineMode() || DJIUpStatusOfflineHelper.isOfflineDevice()) {
            DJIUpConstants.LOGE_WIFI(TAG, "离线产品，跳过状态reset");
            return;
        }
        setCurEvent(DJIUpgradeService.DJIUpP4Event.Disconnect);
        if (!isNeedUpgrade() || !isOfflineMode()) {
            DJIUpConstants.LOGE_WIFI(TAG, "固件升级状态reset");
            setFlycVersion("");
            setRcVersion("");
            setIsNeedUpgradeMC(false);
            setIsNeedUpgradeRC(false);
            setIsNeedLock(false);
            setIsNeedLockGetted(false);
            if (rollBackStatus != null) {
                rollBackStatus.reset();
            }
            if (upgradingStatus != null) {
                upgradingStatus.reset();
            }
            if (mainPageStatus != null) {
                for (DJIUpStatus status : mainPageStatus) {
                    status.reset();
                }
            }
            setIsChecking(true, "DJIUpStatusHelper resetStatus");
            isCheckingSetted = false;
            return;
        }
        DJIUpConstants.LOGE_WIFI(TAG, "需要升级且是离线产品，跳过状态reset");
    }

    public static void setIsAllowRollBack(boolean isAllowRollBack2) {
        isAllowRollBack = isAllowRollBack2;
    }

    public static void setIsNeedUpgradeRC(boolean isNeedUpgradeRC2) {
        isNeedUpgradeRC = isNeedUpgradeRC2;
    }

    public static void setIsNeedUpgradeMC(boolean isNeedUpgradeMC2) {
        isNeedUpgradeMC = isNeedUpgradeMC2;
    }

    public static boolean isAllowRedo() {
        return allowRedo;
    }

    public static boolean isRecovered() {
        return isRecovered;
    }

    public static void setIsChecking(boolean isChecking2, String where) {
        DJIUpConstants.LOGD(TAG, "setIsChecking isChecking=" + isChecking2 + " where:" + where);
        isChecking = isChecking2;
        isCheckingSetted = true;
    }

    public static boolean isChecking() {
        return isChecking;
    }

    public static boolean isCheckingSetted() {
        return isCheckingSetted;
    }

    public static boolean isAlreadyChecked() {
        return (curEvent == null || curEvent == DJIUpgradeService.DJIUpP4Event.Disconnect || isChecking()) ? false : true;
    }

    public static void setGlassVersion(String glassVersion2) {
        glassVersion = glassVersion2;
    }

    public static String getGlassVersion() {
        return glassVersion;
    }

    public static boolean isGlassConnected() {
        return DataGlassGetPushCheckStatus.getInstance().isGetted();
    }

    public static void setConnectDeviceType(DJIUpDeviceType device1) {
        connectDeviceType = device1;
    }

    public static DJIUpDeviceType getConnectDeviceType() {
        return connectDeviceType;
    }

    public static boolean isPackageDownloaded(Context context, DJIUpStatus status) {
        if (status != null) {
            return isPackageDownloaded(context, status.getProductId(), status.getLatestElement());
        }
        DJIUpConstants.LOGD(TAG, "check firmware -> No exists!!! status is null");
        return false;
    }

    public static boolean isPackageDownloaded(Context context, DJIUpDeviceType deviceType, DJIUpListElement element) {
        DJIUpConstants.LOGD(TAG, "check firmware -> element=" + element + " product=" + deviceType);
        if (element == null) {
            return false;
        }
        if (element.isFromSDCard) {
            return true;
        }
        DJIUpCfgModel cfgModel = element.cfgModel;
        if (deviceType == null || cfgModel == null || cfgModel.modules == null || cfgModel.modules.size() == 0) {
            DJIUpConstants.LOGD(TAG, "check firmware -> No exists!!! cfgModel=" + cfgModel);
            return false;
        } else if (!DJIUpTarManager.isTarExsit() || !isOfflineMode()) {
            Iterator<DJIUpCfgModel.DJIUpModule> it2 = cfgModel.modules.iterator();
            while (it2.hasNext()) {
                File f = new File(DJIUpServerManager.getcfgImageTarget(context, deviceType.toString(), cfgModel.releaseVersion, it2.next().name));
                if (!f.exists()) {
                    DJIUpConstants.LOGD(TAG, "check firmware -> No exists!!! " + f.getName());
                    return false;
                }
            }
            DJIUpConstants.LOGD(TAG, "check firmware -> Exists all files!!!");
            return true;
        } else {
            DJIUpConstants.LOGD(TAG, "check firmware -> Exists tar!!!");
            return true;
        }
    }

    public static boolean isPackageRollbackDownloaded(Context context, DJIUpListElement... elements) {
        for (DJIUpListElement element : elements) {
            if (element == null || !isPackageRollbackDownloaded(context, element)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isPackageRollbackDownloaded(Context context, DJIUpListElement element) {
        return element != null && isPackageRollbackDownloaded(context, element.cfgModel);
    }

    public static boolean isPackageRollbackDownloaded(Context context, DJIUpCfgModel cfgModel) {
        if (getUpgradingStatus() == null) {
            DJIUpConstants.LOGD(TAG, "check rollback firmware -> No exists!!! status is null");
            return false;
        }
        DJIUpDeviceType deviceType = getRollBackStatus().getProductId();
        DJIUpConstants.LOGD(TAG, "check firmware rollback -> product=" + deviceType);
        if (deviceType == null || cfgModel == null || cfgModel.modules == null || cfgModel.modules.size() == 0) {
            DJIUpConstants.LOGD(TAG, "check rollback firmware -> No exists!!! cfgModel=" + cfgModel);
            return false;
        } else if (!DJIUpTarManager.isRollbackTarExsit() || !isOfflineMode()) {
            Iterator<DJIUpCfgModel.DJIUpModule> it2 = cfgModel.modules.iterator();
            while (it2.hasNext()) {
                File f = new File(DJIUpServerManager.getcfgImageTarget(context, deviceType.toString(), cfgModel.releaseVersion, it2.next().name));
                if (!f.exists()) {
                    DJIUpConstants.LOGD(TAG, "check firmware -> No exists!!! " + f.getName());
                    return false;
                }
            }
            DJIUpConstants.LOGD(TAG, "check rollback firmware -> Exists all files!!!");
            return true;
        } else {
            DJIUpConstants.LOGD(TAG, "check rollback firmware -> Exists tar!!!");
            return true;
        }
    }

    public static boolean isOfflineMode() {
        return getUpgradingStatus() != null && getUpgradingStatus().getProductId().isOffline();
    }

    public static boolean isUnderSyncUpgradeContext() {
        return isUnderSyncUpgradeContext;
    }

    public static void setUnderSyncUpgradeContext(boolean isUnderSyncUpgradeContext2) {
        isUnderSyncUpgradeContext = isUnderSyncUpgradeContext2;
    }

    public static boolean isIgnoreIsChecking() {
        DataOsdGetPushCommon.DroneType droneType = DataOsdGetPushCommon.getInstance().getDroneType();
        ProductType rcType = DJIRcDetectHelper.getRcType(DJIRcDetectHelper.getInstance().getOsdGetter());
        DJIUpConstants.LOGD(TAG, "trigger upgrade check droneType=" + droneType + " rcType=" + rcType);
        if (droneType == DataOsdGetPushCommon.DroneType.WM240 || rcType == ProductType.WM240) {
            return true;
        }
        return false;
    }

    public static boolean isNeedShieldUpgrade() {
        boolean isNeedShield = false;
        ProductType type = DJIProductManager.getInstance().getType();
        DJIComponentManager.PlatformType platformType = DJIComponentManager.getInstance().getPlatformType();
        DJIComponentManager.RcComponentType rcPlatformType = DJIComponentManager.getInstance().getRcComponentType();
        if (platformType == DJIComponentManager.PlatformType.P4RTK || rcPlatformType == DJIComponentManager.RcComponentType.P4RTK || DJIUpDeviceType.isP4RSeries(getConnectDeviceType())) {
            isNeedShield = true;
        }
        if (type == ProductType.WM230 && DJIUSBWifiSwitchManager.getInstance().isProductWifiConnected(null)) {
            isNeedShield = true;
        }
        if (type == ProductType.KumquatX && DJIUSBWifiSwitchManager.getInstance().isProductWifiConnected(null)) {
            isNeedShield = true;
        }
        if (rcPlatformType != DJIComponentManager.RcComponentType.RM500 || ServiceManager.getInstance().isRemoteOK()) {
            return isNeedShield;
        }
        return true;
    }

    public static boolean isUpgradeFromSDCard() {
        DJIUpStatus status = getUpgradingStatus();
        if (status == null || status.getLatestElement() == null) {
            return false;
        }
        return status.getLatestElement().isFromSDCard;
    }

    public static boolean isNeedAppPushUpgradeStatus() {
        DJIUpStatus status = getUpgradingStatus();
        DJIUpDeviceType deviceType = null;
        if (status != null) {
            deviceType = status.getProductId();
        }
        return deviceType == DJIUpDeviceType.wm240;
    }
}
