package dji.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import dji.thirdparty.ciphersql.database.SQLiteDatabase;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class AppUtils {
    private static Application mApplication;

    private static void init(Application app) {
        if (mApplication != null) {
            return;
        }
        if (app == null) {
            mApplication = getApplicationByReflect();
        } else {
            mApplication = app;
        }
    }

    public static void installApk(String fileSavePath) {
        Uri data;
        if (!TextUtils.isEmpty(fileSavePath)) {
            File file = new File(Uri.parse(fileSavePath).getPath());
            if (isFileExists(file)) {
                String filePath = file.getAbsolutePath();
                Intent intent = new Intent("android.intent.action.VIEW");
                if (Build.VERSION.SDK_INT >= 24) {
                    data = FileProvider.getUriForFile(getApp(), getApp().getPackageName() + ".fileprovider", new File(filePath));
                    intent.setFlags(1);
                } else {
                    data = Uri.fromFile(file);
                }
                intent.setDataAndType(data, "application/vnd.android.package-archive");
                intent.addFlags(SQLiteDatabase.CREATE_IF_NECESSARY);
                getApp().startActivity(intent);
            }
        }
    }

    public static Application getApp() {
        if (mApplication != null) {
            return mApplication;
        }
        Application app = getApplicationByReflect();
        init(app);
        return app;
    }

    private static Application getApplicationByReflect() {
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object app = activityThread.getMethod("getApplication", new Class[0]).invoke(activityThread.getMethod("currentActivityThread", new Class[0]).invoke(null, new Object[0]), new Object[0]);
            if (app != null) {
                return (Application) app;
            }
            throw new NullPointerException();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new NullPointerException();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            throw new NullPointerException();
        } catch (InvocationTargetException e3) {
            e3.printStackTrace();
            throw new NullPointerException();
        } catch (NoSuchMethodException e4) {
            e4.printStackTrace();
            throw new NullPointerException();
        }
    }

    public static PackageInfo getPackageInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 16384);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean isFileExists(File file) {
        return file != null && file.exists();
    }

    public static String getTaskTopActivityName(Context context) {
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1);
        if (runningTaskInfos == null || runningTaskInfos.isEmpty()) {
            return null;
        }
        return runningTaskInfos.get(0).topActivity.getShortClassName();
    }
}
