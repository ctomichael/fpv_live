package com.google.android.gms.common.internal;

import com.google.android.gms.common.api.Response;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.internal.PendingResultUtil;

final class zak implements PendingResultUtil.ResultConverter<R, T> {
    private final /* synthetic */ Response zaoy;

    zak(Response response) {
        this.zaoy = response;
    }

    public final /* synthetic */ Object convert(Result result) {
        this.zaoy.setResult(result);
        return this.zaoy;
    }
}
