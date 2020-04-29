package com.dji.permission;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import com.dji.permission.PermissionActivity;
import com.dji.permission.checker.DoubleChecker;
import com.dji.permission.checker.PermissionChecker;
import com.dji.permission.checker.StandardChecker;
import com.dji.permission.source.Source;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiresApi(api = 23)
class MRequest implements Request, RequestExecutor, PermissionActivity.PermissionListener {
    private static final PermissionChecker CHECKER = new StandardChecker();
    /* access modifiers changed from: private */
    public static final PermissionChecker DOUBLE_CHECKER = new DoubleChecker();
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());
    private Action mDenied;
    private String[] mDeniedPermissions;
    private Action mGranted;
    private String[] mPermissions;
    private Rationale mRationaleListener;
    /* access modifiers changed from: private */
    public Source mSource;

    MRequest(Source source) {
        this.mSource = source;
    }

    @NonNull
    public Request permission(String... permissions) {
        this.mPermissions = permissions;
        return this;
    }

    @NonNull
    public Request permission(String[]... groups) {
        List<String> permissionList = new ArrayList<>();
        for (String[] group : groups) {
            permissionList.addAll(Arrays.asList(group));
        }
        this.mPermissions = (String[]) permissionList.toArray(new String[permissionList.size()]);
        return this;
    }

    @NonNull
    public Request rationale(Rationale listener) {
        this.mRationaleListener = listener;
        return this;
    }

    @NonNull
    public Request onGranted(Action granted) {
        this.mGranted = granted;
        return this;
    }

    @NonNull
    public Request onDenied(Action denied) {
        this.mDenied = denied;
        return this;
    }

    public void start() {
        List<String> deniedList = getDeniedPermissions(CHECKER, this.mSource, this.mPermissions);
        this.mDeniedPermissions = (String[]) deniedList.toArray(new String[deniedList.size()]);
        if (this.mDeniedPermissions.length > 0) {
            List<String> rationaleList = getRationalePermissions(this.mSource, this.mDeniedPermissions);
            if (rationaleList.size() <= 0 || this.mRationaleListener == null) {
                execute();
            } else {
                this.mRationaleListener.showRationale(this.mSource.getContext(), rationaleList, this);
            }
        } else {
            callbackSucceed();
        }
    }

    @RequiresApi(api = 23)
    public void execute() {
        PermissionActivity.requestPermission(this.mSource.getContext(), this.mDeniedPermissions, this);
    }

    public void cancel() {
        onRequestPermissionsResult(this.mDeniedPermissions);
    }

    public void onRequestPermissionsResult(@NonNull final String[] permissions) {
        HANDLER.postDelayed(new Runnable() {
            /* class com.dji.permission.MRequest.AnonymousClass1 */

            public void run() {
                List<String> deniedList = MRequest.getDeniedPermissions(MRequest.DOUBLE_CHECKER, MRequest.this.mSource, permissions);
                if (deniedList.isEmpty()) {
                    MRequest.this.callbackSucceed();
                } else {
                    MRequest.this.callbackFailed(deniedList);
                }
            }
        }, 250);
    }

    /* access modifiers changed from: private */
    public void callbackSucceed() {
        if (this.mGranted != null) {
            List<String> permissionList = Arrays.asList(this.mPermissions);
            try {
                this.mGranted.onAction(permissionList);
            } catch (Exception e) {
                if (this.mDenied != null) {
                    this.mDenied.onAction(permissionList);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void callbackFailed(@NonNull List<String> deniedList) {
        if (this.mDenied != null) {
            this.mDenied.onAction(deniedList);
        }
    }

    /* access modifiers changed from: private */
    public static List<String> getDeniedPermissions(PermissionChecker checker, @NonNull Source source, @NonNull String... permissions) {
        List<String> deniedList = new ArrayList<>(1);
        for (String permission : permissions) {
            if (!checker.hasPermission(source.getContext(), permission)) {
                deniedList.add(permission);
            }
        }
        return deniedList;
    }

    private static List<String> getRationalePermissions(@NonNull Source source, @NonNull String... permissions) {
        List<String> rationaleList = new ArrayList<>(1);
        for (String permission : permissions) {
            if (source.isShowRationalePermission(permission)) {
                rationaleList.add(permission);
            }
        }
        return rationaleList;
    }
}
