package com.dji.mapkit.lbs.helper;

import android.support.annotation.Nullable;

public final class StringUtils {
    private StringUtils() {
    }

    public static boolean isNotEmpty(@Nullable CharSequence str) {
        return str != null && str.length() > 0;
    }
}
