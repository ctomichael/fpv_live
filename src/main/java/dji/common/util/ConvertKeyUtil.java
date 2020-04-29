package dji.common.util;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.params.P3.ParamCfgName;
import java.util.HashMap;

@EXClassNullAway
public class ConvertKeyUtil {
    public static HashMap<String, String> flightControllerConvertMap = new HashMap<String, String>() {
        /* class dji.common.util.ConvertKeyUtil.AnonymousClass1 */

        {
            put("DistanceLimitation", ParamCfgName.GCONFIG_FLY_LIMIT);
            put("HeightLimitation", ParamCfgName.GCONFIG_LIMIT_HEIGHT);
        }
    };
    public static HashMap<String, String> gimbalConvertMap = new HashMap<>();
}
