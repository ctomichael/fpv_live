package org.bouncycastle.crypto.tls;

import com.billy.cc.core.component.CCUtil;

public class AlertLevel {
    public static final short fatal = 2;
    public static final short warning = 1;

    public static String getName(short s) {
        switch (s) {
            case 1:
                return "warning";
            case 2:
                return "fatal";
            default:
                return CCUtil.PROCESS_UNKNOWN;
        }
    }

    public static String getText(short s) {
        return getName(s) + "(" + ((int) s) + ")";
    }
}
