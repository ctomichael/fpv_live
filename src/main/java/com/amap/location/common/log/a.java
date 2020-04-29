package com.amap.location.common.log;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import com.amap.location.common.HeaderConfig;

/* compiled from: LogFileHeaderCollector */
class a {
    private static int a = 0;
    private static String b = "";
    private static String c = "";

    public static String a(Context context) {
        if (TextUtils.isEmpty(c)) {
            b(context);
            StringBuilder sb = new StringBuilder();
            if (a != 0) {
                sb.append("versionCode:" + a + "\n");
            }
            if (!TextUtils.isEmpty(b)) {
                sb.append("versionName:" + b + "\n");
            }
            sb.append("pid:" + Process.myPid() + "\n");
            sb.append("uid:" + Process.myUid() + "\n");
            sb.append("processName:" + HeaderConfig.getProcessName() + "\n");
            sb.append("packageName:" + context.getPackageName() + "\n");
            sb.append("-----------------------------\n");
            c = sb.toString();
        }
        return c;
    }

    public static void b(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            b = packageInfo.versionName;
            a = packageInfo.versionCode;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
    }
}
