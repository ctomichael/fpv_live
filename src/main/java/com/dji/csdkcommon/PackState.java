package com.dji.csdkcommon;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class PackState {
    public static final int Cancel = 2;
    public static final int Success = 0;
    public static final int Timeout = 1;

    @Retention(RetentionPolicy.CLASS)
    public @interface PackStateParam {
    }
}
