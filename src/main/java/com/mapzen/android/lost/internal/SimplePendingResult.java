package com.mapzen.android.lost.internal;

import android.support.annotation.NonNull;
import com.mapzen.android.lost.api.PendingResult;
import com.mapzen.android.lost.api.ResultCallback;
import com.mapzen.android.lost.api.Status;
import java.util.concurrent.TimeUnit;

public class SimplePendingResult extends PendingResult<Status> {
    private SettingsDialogDisplayer dialogDisplayer = new SettingsDialogDisplayer();
    private boolean hasResult = false;

    public SimplePendingResult(boolean hasResult2) {
        this.hasResult = hasResult2;
    }

    @NonNull
    public Status await() {
        return generateStatus();
    }

    @NonNull
    public Status await(long time, @NonNull TimeUnit timeUnit) {
        return generateStatus();
    }

    public void cancel() {
    }

    public boolean isCanceled() {
        return false;
    }

    public void setResultCallback(@NonNull ResultCallback callback) {
        if (this.hasResult) {
            callback.onResult(generateStatus());
        }
    }

    public void setResultCallback(@NonNull ResultCallback callback, long time, @NonNull TimeUnit timeUnit) {
        if (this.hasResult) {
            callback.onResult(generateStatus());
        }
    }

    private Status generateStatus() {
        return new Status(0, this.dialogDisplayer);
    }
}
