package com.google.android.gms.common.api.internal;

import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public final class zacp {
    public static final Status zakw = new Status(8, "The connection to Google Play services was lost");
    private static final BasePendingResult<?>[] zakx = new BasePendingResult[0];
    private final Map<Api.AnyClientKey<?>, Api.Client> zagy;
    @VisibleForTesting
    final Set<BasePendingResult<?>> zaky = Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap()));
    private final zacs zakz = new zacq(this);

    public zacp(Map<Api.AnyClientKey<?>, Api.Client> map) {
        this.zagy = map;
    }

    /* access modifiers changed from: package-private */
    public final void zab(BasePendingResult<? extends Result> basePendingResult) {
        this.zaky.add(basePendingResult);
        basePendingResult.zaa(this.zakz);
    }

    /* JADX WARN: Type inference failed for: r7v0, types: [com.google.android.gms.common.api.ResultCallback, com.google.android.gms.common.api.internal.zacs, com.google.android.gms.common.api.zac, com.google.android.gms.common.api.internal.zacq], assign insn: 0x0001: CONST  (r7v0 ? I:?[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (0 ?[int, float, boolean, short, byte, char, OBJECT, ARRAY]) */
    public final void release() {
        ? r7 = 0;
        BasePendingResult[] basePendingResultArr = (BasePendingResult[]) this.zaky.toArray(zakx);
        for (BasePendingResult basePendingResult : basePendingResultArr) {
            basePendingResult.zaa((zacs) r7);
            if (basePendingResult.zam() != null) {
                basePendingResult.setResultCallback(r7);
                IBinder serviceBrokerBinder = this.zagy.get(((BaseImplementation.ApiMethodImpl) basePendingResult).getClientKey()).getServiceBrokerBinder();
                if (basePendingResult.isReady()) {
                    basePendingResult.zaa(new zacr(basePendingResult, r7, serviceBrokerBinder, r7));
                } else if (serviceBrokerBinder == null || !serviceBrokerBinder.isBinderAlive()) {
                    basePendingResult.zaa((zacs) r7);
                    basePendingResult.cancel();
                    r7.remove(basePendingResult.zam().intValue());
                } else {
                    zacr zacr = new zacr(basePendingResult, r7, serviceBrokerBinder, r7);
                    basePendingResult.zaa(zacr);
                    try {
                        serviceBrokerBinder.linkToDeath(zacr, 0);
                    } catch (RemoteException e) {
                        basePendingResult.cancel();
                        r7.remove(basePendingResult.zam().intValue());
                    }
                }
                this.zaky.remove(basePendingResult);
            } else if (basePendingResult.zat()) {
                this.zaky.remove(basePendingResult);
            }
        }
    }

    public final void zabx() {
        for (BasePendingResult basePendingResult : (BasePendingResult[]) this.zaky.toArray(zakx)) {
            basePendingResult.zab(zakw);
        }
    }
}
