package com.dji.permission.setting;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import com.dji.permission.SettingService;
import com.dji.permission.source.Source;

public class PermissionSetting implements SettingService {
    private static final String MARK = Build.MANUFACTURER.toLowerCase();
    private Source mSource;

    public PermissionSetting(@NonNull Source source) {
        this.mSource = source;
    }

    public void execute() {
        try {
            this.mSource.startActivity(obtainSettingIntent());
        } catch (Exception e) {
            this.mSource.startActivity(defaultApi(this.mSource.getContext()));
        }
    }

    public void execute(int requestCode) {
        try {
            this.mSource.startActivityForResult(obtainSettingIntent(), requestCode);
        } catch (Exception e) {
            this.mSource.startActivityForResult(defaultApi(this.mSource.getContext()), requestCode);
        }
    }

    public void cancel() {
    }

    private Intent obtainSettingIntent() {
        if (MARK.contains("huawei")) {
            return huaweiApi(this.mSource.getContext());
        }
        if (MARK.contains("xiaomi")) {
            return xiaomiApi(this.mSource.getContext());
        }
        if (MARK.contains("oppo")) {
            return oppoApi(this.mSource.getContext());
        }
        if (MARK.contains("samsung")) {
            return samsungApi(this.mSource.getContext());
        }
        if (MARK.contains("meizu")) {
            return meizuApi(this.mSource.getContext());
        }
        if (MARK.contains("smartisan")) {
            return smartisanApi(this.mSource.getContext());
        }
        return defaultApi(this.mSource.getContext());
    }

    private static Intent defaultApi(Context context) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        return intent;
    }

    private static Intent huaweiApi(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return defaultApi(context);
        }
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity"));
        return intent;
    }

    private static Intent xiaomiApi(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.putExtra("extra_pkgname", context.getPackageName());
        return intent;
    }

    private static Intent vivoApi(Context context) {
        Intent intent = new Intent();
        intent.putExtra("packagename", context.getPackageName());
        if (Build.VERSION.SDK_INT >= 25) {
            intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.SoftPermissionDetailActivity"));
        } else {
            intent.setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.safeguard.SoftPermissionDetailActivity"));
        }
        return intent;
    }

    private static Intent oppoApi(Context context) {
        return defaultApi(context);
    }

    private static Intent meizuApi(Context context) {
        if (Build.VERSION.SDK_INT >= 25) {
            return defaultApi(context);
        }
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.putExtra("packageName", context.getPackageName());
        intent.setComponent(new ComponentName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity"));
        return intent;
    }

    private static Intent smartisanApi(Context context) {
        return defaultApi(context);
    }

    private static Intent samsungApi(Context context) {
        return defaultApi(context);
    }
}
