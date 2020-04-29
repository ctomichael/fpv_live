package dji.pilot.publics.util;

public class CapabilityUtil {
    public static boolean isQuickShotNeedPrepare() {
        return DJICommonUtils.isWM240();
    }
}
