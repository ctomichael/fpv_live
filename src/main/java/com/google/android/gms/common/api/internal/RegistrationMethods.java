package com.google.android.gms.common.api.internal;

import android.os.RemoteException;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.BiConsumer;
import com.google.android.gms.tasks.TaskCompletionSource;

@KeepForSdk
public class RegistrationMethods<A extends Api.AnyClient, L> {
    public final RegisterListenerMethod<A, L> zajy;
    public final UnregisterListenerMethod<A, L> zajz;

    private RegistrationMethods(RegisterListenerMethod<A, L> registerListenerMethod, UnregisterListenerMethod<A, L> unregisterListenerMethod) {
        this.zajy = registerListenerMethod;
        this.zajz = unregisterListenerMethod;
    }

    @KeepForSdk
    public static class Builder<A extends Api.AnyClient, L> {
        private boolean zajv;
        /* access modifiers changed from: private */
        public RemoteCall<A, TaskCompletionSource<Void>> zaka;
        /* access modifiers changed from: private */
        public RemoteCall<A, TaskCompletionSource<Boolean>> zakb;
        private ListenerHolder<L> zakc;
        private Feature[] zakd;

        private Builder() {
            this.zajv = true;
        }

        @KeepForSdk
        @Deprecated
        public Builder<A, L> register(BiConsumer<A, TaskCompletionSource<Void>> biConsumer) {
            this.zaka = new zaby(biConsumer);
            return this;
        }

        @KeepForSdk
        @Deprecated
        public Builder<A, L> unregister(BiConsumer<A, TaskCompletionSource<Boolean>> biConsumer) {
            this.zaka = new zabz(this);
            return this;
        }

        @KeepForSdk
        public Builder<A, L> register(RemoteCall<A, TaskCompletionSource<Void>> remoteCall) {
            this.zaka = remoteCall;
            return this;
        }

        @KeepForSdk
        public Builder<A, L> unregister(RemoteCall<A, TaskCompletionSource<Boolean>> remoteCall) {
            this.zakb = remoteCall;
            return this;
        }

        @KeepForSdk
        public Builder<A, L> withHolder(ListenerHolder<L> listenerHolder) {
            this.zakc = listenerHolder;
            return this;
        }

        @KeepForSdk
        public Builder<A, L> setFeatures(Feature[] featureArr) {
            this.zakd = featureArr;
            return this;
        }

        @KeepForSdk
        public Builder<A, L> setAutoResolveMissingFeatures(boolean z) {
            this.zajv = z;
            return this;
        }

        @KeepForSdk
        public RegistrationMethods<A, L> build() {
            boolean z;
            boolean z2 = true;
            Preconditions.checkArgument(this.zaka != null, "Must set register function");
            if (this.zakb != null) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z, "Must set unregister function");
            if (this.zakc == null) {
                z2 = false;
            }
            Preconditions.checkArgument(z2, "Must set holder");
            return new RegistrationMethods<>(new zaca(this, this.zakc, this.zakd, this.zajv), new zacb(this, this.zakc.getListenerKey()));
        }

        /* access modifiers changed from: package-private */
        public final /* synthetic */ void zaa(Api.AnyClient anyClient, TaskCompletionSource taskCompletionSource) throws RemoteException {
            this.zaka.accept(anyClient, taskCompletionSource);
        }
    }

    @KeepForSdk
    public static <A extends Api.AnyClient, L> Builder<A, L> builder() {
        return new Builder<>();
    }
}
