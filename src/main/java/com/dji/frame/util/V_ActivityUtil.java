package com.dji.frame.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import com.dji.frame.R;
import java.util.ArrayList;
import java.util.List;

public class V_ActivityUtil {
    public static final int ANIM_TYPE_FADE = 3;
    public static final int ANIM_TYPE_FLY = 5;
    public static final int ANIM_TYPE_TRANSLATE_H = 1;
    public static final int ANIM_TYPE_TRANSLATE_H_BACK = 6;
    public static final int ANIM_TYPE_TRANSLATE_H_IN = 11;
    public static final int ANIM_TYPE_TRANSLATE_V = 2;
    public static final int ANIM_TYPE_ZOOM = 4;
    public static final int ANIM_TYPE_ZOOM_BACK = 7;

    public static List<PackageInfo> getAllApps(Context context, Boolean contentSystem) {
        List<PackageInfo> apps = new ArrayList<>();
        List<PackageInfo> paklist = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = paklist.get(i);
            if ((pak.applicationInfo.flags & 1) <= 0 || !contentSystem.booleanValue()) {
                apps.add(pak);
            }
        }
        return apps;
    }

    public static List<ResolveInfo> getShareApps(Context context) {
        new ArrayList();
        Intent intent = new Intent("android.intent.action.SEND", (Uri) null);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("text/plain");
        return context.getPackageManager().queryIntentActivities(intent, 0);
    }

    public static boolean goToApp(Context context, String packageName, String activityName) {
        try {
            ComponentName componentName = new ComponentName(packageName, activityName);
            Intent intent = new Intent();
            intent.setComponent(componentName);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void goToActivity(Context from, Class<?> to) {
        Intent intent = new Intent(from, to);
        intent.setFlags(131072);
        from.startActivity(intent);
    }

    public static void goToActivity(Context from, Class<?> to, int animType) {
        from.startActivity(new Intent(from, to));
        overridePendingTransition(from, animType);
    }

    public static void goToActivity(Context from, Class<?> to, Bundle bundle, int animType) {
        Intent intent = new Intent(from, to);
        intent.setFlags(131072);
        intent.putExtras(bundle);
        from.startActivity(intent);
        overridePendingTransition(from, animType);
    }

    public static void goToActivity(Context from, Class<?> to, Bundle bundle) {
        Intent intent = new Intent(from, to);
        intent.setFlags(131072);
        intent.putExtras(bundle);
        from.startActivity(intent);
    }

    public static void overridePendingTransition(Context from, int animType) {
        switch (animType) {
            case 1:
                ((Activity) from).overridePendingTransition(R.anim.core_push_left_in, R.anim.core_push_left_out);
                return;
            case 2:
                ((Activity) from).overridePendingTransition(R.anim.core_push_up_in, R.anim.core_push_up_out);
                return;
            case 3:
                ((Activity) from).overridePendingTransition(17432576, 17432577);
                return;
            case 4:
                ((Activity) from).overridePendingTransition(R.anim.core_zoom_enter, R.anim.core_zoom_exit);
                return;
            case 5:
                ((Activity) from).overridePendingTransition(R.anim.core_hyperspace_in, R.anim.core_hyperspace_out);
                return;
            case 6:
                ((Activity) from).overridePendingTransition(R.anim.core_push_right_in, R.anim.core_push_right_out);
                return;
            case 7:
                ((Activity) from).overridePendingTransition(R.anim.core_zoom_enter_back, R.anim.core_zoom_exit_back);
                return;
            case 8:
            case 9:
            case 10:
            default:
                ((Activity) from).overridePendingTransition(R.anim.core_other, 0);
                return;
            case 11:
                ((Activity) from).overridePendingTransition(R.anim.core_push_left_in, 0);
                return;
        }
    }

    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "null";
        }
    }

    public static boolean isApkInstalled(Context context, String pkgName) {
        try {
            context.getPackageManager().getPackageInfo(pkgName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isApkDebugable(Context context) {
        try {
            if ((context.getApplicationInfo().flags & 2) != 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
