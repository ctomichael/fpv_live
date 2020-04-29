package com.billy.cc.core.component.remote;

import android.app.Application;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import com.billy.cc.core.component.CC;
import dji.thirdparty.ciphersql.database.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class RemoteConnection {
    public static List<String> scanComponentApps() {
        Application application = CC.getApplication();
        String curPkg = application.getPackageName();
        List<ResolveInfo> list = application.getPackageManager().queryIntentActivities(new Intent("action.com.billy.cc.connection"), 0);
        List<String> packageNames = new ArrayList<>();
        for (ResolveInfo info : list) {
            String packageName = info.activityInfo.packageName;
            if (!curPkg.equals(packageName) && tryWakeup(packageName)) {
                packageNames.add(packageName);
            }
        }
        return packageNames;
    }

    public static boolean tryWakeup(String packageName) {
        long time = System.currentTimeMillis();
        Intent intent = new Intent();
        intent.setClassName(packageName, RemoteConnectionActivity.class.getName());
        intent.addFlags(SQLiteDatabase.CREATE_IF_NECESSARY);
        try {
            CC.getApplication().startActivity(intent);
            CC.log("wakeup remote app '%s' success. time=%d", packageName, Long.valueOf(System.currentTimeMillis() - time));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            CC.log("wakeup remote app '%s' failed. time=%d", packageName, Long.valueOf(System.currentTimeMillis() - time));
            return false;
        }
    }
}
