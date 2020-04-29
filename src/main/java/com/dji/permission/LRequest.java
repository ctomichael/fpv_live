package com.dji.permission;

import android.support.annotation.NonNull;
import com.dji.permission.checker.PermissionChecker;
import com.dji.permission.checker.StrictChecker;
import com.dji.permission.source.Source;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class LRequest implements Request {
    private static final PermissionChecker CHECKER = new StrictChecker();
    private Action mDenied;
    private Action mGranted;
    private String[] mPermissions;
    private Source mSource;

    LRequest(Source source) {
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
        List<String> deniedList = getDeniedPermissions(this.mSource, this.mPermissions);
        if (deniedList.isEmpty()) {
            callbackSucceed();
        } else {
            callbackFailed(deniedList);
        }
    }

    private void callbackSucceed() {
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

    private void callbackFailed(@NonNull List<String> deniedList) {
        if (this.mDenied != null) {
            this.mDenied.onAction(deniedList);
        }
    }

    private static List<String> getDeniedPermissions(@NonNull Source source, @NonNull String... permissions) {
        List<String> deniedList = new ArrayList<>(1);
        for (String permission : permissions) {
            if (!CHECKER.hasPermission(source.getContext(), permission)) {
                deniedList.add(permission);
            }
        }
        return deniedList;
    }
}
