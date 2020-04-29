package com.google.android.gms.security;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.GooglePlayServicesUtilLight;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.CrashUtils;
import com.google.android.gms.dynamite.DynamiteModule;
import java.lang.reflect.Method;

public class ProviderInstaller {
    public static final String PROVIDER_NAME = "GmsCore_OpenSSL";
    private static final Object lock = new Object();
    /* access modifiers changed from: private */
    public static final GoogleApiAvailabilityLight zziv = GoogleApiAvailabilityLight.getInstance();
    private static Method zziw = null;

    public interface ProviderInstallListener {
        void onProviderInstallFailed(int i, Intent intent);

        void onProviderInstalled();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v5, resolved type: java.lang.Exception} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: java.lang.Throwable} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v6, resolved type: java.lang.Exception} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v7, resolved type: java.lang.Exception} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void installIfNeeded(android.content.Context r7) throws com.google.android.gms.common.GooglePlayServicesRepairableException, com.google.android.gms.common.GooglePlayServicesNotAvailableException {
        /*
            r2 = 8
            java.lang.String r0 = "Context must not be null"
            com.google.android.gms.common.internal.Preconditions.checkNotNull(r7, r0)
            com.google.android.gms.common.GoogleApiAvailabilityLight r0 = com.google.android.gms.security.ProviderInstaller.zziv
            r1 = 11925000(0xb5f608, float:1.6710484E-38)
            r0.verifyGooglePlayServicesIsAvailable(r7, r1)
            android.content.Context r0 = zzk(r7)
            if (r0 != 0) goto L_0x001a
            android.content.Context r0 = zzl(r7)
        L_0x001a:
            if (r0 != 0) goto L_0x002b
            java.lang.String r0 = "ProviderInstaller"
            java.lang.String r1 = "Failed to get remote context"
            android.util.Log.e(r0, r1)
            com.google.android.gms.common.GooglePlayServicesNotAvailableException r0 = new com.google.android.gms.common.GooglePlayServicesNotAvailableException
            r0.<init>(r2)
            throw r0
        L_0x002b:
            java.lang.Object r3 = com.google.android.gms.security.ProviderInstaller.lock
            monitor-enter(r3)
            java.lang.reflect.Method r1 = com.google.android.gms.security.ProviderInstaller.zziw     // Catch:{ Exception -> 0x005c }
            if (r1 != 0) goto L_0x004e
            java.lang.ClassLoader r1 = r0.getClassLoader()     // Catch:{ Exception -> 0x005c }
            java.lang.String r2 = "com.google.android.gms.common.security.ProviderInstallerImpl"
            java.lang.Class r1 = r1.loadClass(r2)     // Catch:{ Exception -> 0x005c }
            r2 = 1
            java.lang.Class[] r2 = new java.lang.Class[r2]     // Catch:{ Exception -> 0x005c }
            r4 = 0
            java.lang.Class<android.content.Context> r5 = android.content.Context.class
            r2[r4] = r5     // Catch:{ Exception -> 0x005c }
            java.lang.String r4 = "insertProvider"
            java.lang.reflect.Method r1 = r1.getMethod(r4, r2)     // Catch:{ Exception -> 0x005c }
            com.google.android.gms.security.ProviderInstaller.zziw = r1     // Catch:{ Exception -> 0x005c }
        L_0x004e:
            java.lang.reflect.Method r1 = com.google.android.gms.security.ProviderInstaller.zziw     // Catch:{ Exception -> 0x005c }
            r2 = 0
            r4 = 1
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Exception -> 0x005c }
            r5 = 0
            r4[r5] = r0     // Catch:{ Exception -> 0x005c }
            r1.invoke(r2, r4)     // Catch:{ Exception -> 0x005c }
            monitor-exit(r3)     // Catch:{ all -> 0x0095 }
            return
        L_0x005c:
            r0 = move-exception
            java.lang.Throwable r1 = r0.getCause()     // Catch:{ all -> 0x0095 }
            java.lang.String r2 = "ProviderInstaller"
            r4 = 6
            boolean r2 = android.util.Log.isLoggable(r2, r4)     // Catch:{ all -> 0x0095 }
            if (r2 == 0) goto L_0x0088
            if (r1 != 0) goto L_0x0098
            java.lang.String r2 = r0.getMessage()     // Catch:{ all -> 0x0095 }
        L_0x0071:
            java.lang.String r4 = "ProviderInstaller"
            java.lang.String r5 = "Failed to install provider: "
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ all -> 0x0095 }
            int r6 = r2.length()     // Catch:{ all -> 0x0095 }
            if (r6 == 0) goto L_0x009d
            java.lang.String r2 = r5.concat(r2)     // Catch:{ all -> 0x0095 }
        L_0x0085:
            android.util.Log.e(r4, r2)     // Catch:{ all -> 0x0095 }
        L_0x0088:
            if (r1 != 0) goto L_0x00a3
        L_0x008a:
            com.google.android.gms.common.util.CrashUtils.addDynamiteErrorToDropBox(r7, r0)     // Catch:{ all -> 0x0095 }
            com.google.android.gms.common.GooglePlayServicesNotAvailableException r0 = new com.google.android.gms.common.GooglePlayServicesNotAvailableException     // Catch:{ all -> 0x0095 }
            r1 = 8
            r0.<init>(r1)     // Catch:{ all -> 0x0095 }
            throw r0     // Catch:{ all -> 0x0095 }
        L_0x0095:
            r0 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0095 }
            throw r0
        L_0x0098:
            java.lang.String r2 = r1.getMessage()     // Catch:{ all -> 0x0095 }
            goto L_0x0071
        L_0x009d:
            java.lang.String r2 = new java.lang.String     // Catch:{ all -> 0x0095 }
            r2.<init>(r5)     // Catch:{ all -> 0x0095 }
            goto L_0x0085
        L_0x00a3:
            r0 = r1
            goto L_0x008a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.security.ProviderInstaller.installIfNeeded(android.content.Context):void");
    }

    public static void installIfNeededAsync(Context context, ProviderInstallListener providerInstallListener) {
        Preconditions.checkNotNull(context, "Context must not be null");
        Preconditions.checkNotNull(providerInstallListener, "Listener must not be null");
        Preconditions.checkMainThread("Must be called on the UI thread");
        new zza(context, providerInstallListener).execute(new Void[0]);
    }

    @Nullable
    private static Context zzk(Context context) {
        try {
            return DynamiteModule.load(context, DynamiteModule.PREFER_HIGHEST_OR_LOCAL_VERSION_NO_FORCE_STAGING, "providerinstaller").getModuleContext();
        } catch (DynamiteModule.LoadingException e) {
            String valueOf = String.valueOf(e.getMessage());
            Log.w("ProviderInstaller", valueOf.length() != 0 ? "Failed to load providerinstaller module: ".concat(valueOf) : new String("Failed to load providerinstaller module: "));
            return null;
        }
    }

    @Nullable
    private static Context zzl(Context context) {
        try {
            return GooglePlayServicesUtilLight.getRemoteContext(context);
        } catch (Resources.NotFoundException e) {
            String valueOf = String.valueOf(e.getMessage());
            Log.w("ProviderInstaller", valueOf.length() != 0 ? "Failed to load GMS Core context for providerinstaller: ".concat(valueOf) : new String("Failed to load GMS Core context for providerinstaller: "));
            CrashUtils.addDynamiteErrorToDropBox(context, e);
            return null;
        }
    }
}
