package com.google.android.gms.common.api.internal;

import android.app.Dialog;

final class zao extends zabr {
    private final /* synthetic */ Dialog zadk;
    private final /* synthetic */ zan zadl;

    zao(zan zan, Dialog dialog) {
        this.zadl = zan;
        this.zadk = dialog;
    }

    public final void zas() {
        this.zadl.zadj.zaq();
        if (this.zadk.isShowing()) {
            this.zadk.dismiss();
        }
    }
}
