package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.internal.BaseImplementation;

public final class zabp<O extends Api.ApiOptions> extends zaag {
    private final GoogleApi<O> zajg;

    public zabp(GoogleApi<O> googleApi) {
        super("Method is not supported by connectionless client. APIs supporting connectionless client must not call this method.");
        this.zajg = googleApi;
    }

    public final <A extends Api.AnyClient, R extends Result, T extends BaseImplementation.ApiMethodImpl<R, A>> T enqueue(@NonNull T t) {
        return this.zajg.doRead((BaseImplementation.ApiMethodImpl) t);
    }

    public final <A extends Api.AnyClient, T extends BaseImplementation.ApiMethodImpl<? extends Result, A>> T execute(@NonNull T t) {
        return this.zajg.doWrite((BaseImplementation.ApiMethodImpl) t);
    }

    public final Looper getLooper() {
        return this.zajg.getLooper();
    }

    public final void zaa(zacm zacm) {
    }

    public final void zab(zacm zacm) {
    }

    public final Context getContext() {
        return this.zajg.getApplicationContext();
    }
}
