package com.dji.permission.checker;

import android.content.Context;
import android.support.annotation.NonNull;
import java.util.List;

public final class DoubleChecker implements PermissionChecker {
    private static final PermissionChecker STANDARD_CHECKER = new StandardChecker();
    private static final PermissionChecker STRICT_CHECKER = new StrictChecker();

    public boolean hasPermission(@NonNull Context context, @NonNull String... permissions) {
        return STANDARD_CHECKER.hasPermission(context, permissions);
    }

    public boolean hasPermission(@NonNull Context context, @NonNull List<String> permissions) {
        return STANDARD_CHECKER.hasPermission(context, permissions);
    }
}
