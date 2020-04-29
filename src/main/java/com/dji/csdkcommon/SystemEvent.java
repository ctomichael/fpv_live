package com.dji.csdkcommon;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class SystemEvent {
    public static final int AccountChanged = 2;
    public static final int CountryCodeChanged = 3;
    public static final int NetworkChanged = 0;
    public static final int PhoneLocationChanged = 1;

    @Retention(RetentionPolicy.CLASS)
    public @interface SystemEventParam {
    }
}
