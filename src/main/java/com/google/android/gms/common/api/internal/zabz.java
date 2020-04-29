package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.internal.RegistrationMethods;
import com.google.android.gms.tasks.TaskCompletionSource;

final /* synthetic */ class zabz implements RemoteCall {
    private final RegistrationMethods.Builder zakf;

    zabz(RegistrationMethods.Builder builder) {
        this.zakf = builder;
    }

    public final void accept(Object obj, Object obj2) {
        this.zakf.zaa((Api.AnyClient) obj, (TaskCompletionSource) obj2);
    }
}
