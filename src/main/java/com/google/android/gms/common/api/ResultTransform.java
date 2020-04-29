package com.google.android.gms.common.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.internal.zacd;

public abstract class ResultTransform<R extends Result, S extends Result> {
    @Nullable
    @WorkerThread
    public abstract PendingResult<S> onSuccess(@NonNull R r);

    @NonNull
    public Status onFailure(@NonNull Status status) {
        return status;
    }

    @NonNull
    public final PendingResult<S> createFailedResult(@NonNull Status status) {
        return new zacd(status);
    }
}
