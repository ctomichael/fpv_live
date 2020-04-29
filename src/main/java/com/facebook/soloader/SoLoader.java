package com.facebook.soloader;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import dalvik.system.BaseDexClassLoader;
import dji.component.accountcenter.IMemberProtocol;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class SoLoader {
    static final boolean DEBUG = false;
    public static final int SOLOADER_ALLOW_ASYNC_INIT = 2;
    public static final int SOLOADER_DISABLE_BACKUP_SOSOURCE = 8;
    public static final int SOLOADER_ENABLE_EXOPACKAGE = 1;
    public static final int SOLOADER_LOOK_IN_ZIP = 4;
    private static final String SO_STORE_NAME_MAIN = "lib-main";
    private static final String SO_STORE_NAME_SPLIT = "lib-";
    static final boolean SYSTRACE_LIBRARY_LOADING;
    static final String TAG = "SoLoader";
    private static boolean isSystemApp;
    @GuardedBy("sSoSourcesLock")
    @Nullable
    private static ApplicationSoSource sApplicationSoSource;
    @GuardedBy("sSoSourcesLock")
    @Nullable
    private static UnpackingSoSource[] sBackupSoSources;
    @GuardedBy("sSoSourcesLock")
    private static int sFlags;
    private static final Set<String> sLoadedAndMergedLibraries = Collections.newSetFromMap(new ConcurrentHashMap());
    @GuardedBy("SoLoader.class")
    private static final HashSet<String> sLoadedLibraries = new HashSet<>();
    @GuardedBy("SoLoader.class")
    private static final Map<String, Object> sLoadingLibraries = new HashMap();
    @Nullable
    static SoFileLoader sSoFileLoader;
    @GuardedBy("sSoSourcesLock")
    @Nullable
    private static SoSource[] sSoSources = null;
    private static final ReentrantReadWriteLock sSoSourcesLock = new ReentrantReadWriteLock();
    private static int sSoSourcesVersion = 0;
    @Nullable
    private static SystemLoadLibraryWrapper sSystemLoadLibraryWrapper = null;

    static {
        boolean shouldSystrace = false;
        try {
            shouldSystrace = Build.VERSION.SDK_INT >= 18;
        } catch (NoClassDefFoundError | UnsatisfiedLinkError e) {
        }
        SYSTRACE_LIBRARY_LOADING = shouldSystrace;
    }

    public static void init(Context context, int flags) throws IOException {
        init(context, flags, null);
    }

    private static void init(Context context, int flags, @Nullable SoFileLoader soFileLoader) throws IOException {
        StrictMode.ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
        try {
            isSystemApp = checkIfSystemApp(context);
            initSoLoader(soFileLoader);
            initSoSources(context, flags, soFileLoader);
        } finally {
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    public static void init(Context context, boolean nativeExopackage) {
        try {
            init(context, nativeExopackage ? 1 : 0);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void initSoSources(Context context, int flags, @Nullable SoFileLoader soFileLoader) throws IOException {
        int apkSoSourceFlags;
        sSoSourcesLock.writeLock().lock();
        try {
            if (sSoSources == null) {
                Log.d(TAG, "init start");
                sFlags = flags;
                ArrayList<SoSource> soSources = new ArrayList<>();
                String LD_LIBRARY_PATH = System.getenv("LD_LIBRARY_PATH");
                if (LD_LIBRARY_PATH == null) {
                    LD_LIBRARY_PATH = "/vendor/lib:/system/lib";
                }
                String[] systemLibraryDirectories = LD_LIBRARY_PATH.split(":");
                for (int i = 0; i < systemLibraryDirectories.length; i++) {
                    Log.d(TAG, "adding system library source: " + systemLibraryDirectories[i]);
                    soSources.add(new DirectorySoSource(new File(systemLibraryDirectories[i]), 2));
                }
                if (context != null) {
                    if ((flags & 1) != 0) {
                        sBackupSoSources = null;
                        Log.d(TAG, "adding exo package source: lib-main");
                        soSources.add(0, new ExoSoSource(context, SO_STORE_NAME_MAIN));
                    } else {
                        if (isSystemApp) {
                            apkSoSourceFlags = 0;
                        } else {
                            apkSoSourceFlags = 1;
                            int ourSoSourceFlags = 0;
                            if (Build.VERSION.SDK_INT <= 17) {
                                ourSoSourceFlags = 0 | 1;
                            }
                            sApplicationSoSource = new ApplicationSoSource(context, ourSoSourceFlags);
                            Log.d(TAG, "adding application source: " + sApplicationSoSource.toString());
                            soSources.add(0, sApplicationSoSource);
                        }
                        if ((sFlags & 8) != 0) {
                            sBackupSoSources = null;
                        } else {
                            File mainApkDir = new File(context.getApplicationInfo().sourceDir);
                            ArrayList<UnpackingSoSource> backupSources = new ArrayList<>();
                            ApkSoSource mainApkSource = new ApkSoSource(context, mainApkDir, SO_STORE_NAME_MAIN, apkSoSourceFlags);
                            backupSources.add(mainApkSource);
                            Log.d(TAG, "adding backup source from : " + mainApkSource.toString());
                            if (Build.VERSION.SDK_INT >= 21 && context.getApplicationInfo().splitSourceDirs != null) {
                                Log.d(TAG, "adding backup sources from split apks");
                                String[] strArr = context.getApplicationInfo().splitSourceDirs;
                                int length = strArr.length;
                                int i2 = 0;
                                int splitIndex = 0;
                                while (i2 < length) {
                                    ApkSoSource splitApkSource = new ApkSoSource(context, new File(strArr[i2]), SO_STORE_NAME_SPLIT + splitIndex, apkSoSourceFlags);
                                    Log.d(TAG, "adding backup source: " + splitApkSource.toString());
                                    backupSources.add(splitApkSource);
                                    i2++;
                                    splitIndex++;
                                }
                            }
                            sBackupSoSources = (UnpackingSoSource[]) backupSources.toArray(new UnpackingSoSource[backupSources.size()]);
                            soSources.addAll(0, backupSources);
                        }
                    }
                }
                SoSource[] finalSoSources = (SoSource[]) soSources.toArray(new SoSource[soSources.size()]);
                int prepareFlags = makePrepareFlags();
                int i3 = finalSoSources.length;
                while (true) {
                    int i4 = i3 - 1;
                    if (i3 <= 0) {
                        break;
                    }
                    Log.d(TAG, "Preparing SO source: " + finalSoSources[i4]);
                    finalSoSources[i4].prepare(prepareFlags);
                    i3 = i4;
                }
                sSoSources = finalSoSources;
                sSoSourcesVersion++;
                Log.d(TAG, "init finish: " + sSoSources.length + " SO sources prepared");
            }
        } finally {
            Log.d(TAG, "init exiting");
            sSoSourcesLock.writeLock().unlock();
        }
    }

    private static int makePrepareFlags() {
        int prepareFlags = 0;
        sSoSourcesLock.writeLock().lock();
        try {
            if ((sFlags & 2) != 0) {
                prepareFlags = 0 | 1;
            }
            return prepareFlags;
        } finally {
            sSoSourcesLock.writeLock().unlock();
        }
    }

    private static synchronized void initSoLoader(@Nullable SoFileLoader soFileLoader) {
        synchronized (SoLoader.class) {
            if (soFileLoader != null) {
                sSoFileLoader = soFileLoader;
            } else {
                final Runtime runtime = Runtime.getRuntime();
                final Method nativeLoadRuntimeMethod = getNativeLoadRuntimeMethod();
                final boolean hasNativeLoadMethod = nativeLoadRuntimeMethod != null;
                final String localLdLibraryPath = hasNativeLoadMethod ? Api14Utils.getClassLoaderLdLoadLibrary() : null;
                final String localLdLibraryPathNoZips = makeNonZipPath(localLdLibraryPath);
                sSoFileLoader = new SoFileLoader() {
                    /* class com.facebook.soloader.SoLoader.AnonymousClass1 */

                    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v11, resolved type: java.lang.Object} */
                    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: java.lang.String} */
                    /* JADX WARNING: Multi-variable type inference failed */
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    public void load(java.lang.String r12, int r13) {
                        /*
                            r11 = this;
                            r3 = 1
                            r5 = 0
                            r2 = 0
                            boolean r6 = r1
                            if (r6 == 0) goto L_0x00cf
                            r6 = r13 & 4
                            r7 = 4
                            if (r6 != r7) goto L_0x0090
                        L_0x000c:
                            if (r3 == 0) goto L_0x0093
                            java.lang.String r4 = r2
                        L_0x0010:
                            java.lang.Runtime r6 = r4     // Catch:{ IllegalAccessException -> 0x003c, IllegalArgumentException -> 0x00d3, InvocationTargetException -> 0x00d7 }
                            monitor-enter(r6)     // Catch:{ IllegalAccessException -> 0x003c, IllegalArgumentException -> 0x00d3, InvocationTargetException -> 0x00d7 }
                            java.lang.reflect.Method r5 = r5     // Catch:{ all -> 0x0039 }
                            java.lang.Runtime r7 = r4     // Catch:{ all -> 0x0039 }
                            r8 = 3
                            java.lang.Object[] r8 = new java.lang.Object[r8]     // Catch:{ all -> 0x0039 }
                            r9 = 0
                            r8[r9] = r12     // Catch:{ all -> 0x0039 }
                            r9 = 1
                            java.lang.Class<com.facebook.soloader.SoLoader> r10 = com.facebook.soloader.SoLoader.class
                            java.lang.ClassLoader r10 = r10.getClassLoader()     // Catch:{ all -> 0x0039 }
                            r8[r9] = r10     // Catch:{ all -> 0x0039 }
                            r9 = 2
                            r8[r9] = r4     // Catch:{ all -> 0x0039 }
                            java.lang.Object r5 = r5.invoke(r7, r8)     // Catch:{ all -> 0x0039 }
                            r0 = r5
                            java.lang.String r0 = (java.lang.String) r0     // Catch:{ all -> 0x0039 }
                            r2 = r0
                            if (r2 == 0) goto L_0x0097
                            java.lang.UnsatisfiedLinkError r5 = new java.lang.UnsatisfiedLinkError     // Catch:{ all -> 0x0039 }
                            r5.<init>(r2)     // Catch:{ all -> 0x0039 }
                            throw r5     // Catch:{ all -> 0x0039 }
                        L_0x0039:
                            r5 = move-exception
                            monitor-exit(r6)     // Catch:{ all -> 0x0039 }
                            throw r5     // Catch:{ IllegalAccessException -> 0x003c, IllegalArgumentException -> 0x00d3, InvocationTargetException -> 0x00d7 }
                        L_0x003c:
                            r5 = move-exception
                            r1 = r5
                        L_0x003e:
                            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0058 }
                            r5.<init>()     // Catch:{ all -> 0x0058 }
                            java.lang.String r6 = "Error: Cannot load "
                            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ all -> 0x0058 }
                            java.lang.StringBuilder r5 = r5.append(r12)     // Catch:{ all -> 0x0058 }
                            java.lang.String r2 = r5.toString()     // Catch:{ all -> 0x0058 }
                            java.lang.RuntimeException r5 = new java.lang.RuntimeException     // Catch:{ all -> 0x0058 }
                            r5.<init>(r2, r1)     // Catch:{ all -> 0x0058 }
                            throw r5     // Catch:{ all -> 0x0058 }
                        L_0x0058:
                            r5 = move-exception
                            if (r2 == 0) goto L_0x008f
                            java.lang.String r6 = "SoLoader"
                            java.lang.StringBuilder r7 = new java.lang.StringBuilder
                            r7.<init>()
                            java.lang.String r8 = "Error when loading lib: "
                            java.lang.StringBuilder r7 = r7.append(r8)
                            java.lang.StringBuilder r7 = r7.append(r2)
                            java.lang.String r8 = " lib hash: "
                            java.lang.StringBuilder r7 = r7.append(r8)
                            java.lang.String r8 = r11.getLibHash(r12)
                            java.lang.StringBuilder r7 = r7.append(r8)
                            java.lang.String r8 = " search path is "
                            java.lang.StringBuilder r7 = r7.append(r8)
                            java.lang.StringBuilder r7 = r7.append(r4)
                            java.lang.String r7 = r7.toString()
                            android.util.Log.e(r6, r7)
                        L_0x008f:
                            throw r5
                        L_0x0090:
                            r3 = r5
                            goto L_0x000c
                        L_0x0093:
                            java.lang.String r4 = r3
                            goto L_0x0010
                        L_0x0097:
                            monitor-exit(r6)     // Catch:{ all -> 0x0039 }
                            if (r2 == 0) goto L_0x00ce
                            java.lang.String r5 = "SoLoader"
                            java.lang.StringBuilder r6 = new java.lang.StringBuilder
                            r6.<init>()
                            java.lang.String r7 = "Error when loading lib: "
                            java.lang.StringBuilder r6 = r6.append(r7)
                            java.lang.StringBuilder r6 = r6.append(r2)
                            java.lang.String r7 = " lib hash: "
                            java.lang.StringBuilder r6 = r6.append(r7)
                            java.lang.String r7 = r11.getLibHash(r12)
                            java.lang.StringBuilder r6 = r6.append(r7)
                            java.lang.String r7 = " search path is "
                            java.lang.StringBuilder r6 = r6.append(r7)
                            java.lang.StringBuilder r6 = r6.append(r4)
                            java.lang.String r6 = r6.toString()
                            android.util.Log.e(r5, r6)
                        L_0x00ce:
                            return
                        L_0x00cf:
                            java.lang.System.load(r12)
                            goto L_0x00ce
                        L_0x00d3:
                            r5 = move-exception
                            r1 = r5
                            goto L_0x003e
                        L_0x00d7:
                            r5 = move-exception
                            r1 = r5
                            goto L_0x003e
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.facebook.soloader.SoLoader.AnonymousClass1.load(java.lang.String, int):void");
                    }

                    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0023, code lost:
                        r8 = th;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0024, code lost:
                        r9 = r7;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0070, code lost:
                        r7 = move-exception;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0071, code lost:
                        r8 = r7;
                     */
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    private java.lang.String getLibHash(java.lang.String r15) {
                        /*
                            r14 = this;
                            java.io.File r5 = new java.io.File     // Catch:{ IOException -> 0x002d, SecurityException -> 0x0057, NoSuchAlgorithmException -> 0x0061 }
                            r5.<init>(r15)     // Catch:{ IOException -> 0x002d, SecurityException -> 0x0057, NoSuchAlgorithmException -> 0x0061 }
                            java.lang.String r7 = "MD5"
                            java.security.MessageDigest r2 = java.security.MessageDigest.getInstance(r7)     // Catch:{ IOException -> 0x002d, SecurityException -> 0x0057, NoSuchAlgorithmException -> 0x0061 }
                            java.io.FileInputStream r6 = new java.io.FileInputStream     // Catch:{ IOException -> 0x002d, SecurityException -> 0x0057, NoSuchAlgorithmException -> 0x0061 }
                            r6.<init>(r5)     // Catch:{ IOException -> 0x002d, SecurityException -> 0x0057, NoSuchAlgorithmException -> 0x0061 }
                            r9 = 0
                            r7 = 4096(0x1000, float:5.74E-42)
                            byte[] r0 = new byte[r7]     // Catch:{ Throwable -> 0x0021, all -> 0x0070 }
                        L_0x0016:
                            int r1 = r6.read(r0)     // Catch:{ Throwable -> 0x0021, all -> 0x0070 }
                            if (r1 <= 0) goto L_0x0033
                            r7 = 0
                            r2.update(r0, r7, r1)     // Catch:{ Throwable -> 0x0021, all -> 0x0070 }
                            goto L_0x0016
                        L_0x0021:
                            r7 = move-exception
                            throw r7     // Catch:{ all -> 0x0023 }
                        L_0x0023:
                            r8 = move-exception
                            r9 = r7
                        L_0x0025:
                            if (r6 == 0) goto L_0x002c
                            if (r9 == 0) goto L_0x006c
                            r6.close()     // Catch:{ Throwable -> 0x0067 }
                        L_0x002c:
                            throw r8     // Catch:{ IOException -> 0x002d, SecurityException -> 0x0057, NoSuchAlgorithmException -> 0x0061 }
                        L_0x002d:
                            r4 = move-exception
                            java.lang.String r3 = r4.toString()
                        L_0x0032:
                            return r3
                        L_0x0033:
                            java.lang.String r7 = "%32x"
                            r8 = 1
                            java.lang.Object[] r8 = new java.lang.Object[r8]     // Catch:{ Throwable -> 0x0021, all -> 0x0070 }
                            r10 = 0
                            java.math.BigInteger r11 = new java.math.BigInteger     // Catch:{ Throwable -> 0x0021, all -> 0x0070 }
                            r12 = 1
                            byte[] r13 = r2.digest()     // Catch:{ Throwable -> 0x0021, all -> 0x0070 }
                            r11.<init>(r12, r13)     // Catch:{ Throwable -> 0x0021, all -> 0x0070 }
                            r8[r10] = r11     // Catch:{ Throwable -> 0x0021, all -> 0x0070 }
                            java.lang.String r3 = java.lang.String.format(r7, r8)     // Catch:{ Throwable -> 0x0021, all -> 0x0070 }
                            if (r6 == 0) goto L_0x0032
                            if (r9 == 0) goto L_0x005d
                            r6.close()     // Catch:{ Throwable -> 0x0052 }
                            goto L_0x0032
                        L_0x0052:
                            r7 = move-exception
                            r9.addSuppressed(r7)     // Catch:{ IOException -> 0x002d, SecurityException -> 0x0057, NoSuchAlgorithmException -> 0x0061 }
                            goto L_0x0032
                        L_0x0057:
                            r4 = move-exception
                            java.lang.String r3 = r4.toString()
                            goto L_0x0032
                        L_0x005d:
                            r6.close()     // Catch:{ IOException -> 0x002d, SecurityException -> 0x0057, NoSuchAlgorithmException -> 0x0061 }
                            goto L_0x0032
                        L_0x0061:
                            r4 = move-exception
                            java.lang.String r3 = r4.toString()
                            goto L_0x0032
                        L_0x0067:
                            r7 = move-exception
                            r9.addSuppressed(r7)     // Catch:{ IOException -> 0x002d, SecurityException -> 0x0057, NoSuchAlgorithmException -> 0x0061 }
                            goto L_0x002c
                        L_0x006c:
                            r6.close()     // Catch:{ IOException -> 0x002d, SecurityException -> 0x0057, NoSuchAlgorithmException -> 0x0061 }
                            goto L_0x002c
                        L_0x0070:
                            r7 = move-exception
                            r8 = r7
                            goto L_0x0025
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.facebook.soloader.SoLoader.AnonymousClass1.getLibHash(java.lang.String):java.lang.String");
                    }
                };
            }
        }
    }

    @Nullable
    private static Method getNativeLoadRuntimeMethod() {
        if (Build.VERSION.SDK_INT < 23 || Build.VERSION.SDK_INT > 27) {
            return null;
        }
        try {
            Method method = Runtime.class.getDeclaredMethod("nativeLoad", String.class, ClassLoader.class, String.class);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException | SecurityException e) {
            Log.w(TAG, "Cannot get nativeLoad method", e);
            return null;
        }
    }

    private static boolean checkIfSystemApp(Context context) {
        return (context == null || (context.getApplicationInfo().flags & 129) == 0) ? false : true;
    }

    public static void setInTestMode() {
        setSoSources(new SoSource[]{new NoopSoSource()});
    }

    public static void deinitForTest() {
        setSoSources(null);
    }

    static void setSoSources(SoSource[] sources) {
        sSoSourcesLock.writeLock().lock();
        try {
            sSoSources = sources;
            sSoSourcesVersion++;
        } finally {
            sSoSourcesLock.writeLock().unlock();
        }
    }

    static void setSoFileLoader(SoFileLoader loader) {
        sSoFileLoader = loader;
    }

    static void resetStatus() {
        synchronized (SoLoader.class) {
            sLoadedLibraries.clear();
            sLoadingLibraries.clear();
            sSoFileLoader = null;
        }
        setSoSources(null);
    }

    public static void setSystemLoadLibraryWrapper(SystemLoadLibraryWrapper wrapper) {
        sSystemLoadLibraryWrapper = wrapper;
    }

    public static final class WrongAbiError extends UnsatisfiedLinkError {
        WrongAbiError(Throwable cause) {
            super("APK was built for a different platform");
            initCause(cause);
        }
    }

    @Nullable
    public static String getLibraryPath(String libName) throws IOException {
        sSoSourcesLock.readLock().lock();
        String libPath = null;
        try {
            if (sSoSources != null) {
                int i = 0;
                while (libPath == null && i < sSoSources.length) {
                    libPath = sSoSources[i].getLibraryPath(libName);
                    i++;
                }
            }
            return libPath;
        } finally {
            sSoSourcesLock.readLock().unlock();
        }
    }

    public static boolean loadLibrary(String shortName) {
        return loadLibrary(shortName, 0);
    }

    /* JADX INFO: finally extract failed */
    public static boolean loadLibrary(String shortName, int loadFlags) throws UnsatisfiedLinkError {
        String soName;
        boolean needsLoad = true;
        sSoSourcesLock.readLock().lock();
        try {
            if (sSoSources == null) {
                if ("http://www.android.com/".equals(System.getProperty("java.vendor.url"))) {
                    assertInitialized();
                } else {
                    synchronized (SoLoader.class) {
                        if (sLoadedLibraries.contains(shortName)) {
                            needsLoad = false;
                        }
                        if (needsLoad) {
                            if (sSystemLoadLibraryWrapper != null) {
                                sSystemLoadLibraryWrapper.loadLibrary(shortName);
                            } else {
                                System.loadLibrary(shortName);
                            }
                        }
                    }
                    sSoSourcesLock.readLock().unlock();
                    return needsLoad;
                }
            }
            sSoSourcesLock.readLock().unlock();
            if (!isSystemApp || sSystemLoadLibraryWrapper == null) {
                String mergedLibName = MergedSoMapping.mapLibName(shortName);
                if (mergedLibName != null) {
                    soName = mergedLibName;
                } else {
                    soName = shortName;
                }
                return loadLibraryBySoName(System.mapLibraryName(soName), shortName, mergedLibName, loadFlags | 2, null);
            }
            sSystemLoadLibraryWrapper.loadLibrary(shortName);
            return true;
        } catch (Throwable th) {
            sSoSourcesLock.readLock().unlock();
            throw th;
        }
    }

    static void loadLibraryBySoName(String soName, int loadFlags, StrictMode.ThreadPolicy oldPolicy) {
        loadLibraryBySoName(soName, null, null, loadFlags, oldPolicy);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:100:0x0137, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:101:0x0138, code lost:
        r5 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:109:?, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:110:?, code lost:
        return r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0034, code lost:
        monitor-enter(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0035, code lost:
        if (r2 != false) goto L_0x0099;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0037, code lost:
        r7 = com.facebook.soloader.SoLoader.class;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        monitor-enter(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0040, code lost:
        if (com.facebook.soloader.SoLoader.sLoadedLibraries.contains(r11) == false) goto L_0x0056;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0042, code lost:
        if (r13 != null) goto L_0x0055;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0044, code lost:
        monitor-exit(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
        monitor-exit(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0055, code lost:
        r2 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
        monitor-exit(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0057, code lost:
        if (r2 != false) goto L_0x0099;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:?, code lost:
        android.util.Log.d(com.facebook.soloader.SoLoader.TAG, "About to load: " + r11);
        doLoadLibraryBySoName(r11, r14, r15);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0076, code lost:
        r7 = com.facebook.soloader.SoLoader.class;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:?, code lost:
        monitor-enter(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:?, code lost:
        android.util.Log.d(com.facebook.soloader.SoLoader.TAG, "Loaded: " + r11);
        com.facebook.soloader.SoLoader.sLoadedLibraries.add(r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x0098, code lost:
        monitor-exit(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x009d, code lost:
        if (android.text.TextUtils.isEmpty(r12) != false) goto L_0x012c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00a5, code lost:
        if (com.facebook.soloader.SoLoader.sLoadedAndMergedLibraries.contains(r12) == false) goto L_0x012c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00a7, code lost:
        r1 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00a8, code lost:
        if (r13 == null) goto L_0x0102;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x00aa, code lost:
        if (r1 != false) goto L_0x0102;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x00ae, code lost:
        if (com.facebook.soloader.SoLoader.SYSTRACE_LIBRARY_LOADING == false) goto L_0x00ce;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x00b0, code lost:
        com.facebook.soloader.Api18TraceUtils.beginTraceSection("MergedSoMapping.invokeJniOnload[" + r12 + dji.component.accountcenter.IMemberProtocol.STRING_SEPERATOR_RIGHT);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:?, code lost:
        android.util.Log.d(com.facebook.soloader.SoLoader.TAG, "About to merge: " + r12 + " / " + r11);
        com.facebook.soloader.MergedSoMapping.invokeJniOnload(r12);
        com.facebook.soloader.SoLoader.sLoadedAndMergedLibraries.add(r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x00fd, code lost:
        if (com.facebook.soloader.SoLoader.SYSTRACE_LIBRARY_LOADING == false) goto L_0x0102;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x00ff, code lost:
        com.facebook.soloader.Api18TraceUtils.endSection();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x0102, code lost:
        monitor-exit(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x0103, code lost:
        if (r2 != false) goto L_0x0138;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x010b, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x0111, code lost:
        throw new java.lang.RuntimeException(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x0112, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x0113, code lost:
        r4 = r0.getMessage();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x0117, code lost:
        if (r4 == null) goto L_0x0128;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x0127, code lost:
        throw new com.facebook.soloader.SoLoader.WrongAbiError(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x0128, code lost:
        throw r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x012c, code lost:
        r1 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x012f, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x0132, code lost:
        if (com.facebook.soloader.SoLoader.SYSTRACE_LIBRARY_LOADING != false) goto L_0x0134;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:0x0134, code lost:
        com.facebook.soloader.Api18TraceUtils.endSection();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean loadLibraryBySoName(java.lang.String r11, @javax.annotation.Nullable java.lang.String r12, @javax.annotation.Nullable java.lang.String r13, int r14, @javax.annotation.Nullable android.os.StrictMode.ThreadPolicy r15) {
        /*
            r5 = 1
            r6 = 0
            boolean r7 = android.text.TextUtils.isEmpty(r12)
            if (r7 != 0) goto L_0x0011
            java.util.Set<java.lang.String> r7 = com.facebook.soloader.SoLoader.sLoadedAndMergedLibraries
            boolean r7 = r7.contains(r12)
            if (r7 == 0) goto L_0x0011
        L_0x0010:
            return r6
        L_0x0011:
            r2 = 0
            java.lang.Class<com.facebook.soloader.SoLoader> r7 = com.facebook.soloader.SoLoader.class
            monitor-enter(r7)
            java.util.HashSet<java.lang.String> r8 = com.facebook.soloader.SoLoader.sLoadedLibraries     // Catch:{ all -> 0x0021 }
            boolean r8 = r8.contains(r11)     // Catch:{ all -> 0x0021 }
            if (r8 == 0) goto L_0x0025
            if (r13 != 0) goto L_0x0024
            monitor-exit(r7)     // Catch:{ all -> 0x0021 }
            goto L_0x0010
        L_0x0021:
            r5 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x0021 }
            throw r5
        L_0x0024:
            r2 = 1
        L_0x0025:
            java.util.Map<java.lang.String, java.lang.Object> r8 = com.facebook.soloader.SoLoader.sLoadingLibraries     // Catch:{ all -> 0x0021 }
            boolean r8 = r8.containsKey(r11)     // Catch:{ all -> 0x0021 }
            if (r8 == 0) goto L_0x004a
            java.util.Map<java.lang.String, java.lang.Object> r8 = com.facebook.soloader.SoLoader.sLoadingLibraries     // Catch:{ all -> 0x0021 }
            java.lang.Object r3 = r8.get(r11)     // Catch:{ all -> 0x0021 }
        L_0x0033:
            monitor-exit(r7)     // Catch:{ all -> 0x0021 }
            monitor-enter(r3)
            if (r2 != 0) goto L_0x0099
            java.lang.Class<com.facebook.soloader.SoLoader> r7 = com.facebook.soloader.SoLoader.class
            monitor-enter(r7)     // Catch:{ all -> 0x0047 }
            java.util.HashSet<java.lang.String> r8 = com.facebook.soloader.SoLoader.sLoadedLibraries     // Catch:{ all -> 0x0108 }
            boolean r8 = r8.contains(r11)     // Catch:{ all -> 0x0108 }
            if (r8 == 0) goto L_0x0056
            if (r13 != 0) goto L_0x0055
            monitor-exit(r7)     // Catch:{ all -> 0x0108 }
            monitor-exit(r3)     // Catch:{ all -> 0x0047 }
            goto L_0x0010
        L_0x0047:
            r5 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0047 }
            throw r5
        L_0x004a:
            java.lang.Object r3 = new java.lang.Object     // Catch:{ all -> 0x0021 }
            r3.<init>()     // Catch:{ all -> 0x0021 }
            java.util.Map<java.lang.String, java.lang.Object> r8 = com.facebook.soloader.SoLoader.sLoadingLibraries     // Catch:{ all -> 0x0021 }
            r8.put(r11, r3)     // Catch:{ all -> 0x0021 }
            goto L_0x0033
        L_0x0055:
            r2 = 1
        L_0x0056:
            monitor-exit(r7)     // Catch:{ all -> 0x0108 }
            if (r2 != 0) goto L_0x0099
            java.lang.String r7 = "SoLoader"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x010b, UnsatisfiedLinkError -> 0x0112 }
            r8.<init>()     // Catch:{ IOException -> 0x010b, UnsatisfiedLinkError -> 0x0112 }
            java.lang.String r9 = "About to load: "
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ IOException -> 0x010b, UnsatisfiedLinkError -> 0x0112 }
            java.lang.StringBuilder r8 = r8.append(r11)     // Catch:{ IOException -> 0x010b, UnsatisfiedLinkError -> 0x0112 }
            java.lang.String r8 = r8.toString()     // Catch:{ IOException -> 0x010b, UnsatisfiedLinkError -> 0x0112 }
            android.util.Log.d(r7, r8)     // Catch:{ IOException -> 0x010b, UnsatisfiedLinkError -> 0x0112 }
            doLoadLibraryBySoName(r11, r14, r15)     // Catch:{ IOException -> 0x010b, UnsatisfiedLinkError -> 0x0112 }
            java.lang.Class<com.facebook.soloader.SoLoader> r7 = com.facebook.soloader.SoLoader.class
            monitor-enter(r7)     // Catch:{ all -> 0x0047 }
            java.lang.String r8 = "SoLoader"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ all -> 0x0129 }
            r9.<init>()     // Catch:{ all -> 0x0129 }
            java.lang.String r10 = "Loaded: "
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ all -> 0x0129 }
            java.lang.StringBuilder r9 = r9.append(r11)     // Catch:{ all -> 0x0129 }
            java.lang.String r9 = r9.toString()     // Catch:{ all -> 0x0129 }
            android.util.Log.d(r8, r9)     // Catch:{ all -> 0x0129 }
            java.util.HashSet<java.lang.String> r8 = com.facebook.soloader.SoLoader.sLoadedLibraries     // Catch:{ all -> 0x0129 }
            r8.add(r11)     // Catch:{ all -> 0x0129 }
            monitor-exit(r7)     // Catch:{ all -> 0x0129 }
        L_0x0099:
            boolean r7 = android.text.TextUtils.isEmpty(r12)     // Catch:{ all -> 0x0047 }
            if (r7 != 0) goto L_0x012c
            java.util.Set<java.lang.String> r7 = com.facebook.soloader.SoLoader.sLoadedAndMergedLibraries     // Catch:{ all -> 0x0047 }
            boolean r7 = r7.contains(r12)     // Catch:{ all -> 0x0047 }
            if (r7 == 0) goto L_0x012c
            r1 = r5
        L_0x00a8:
            if (r13 == 0) goto L_0x0102
            if (r1 != 0) goto L_0x0102
            boolean r7 = com.facebook.soloader.SoLoader.SYSTRACE_LIBRARY_LOADING     // Catch:{ all -> 0x0047 }
            if (r7 == 0) goto L_0x00ce
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x0047 }
            r7.<init>()     // Catch:{ all -> 0x0047 }
            java.lang.String r8 = "MergedSoMapping.invokeJniOnload["
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ all -> 0x0047 }
            java.lang.StringBuilder r7 = r7.append(r12)     // Catch:{ all -> 0x0047 }
            java.lang.String r8 = "]"
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ all -> 0x0047 }
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x0047 }
            com.facebook.soloader.Api18TraceUtils.beginTraceSection(r7)     // Catch:{ all -> 0x0047 }
        L_0x00ce:
            java.lang.String r7 = "SoLoader"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x012f }
            r8.<init>()     // Catch:{ all -> 0x012f }
            java.lang.String r9 = "About to merge: "
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ all -> 0x012f }
            java.lang.StringBuilder r8 = r8.append(r12)     // Catch:{ all -> 0x012f }
            java.lang.String r9 = " / "
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ all -> 0x012f }
            java.lang.StringBuilder r8 = r8.append(r11)     // Catch:{ all -> 0x012f }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x012f }
            android.util.Log.d(r7, r8)     // Catch:{ all -> 0x012f }
            com.facebook.soloader.MergedSoMapping.invokeJniOnload(r12)     // Catch:{ all -> 0x012f }
            java.util.Set<java.lang.String> r7 = com.facebook.soloader.SoLoader.sLoadedAndMergedLibraries     // Catch:{ all -> 0x012f }
            r7.add(r12)     // Catch:{ all -> 0x012f }
            boolean r7 = com.facebook.soloader.SoLoader.SYSTRACE_LIBRARY_LOADING     // Catch:{ all -> 0x0047 }
            if (r7 == 0) goto L_0x0102
            com.facebook.soloader.Api18TraceUtils.endSection()     // Catch:{ all -> 0x0047 }
        L_0x0102:
            monitor-exit(r3)     // Catch:{ all -> 0x0047 }
            if (r2 != 0) goto L_0x0138
        L_0x0105:
            r6 = r5
            goto L_0x0010
        L_0x0108:
            r5 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x0108 }
            throw r5     // Catch:{ all -> 0x0047 }
        L_0x010b:
            r0 = move-exception
            java.lang.RuntimeException r5 = new java.lang.RuntimeException     // Catch:{ all -> 0x0047 }
            r5.<init>(r0)     // Catch:{ all -> 0x0047 }
            throw r5     // Catch:{ all -> 0x0047 }
        L_0x0112:
            r0 = move-exception
            java.lang.String r4 = r0.getMessage()     // Catch:{ all -> 0x0047 }
            if (r4 == 0) goto L_0x0128
            java.lang.String r5 = "unexpected e_machine:"
            boolean r5 = r4.contains(r5)     // Catch:{ all -> 0x0047 }
            if (r5 == 0) goto L_0x0128
            com.facebook.soloader.SoLoader$WrongAbiError r5 = new com.facebook.soloader.SoLoader$WrongAbiError     // Catch:{ all -> 0x0047 }
            r5.<init>(r0)     // Catch:{ all -> 0x0047 }
            throw r5     // Catch:{ all -> 0x0047 }
        L_0x0128:
            throw r0     // Catch:{ all -> 0x0047 }
        L_0x0129:
            r5 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x0129 }
            throw r5     // Catch:{ all -> 0x0047 }
        L_0x012c:
            r1 = r6
            goto L_0x00a8
        L_0x012f:
            r5 = move-exception
            boolean r6 = com.facebook.soloader.SoLoader.SYSTRACE_LIBRARY_LOADING     // Catch:{ all -> 0x0047 }
            if (r6 == 0) goto L_0x0137
            com.facebook.soloader.Api18TraceUtils.endSection()     // Catch:{ all -> 0x0047 }
        L_0x0137:
            throw r5     // Catch:{ all -> 0x0047 }
        L_0x0138:
            r5 = r6
            goto L_0x0105
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.soloader.SoLoader.loadLibraryBySoName(java.lang.String, java.lang.String, java.lang.String, int, android.os.StrictMode$ThreadPolicy):boolean");
    }

    public static File unpackLibraryAndDependencies(String shortName) throws UnsatisfiedLinkError {
        assertInitialized();
        try {
            return unpackLibraryBySoName(System.mapLibraryName(shortName));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /* JADX INFO: finally extract failed */
    private static void doLoadLibraryBySoName(String soName, int loadFlags, StrictMode.ThreadPolicy oldPolicy) throws IOException {
        boolean retry;
        int result = 0;
        sSoSourcesLock.readLock().lock();
        try {
            if (sSoSources == null) {
                Log.e(TAG, "Could not load: " + soName + " because no SO source exists");
                throw new UnsatisfiedLinkError("couldn't find DSO to load: " + soName);
            }
            sSoSourcesLock.readLock().unlock();
            boolean restoreOldPolicy = false;
            if (oldPolicy == null) {
                oldPolicy = StrictMode.allowThreadDiskReads();
                restoreOldPolicy = true;
            }
            if (SYSTRACE_LIBRARY_LOADING) {
                Api18TraceUtils.beginTraceSection("SoLoader.loadLibrary[" + soName + IMemberProtocol.STRING_SEPERATOR_RIGHT);
            }
            Throwable error = null;
            do {
                retry = false;
                try {
                    sSoSourcesLock.readLock().lock();
                    int currentSoSourcesVersion = sSoSourcesVersion;
                    int i = 0;
                    while (true) {
                        if (result == 0) {
                            if (i < sSoSources.length) {
                                result = sSoSources[i].loadLibrary(soName, loadFlags, oldPolicy);
                                if (result != 3 || sBackupSoSources == null) {
                                    i++;
                                } else {
                                    Log.d(TAG, "Trying backup SoSource for " + soName);
                                    UnpackingSoSource[] unpackingSoSourceArr = sBackupSoSources;
                                    int length = unpackingSoSourceArr.length;
                                    int i2 = 0;
                                    while (true) {
                                        if (i2 < length) {
                                            UnpackingSoSource backupSoSource = unpackingSoSourceArr[i2];
                                            backupSoSource.prepare(soName);
                                            int resultFromBackup = backupSoSource.loadLibrary(soName, loadFlags, oldPolicy);
                                            if (resultFromBackup == 1) {
                                                result = resultFromBackup;
                                                break;
                                            }
                                            i2++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    sSoSourcesLock.readLock().unlock();
                    if ((loadFlags & 2) == 2 && result == 0) {
                        sSoSourcesLock.writeLock().lock();
                        if (sApplicationSoSource != null && sApplicationSoSource.checkAndMaybeUpdate()) {
                            sSoSourcesVersion++;
                        }
                        if (sSoSourcesVersion != currentSoSourcesVersion) {
                            retry = true;
                        }
                        sSoSourcesLock.writeLock().unlock();
                        continue;
                    }
                } catch (Throwable th) {
                    if (SYSTRACE_LIBRARY_LOADING) {
                        Api18TraceUtils.endSection();
                    }
                    if (restoreOldPolicy) {
                        StrictMode.setThreadPolicy(oldPolicy);
                    }
                    if (result == 0 || result == 3) {
                        String message = "couldn't find DSO to load: " + soName;
                        if (error != null) {
                            String cause = error.getMessage();
                            if (cause == null) {
                                cause = error.toString();
                            }
                            message = message + " caused by: " + cause;
                        }
                        Log.e(TAG, message);
                        throw new UnsatisfiedLinkError(message);
                    }
                    throw th;
                }
            } while (retry);
            if (SYSTRACE_LIBRARY_LOADING) {
                Api18TraceUtils.endSection();
            }
            if (restoreOldPolicy) {
                StrictMode.setThreadPolicy(oldPolicy);
            }
            if (result == 0 || result == 3) {
                String message2 = "couldn't find DSO to load: " + soName;
                if (error != null) {
                    String cause2 = error.getMessage();
                    if (cause2 == null) {
                        cause2 = error.toString();
                    }
                    message2 = message2 + " caused by: " + cause2;
                }
                Log.e(TAG, message2);
                throw new UnsatisfiedLinkError(message2);
            }
        } catch (Throwable th2) {
            sSoSourcesLock.readLock().unlock();
            throw th2;
        }
    }

    @Nullable
    public static String makeNonZipPath(String localLdLibraryPath) {
        if (localLdLibraryPath == null) {
            return null;
        }
        String[] paths = localLdLibraryPath.split(":");
        ArrayList<String> pathsWithoutZip = new ArrayList<>(paths.length);
        for (String path : paths) {
            if (!path.contains("!")) {
                pathsWithoutZip.add(path);
            }
        }
        return TextUtils.join(":", pathsWithoutZip);
    }

    static File unpackLibraryBySoName(String soName) throws IOException {
        sSoSourcesLock.readLock().lock();
        int i = 0;
        while (i < sSoSources.length) {
            try {
                File unpacked = sSoSources[i].unpackLibrary(soName);
                if (unpacked != null) {
                    return unpacked;
                }
                i++;
            } finally {
                sSoSourcesLock.readLock().unlock();
            }
        }
        sSoSourcesLock.readLock().unlock();
        throw new FileNotFoundException(soName);
    }

    private static void assertInitialized() {
        sSoSourcesLock.readLock().lock();
        try {
            if (sSoSources == null) {
                throw new RuntimeException("SoLoader.init() not yet called");
            }
        } finally {
            sSoSourcesLock.readLock().unlock();
        }
    }

    public static void prependSoSource(SoSource extraSoSource) throws IOException {
        sSoSourcesLock.writeLock().lock();
        try {
            Log.d(TAG, "Prepending to SO sources: " + extraSoSource);
            assertInitialized();
            extraSoSource.prepare(makePrepareFlags());
            SoSource[] newSoSources = new SoSource[(sSoSources.length + 1)];
            newSoSources[0] = extraSoSource;
            System.arraycopy(sSoSources, 0, newSoSources, 1, sSoSources.length);
            sSoSources = newSoSources;
            sSoSourcesVersion++;
            Log.d(TAG, "Prepended to SO sources: " + extraSoSource);
        } finally {
            sSoSourcesLock.writeLock().unlock();
        }
    }

    public static String makeLdLibraryPath() {
        SoSource[] soSources;
        sSoSourcesLock.readLock().lock();
        try {
            assertInitialized();
            Log.d(TAG, "makeLdLibraryPath");
            ArrayList<String> pathElements = new ArrayList<>();
            for (SoSource soSource : sSoSources) {
                soSource.addToLdLibraryPath(pathElements);
            }
            String joinedPaths = TextUtils.join(":", pathElements);
            Log.d(TAG, "makeLdLibraryPath final path: " + joinedPaths);
            return joinedPaths;
        } finally {
            sSoSourcesLock.readLock().unlock();
        }
    }

    public static boolean areSoSourcesAbisSupported() {
        sSoSourcesLock.readLock().lock();
        try {
            if (sSoSources == null) {
                return false;
            }
            String[] supportedAbis = SysUtil.getSupportedAbis();
            for (int i = 0; i < sSoSources.length; i++) {
                String[] soSourceAbis = sSoSources[i].getSoSourceAbis();
                for (int j = 0; j < soSourceAbis.length; j++) {
                    boolean soSourceSupported = false;
                    for (int k = 0; k < supportedAbis.length && !soSourceSupported; k++) {
                        soSourceSupported = soSourceAbis[j].equals(supportedAbis[k]);
                    }
                    if (!soSourceSupported) {
                        Log.e(TAG, "abi not supported: " + soSourceAbis[j]);
                        sSoSourcesLock.readLock().unlock();
                        return false;
                    }
                }
            }
            sSoSourcesLock.readLock().unlock();
            return true;
        } finally {
            sSoSourcesLock.readLock().unlock();
        }
    }

    @DoNotOptimize
    @TargetApi(14)
    private static class Api14Utils {
        private Api14Utils() {
        }

        public static String getClassLoaderLdLoadLibrary() {
            ClassLoader classLoader = SoLoader.class.getClassLoader();
            if (!(classLoader instanceof BaseDexClassLoader)) {
                throw new IllegalStateException("ClassLoader " + classLoader.getClass().getName() + " should be of type BaseDexClassLoader");
            }
            try {
                return (String) BaseDexClassLoader.class.getMethod("getLdLibraryPath", new Class[0]).invoke((BaseDexClassLoader) classLoader, new Object[0]);
            } catch (Exception e) {
                throw new RuntimeException("Cannot call getLdLibraryPath", e);
            }
        }
    }
}
