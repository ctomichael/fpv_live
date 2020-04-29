package com.mapbox.mapboxsdk.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import com.mapbox.mapboxsdk.MapStrictMode;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.constants.MapboxConstants;
import com.mapbox.mapboxsdk.log.Logger;
import com.mapbox.mapboxsdk.utils.FileUtils;
import com.mapbox.mapboxsdk.utils.ThreadUtils;
import java.io.File;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FileSource {
    private static FileSource INSTANCE = null;
    private static final String MAPBOX_SHARED_PREFERENCE_RESOURCES_CACHE_PATH = "fileSourceResourcesCachePath";
    private static final String TAG = "Mbgl-FileSource";
    /* access modifiers changed from: private */
    public static String internalCachePath;
    private static final Lock internalCachePathLoaderLock = new ReentrantLock();
    /* access modifiers changed from: private */
    @Nullable
    public static String resourcesCachePath;
    /* access modifiers changed from: private */
    public static final Lock resourcesCachePathLoaderLock = new ReentrantLock();
    @Keep
    private long nativePtr;

    @Keep
    public interface ResourceTransformCallback {
        String onURL(int i, String str);
    }

    @Keep
    public interface ResourcesCachePathChangeCallback {
        void onError(@NonNull String str);

        void onSuccess(@NonNull String str);
    }

    @Keep
    private native void initialize(String str, String str2, AssetManager assetManager);

    @Keep
    private native void setResourceCachePath(String str, ResourcesCachePathChangeCallback resourcesCachePathChangeCallback);

    @Keep
    public native void activate();

    @Keep
    public native void deactivate();

    /* access modifiers changed from: protected */
    @Keep
    public native void finalize() throws Throwable;

    @Keep
    @NonNull
    public native String getAccessToken();

    @Keep
    public native boolean isActivated();

    @Keep
    public native void setAccessToken(String str);

    @Keep
    public native void setApiBaseUrl(String str);

    @Keep
    public native void setResourceTransform(ResourceTransformCallback resourceTransformCallback);

    @UiThread
    public static synchronized FileSource getInstance(@NonNull Context context) {
        FileSource fileSource;
        synchronized (FileSource.class) {
            if (INSTANCE == null) {
                INSTANCE = new FileSource(getResourcesCachePath(context), context.getResources().getAssets());
            }
            fileSource = INSTANCE;
        }
        return fileSource;
    }

    /* access modifiers changed from: private */
    @NonNull
    public static String getCachePath(@NonNull Context context) {
        String cachePath = context.getSharedPreferences("MapboxSharedPreferences", 0).getString(MAPBOX_SHARED_PREFERENCE_RESOURCES_CACHE_PATH, null);
        if (isPathWritable(cachePath)) {
            return cachePath;
        }
        String cachePath2 = getDefaultCachePath(context);
        context.getSharedPreferences("MapboxSharedPreferences", 0).edit().remove(MAPBOX_SHARED_PREFERENCE_RESOURCES_CACHE_PATH).apply();
        return cachePath2;
    }

    @NonNull
    private static String getDefaultCachePath(@NonNull Context context) {
        File externalFilesDir;
        if (!isExternalStorageConfiguration(context) || !isExternalStorageReadable() || (externalFilesDir = context.getExternalFilesDir(null)) == null) {
            return context.getFilesDir().getAbsolutePath();
        }
        return externalFilesDir.getAbsolutePath();
    }

    private static boolean isExternalStorageConfiguration(@NonNull Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (appInfo.metaData != null) {
                return appInfo.metaData.getBoolean(MapboxConstants.KEY_META_DATA_SET_STORAGE_EXTERNAL, false);
            }
            return false;
        } catch (PackageManager.NameNotFoundException exception) {
            Logger.e(TAG, "Failed to read the package metadata: ", exception);
            MapStrictMode.strictModeViolation(exception);
            return false;
        } catch (Exception exception2) {
            Logger.e(TAG, "Failed to read the storage key: ", exception2);
            MapStrictMode.strictModeViolation(exception2);
            return false;
        }
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if ("mounted".equals(state) || "mounted_ro".equals(state)) {
            return true;
        }
        Logger.w(TAG, "External storage was requested but it isn't readable. For API level < 18 make sure you've requested READ_EXTERNAL_STORAGE or WRITE_EXTERNAL_STORAGE permissions in your app Manifest (defaulting to internal storage).");
        return false;
    }

    @UiThread
    public static void initializeFileDirsPaths(Context context) {
        ThreadUtils.checkThread(TAG);
        lockPathLoaders();
        if (resourcesCachePath == null || internalCachePath == null) {
            new FileDirsPathsTask().execute(context);
        }
    }

    private static class FileDirsPathsTask extends AsyncTask<Context, Void, String[]> {
        private FileDirsPathsTask() {
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            FileSource.unlockPathLoaders();
        }

        /* access modifiers changed from: protected */
        @NonNull
        public String[] doInBackground(Context... contexts) {
            return new String[]{FileSource.getCachePath(contexts[0]), contexts[0].getCacheDir().getAbsolutePath()};
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String[] paths) {
            String unused = FileSource.resourcesCachePath = paths[0];
            String unused2 = FileSource.internalCachePath = paths[1];
            FileSource.unlockPathLoaders();
        }
    }

    @NonNull
    public static String getResourcesCachePath(@NonNull Context context) {
        resourcesCachePathLoaderLock.lock();
        try {
            if (resourcesCachePath == null) {
                resourcesCachePath = getCachePath(context);
            }
            return resourcesCachePath;
        } finally {
            resourcesCachePathLoaderLock.unlock();
        }
    }

    public static String getInternalCachePath(@NonNull Context context) {
        internalCachePathLoaderLock.lock();
        try {
            if (internalCachePath == null) {
                internalCachePath = context.getCacheDir().getAbsolutePath();
            }
            return internalCachePath;
        } finally {
            internalCachePathLoaderLock.unlock();
        }
    }

    @Deprecated
    public static void setResourcesCachePath(@NonNull Context context, @NonNull String path, @NonNull ResourcesCachePathChangeCallback callback) {
        setResourcesCachePath(path, callback);
    }

    public static void setResourcesCachePath(@NonNull final String path, @NonNull final ResourcesCachePathChangeCallback callback) {
        final Context applicationContext = Mapbox.getApplicationContext();
        if (getInstance(applicationContext).isActivated()) {
            Logger.w(TAG, "Cannot set path, file source is activated. Make sure that the map or a resources download is not running.");
            callback.onError("Cannot set path, file source is activated. Make sure that the map or a resources download is not running.");
        } else if (path.equals(getResourcesCachePath(applicationContext))) {
            callback.onSuccess(path);
        } else {
            new FileUtils.CheckFileWritePermissionTask(new FileUtils.OnCheckFileWritePermissionListener() {
                /* class com.mapbox.mapboxsdk.storage.FileSource.AnonymousClass1 */

                public void onWritePermissionGranted() {
                    SharedPreferences.Editor editor = applicationContext.getSharedPreferences("MapboxSharedPreferences", 0).edit();
                    editor.putString(FileSource.MAPBOX_SHARED_PREFERENCE_RESOURCES_CACHE_PATH, path);
                    editor.apply();
                    FileSource.internalSetResourcesCachePath(applicationContext, path, callback);
                }

                public void onError() {
                    String message = "Path is not writable: " + path;
                    Logger.e(FileSource.TAG, message);
                    callback.onError(message);
                }
            }).execute(new File(path));
        }
    }

    /* access modifiers changed from: private */
    public static void internalSetResourcesCachePath(@NonNull Context context, @NonNull String path, @NonNull final ResourcesCachePathChangeCallback callback) {
        FileSource fileSource = getInstance(context);
        fileSource.setResourceCachePath(path, new ResourcesCachePathChangeCallback(fileSource) {
            /* class com.mapbox.mapboxsdk.storage.FileSource.AnonymousClass2 */
            final /* synthetic */ FileSource val$fileSource;

            {
                this.val$fileSource = r1;
            }

            public void onSuccess(@NonNull String path) {
                this.val$fileSource.deactivate();
                FileSource.resourcesCachePathLoaderLock.lock();
                String unused = FileSource.resourcesCachePath = path;
                FileSource.resourcesCachePathLoaderLock.unlock();
                callback.onSuccess(path);
            }

            public void onError(@NonNull String message) {
                this.val$fileSource.deactivate();
                callback.onError(message);
            }
        });
        fileSource.activate();
    }

    private static boolean isPathWritable(String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }
        return new File(path).canWrite();
    }

    private static void lockPathLoaders() {
        internalCachePathLoaderLock.lock();
        resourcesCachePathLoaderLock.lock();
    }

    /* access modifiers changed from: private */
    public static void unlockPathLoaders() {
        resourcesCachePathLoaderLock.unlock();
        internalCachePathLoaderLock.unlock();
    }

    private FileSource(String cachePath, AssetManager assetManager) {
        initialize(Mapbox.getAccessToken(), cachePath, assetManager);
    }
}
