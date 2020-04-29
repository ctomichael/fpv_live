package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public final class zaab {
    /* access modifiers changed from: private */
    public final Map<BasePendingResult<?>, Boolean> zafj = Collections.synchronizedMap(new WeakHashMap());
    /* access modifiers changed from: private */
    public final Map<TaskCompletionSource<?>, Boolean> zafk = Collections.synchronizedMap(new WeakHashMap());

    /* access modifiers changed from: package-private */
    public final void zaa(BasePendingResult<? extends Result> basePendingResult, boolean z) {
        this.zafj.put(basePendingResult, Boolean.valueOf(z));
        basePendingResult.addStatusListener(new zaac(this, basePendingResult));
    }

    /* access modifiers changed from: package-private */
    public final <TResult> void zaa(TaskCompletionSource<TResult> taskCompletionSource, boolean z) {
        this.zafk.put(taskCompletionSource, Boolean.valueOf(z));
        taskCompletionSource.getTask().addOnCompleteListener(new zaad(this, taskCompletionSource));
    }

    /* access modifiers changed from: package-private */
    public final boolean zaag() {
        return !this.zafj.isEmpty() || !this.zafk.isEmpty();
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.android.gms.common.api.internal.zaab.zaa(boolean, com.google.android.gms.common.api.Status):void
     arg types: [int, com.google.android.gms.common.api.Status]
     candidates:
      com.google.android.gms.common.api.internal.zaab.zaa(com.google.android.gms.common.api.internal.BasePendingResult<? extends com.google.android.gms.common.api.Result>, boolean):void
      com.google.android.gms.common.api.internal.zaab.zaa(com.google.android.gms.tasks.TaskCompletionSource, boolean):void
      com.google.android.gms.common.api.internal.zaab.zaa(boolean, com.google.android.gms.common.api.Status):void */
    public final void zaah() {
        zaa(false, GoogleApiManager.zahw);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.android.gms.common.api.internal.zaab.zaa(boolean, com.google.android.gms.common.api.Status):void
     arg types: [int, com.google.android.gms.common.api.Status]
     candidates:
      com.google.android.gms.common.api.internal.zaab.zaa(com.google.android.gms.common.api.internal.BasePendingResult<? extends com.google.android.gms.common.api.Result>, boolean):void
      com.google.android.gms.common.api.internal.zaab.zaa(com.google.android.gms.tasks.TaskCompletionSource, boolean):void
      com.google.android.gms.common.api.internal.zaab.zaa(boolean, com.google.android.gms.common.api.Status):void */
    public final void zaai() {
        zaa(true, zacp.zakw);
    }

    private final void zaa(boolean z, Status status) {
        HashMap hashMap;
        HashMap hashMap2;
        synchronized (this.zafj) {
            hashMap = new HashMap(this.zafj);
        }
        synchronized (this.zafk) {
            hashMap2 = new HashMap(this.zafk);
        }
        for (Map.Entry entry : hashMap.entrySet()) {
            if (z || ((Boolean) entry.getValue()).booleanValue()) {
                ((BasePendingResult) entry.getKey()).zab(status);
            }
        }
        for (Map.Entry entry2 : hashMap2.entrySet()) {
            if (z || ((Boolean) entry2.getValue()).booleanValue()) {
                ((TaskCompletionSource) entry2.getKey()).trySetException(new ApiException(status));
            }
        }
    }
}
