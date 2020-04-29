package com.mapzen.android.lost.api;

import android.support.annotation.NonNull;
import com.mapzen.android.lost.api.Result;
import java.util.concurrent.TimeUnit;

public abstract class PendingResult<R extends Result> {
    @NonNull
    public abstract R await();

    @NonNull
    public abstract R await(long j, @NonNull TimeUnit timeUnit);

    public abstract void cancel();

    public abstract boolean isCanceled();

    public abstract void setResultCallback(@NonNull ResultCallback<? super R> resultCallback);

    public abstract void setResultCallback(@NonNull ResultCallback<? super R> resultCallback, long j, @NonNull TimeUnit timeUnit);
}
