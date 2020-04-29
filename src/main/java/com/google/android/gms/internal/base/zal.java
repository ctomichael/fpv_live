package com.google.android.gms.internal.base;

import android.os.Handler;
import android.os.Looper;

public class zal extends Handler {
    private static volatile zam zaro = null;

    public zal() {
    }

    public zal(Looper looper) {
        super(looper);
    }

    public zal(Looper looper, Handler.Callback callback) {
        super(looper, callback);
    }
}
