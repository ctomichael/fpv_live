package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.Message;
import android.support.v4.os.EnvironmentCompat;
import android.util.Log;
import com.google.android.gms.common.internal.GmsClientSupervisor;
import com.google.android.gms.common.stats.ConnectionTracker;
import java.util.HashMap;
import javax.annotation.concurrent.GuardedBy;

final class zze extends GmsClientSupervisor implements Handler.Callback {
    /* access modifiers changed from: private */
    public final Handler mHandler;
    /* access modifiers changed from: private */
    @GuardedBy("mConnectionStatus")
    public final HashMap<GmsClientSupervisor.zza, zzf> zzdu = new HashMap<>();
    /* access modifiers changed from: private */
    public final Context zzdv;
    /* access modifiers changed from: private */
    public final ConnectionTracker zzdw;
    private final long zzdx;
    /* access modifiers changed from: private */
    public final long zzdy;

    zze(Context context) {
        this.zzdv = context.getApplicationContext();
        this.mHandler = new com.google.android.gms.internal.common.zze(context.getMainLooper(), this);
        this.zzdw = ConnectionTracker.getInstance();
        this.zzdx = 5000;
        this.zzdy = 300000;
    }

    /* access modifiers changed from: protected */
    public final boolean zza(GmsClientSupervisor.zza zza, ServiceConnection serviceConnection, String str) {
        boolean isBound;
        Preconditions.checkNotNull(serviceConnection, "ServiceConnection must not be null");
        synchronized (this.zzdu) {
            zzf zzf = this.zzdu.get(zza);
            if (zzf != null) {
                this.mHandler.removeMessages(0, zza);
                if (!zzf.zza(serviceConnection)) {
                    zzf.zza(serviceConnection, str);
                    switch (zzf.getState()) {
                        case 1:
                            serviceConnection.onServiceConnected(zzf.getComponentName(), zzf.getBinder());
                            break;
                        case 2:
                            zzf.zze(str);
                            break;
                    }
                } else {
                    String valueOf = String.valueOf(zza);
                    throw new IllegalStateException(new StringBuilder(String.valueOf(valueOf).length() + 81).append("Trying to bind a GmsServiceConnection that was already connected before.  config=").append(valueOf).toString());
                }
            } else {
                zzf = new zzf(this, zza);
                zzf.zza(serviceConnection, str);
                zzf.zze(str);
                this.zzdu.put(zza, zzf);
            }
            isBound = zzf.isBound();
        }
        return isBound;
    }

    /* access modifiers changed from: protected */
    public final void zzb(GmsClientSupervisor.zza zza, ServiceConnection serviceConnection, String str) {
        Preconditions.checkNotNull(serviceConnection, "ServiceConnection must not be null");
        synchronized (this.zzdu) {
            zzf zzf = this.zzdu.get(zza);
            if (zzf == null) {
                String valueOf = String.valueOf(zza);
                throw new IllegalStateException(new StringBuilder(String.valueOf(valueOf).length() + 50).append("Nonexistent connection status for service config: ").append(valueOf).toString());
            } else if (!zzf.zza(serviceConnection)) {
                String valueOf2 = String.valueOf(zza);
                throw new IllegalStateException(new StringBuilder(String.valueOf(valueOf2).length() + 76).append("Trying to unbind a GmsServiceConnection  that was not bound before.  config=").append(valueOf2).toString());
            } else {
                zzf.zzb(serviceConnection, str);
                if (zzf.zzr()) {
                    this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0, zza), this.zzdx);
                }
            }
        }
    }

    public final boolean handleMessage(Message message) {
        ComponentName componentName;
        switch (message.what) {
            case 0:
                synchronized (this.zzdu) {
                    GmsClientSupervisor.zza zza = (GmsClientSupervisor.zza) message.obj;
                    zzf zzf = this.zzdu.get(zza);
                    if (zzf != null && zzf.zzr()) {
                        if (zzf.isBound()) {
                            zzf.zzf("GmsClientSupervisor");
                        }
                        this.zzdu.remove(zza);
                    }
                }
                return true;
            case 1:
                synchronized (this.zzdu) {
                    GmsClientSupervisor.zza zza2 = (GmsClientSupervisor.zza) message.obj;
                    zzf zzf2 = this.zzdu.get(zza2);
                    if (zzf2 != null && zzf2.getState() == 3) {
                        String valueOf = String.valueOf(zza2);
                        Log.e("GmsClientSupervisor", new StringBuilder(String.valueOf(valueOf).length() + 47).append("Timeout waiting for ServiceConnection callback ").append(valueOf).toString(), new Exception());
                        ComponentName componentName2 = zzf2.getComponentName();
                        if (componentName2 == null) {
                            componentName2 = zza2.getComponentName();
                        }
                        if (componentName2 == null) {
                            componentName = new ComponentName(zza2.getPackage(), EnvironmentCompat.MEDIA_UNKNOWN);
                        } else {
                            componentName = componentName2;
                        }
                        zzf2.onServiceDisconnected(componentName);
                    }
                }
                return true;
            default:
                return false;
        }
    }
}
