package com.google.android.gms.common.internal;

import android.app.Activity;
import android.content.Intent;

final class zac extends DialogRedirect {
    private final /* synthetic */ Activity val$activity;
    private final /* synthetic */ int val$requestCode;
    private final /* synthetic */ Intent zaog;

    zac(Intent intent, Activity activity, int i) {
        this.zaog = intent;
        this.val$activity = activity;
        this.val$requestCode = i;
    }

    public final void redirect() {
        if (this.zaog != null) {
            this.val$activity.startActivityForResult(this.zaog, this.val$requestCode);
        }
    }
}
