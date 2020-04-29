package dji.dbox.upgrade.p4.constants;

import com.dji.api.FlightHttpApi;
import com.dji.frame.util.V_DiskUtil;
import dji.component.accountcenter.IAccountCenterService;
import dji.component.accountcenter.IMemberProtocol;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.log.DJILogPaths;
import dji.service.DJIAppServiceManager;

public class DJIUpConstants {
    public static final String DATA_UP_LOG_PREFIX = "dataug-";
    public static final String FIRM_UP_LOG_PREFIX = "firmUpgrade-";
    public static final String LOG_of_1860_DIR = (V_DiskUtil.SDCardRoot + "/DJI/1860log/");
    public static final String UP_TEMP_FILE = "-djitemp";
    public static final String api_release_note = FlightHttpApi.getFirmwareReleaseNoteUrl();
    public static final String api_upgrade_log = FlightHttpApi.getFirmwareUpgradeLogUrl();
    public static String appVersion = "";
    public static final String directory = "/upCfgFiles/";
    public static boolean isBuildConfigForDebug;
    public static boolean isBuildConfigForFactory;
    public static boolean isBuildConfigForInner;
    private static final boolean isDebug = false;
    public static String mCountryCodeMem = "";
    public static final Object mSyncCountryCodeMem = new Object();

    public static String getUserToken() {
        try {
            return getService().getToken();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getUserAccount() {
        try {
            return getService().getEmail();
        } catch (Exception e) {
            return "";
        }
    }

    private static IAccountCenterService getService() {
        return (IAccountCenterService) DJIAppServiceManager.getInstance().getService(IAccountCenterService.NAME, IAccountCenterService.class);
    }

    public static void setBuildConfigValues(boolean isBuildConfigForInner2, boolean isBuildConfigForFactory2, boolean isBuildConfigForDebug2) {
        isBuildConfigForInner = isBuildConfigForInner2;
        isBuildConfigForFactory = isBuildConfigForFactory2;
        isBuildConfigForDebug = isBuildConfigForDebug2;
    }

    public static String getFileName() {
        StringBuilder fileName = new StringBuilder(50);
        fileName.append(DJILogHelper.getInstance().getLogParentDir()).append(DJILogPaths.LOG_UP_NEW_ALL).append(IMemberProtocol.PARAM_SEPERATOR).append(DJILogHelper.getInstance().getLogName());
        return fileName.toString();
    }

    public static void LOGE(String TAG, String s) {
        DJILog.logWriteD(FIRM_UP_LOG_PREFIX + TAG, s, DJILogPaths.LOG_UP_NEW_ALL, new Object[0]);
    }

    public static void LOGE_DATA(String TAG, String s) {
        DJILog.logWriteE(DATA_UP_LOG_PREFIX + TAG, s, DJILogPaths.LOG_UP_DATA_ALL, new Object[0]);
    }

    public static void LOGD_WIFI(String TAG, String s) {
        DJILog.logWriteD(FIRM_UP_LOG_PREFIX + TAG, s, DJILogPaths.LOG_UP_WIFI_PR, new Object[0]);
    }

    public static void LOGE_WIFI(String TAG, String s) {
        DJILog.logWriteE(FIRM_UP_LOG_PREFIX + TAG, s, DJILogPaths.LOG_UP_WIFI_PR, new Object[0]);
    }

    public static void LOGD(String TAG, String s) {
        DJILog.logWriteD(FIRM_UP_LOG_PREFIX + TAG, s, DJILogPaths.LOG_UP_NEW_ALL, new Object[0]);
    }

    public static void print(String TAG) {
        StringBuilder info = new StringBuilder();
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        for (StackTraceElement aStack : stack) {
            info.append("\tat ");
            info.append(aStack.toString());
            info.append("\n");
        }
        LOGD(TAG, info.toString());
    }

    public static boolean isLogin() {
        if (getService() == null) {
            return false;
        }
        return getService().isLogin();
    }

    public static void ELogUp(String s) {
    }
}
