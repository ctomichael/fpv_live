package com.google.android.gms.common.api.internal;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.AvailabilityException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.util.Collections;
import java.util.Map;

final class zaz implements OnCompleteListener<Map<zai<?>, String>> {
    private final /* synthetic */ zax zafh;

    private zaz(zax zax) {
        this.zafh = zax;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.android.gms.common.api.internal.zax.zaa(com.google.android.gms.common.api.internal.zax, boolean):boolean
     arg types: [com.google.android.gms.common.api.internal.zax, int]
     candidates:
      com.google.android.gms.common.api.internal.zax.zaa(com.google.android.gms.common.api.internal.zax, com.google.android.gms.common.ConnectionResult):com.google.android.gms.common.ConnectionResult
      com.google.android.gms.common.api.internal.zax.zaa(com.google.android.gms.common.api.internal.zax, java.util.Map):java.util.Map
      com.google.android.gms.common.api.internal.zax.zaa(com.google.android.gms.common.api.internal.zaw<?>, com.google.android.gms.common.ConnectionResult):boolean
      com.google.android.gms.common.api.internal.zax.zaa(com.google.android.gms.common.api.internal.zax, boolean):boolean */
    public final void onComplete(@NonNull Task<Map<zai<?>, String>> task) {
        this.zafh.zaen.lock();
        try {
            if (this.zafh.zafc) {
                if (task.isSuccessful()) {
                    Map unused = this.zafh.zafd = new ArrayMap(this.zafh.zaet.size());
                    for (zaw zaw : this.zafh.zaet.values()) {
                        this.zafh.zafd.put(zaw.zak(), ConnectionResult.RESULT_SUCCESS);
                    }
                } else if (task.getException() instanceof AvailabilityException) {
                    AvailabilityException availabilityException = (AvailabilityException) task.getException();
                    if (this.zafh.zafa) {
                        Map unused2 = this.zafh.zafd = new ArrayMap(this.zafh.zaet.size());
                        for (zaw zaw2 : this.zafh.zaet.values()) {
                            zai zak = zaw2.zak();
                            ConnectionResult connectionResult = availabilityException.getConnectionResult(zaw2);
                            if (this.zafh.zaa(zaw2, connectionResult)) {
                                this.zafh.zafd.put(zak, new ConnectionResult(16));
                            } else {
                                this.zafh.zafd.put(zak, connectionResult);
                            }
                        }
                    } else {
                        Map unused3 = this.zafh.zafd = availabilityException.zaj();
                    }
                    ConnectionResult unused4 = this.zafh.zafg = this.zafh.zaaf();
                } else {
                    Log.e("ConnectionlessGAC", "Unexpected availability exception", task.getException());
                    Map unused5 = this.zafh.zafd = Collections.emptyMap();
                    ConnectionResult unused6 = this.zafh.zafg = new ConnectionResult(8);
                }
                if (this.zafh.zafe != null) {
                    this.zafh.zafd.putAll(this.zafh.zafe);
                    ConnectionResult unused7 = this.zafh.zafg = this.zafh.zaaf();
                }
                if (this.zafh.zafg == null) {
                    this.zafh.zaad();
                    this.zafh.zaae();
                } else {
                    boolean unused8 = this.zafh.zafc = false;
                    this.zafh.zaew.zac(this.zafh.zafg);
                }
                this.zafh.zaey.signalAll();
                this.zafh.zaen.unlock();
            }
        } finally {
            this.zafh.zaen.unlock();
        }
    }
}
