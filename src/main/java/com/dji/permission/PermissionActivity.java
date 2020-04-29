package com.dji.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import dji.thirdparty.ciphersql.database.SQLiteDatabase;

@RequiresApi(api = 23)
public final class PermissionActivity extends Activity {
    private static final String KEY_INPUT_PERMISSIONS = "KEY_INPUT_PERMISSIONS";
    private static PermissionListener sPermissionListener;

    interface PermissionListener {
        void onRequestPermissionsResult(@NonNull String[] strArr);
    }

    public static void requestPermission(Context context, String[] permissions, PermissionListener permissionListener) {
        sPermissionListener = permissionListener;
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.setFlags(SQLiteDatabase.CREATE_IF_NECESSARY);
        intent.putExtra(KEY_INPUT_PERMISSIONS, permissions);
        context.startActivity(intent);
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        invasionStatusBar(this);
        String[] permissions = getIntent().getStringArrayExtra(KEY_INPUT_PERMISSIONS);
        if (permissions == null || sPermissionListener == null) {
            sPermissionListener = null;
            finish();
            return;
        }
        requestPermissions(permissions, 1);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (sPermissionListener != null) {
            sPermissionListener.onRequestPermissionsResult(permissions);
            sPermissionListener = null;
        }
        finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private static void invasionStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | 1024 | 256);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
        }
    }
}
