package dji.dbox.upgrade.p4.utils;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataGlassGetPushCheckStatus;
import dji.midware.data.model.P3.DataGlassPushParam;

@EXClassNullAway
public class DJIUpGlassUtil {
    public static boolean isGlassConnected() {
        return DataGlassGetPushCheckStatus.getInstance().isGetted();
    }

    public static DataGlassPushParam.GlassType getGlassType() {
        return DataGlassPushParam.getInstance().getGlassType();
    }

    public static boolean isUpgradeSame() {
        return isGlassConnected() && getGlassType() != DataGlassPushParam.GlassType.ZV810Adv;
    }

    public static boolean isUpgradeOwn() {
        return isGlassConnected() && getGlassType() == DataGlassPushParam.GlassType.ZV810Adv;
    }
}
