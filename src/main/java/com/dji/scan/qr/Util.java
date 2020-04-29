package com.dji.scan.qr;

import android.os.Looper;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class Util {
    public static void validateMainThread() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new IllegalStateException("Must be called from the main thread.");
        }
    }
}
