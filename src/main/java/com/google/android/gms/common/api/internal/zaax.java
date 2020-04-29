package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import com.google.android.gms.common.internal.GmsClientEventManager;

final class zaax implements GmsClientEventManager.GmsClientEventState {
    private final /* synthetic */ zaaw zahg;

    zaax(zaaw zaaw) {
        this.zahg = zaaw;
    }

    public final boolean isConnected() {
        return this.zahg.isConnected();
    }

    public final Bundle getConnectionHint() {
        return null;
    }
}
