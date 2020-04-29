package dji.midware.util;

import android.content.Context;
import android.os.Process;
import android.support.annotation.Keep;
import dji.log.DJILog;
import dji.midware.data.manager.Dpad.DpadProductManager;

@Keep
public class Affinity {
    private static final String TAG = "Affinity";

    private static native void bindToCpu(String str);

    public static void bindCurrentThreadToCpu(Context context) {
        if (!DpadProductManager.getInstance().isRM500()) {
            DJILog.e(TAG, "Not RM500 !", new Object[0]);
            return;
        }
        DJILog.e(TAG, "Current Thread: %s, tid = %d", Thread.currentThread().getName(), Integer.valueOf(Process.myTid()));
        bindThreadToCpu(context, Thread.currentThread());
    }

    public static void bindThreadToCpu(Context context, Thread thread) {
        if (!DpadProductManager.getInstance().isRM500()) {
            DJILog.e(TAG, "Not RM500 !", new Object[0]);
            return;
        }
        try {
            DJILog.e(TAG, "try to load DjiAffinity", new Object[0]);
            System.loadLibrary("DjiAffinity");
        } catch (UnsatisfiedLinkError e) {
            DJILog.e(TAG, "Couldn't load lib", new Object[0]);
        }
        DJILog.e(TAG, "Current Thread: %s, tid = %d", thread.getName(), Long.valueOf(thread.getId()));
        String name = thread.getName();
        if (name.equals("main")) {
            String name2 = context.getPackageName();
            name = name2.substring(name2.length() - 15, name2.length());
        }
        bindToCpu(name.substring(0, 15));
    }
}
