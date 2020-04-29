package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;

final class zaac implements PendingResult.StatusListener {
    private final /* synthetic */ BasePendingResult zafl;
    private final /* synthetic */ zaab zafm;

    zaac(zaab zaab, BasePendingResult basePendingResult) {
        this.zafm = zaab;
        this.zafl = basePendingResult;
    }

    public final void onComplete(Status status) {
        this.zafm.zafj.remove(this.zafl);
    }
}
