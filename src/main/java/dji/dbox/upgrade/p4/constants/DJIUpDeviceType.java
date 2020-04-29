package dji.dbox.upgrade.p4.constants;

import dji.dbox.upgrade.p4.utils.DJIUpStatusHelper;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.transfer.base.FileTransferStrategies;
import dji.midware.upgradeComponent.DJIUpgradeProductID;

@EXClassNullAway
public enum DJIUpDeviceType {
    gl811("gl811", false),
    wm220(DJIUpgradeProductID.WM220, true),
    wm221(DJIUpgradeProductID.WM221, true),
    wm330(DJIUpgradeProductID.WM330, true),
    wm331(DJIUpgradeProductID.WM331, true),
    wm332(DJIUpgradeProductID.WM332, true),
    ag410("ag410", true),
    wm334(DJIUpgradeProductID.WM334Prefix, true),
    gl300k(DJIUpgradeProductID.P4PSDRRCScreen, true),
    wm335(DJIUpgradeProductID.P4PSDRPrefix, true),
    wm620(DJIUpgradeProductID.WM620, true),
    pm410(DJIUpgradeProductID.PM410, true),
    rc001(DJIUpgradeProductID.RC001, false),
    de01(DJIUpgradeProductID.RC001, false),
    rc002(DJIUpgradeProductID.RC002, false, true),
    rc003(DJIUpgradeProductID.RC003, false),
    rm500("rm500", false, false, 1),
    wm100ac("wm100", true, true),
    wm230(DJIUpgradeProductID.WM230AC, true, false, 1),
    rc230(DJIUpgradeProductID.RC230, false),
    wm240("wm240", true, false, 1),
    rc240("rc240", false, false, 1),
    rc010("rc010", false, false, false, 0, ".cfg.sig"),
    unknow("unknow", false);
    
    public static final String MODE_CFG_SUFFIX = ".cfg.sig";
    public static final String MODE_PARSER_SUFFIX = "_verify.xml";
    public static final String MODE_TLV_SUFFIX = ".bin.sig";
    private static final int UPGRADE_MODE_MULTI_FILES = 1;
    private static final int UPGRADE_MODE_TAR = 0;
    private final String cfgSuffix;
    private final boolean isMc;
    private final boolean isNeedVerify;
    private final boolean isOffline;
    private final String product_id;
    private final int upgradeMode;

    private DJIUpDeviceType(String product_id2, boolean isMc2) {
        this(r7, r8, product_id2, isMc2, false);
    }

    private DJIUpDeviceType(String product_id2, boolean isMc2, boolean isOffline2) {
        this(r10, r11, product_id2, isMc2, isOffline2, true, 0, ".cfg.sig");
    }

    private DJIUpDeviceType(String product_id2, boolean isMc2, boolean isNeedVerify2, int upgradeMode2) {
        this(r10, r11, product_id2, isMc2, false, isNeedVerify2, upgradeMode2, ".cfg.sig");
    }

    private DJIUpDeviceType(String product_id2, boolean isMc2, boolean isOffline2, boolean isNeedVerify2, int upgradeMode2, String cfgSuffix2) {
        this.product_id = product_id2;
        this.isMc = isMc2;
        this.isOffline = isOffline2;
        this.isNeedVerify = isNeedVerify2;
        this.upgradeMode = upgradeMode2;
        this.cfgSuffix = cfgSuffix2;
    }

    public String getProductId() {
        return this.product_id;
    }

    public boolean isOffline() {
        return this.isOffline;
    }

    public boolean isMc() {
        return this.isMc;
    }

    public boolean isNeedVerify() {
        return this.isNeedVerify;
    }

    public boolean isUseMultiFile() {
        return this.upgradeMode == 1;
    }

    public boolean isUseTar() {
        return this.upgradeMode == 0;
    }

    public boolean isTlvMode() {
        return MODE_TLV_SUFFIX.equals(this.cfgSuffix);
    }

    public String getCfgSuffix() {
        return this.cfgSuffix;
    }

    public String toString() {
        return this.product_id;
    }

    public static boolean isNeedRequestReverse(DJIUpDeviceType deviceType) {
        return deviceType == wm240;
    }

    public static FileTransferStrategies getStrategy(DJIUpDeviceType deviceType) {
        switch (deviceType) {
            case wm230:
                return FileTransferStrategies.APP_AOA_RC_WIFI_UAV;
            case wm240:
                if (DJIUpStatusHelper.getConnectDeviceType() == rm500) {
                    return FileTransferStrategies.APP_TCP_3399_TCP_RC_SDR_UAV;
                }
                return FileTransferStrategies.APP_AOA_RC_SDR_UAV;
            case rc240:
                return FileTransferStrategies.APP_AOA_RC;
            case rm500:
                return FileTransferStrategies.APP_TCP_3399_TCP_RC;
            default:
                return FileTransferStrategies.APP_AOA_RC_WIFI_UAV;
        }
    }

    public static boolean isMcUpBefore(DJIUpDeviceType deviceType) {
        return deviceType == rc002 || deviceType == rc230 || deviceType == rc240 || deviceType == rc010 || deviceType == rm500;
    }

    public static boolean isUpTogether(DJIUpDeviceType deviceType) {
        return deviceType == wm100ac || deviceType == rc002 || deviceType == wm240 || deviceType == rc240 || deviceType == rc010;
    }

    public static boolean isSupportUpAutoRepeat(DJIUpDeviceType deviceType) {
        if (!DJIUpConstants.isBuildConfigForInner && !DJIUpConstants.isBuildConfigForFactory && !DJIUpConstants.isBuildConfigForDebug) {
            return false;
        }
        if (deviceType == rm500 || deviceType == wm240 || deviceType == rc240 || deviceType == rc010 || deviceType == wm230 || deviceType == rc230) {
            return true;
        }
        return false;
    }

    public static boolean isSupportCompareOffline(DJIUpDeviceType deviceType) {
        if (deviceType == null) {
            return false;
        }
        if (deviceType.isOffline() || deviceType == rc240 || deviceType == rc010 || deviceType == wm240) {
            return true;
        }
        return false;
    }

    public static boolean is1860Rc(DJIUpDeviceType deviceType) {
        return deviceType == wm220 || deviceType == wm221;
    }

    public static boolean isSepRc(DJIUpDeviceType deviceType) {
        return deviceType == rc001 || deviceType == rc002 || deviceType == rc003 || deviceType == rc230 || deviceType == rc240 || deviceType == rc010 || deviceType == rm500;
    }

    public static boolean isRcSeries(DJIUpDeviceType deviceType) {
        return deviceType == rc001 || deviceType == rc003;
    }

    public static boolean isWM220Series(DJIUpDeviceType deviceType) {
        return deviceType == wm220 || deviceType == wm221;
    }

    public static boolean isGlassSeries(DJIUpDeviceType deviceType) {
        return deviceType == gl811;
    }

    public static boolean isP4RSeries(DJIUpDeviceType deviceType) {
        return deviceType == wm334 || deviceType == ag410;
    }

    public static DJIUpDeviceType find(String value) {
        DJIUpDeviceType[] values = values();
        for (DJIUpDeviceType type : values) {
            if (type.getProductId().equals(value)) {
                return type;
            }
        }
        return unknow;
    }
}
