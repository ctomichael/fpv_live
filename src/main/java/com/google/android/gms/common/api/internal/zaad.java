package com.google.android.gms.common.api.internal;

import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

final class zaad implements OnCompleteListener<TResult> {
    private final /* synthetic */ zaab zafm;
    private final /* synthetic */ TaskCompletionSource zafn;

    zaad(zaab zaab, TaskCompletionSource taskCompletionSource) {
        this.zafm = zaab;
        this.zafn = taskCompletionSource;
    }

    public final void onComplete(@NonNull Task<TResult> task) {
        this.zafm.zafk.remove(this.zafn);
    }
}
