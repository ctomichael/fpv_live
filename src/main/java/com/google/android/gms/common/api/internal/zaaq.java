package com.google.android.gms.common.api.internal;

import android.support.annotation.WorkerThread;
import com.google.android.gms.common.api.Api;
import java.util.ArrayList;

final class zaaq extends zaau {
    private final /* synthetic */ zaak zagi;
    private final ArrayList<Api.Client> zago;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public zaaq(zaak zaak, ArrayList<Api.Client> arrayList) {
        super(zaak, null);
        this.zagi = zaak;
        this.zago = arrayList;
    }

    @WorkerThread
    public final void zaan() {
        this.zagi.zafs.zaed.zagz = this.zagi.zaat();
        ArrayList arrayList = this.zago;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            ((Api.Client) obj).getRemoteService(this.zagi.zage, this.zagi.zafs.zaed.zagz);
        }
    }
}
