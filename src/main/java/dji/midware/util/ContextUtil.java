package dji.midware.util;

import android.app.ActivityManager;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;

@EXClassNullAway
public class ContextUtil {
    private static Context CONTEXT_INSTANCE = null;
    public static final String TAG = "ContextUtil";

    public static synchronized Context getContext() {
        Context context;
        synchronized (ContextUtil.class) {
            if (CONTEXT_INSTANCE == null) {
                try {
                    Class<?> ActivityThread = Class.forName("android.app.ActivityThread");
                    Object currentActivityThread = ActivityThread.getMethod("currentActivityThread", new Class[0]).invoke(ActivityThread, new Object[0]);
                    CONTEXT_INSTANCE = (Context) currentActivityThread.getClass().getMethod("getApplication", new Class[0]).invoke(currentActivityThread, new Object[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            context = CONTEXT_INSTANCE;
        }
        return context;
    }

    public static IBinder getService(String serviceName) {
        try {
            return (IBinder) Class.forName("android.os.ServiceManager").getMethod("getService", String.class).invoke(null, serviceName);
        } catch (Exception e) {
            DJILog.e(TAG, DJILog.exceptionToString(e), new Object[0]);
            return null;
        }
    }

    public static String getWifiIP() {
        WifiManager wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService("wifi");
        if (wifiManager.isWifiEnabled()) {
            return intToIp(wifiManager.getConnectionInfo().getIpAddress());
        }
        return null;
    }

    private static String intToIp(int i) {
        return (i & 255) + "." + ((i >> 8) & 255) + "." + ((i >> 16) & 255) + "." + ((i >> 24) & 255);
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x003c A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0056 A[SYNTHETIC, Splitter:B:24:0x0056] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x005f A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isAppBroughtToBackground() {
        /*
            r3 = 0
            r0 = 0
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0044 }
            java.io.FileReader r5 = new java.io.FileReader     // Catch:{ Exception -> 0x0044 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0044 }
            r6.<init>()     // Catch:{ Exception -> 0x0044 }
            java.lang.String r7 = "/proc/"
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ Exception -> 0x0044 }
            int r7 = getPID()     // Catch:{ Exception -> 0x0044 }
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ Exception -> 0x0044 }
            java.lang.String r7 = "/oom_adj"
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ Exception -> 0x0044 }
            java.lang.String r6 = r6.toString()     // Catch:{ Exception -> 0x0044 }
            r5.<init>(r6)     // Catch:{ Exception -> 0x0044 }
            r1.<init>(r5)     // Catch:{ Exception -> 0x0044 }
            r4 = 0
            java.lang.String r4 = r1.readLine()     // Catch:{ Exception -> 0x0064, all -> 0x0061 }
            int r3 = java.lang.Integer.parseInt(r4)     // Catch:{ Exception -> 0x0064, all -> 0x0061 }
            if (r1 == 0) goto L_0x0067
            r1.close()     // Catch:{ IOException -> 0x003e }
            r0 = r1
        L_0x003a:
            if (r3 <= 0) goto L_0x005f
            r5 = 1
        L_0x003d:
            return r5
        L_0x003e:
            r2 = move-exception
            r2.printStackTrace()
            r0 = r1
            goto L_0x003a
        L_0x0044:
            r2 = move-exception
        L_0x0045:
            r2.printStackTrace()     // Catch:{ all -> 0x0053 }
            if (r0 == 0) goto L_0x003a
            r0.close()     // Catch:{ IOException -> 0x004e }
            goto L_0x003a
        L_0x004e:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x003a
        L_0x0053:
            r5 = move-exception
        L_0x0054:
            if (r0 == 0) goto L_0x0059
            r0.close()     // Catch:{ IOException -> 0x005a }
        L_0x0059:
            throw r5
        L_0x005a:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0059
        L_0x005f:
            r5 = 0
            goto L_0x003d
        L_0x0061:
            r5 = move-exception
            r0 = r1
            goto L_0x0054
        L_0x0064:
            r2 = move-exception
            r0 = r1
            goto L_0x0045
        L_0x0067:
            r0 = r1
            goto L_0x003a
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.util.ContextUtil.isAppBroughtToBackground():boolean");
    }

    public static int getPID() {
        ActivityManager mActivityManager = (ActivityManager) getContext().getSystemService("activity");
        if (mActivityManager != null) {
            for (ActivityManager.RunningAppProcessInfo appProcessInfo : mActivityManager.getRunningAppProcesses()) {
                int pid = appProcessInfo.pid;
                String processName = appProcessInfo.processName;
                if (processName.contains(getContext().getPackageName())) {
                    DJILog.e(TAG, processName + " " + pid, new Object[0]);
                    return pid;
                }
            }
        }
        return -1;
    }
}
