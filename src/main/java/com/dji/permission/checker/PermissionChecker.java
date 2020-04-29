package com.dji.permission.checker;

import android.content.Context;
import android.support.annotation.NonNull;
import java.util.List;

public interface PermissionChecker {
    boolean hasPermission(@NonNull Context context, @NonNull List<String> list);

    boolean hasPermission(@NonNull Context context, @NonNull String... strArr);
}
