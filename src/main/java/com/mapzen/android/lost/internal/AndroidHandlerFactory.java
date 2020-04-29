package com.mapzen.android.lost.internal;

import android.os.Handler;
import android.os.Looper;

class AndroidHandlerFactory implements HandlerFactory {
    AndroidHandlerFactory() {
    }

    public void run(Looper looper, Runnable runnable) {
        new Handler(looper).post(runnable);
    }
}
