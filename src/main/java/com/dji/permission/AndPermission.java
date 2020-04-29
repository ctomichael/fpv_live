package com.dji.permission;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import com.dji.permission.setting.PermissionSetting;
import com.dji.permission.source.AppActivitySource;
import com.dji.permission.source.ContextSource;
import com.dji.permission.source.FragmentSource;
import com.dji.permission.source.Source;
import com.dji.permission.source.SupportFragmentSource;
import java.util.List;

public class AndPermission {
    private static final RequestFactory FACTORY;

    private interface RequestFactory {
        Request create(Source source);
    }

    static {
        if (Build.VERSION.SDK_INT >= 23) {
            FACTORY = new MRequestFactory();
        } else {
            FACTORY = new LRequestFactory();
        }
    }

    public static boolean hasAlwaysDeniedPermission(@NonNull Activity activity, @NonNull List<String> deniedPermissions) {
        return hasAlwaysDeniedPermission(new AppActivitySource(activity), deniedPermissions);
    }

    public static boolean hasAlwaysDeniedPermission(@NonNull Fragment fragment, @NonNull List<String> deniedPermissions) {
        return hasAlwaysDeniedPermission(new SupportFragmentSource(fragment), deniedPermissions);
    }

    public static boolean hasAlwaysDeniedPermission(@NonNull android.app.Fragment fragment, @NonNull List<String> deniedPermissions) {
        return hasAlwaysDeniedPermission(new FragmentSource(fragment), deniedPermissions);
    }

    public static boolean hasAlwaysDeniedPermission(@NonNull Context context, @NonNull List<String> deniedPermissions) {
        return hasAlwaysDeniedPermission(new ContextSource(context), deniedPermissions);
    }

    private static boolean hasAlwaysDeniedPermission(@NonNull Source source, @NonNull List<String> deniedPermissions) {
        for (String permission : deniedPermissions) {
            if (!source.isShowRationalePermission(permission)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasAlwaysDeniedPermission(@NonNull Activity activity, @NonNull String... deniedPermissions) {
        return hasAlwaysDeniedPermission(new AppActivitySource(activity), deniedPermissions);
    }

    public static boolean hasAlwaysDeniedPermission(@NonNull Fragment fragment, @NonNull String... deniedPermissions) {
        return hasAlwaysDeniedPermission(new SupportFragmentSource(fragment), deniedPermissions);
    }

    public static boolean hasAlwaysDeniedPermission(@NonNull android.app.Fragment fragment, @NonNull String... deniedPermissions) {
        return hasAlwaysDeniedPermission(new FragmentSource(fragment), deniedPermissions);
    }

    public static boolean hasAlwaysDeniedPermission(@NonNull Context context, @NonNull String... deniedPermissions) {
        return hasAlwaysDeniedPermission(new ContextSource(context), deniedPermissions);
    }

    private static boolean hasAlwaysDeniedPermission(@NonNull Source source, @NonNull String... deniedPermissions) {
        for (String permission : deniedPermissions) {
            if (!source.isShowRationalePermission(permission)) {
                return true;
            }
        }
        return false;
    }

    @NonNull
    public static SettingService permissionSetting(@NonNull Activity activity) {
        return new PermissionSetting(new AppActivitySource(activity));
    }

    @NonNull
    public static SettingService permissionSetting(@NonNull Fragment fragment) {
        return new PermissionSetting(new SupportFragmentSource(fragment));
    }

    @NonNull
    public static SettingService permissionSetting(@NonNull android.app.Fragment fragment) {
        return new PermissionSetting(new FragmentSource(fragment));
    }

    @NonNull
    public static SettingService permissionSetting(@NonNull Context context) {
        return new PermissionSetting(new ContextSource(context));
    }

    @NonNull
    public static Request with(@NonNull Activity activity) {
        return FACTORY.create(new AppActivitySource(activity));
    }

    @NonNull
    public static Request with(@NonNull Fragment fragment) {
        return FACTORY.create(new SupportFragmentSource(fragment));
    }

    @NonNull
    public static Request with(@NonNull android.app.Fragment fragment) {
        return FACTORY.create(new FragmentSource(fragment));
    }

    @NonNull
    public static Request with(@NonNull Context context) {
        return FACTORY.create(new ContextSource(context));
    }

    private AndPermission() {
    }

    private static class LRequestFactory implements RequestFactory {
        private LRequestFactory() {
        }

        public Request create(Source source) {
            return new LRequest(source);
        }
    }

    @RequiresApi(api = 23)
    private static class MRequestFactory implements RequestFactory {
        private MRequestFactory() {
        }

        public Request create(Source source) {
            return new MRequest(source);
        }
    }
}
