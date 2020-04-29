package com.google.android.gms.signin;

import android.support.annotation.Nullable;
import com.google.android.gms.common.api.Api;

public final class SignInOptions implements Api.ApiOptions.Optional {
    public static final SignInOptions DEFAULT = new SignInOptions(false, false, null, false, null, false, null, null);
    private final boolean zaaa = false;
    private final String zaab = null;
    private final String zaac = null;
    private final boolean zars = false;
    private final boolean zart = false;
    private final Long zaru = null;
    private final Long zarv = null;
    private final boolean zay = false;

    public static final class zaa {
    }

    private SignInOptions(boolean z, boolean z2, String str, boolean z3, String str2, boolean z4, Long l, Long l2) {
    }

    public final boolean isOfflineAccessRequested() {
        return this.zars;
    }

    public final boolean isIdTokenRequested() {
        return this.zay;
    }

    public final String getServerClientId() {
        return this.zaab;
    }

    public final boolean isForceCodeForRefreshToken() {
        return this.zaaa;
    }

    @Nullable
    public final String getHostedDomain() {
        return this.zaac;
    }

    public final boolean waitForAccessTokenRefresh() {
        return this.zart;
    }

    @Nullable
    public final Long getAuthApiSignInModuleVersion() {
        return this.zaru;
    }

    @Nullable
    public final Long getRealClientLibraryVersion() {
        return this.zarv;
    }

    static {
        new zaa();
    }
}
