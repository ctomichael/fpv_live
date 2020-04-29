package com.dji.permission;

import android.support.annotation.NonNull;

public interface Request {
    @NonNull
    Request onDenied(Action action);

    @NonNull
    Request onGranted(Action action);

    @NonNull
    Request permission(String... strArr);

    @NonNull
    Request permission(String[]... strArr);

    @NonNull
    Request rationale(Rationale rationale);

    void start();
}
