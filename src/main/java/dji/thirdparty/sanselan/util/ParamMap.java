package dji.thirdparty.sanselan.util;

import java.util.Map;

public class ParamMap {
    public static boolean getParamBoolean(Map params, Object key, boolean default_value) {
        boolean result = default_value;
        Object o = params == null ? null : params.get(key);
        if (o == null || !(o instanceof Boolean)) {
            return result;
        }
        return ((Boolean) o).booleanValue();
    }
}
