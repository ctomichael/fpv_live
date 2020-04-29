package dji.publics.DJIObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@EXClassNullAway
public final class DJICrashExecutor {
    private static final String TAG = "DJICrashExecutor";
    private static final LinkedList<Activity> mRunningActivities = new LinkedList<>();

    public static void doAll(@Nullable Context appCxt) {
        finishAllActivities();
    }

    public static void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mRunningActivities.add(0, activity);
    }

    public static void onActivityDestroyed(Activity activity) {
        mRunningActivities.remove(activity);
    }

    public static void finishAllActivities() {
        Iterator<Activity> it2 = mRunningActivities.iterator();
        while (it2.hasNext()) {
            Activity activity = it2.next();
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        mRunningActivities.clear();
    }

    public static void killMySelf(@Nullable Context context) {
        if (context != null) {
            finishAllActivities();
            ActivityManager manager = (ActivityManager) context.getApplicationContext().getSystemService("activity");
            List<ActivityManager.RunningAppProcessInfo> activityes = manager.getRunningAppProcesses();
            DJILog.logWriteE(TAG, "killMySelf: packagename = " + context.getPackageName() + "\n" + Log.getStackTraceString(new Throwable()), new Object[0]);
            int iCnt = 0;
            while (activityes != null && iCnt < activityes.size()) {
                DJILog.logWriteE(TAG, "killMySelf: " + iCnt + " " + activityes.get(iCnt).processName, new Object[0]);
                if (activityes.get(iCnt).processName.contains(context.getPackageName() + ":")) {
                    manager.killBackgroundProcesses(activityes.get(iCnt).processName);
                    Process.killProcess(activityes.get(iCnt).pid);
                    DJILog.logWriteE(TAG, "killMySelf " + activityes.get(iCnt).processName, new Object[0]);
                }
                iCnt++;
            }
            int iCnt2 = 0;
            while (activityes != null && iCnt2 < activityes.size()) {
                if (activityes.get(iCnt2).processName.equals(context.getPackageName())) {
                    manager.killBackgroundProcesses(activityes.get(iCnt2).processName);
                    Process.killProcess(activityes.get(iCnt2).pid);
                    DJILog.logWriteE(TAG, "killMySelf " + context.getPackageName(), new Object[0]);
                }
                iCnt2++;
            }
        }
    }
}
