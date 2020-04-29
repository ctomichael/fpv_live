package dji.pilot.publics.util;

import dji.common.remotecontroller.RCMode;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.component.DJIComponentManager;
import dji.sdksharedlib.extension.CacheHelper;

@EXClassNullAway
public class RcModeHelper {
    public static boolean isSlaveRc(RCMode mode) {
        if (mode == null) {
            RCMode modeParam = (RCMode) CacheHelper.getRemoteController("Mode");
            if (modeParam == null) {
                mode = RCMode.MASTER;
            } else {
                mode = modeParam;
            }
        }
        return mode == RCMode.SLAVE || mode == RCMode.SLAVE_SUB;
    }

    public static boolean isSubRc(RCMode mode) {
        if (mode == null) {
            RCMode modeParam = (RCMode) CacheHelper.getRemoteController("Mode");
            if (modeParam == null) {
                mode = RCMode.MASTER;
            } else {
                mode = modeParam;
            }
        }
        return mode == RCMode.MASTER_SUB || mode == RCMode.SLAVE_SUB;
    }

    public static boolean isRcNormal() {
        RCMode modeParam = (RCMode) CacheHelper.getRemoteController("Mode");
        RCMode mode = RCMode.NORMAL;
        if (modeParam != null) {
            mode = modeParam;
        }
        return mode == RCMode.NORMAL;
    }

    public static boolean isRcMaster() {
        RCMode modeParam = (RCMode) CacheHelper.getRemoteController("Mode");
        RCMode mode = RCMode.NORMAL;
        if (modeParam != null) {
            mode = modeParam;
        }
        return mode == RCMode.MASTER;
    }

    public static boolean isRcSlave() {
        RCMode modeParam = (RCMode) CacheHelper.getRemoteController("Mode");
        RCMode mode = RCMode.NORMAL;
        if (modeParam != null) {
            mode = modeParam;
        }
        return mode == RCMode.SLAVE;
    }

    public static boolean isRcSlaveWithIN2() {
        return isRcSlave() && DJIComponentManager.getInstance().getPlatformType() == DJIComponentManager.PlatformType.Inspire2;
    }

    public static boolean isSubRcMaster() {
        RCMode modeParam = (RCMode) CacheHelper.getRemoteController("Mode");
        RCMode mode = RCMode.NORMAL;
        if (modeParam != null) {
            mode = modeParam;
        }
        return mode == RCMode.MASTER_SUB;
    }

    public static boolean isSubRcSlave() {
        RCMode modeParam = (RCMode) CacheHelper.getRemoteController("Mode");
        RCMode mode = RCMode.NORMAL;
        if (modeParam != null) {
            mode = modeParam;
        }
        return mode == RCMode.SLAVE_SUB;
    }
}
