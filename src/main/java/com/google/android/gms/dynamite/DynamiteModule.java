package com.google.android.gms.dynamite;

import android.content.Context;
import android.database.Cursor;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.DynamiteApi;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.ObjectWrapper;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import javax.annotation.concurrent.GuardedBy;

@KeepForSdk
public final class DynamiteModule {
    @KeepForSdk
    public static final VersionPolicy PREFER_HIGHEST_OR_LOCAL_VERSION = new zzd();
    @KeepForSdk
    public static final VersionPolicy PREFER_HIGHEST_OR_LOCAL_VERSION_NO_FORCE_STAGING = new zze();
    @KeepForSdk
    public static final VersionPolicy PREFER_HIGHEST_OR_REMOTE_VERSION = new zzf();
    @KeepForSdk
    public static final VersionPolicy PREFER_LOCAL = new zzc();
    @KeepForSdk
    public static final VersionPolicy PREFER_REMOTE = new zzb();
    @GuardedBy("DynamiteModule.class")
    private static Boolean zzif;
    @GuardedBy("DynamiteModule.class")
    private static zzi zzig;
    @GuardedBy("DynamiteModule.class")
    private static zzk zzih;
    @GuardedBy("DynamiteModule.class")
    private static String zzii;
    @GuardedBy("DynamiteModule.class")
    private static int zzij = -1;
    private static final ThreadLocal<zza> zzik = new ThreadLocal<>();
    private static final VersionPolicy.zza zzil = new zza();
    private static final VersionPolicy zzim = new zzg();
    private final Context zzin;

    @DynamiteApi
    public static class DynamiteLoaderClassLoader {
        @GuardedBy("DynamiteLoaderClassLoader.class")
        public static ClassLoader sClassLoader;
    }

    public interface VersionPolicy {

        public interface zza {
            int getLocalVersion(Context context, String str);

            int zza(Context context, String str, boolean z) throws LoadingException;
        }

        public static class zzb {
            public int zzir = 0;
            public int zzis = 0;
            public int zzit = 0;
        }

        zzb zza(Context context, String str, zza zza2) throws LoadingException;
    }

    private static class zza {
        public Cursor zzio;

        private zza() {
        }

        /* synthetic */ zza(zza zza) {
            this();
        }
    }

    @KeepForSdk
    public static DynamiteModule load(Context context, VersionPolicy versionPolicy, String str) throws LoadingException {
        VersionPolicy.zzb zza2;
        zza zza3 = zzik.get();
        zza zza4 = new zza(null);
        zzik.set(zza4);
        try {
            zza2 = versionPolicy.zza(context, str, zzil);
            Log.i("DynamiteModule", new StringBuilder(String.valueOf(str).length() + 68 + String.valueOf(str).length()).append("Considering local module ").append(str).append(":").append(zza2.zzir).append(" and remote module ").append(str).append(":").append(zza2.zzis).toString());
            if (zza2.zzit == 0 || ((zza2.zzit == -1 && zza2.zzir == 0) || (zza2.zzit == 1 && zza2.zzis == 0))) {
                throw new LoadingException(new StringBuilder(91).append("No acceptable module found. Local version is ").append(zza2.zzir).append(" and remote version is ").append(zza2.zzis).append(".").toString(), (zza) null);
            } else if (zza2.zzit == -1) {
                DynamiteModule zze = zze(context, str);
                if (zza4.zzio != null) {
                    zza4.zzio.close();
                }
                zzik.set(zza3);
                return zze;
            } else if (zza2.zzit == 1) {
                DynamiteModule zza5 = zza(context, str, zza2.zzis);
                if (zza4.zzio != null) {
                    zza4.zzio.close();
                }
                zzik.set(zza3);
                return zza5;
            } else {
                throw new LoadingException(new StringBuilder(47).append("VersionPolicy returned invalid code:").append(zza2.zzit).toString(), (zza) null);
            }
        } catch (LoadingException e) {
            String valueOf = String.valueOf(e.getMessage());
            Log.w("DynamiteModule", valueOf.length() != 0 ? "Failed to load remote module: ".concat(valueOf) : new String("Failed to load remote module: "));
            if (zza2.zzir == 0 || versionPolicy.zza(context, str, new zzb(zza2.zzir, 0)).zzit != -1) {
                throw new LoadingException("Remote load failed. No local fallback found.", e, null);
            }
            DynamiteModule zze2 = zze(context, str);
            if (zza4.zzio != null) {
                zza4.zzio.close();
            }
            zzik.set(zza3);
            return zze2;
        } catch (Throwable th) {
            if (zza4.zzio != null) {
                zza4.zzio.close();
            }
            zzik.set(zza3);
            throw th;
        }
    }

    @KeepForSdk
    public static class LoadingException extends Exception {
        private LoadingException(String str) {
            super(str);
        }

        private LoadingException(String str, Throwable th) {
            super(str, th);
        }

        /* synthetic */ LoadingException(String str, zza zza) {
            this(str);
        }

        /* synthetic */ LoadingException(String str, Throwable th, zza zza) {
            this(str, th);
        }
    }

    private static class zzb implements VersionPolicy.zza {
        private final int zzip;
        private final int zziq = 0;

        public zzb(int i, int i2) {
            this.zzip = i;
        }

        public final int zza(Context context, String str, boolean z) {
            return 0;
        }

        public final int getLocalVersion(Context context, String str) {
            return this.zzip;
        }
    }

    @KeepForSdk
    public static int getLocalVersion(Context context, String str) {
        try {
            Class<?> loadClass = context.getApplicationContext().getClassLoader().loadClass(new StringBuilder(String.valueOf(str).length() + 61).append("com.google.android.gms.dynamite.descriptors.").append(str).append(".ModuleDescriptor").toString());
            Field declaredField = loadClass.getDeclaredField("MODULE_ID");
            Field declaredField2 = loadClass.getDeclaredField("MODULE_VERSION");
            if (declaredField.get(null).equals(str)) {
                return declaredField2.getInt(null);
            }
            String valueOf = String.valueOf(declaredField.get(null));
            Log.e("DynamiteModule", new StringBuilder(String.valueOf(valueOf).length() + 51 + String.valueOf(str).length()).append("Module descriptor id '").append(valueOf).append("' didn't match expected id '").append(str).append("'").toString());
            return 0;
        } catch (ClassNotFoundException e) {
            Log.w("DynamiteModule", new StringBuilder(String.valueOf(str).length() + 45).append("Local module descriptor class for ").append(str).append(" not found.").toString());
            return 0;
        } catch (Exception e2) {
            String valueOf2 = String.valueOf(e2.getMessage());
            Log.e("DynamiteModule", valueOf2.length() != 0 ? "Failed to load module descriptor class: ".concat(valueOf2) : new String("Failed to load module descriptor class: "));
            return 0;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x003c A[SYNTHETIC, Splitter:B:23:0x003c] */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x00f8 A[Catch:{ Throwable -> 0x0079 }] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:37:0x0073=Splitter:B:37:0x0073, B:27:0x0044=Splitter:B:27:0x0044} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int zza(android.content.Context r7, java.lang.String r8, boolean r9) {
        /*
            java.lang.Class<com.google.android.gms.dynamite.DynamiteModule> r1 = com.google.android.gms.dynamite.DynamiteModule.class
            monitor-enter(r1)     // Catch:{ Throwable -> 0x0079 }
            java.lang.Boolean r0 = com.google.android.gms.dynamite.DynamiteModule.zzif     // Catch:{ all -> 0x0076 }
            if (r0 != 0) goto L_0x0035
            android.content.Context r0 = r7.getApplicationContext()     // Catch:{ ClassNotFoundException -> 0x00a6, IllegalAccessException -> 0x0103, NoSuchFieldException -> 0x0101 }
            java.lang.ClassLoader r0 = r0.getClassLoader()     // Catch:{ ClassNotFoundException -> 0x00a6, IllegalAccessException -> 0x0103, NoSuchFieldException -> 0x0101 }
            java.lang.Class<com.google.android.gms.dynamite.DynamiteModule$DynamiteLoaderClassLoader> r2 = com.google.android.gms.dynamite.DynamiteModule.DynamiteLoaderClassLoader.class
            java.lang.String r2 = r2.getName()     // Catch:{ ClassNotFoundException -> 0x00a6, IllegalAccessException -> 0x0103, NoSuchFieldException -> 0x0101 }
            java.lang.Class r2 = r0.loadClass(r2)     // Catch:{ ClassNotFoundException -> 0x00a6, IllegalAccessException -> 0x0103, NoSuchFieldException -> 0x0101 }
            java.lang.String r0 = "sClassLoader"
            java.lang.reflect.Field r3 = r2.getDeclaredField(r0)     // Catch:{ ClassNotFoundException -> 0x00a6, IllegalAccessException -> 0x0103, NoSuchFieldException -> 0x0101 }
            monitor-enter(r2)     // Catch:{ ClassNotFoundException -> 0x00a6, IllegalAccessException -> 0x0103, NoSuchFieldException -> 0x0101 }
            r0 = 0
            java.lang.Object r0 = r3.get(r0)     // Catch:{ all -> 0x00a3 }
            java.lang.ClassLoader r0 = (java.lang.ClassLoader) r0     // Catch:{ all -> 0x00a3 }
            if (r0 == 0) goto L_0x0047
            java.lang.ClassLoader r3 = java.lang.ClassLoader.getSystemClassLoader()     // Catch:{ all -> 0x00a3 }
            if (r0 != r3) goto L_0x0041
            java.lang.Boolean r0 = java.lang.Boolean.FALSE     // Catch:{ all -> 0x00a3 }
        L_0x0032:
            monitor-exit(r2)     // Catch:{ all -> 0x00a3 }
        L_0x0033:
            com.google.android.gms.dynamite.DynamiteModule.zzif = r0     // Catch:{ all -> 0x0076 }
        L_0x0035:
            monitor-exit(r1)     // Catch:{ all -> 0x0076 }
            boolean r0 = r0.booleanValue()     // Catch:{ Throwable -> 0x0079 }
            if (r0 == 0) goto L_0x00f8
            int r0 = zzc(r7, r8, r9)     // Catch:{ LoadingException -> 0x00d3 }
        L_0x0040:
            return r0
        L_0x0041:
            zza(r0)     // Catch:{ LoadingException -> 0x00fe }
        L_0x0044:
            java.lang.Boolean r0 = java.lang.Boolean.TRUE     // Catch:{ all -> 0x00a3 }
            goto L_0x0032
        L_0x0047:
            java.lang.String r0 = "com.google.android.gms"
            android.content.Context r4 = r7.getApplicationContext()     // Catch:{ all -> 0x00a3 }
            java.lang.String r4 = r4.getPackageName()     // Catch:{ all -> 0x00a3 }
            boolean r0 = r0.equals(r4)     // Catch:{ all -> 0x00a3 }
            if (r0 == 0) goto L_0x0063
            r0 = 0
            java.lang.ClassLoader r4 = java.lang.ClassLoader.getSystemClassLoader()     // Catch:{ all -> 0x00a3 }
            r3.set(r0, r4)     // Catch:{ all -> 0x00a3 }
            java.lang.Boolean r0 = java.lang.Boolean.FALSE     // Catch:{ all -> 0x00a3 }
            goto L_0x0032
        L_0x0063:
            int r0 = zzc(r7, r8, r9)     // Catch:{ LoadingException -> 0x0097 }
            java.lang.String r4 = com.google.android.gms.dynamite.DynamiteModule.zzii     // Catch:{ LoadingException -> 0x0097 }
            if (r4 == 0) goto L_0x0073
            java.lang.String r4 = com.google.android.gms.dynamite.DynamiteModule.zzii     // Catch:{ LoadingException -> 0x0097 }
            boolean r4 = r4.isEmpty()     // Catch:{ LoadingException -> 0x0097 }
            if (r4 == 0) goto L_0x007e
        L_0x0073:
            monitor-exit(r2)     // Catch:{ all -> 0x00a3 }
            monitor-exit(r1)     // Catch:{ all -> 0x0076 }
            goto L_0x0040
        L_0x0076:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0076 }
            throw r0     // Catch:{ Throwable -> 0x0079 }
        L_0x0079:
            r0 = move-exception
            com.google.android.gms.common.util.CrashUtils.addDynamiteErrorToDropBox(r7, r0)
            throw r0
        L_0x007e:
            com.google.android.gms.dynamite.zzh r4 = new com.google.android.gms.dynamite.zzh     // Catch:{ LoadingException -> 0x0097 }
            java.lang.String r5 = com.google.android.gms.dynamite.DynamiteModule.zzii     // Catch:{ LoadingException -> 0x0097 }
            java.lang.ClassLoader r6 = java.lang.ClassLoader.getSystemClassLoader()     // Catch:{ LoadingException -> 0x0097 }
            r4.<init>(r5, r6)     // Catch:{ LoadingException -> 0x0097 }
            zza(r4)     // Catch:{ LoadingException -> 0x0097 }
            r5 = 0
            r3.set(r5, r4)     // Catch:{ LoadingException -> 0x0097 }
            java.lang.Boolean r4 = java.lang.Boolean.TRUE     // Catch:{ LoadingException -> 0x0097 }
            com.google.android.gms.dynamite.DynamiteModule.zzif = r4     // Catch:{ LoadingException -> 0x0097 }
            monitor-exit(r2)     // Catch:{ all -> 0x00a3 }
            monitor-exit(r1)     // Catch:{ all -> 0x0076 }
            goto L_0x0040
        L_0x0097:
            r0 = move-exception
            r0 = 0
            java.lang.ClassLoader r4 = java.lang.ClassLoader.getSystemClassLoader()     // Catch:{ all -> 0x00a3 }
            r3.set(r0, r4)     // Catch:{ all -> 0x00a3 }
            java.lang.Boolean r0 = java.lang.Boolean.FALSE     // Catch:{ all -> 0x00a3 }
            goto L_0x0032
        L_0x00a3:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x00a3 }
            throw r0     // Catch:{ ClassNotFoundException -> 0x00a6, IllegalAccessException -> 0x0103, NoSuchFieldException -> 0x0101 }
        L_0x00a6:
            r0 = move-exception
        L_0x00a7:
            java.lang.String r2 = "DynamiteModule"
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ all -> 0x0076 }
            java.lang.String r3 = java.lang.String.valueOf(r0)     // Catch:{ all -> 0x0076 }
            int r3 = r3.length()     // Catch:{ all -> 0x0076 }
            int r3 = r3 + 30
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0076 }
            r4.<init>(r3)     // Catch:{ all -> 0x0076 }
            java.lang.String r3 = "Failed to load module via V2: "
            java.lang.StringBuilder r3 = r4.append(r3)     // Catch:{ all -> 0x0076 }
            java.lang.StringBuilder r0 = r3.append(r0)     // Catch:{ all -> 0x0076 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0076 }
            android.util.Log.w(r2, r0)     // Catch:{ all -> 0x0076 }
            java.lang.Boolean r0 = java.lang.Boolean.FALSE     // Catch:{ all -> 0x0076 }
            goto L_0x0033
        L_0x00d3:
            r0 = move-exception
            java.lang.String r1 = "DynamiteModule"
            java.lang.String r2 = "Failed to retrieve remote module version: "
            java.lang.String r0 = r0.getMessage()     // Catch:{ Throwable -> 0x0079 }
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Throwable -> 0x0079 }
            int r3 = r0.length()     // Catch:{ Throwable -> 0x0079 }
            if (r3 == 0) goto L_0x00f2
            java.lang.String r0 = r2.concat(r0)     // Catch:{ Throwable -> 0x0079 }
        L_0x00ec:
            android.util.Log.w(r1, r0)     // Catch:{ Throwable -> 0x0079 }
            r0 = 0
            goto L_0x0040
        L_0x00f2:
            java.lang.String r0 = new java.lang.String     // Catch:{ Throwable -> 0x0079 }
            r0.<init>(r2)     // Catch:{ Throwable -> 0x0079 }
            goto L_0x00ec
        L_0x00f8:
            int r0 = zzb(r7, r8, r9)     // Catch:{ Throwable -> 0x0079 }
            goto L_0x0040
        L_0x00fe:
            r0 = move-exception
            goto L_0x0044
        L_0x0101:
            r0 = move-exception
            goto L_0x00a7
        L_0x0103:
            r0 = move-exception
            goto L_0x00a7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.dynamite.DynamiteModule.zza(android.content.Context, java.lang.String, boolean):int");
    }

    private static int zzb(Context context, String str, boolean z) {
        zzi zzj = zzj(context);
        if (zzj == null) {
            return 0;
        }
        try {
            if (zzj.zzak() >= 2) {
                return zzj.zzb(ObjectWrapper.wrap(context), str, z);
            }
            Log.w("DynamiteModule", "IDynamite loader version < 2, falling back to getModuleVersion2");
            return zzj.zza(ObjectWrapper.wrap(context), str, z);
        } catch (RemoteException e) {
            String valueOf = String.valueOf(e.getMessage());
            Log.w("DynamiteModule", valueOf.length() != 0 ? "Failed to retrieve remote module version: ".concat(valueOf) : new String("Failed to retrieve remote module version: "));
            return 0;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x006c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int zzc(android.content.Context r7, java.lang.String r8, boolean r9) throws com.google.android.gms.dynamite.DynamiteModule.LoadingException {
        /*
            r6 = 0
            android.content.ContentResolver r0 = r7.getContentResolver()     // Catch:{ Exception -> 0x00bc, all -> 0x00b9 }
            if (r9 == 0) goto L_0x0070
            java.lang.String r1 = "api_force_staging"
        L_0x000a:
            java.lang.String r2 = java.lang.String.valueOf(r1)     // Catch:{ Exception -> 0x00bc, all -> 0x00b9 }
            int r2 = r2.length()     // Catch:{ Exception -> 0x00bc, all -> 0x00b9 }
            int r2 = r2 + 42
            java.lang.String r3 = java.lang.String.valueOf(r8)     // Catch:{ Exception -> 0x00bc, all -> 0x00b9 }
            int r3 = r3.length()     // Catch:{ Exception -> 0x00bc, all -> 0x00b9 }
            int r2 = r2 + r3
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00bc, all -> 0x00b9 }
            r3.<init>(r2)     // Catch:{ Exception -> 0x00bc, all -> 0x00b9 }
            java.lang.String r2 = "content://com.google.android.gms.chimera/"
            java.lang.StringBuilder r2 = r3.append(r2)     // Catch:{ Exception -> 0x00bc, all -> 0x00b9 }
            java.lang.StringBuilder r1 = r2.append(r1)     // Catch:{ Exception -> 0x00bc, all -> 0x00b9 }
            java.lang.String r2 = "/"
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Exception -> 0x00bc, all -> 0x00b9 }
            java.lang.StringBuilder r1 = r1.append(r8)     // Catch:{ Exception -> 0x00bc, all -> 0x00b9 }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x00bc, all -> 0x00b9 }
            android.net.Uri r1 = android.net.Uri.parse(r1)     // Catch:{ Exception -> 0x00bc, all -> 0x00b9 }
            r2 = 0
            r3 = 0
            r4 = 0
            r5 = 0
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5)     // Catch:{ Exception -> 0x00bc, all -> 0x00b9 }
            if (r1 == 0) goto L_0x0050
            boolean r0 = r1.moveToFirst()     // Catch:{ Exception -> 0x0063 }
            if (r0 != 0) goto L_0x0074
        L_0x0050:
            java.lang.String r0 = "DynamiteModule"
            java.lang.String r2 = "Failed to retrieve remote module version."
            android.util.Log.w(r0, r2)     // Catch:{ Exception -> 0x0063 }
            com.google.android.gms.dynamite.DynamiteModule$LoadingException r0 = new com.google.android.gms.dynamite.DynamiteModule$LoadingException     // Catch:{ Exception -> 0x0063 }
            java.lang.String r2 = "Failed to connect to dynamite module ContentResolver."
            r3 = 0
            r0.<init>(r2, r3)     // Catch:{ Exception -> 0x0063 }
            throw r0     // Catch:{ Exception -> 0x0063 }
        L_0x0063:
            r0 = move-exception
        L_0x0064:
            boolean r2 = r0 instanceof com.google.android.gms.dynamite.DynamiteModule.LoadingException     // Catch:{ all -> 0x0069 }
            if (r2 == 0) goto L_0x00af
            throw r0     // Catch:{ all -> 0x0069 }
        L_0x0069:
            r0 = move-exception
        L_0x006a:
            if (r1 == 0) goto L_0x006f
            r1.close()
        L_0x006f:
            throw r0
        L_0x0070:
            java.lang.String r1 = "api"
            goto L_0x000a
        L_0x0074:
            r0 = 0
            int r2 = r1.getInt(r0)     // Catch:{ Exception -> 0x0063 }
            if (r2 <= 0) goto L_0x00a6
            java.lang.Class<com.google.android.gms.dynamite.DynamiteModule> r3 = com.google.android.gms.dynamite.DynamiteModule.class
            monitor-enter(r3)     // Catch:{ Exception -> 0x0063 }
            r0 = 2
            java.lang.String r0 = r1.getString(r0)     // Catch:{ all -> 0x00ac }
            com.google.android.gms.dynamite.DynamiteModule.zzii = r0     // Catch:{ all -> 0x00ac }
            java.lang.String r0 = "loaderVersion"
            int r0 = r1.getColumnIndex(r0)     // Catch:{ all -> 0x00ac }
            if (r0 < 0) goto L_0x0094
            int r0 = r1.getInt(r0)     // Catch:{ all -> 0x00ac }
            com.google.android.gms.dynamite.DynamiteModule.zzij = r0     // Catch:{ all -> 0x00ac }
        L_0x0094:
            monitor-exit(r3)     // Catch:{ all -> 0x00ac }
            java.lang.ThreadLocal<com.google.android.gms.dynamite.DynamiteModule$zza> r0 = com.google.android.gms.dynamite.DynamiteModule.zzik     // Catch:{ Exception -> 0x0063 }
            java.lang.Object r0 = r0.get()     // Catch:{ Exception -> 0x0063 }
            com.google.android.gms.dynamite.DynamiteModule$zza r0 = (com.google.android.gms.dynamite.DynamiteModule.zza) r0     // Catch:{ Exception -> 0x0063 }
            if (r0 == 0) goto L_0x00a6
            android.database.Cursor r3 = r0.zzio     // Catch:{ Exception -> 0x0063 }
            if (r3 != 0) goto L_0x00a6
            r0.zzio = r1     // Catch:{ Exception -> 0x0063 }
            r1 = r6
        L_0x00a6:
            if (r1 == 0) goto L_0x00ab
            r1.close()
        L_0x00ab:
            return r2
        L_0x00ac:
            r0 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x00ac }
            throw r0     // Catch:{ Exception -> 0x0063 }
        L_0x00af:
            com.google.android.gms.dynamite.DynamiteModule$LoadingException r2 = new com.google.android.gms.dynamite.DynamiteModule$LoadingException     // Catch:{ all -> 0x0069 }
            java.lang.String r3 = "V2 version check failed"
            r4 = 0
            r2.<init>(r3, r0, r4)     // Catch:{ all -> 0x0069 }
            throw r2     // Catch:{ all -> 0x0069 }
        L_0x00b9:
            r0 = move-exception
            r1 = r6
            goto L_0x006a
        L_0x00bc:
            r0 = move-exception
            r1 = r6
            goto L_0x0064
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.dynamite.DynamiteModule.zzc(android.content.Context, java.lang.String, boolean):int");
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.android.gms.dynamite.DynamiteModule.zza(android.content.Context, java.lang.String, boolean):int
     arg types: [android.content.Context, java.lang.String, int]
     candidates:
      com.google.android.gms.dynamite.DynamiteModule.zza(android.content.Context, java.lang.String, int):com.google.android.gms.dynamite.DynamiteModule
      com.google.android.gms.dynamite.DynamiteModule.zza(android.content.Context, java.lang.String, boolean):int */
    @KeepForSdk
    public static int getRemoteVersion(Context context, String str) {
        return zza(context, str, false);
    }

    private static DynamiteModule zze(Context context, String str) {
        String valueOf = String.valueOf(str);
        Log.i("DynamiteModule", valueOf.length() != 0 ? "Selected local version of ".concat(valueOf) : new String("Selected local version of "));
        return new DynamiteModule(context.getApplicationContext());
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static com.google.android.gms.dynamite.DynamiteModule zza(android.content.Context r4, java.lang.String r5, int r6) throws com.google.android.gms.dynamite.DynamiteModule.LoadingException {
        /*
            r3 = 0
            java.lang.Class<com.google.android.gms.dynamite.DynamiteModule> r1 = com.google.android.gms.dynamite.DynamiteModule.class
            monitor-enter(r1)     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            java.lang.Boolean r0 = com.google.android.gms.dynamite.DynamiteModule.zzif     // Catch:{ all -> 0x001d }
            monitor-exit(r1)     // Catch:{ all -> 0x001d }
            if (r0 != 0) goto L_0x0022
            com.google.android.gms.dynamite.DynamiteModule$LoadingException r0 = new com.google.android.gms.dynamite.DynamiteModule$LoadingException     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            java.lang.String r1 = "Failed to determine which loading route to use."
            r2 = 0
            r0.<init>(r1, r2)     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            throw r0     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
        L_0x0013:
            r0 = move-exception
            com.google.android.gms.dynamite.DynamiteModule$LoadingException r1 = new com.google.android.gms.dynamite.DynamiteModule$LoadingException
            java.lang.String r2 = "Failed to load remote module."
            r1.<init>(r2, r0, r3)
            throw r1
        L_0x001d:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x001d }
            throw r0     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
        L_0x0020:
            r0 = move-exception
            throw r0
        L_0x0022:
            boolean r0 = r0.booleanValue()     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            if (r0 == 0) goto L_0x002d
            com.google.android.gms.dynamite.DynamiteModule r0 = zzb(r4, r5, r6)     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
        L_0x002c:
            return r0
        L_0x002d:
            java.lang.String r0 = "DynamiteModule"
            java.lang.String r1 = java.lang.String.valueOf(r5)     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            int r1 = r1.length()     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            int r1 = r1 + 51
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            r2.<init>(r1)     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            java.lang.String r1 = "Selected remote version of "
            java.lang.StringBuilder r1 = r2.append(r1)     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            java.lang.StringBuilder r1 = r1.append(r5)     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            java.lang.String r2 = ", version >= "
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            java.lang.StringBuilder r1 = r1.append(r6)     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            java.lang.String r1 = r1.toString()     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            android.util.Log.i(r0, r1)     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            com.google.android.gms.dynamite.zzi r0 = zzj(r4)     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            if (r0 != 0) goto L_0x0079
            com.google.android.gms.dynamite.DynamiteModule$LoadingException r0 = new com.google.android.gms.dynamite.DynamiteModule$LoadingException     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            java.lang.String r1 = "Failed to create IDynamiteLoader."
            r2 = 0
            r0.<init>(r1, r2)     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            throw r0     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
        L_0x006c:
            r0 = move-exception
            com.google.android.gms.common.util.CrashUtils.addDynamiteErrorToDropBox(r4, r0)
            com.google.android.gms.dynamite.DynamiteModule$LoadingException r1 = new com.google.android.gms.dynamite.DynamiteModule$LoadingException
            java.lang.String r2 = "Failed to load remote module."
            r1.<init>(r2, r0, r3)
            throw r1
        L_0x0079:
            int r1 = r0.zzak()     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            r2 = 2
            if (r1 < r2) goto L_0x0098
            com.google.android.gms.dynamic.IObjectWrapper r1 = com.google.android.gms.dynamic.ObjectWrapper.wrap(r4)     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            com.google.android.gms.dynamic.IObjectWrapper r0 = r0.zzb(r1, r5, r6)     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
        L_0x0088:
            java.lang.Object r1 = com.google.android.gms.dynamic.ObjectWrapper.unwrap(r0)     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            if (r1 != 0) goto L_0x00aa
            com.google.android.gms.dynamite.DynamiteModule$LoadingException r0 = new com.google.android.gms.dynamite.DynamiteModule$LoadingException     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            java.lang.String r1 = "Failed to load remote module."
            r2 = 0
            r0.<init>(r1, r2)     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            throw r0     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
        L_0x0098:
            java.lang.String r1 = "DynamiteModule"
            java.lang.String r2 = "Dynamite loader version < 2, falling back to createModuleContext"
            android.util.Log.w(r1, r2)     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            com.google.android.gms.dynamic.IObjectWrapper r1 = com.google.android.gms.dynamic.ObjectWrapper.wrap(r4)     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            com.google.android.gms.dynamic.IObjectWrapper r0 = r0.zza(r1, r5, r6)     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            goto L_0x0088
        L_0x00aa:
            com.google.android.gms.dynamite.DynamiteModule r1 = new com.google.android.gms.dynamite.DynamiteModule     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            java.lang.Object r0 = com.google.android.gms.dynamic.ObjectWrapper.unwrap(r0)     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            android.content.Context r0 = (android.content.Context) r0     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            r1.<init>(r0)     // Catch:{ RemoteException -> 0x0013, LoadingException -> 0x0020, Throwable -> 0x006c }
            r0 = r1
            goto L_0x002c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.dynamite.DynamiteModule.zza(android.content.Context, java.lang.String, int):com.google.android.gms.dynamite.DynamiteModule");
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static com.google.android.gms.dynamite.zzi zzj(android.content.Context r7) {
        /*
            r3 = 0
            java.lang.Class<com.google.android.gms.dynamite.DynamiteModule> r4 = com.google.android.gms.dynamite.DynamiteModule.class
            monitor-enter(r4)
            com.google.android.gms.dynamite.zzi r1 = com.google.android.gms.dynamite.DynamiteModule.zzig     // Catch:{ all -> 0x003b }
            if (r1 == 0) goto L_0x000c
            com.google.android.gms.dynamite.zzi r1 = com.google.android.gms.dynamite.DynamiteModule.zzig     // Catch:{ all -> 0x003b }
            monitor-exit(r4)     // Catch:{ all -> 0x003b }
        L_0x000b:
            return r1
        L_0x000c:
            com.google.android.gms.common.GoogleApiAvailabilityLight r1 = com.google.android.gms.common.GoogleApiAvailabilityLight.getInstance()     // Catch:{ all -> 0x003b }
            int r1 = r1.isGooglePlayServicesAvailable(r7)     // Catch:{ all -> 0x003b }
            if (r1 == 0) goto L_0x0019
            monitor-exit(r4)     // Catch:{ all -> 0x003b }
            r1 = r3
            goto L_0x000b
        L_0x0019:
            java.lang.String r1 = "com.google.android.gms"
            r2 = 3
            android.content.Context r1 = r7.createPackageContext(r1, r2)     // Catch:{ Exception -> 0x0055 }
            java.lang.ClassLoader r1 = r1.getClassLoader()     // Catch:{ Exception -> 0x0055 }
            java.lang.String r2 = "com.google.android.gms.chimera.container.DynamiteLoaderImpl"
            java.lang.Class r1 = r1.loadClass(r2)     // Catch:{ Exception -> 0x0055 }
            java.lang.Object r1 = r1.newInstance()     // Catch:{ Exception -> 0x0055 }
            android.os.IBinder r1 = (android.os.IBinder) r1     // Catch:{ Exception -> 0x0055 }
            if (r1 != 0) goto L_0x003e
            r1 = r3
        L_0x0035:
            if (r1 == 0) goto L_0x0071
            com.google.android.gms.dynamite.DynamiteModule.zzig = r1     // Catch:{ Exception -> 0x0055 }
            monitor-exit(r4)     // Catch:{ all -> 0x003b }
            goto L_0x000b
        L_0x003b:
            r1 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x003b }
            throw r1
        L_0x003e:
            java.lang.String r2 = "com.google.android.gms.dynamite.IDynamiteLoader"
            android.os.IInterface r2 = r1.queryLocalInterface(r2)     // Catch:{ Exception -> 0x0055 }
            boolean r5 = r2 instanceof com.google.android.gms.dynamite.zzi     // Catch:{ Exception -> 0x0055 }
            if (r5 == 0) goto L_0x004e
            r0 = r2
            com.google.android.gms.dynamite.zzi r0 = (com.google.android.gms.dynamite.zzi) r0     // Catch:{ Exception -> 0x0055 }
            r1 = r0
            goto L_0x0035
        L_0x004e:
            com.google.android.gms.dynamite.zzj r2 = new com.google.android.gms.dynamite.zzj     // Catch:{ Exception -> 0x0055 }
            r2.<init>(r1)     // Catch:{ Exception -> 0x0055 }
            r1 = r2
            goto L_0x0035
        L_0x0055:
            r1 = move-exception
            java.lang.String r2 = "DynamiteModule"
            java.lang.String r5 = "Failed to load IDynamiteLoader from GmsCore: "
            java.lang.String r1 = r1.getMessage()     // Catch:{ all -> 0x003b }
            java.lang.String r1 = java.lang.String.valueOf(r1)     // Catch:{ all -> 0x003b }
            int r6 = r1.length()     // Catch:{ all -> 0x003b }
            if (r6 == 0) goto L_0x0074
            java.lang.String r1 = r5.concat(r1)     // Catch:{ all -> 0x003b }
        L_0x006e:
            android.util.Log.e(r2, r1)     // Catch:{ all -> 0x003b }
        L_0x0071:
            monitor-exit(r4)     // Catch:{ all -> 0x003b }
            r1 = r3
            goto L_0x000b
        L_0x0074:
            java.lang.String r1 = new java.lang.String     // Catch:{ all -> 0x003b }
            r1.<init>(r5)     // Catch:{ all -> 0x003b }
            goto L_0x006e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.dynamite.DynamiteModule.zzj(android.content.Context):com.google.android.gms.dynamite.zzi");
    }

    @KeepForSdk
    public final Context getModuleContext() {
        return this.zzin;
    }

    private static DynamiteModule zzb(Context context, String str, int i) throws LoadingException, RemoteException {
        zzk zzk;
        IObjectWrapper zza2;
        Log.i("DynamiteModule", new StringBuilder(String.valueOf(str).length() + 51).append("Selected remote version of ").append(str).append(", version >= ").append(i).toString());
        synchronized (DynamiteModule.class) {
            zzk = zzih;
        }
        if (zzk == null) {
            throw new LoadingException("DynamiteLoaderV2 was not cached.", (zza) null);
        }
        zza zza3 = zzik.get();
        if (zza3 == null || zza3.zzio == null) {
            throw new LoadingException("No result cursor", (zza) null);
        }
        Context applicationContext = context.getApplicationContext();
        Cursor cursor = zza3.zzio;
        ObjectWrapper.wrap(null);
        if (zzaj().booleanValue()) {
            Log.v("DynamiteModule", "Dynamite loader version >= 2, using loadModule2NoCrashUtils");
            zza2 = zzk.zzb(ObjectWrapper.wrap(applicationContext), str, i, ObjectWrapper.wrap(cursor));
        } else {
            Log.w("DynamiteModule", "Dynamite loader version < 2, falling back to loadModule2");
            zza2 = zzk.zza(ObjectWrapper.wrap(applicationContext), str, i, ObjectWrapper.wrap(cursor));
        }
        Context context2 = (Context) ObjectWrapper.unwrap(zza2);
        if (context2 != null) {
            return new DynamiteModule(context2);
        }
        throw new LoadingException("Failed to get module context", (zza) null);
    }

    private static Boolean zzaj() {
        Boolean valueOf;
        synchronized (DynamiteModule.class) {
            valueOf = Boolean.valueOf(zzij >= 2);
        }
        return valueOf;
    }

    @GuardedBy("DynamiteModule.class")
    private static void zza(ClassLoader classLoader) throws LoadingException {
        zzk zzl;
        try {
            IBinder iBinder = (IBinder) classLoader.loadClass("com.google.android.gms.dynamiteloader.DynamiteLoaderV2").getConstructor(new Class[0]).newInstance(new Object[0]);
            if (iBinder == null) {
                zzl = null;
            } else {
                IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.dynamite.IDynamiteLoaderV2");
                if (queryLocalInterface instanceof zzk) {
                    zzl = (zzk) queryLocalInterface;
                } else {
                    zzl = new zzl(iBinder);
                }
            }
            zzih = zzl;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new LoadingException("Failed to instantiate dynamite loader", e, null);
        }
    }

    @KeepForSdk
    public final IBinder instantiate(String str) throws LoadingException {
        try {
            return (IBinder) this.zzin.getClassLoader().loadClass(str).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            String valueOf = String.valueOf(str);
            throw new LoadingException(valueOf.length() != 0 ? "Failed to instantiate module class: ".concat(valueOf) : new String("Failed to instantiate module class: "), e, null);
        }
    }

    private DynamiteModule(Context context) {
        this.zzin = (Context) Preconditions.checkNotNull(context);
    }
}
