package com.dji.lifecycle;

import android.app.Activity;
import android.support.annotation.Nullable;

public interface ILifecycle {
    @Nullable
    Activity getApplicationTopActivity();

    @Nullable
    Activity getTopActivity();
}
