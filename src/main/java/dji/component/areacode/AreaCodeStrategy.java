package dji.component.areacode;

import android.text.TextUtils;

public enum AreaCodeStrategy {
    DATA_CHANGE,
    DRONE_GPS,
    PHONE_GPS,
    MCC,
    IP,
    NEAR_CITY,
    NULL_VALUE;
    
    public String areaCode = "";

    public static AreaCodeStrategy getBest() {
        AreaCodeStrategy[] values = values();
        for (AreaCodeStrategy strategy : values) {
            if (!TextUtils.isEmpty(strategy.areaCode)) {
                return strategy;
            }
        }
        return NULL_VALUE;
    }
}
