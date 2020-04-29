package com.dji.lifecycle;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public abstract class AbstractLifecycleLogic {
    private final Context mAppContext;
    private final ILifecycle mLifecycle;

    public AbstractLifecycleLogic(@NonNull Context appContext, @NonNull ILifecycle lifecycle) {
        this.mAppContext = appContext;
        this.mLifecycle = lifecycle;
    }

    /* access modifiers changed from: protected */
    @NonNull
    public Context getApplicationContext() {
        return this.mAppContext;
    }

    /* access modifiers changed from: protected */
    @Nullable
    public Activity getTopActivity() {
        return this.mLifecycle.getTopActivity();
    }

    /* access modifiers changed from: protected */
    @Nullable
    public Activity getApplicationTopActivity() {
        return this.mLifecycle.getApplicationTopActivity();
    }
}
