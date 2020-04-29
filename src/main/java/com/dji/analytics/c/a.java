package com.dji.analytics.c;

import android.util.Log;

/* compiled from: AndroidLogDelegate */
public final class a implements b {
    public int a(String str, String str2) {
        return Log.d(str, str2);
    }

    public int b(String str, String str2) {
        return Log.e(str, str2);
    }
}
