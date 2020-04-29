package dji.pilot2.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Debug;
import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import java.util.List;

@Keep
@EXClassNullAway
public class PerformanceUtils {
    public static String getAllThreadInfo() {
        ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
        int noThreads = currentGroup.activeCount();
        Thread[] lstThreads = new Thread[noThreads];
        currentGroup.enumerate(lstThreads);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("============Start dump thread info============");
        stringBuilder.append("\ntotal =").append(lstThreads.length);
        for (int i = 0; i < noThreads; i++) {
            stringBuilder.append("\n线程号：").append(lstThreads[i].getId()).append(" = ").append(lstThreads[i].getName());
        }
        stringBuilder.append("\n============end of dump ==========\n\n");
        return stringBuilder.toString();
    }

    public static String getMemory(Context context) {
        if (context == null) {
            return "context is null";
        }
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        if (activityManager == null) {
            return "am is null";
        }
        Debug.MemoryInfo[] memoryInfo = activityManager.getProcessMemoryInfo(new int[]{getPID(context, activityManager)});
        if (memoryInfo == null || memoryInfo.length < 1) {
            return "info is null";
        }
        if (Build.VERSION.SDK_INT >= 23) {
            return "pid: " + getPID(context, activityManager) + " " + DJILog.formatObject(memoryInfo[0].getMemoryStats());
        }
        int memSize0 = memoryInfo[0].dalvikPrivateDirty;
        int memSize1 = memoryInfo[0].nativePrivateDirty;
        int memSize2 = memoryInfo[0].otherPrivateDirty;
        return "pid: " + getPID(context, activityManager) + " memorySize is -->" + memSize0 + "kb " + memSize1 + "kb " + memSize2 + "kb " + (memSize0 + memSize1 + memSize2) + "kb";
    }

    private static int getPID(Context context, ActivityManager mActivityManager) {
        List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager.getRunningAppProcesses();
        if (appProcessList == null) {
            return -1;
        }
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessList) {
            int i = appProcessInfo.pid;
            if (appProcessInfo.processName.contains(context.getPackageName())) {
                return i;
            }
        }
        return -1;
    }
}
