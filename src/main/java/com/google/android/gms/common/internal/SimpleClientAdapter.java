package com.google.android.gms.common.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;

public class SimpleClientAdapter<T extends IInterface> extends GmsClient<T> {
    private final Api.SimpleClient<T> zapf;

    public SimpleClientAdapter(Context context, Looper looper, int i, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener, ClientSettings clientSettings, Api.SimpleClient<T> simpleClient) {
        super(context, looper, i, clientSettings, connectionCallbacks, onConnectionFailedListener);
        this.zapf = simpleClient;
    }

    /* access modifiers changed from: protected */
    public String getStartServiceAction() {
        return this.zapf.getStartServiceAction();
    }

    /* access modifiers changed from: protected */
    public String getServiceDescriptor() {
        return this.zapf.getServiceDescriptor();
    }

    /* access modifiers changed from: protected */
    public T createServiceInterface(IBinder iBinder) {
        return this.zapf.createServiceInterface(iBinder);
    }

    /* access modifiers changed from: protected */
    public void onSetConnectState(int i, T t) {
        this.zapf.setState(i, t);
    }

    public Api.SimpleClient<T> getClient() {
        return this.zapf;
    }

    public int getMinApkVersion() {
        return super.getMinApkVersion();
    }
}
