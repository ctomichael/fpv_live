package com.mapbox.mapboxsdk.offline;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import com.mapbox.mapboxsdk.LibraryLoader;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.R;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.TelemetryDefinition;
import com.mapbox.mapboxsdk.net.ConnectivityReceiver;
import com.mapbox.mapboxsdk.storage.FileSource;
import com.mapbox.mapboxsdk.utils.FileUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

@UiThread
public class OfflineManager {
    private static final String TAG = "Mbgl - OfflineManager";
    @SuppressLint({"StaticFieldLeak"})
    private static OfflineManager instance;
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public final FileSource fileSource;
    /* access modifiers changed from: private */
    public final Handler handler = new Handler(Looper.getMainLooper());
    @Keep
    private long nativePtr;

    @Keep
    public interface CreateOfflineRegionCallback {
        void onCreate(OfflineRegion offlineRegion);

        void onError(String str);
    }

    @Keep
    public interface FileSourceCallback {
        void onError(@NonNull String str);

        void onSuccess();
    }

    @Keep
    public interface ListOfflineRegionsCallback {
        void onError(String str);

        void onList(OfflineRegion[] offlineRegionArr);
    }

    @Keep
    public interface MergeOfflineRegionsCallback {
        void onError(String str);

        void onMerge(OfflineRegion[] offlineRegionArr);
    }

    @Keep
    private native void createOfflineRegion(FileSource fileSource2, OfflineRegionDefinition offlineRegionDefinition, byte[] bArr, CreateOfflineRegionCallback createOfflineRegionCallback);

    @Keep
    private native void initialize(FileSource fileSource2);

    @Keep
    private native void listOfflineRegions(FileSource fileSource2, ListOfflineRegionsCallback listOfflineRegionsCallback);

    @Keep
    private native void mergeOfflineRegions(FileSource fileSource2, String str, MergeOfflineRegionsCallback mergeOfflineRegionsCallback);

    @Keep
    private native void nativeClearAmbientCache(@Nullable FileSourceCallback fileSourceCallback);

    @Keep
    private native void nativeInvalidateAmbientCache(@Nullable FileSourceCallback fileSourceCallback);

    @Keep
    private native void nativeResetDatabase(@Nullable FileSourceCallback fileSourceCallback);

    @Keep
    private native void nativeSetMaximumAmbientCacheSize(long j, @Nullable FileSourceCallback fileSourceCallback);

    /* access modifiers changed from: protected */
    @Keep
    public native void finalize() throws Throwable;

    @Keep
    public native void putResourceWithUrl(String str, byte[] bArr, long j, long j2, String str2, boolean z);

    @Keep
    public native void setOfflineMapboxTileCountLimit(long j);

    static {
        LibraryLoader.load();
    }

    private OfflineManager(Context context2) {
        this.context = context2.getApplicationContext();
        this.fileSource = FileSource.getInstance(this.context);
        initialize(this.fileSource);
        deleteAmbientDatabase(this.context);
    }

    private void deleteAmbientDatabase(Context context2) {
        FileUtils.deleteFile(FileSource.getInternalCachePath(context2) + File.separator + "mbgl-cache.db");
    }

    public static synchronized OfflineManager getInstance(@NonNull Context context2) {
        OfflineManager offlineManager;
        synchronized (OfflineManager.class) {
            if (instance == null) {
                instance = new OfflineManager(context2);
            }
            offlineManager = instance;
        }
        return offlineManager;
    }

    public void listOfflineRegions(@NonNull final ListOfflineRegionsCallback callback) {
        this.fileSource.activate();
        listOfflineRegions(this.fileSource, new ListOfflineRegionsCallback() {
            /* class com.mapbox.mapboxsdk.offline.OfflineManager.AnonymousClass1 */

            public void onList(final OfflineRegion[] offlineRegions) {
                OfflineManager.this.handler.post(new Runnable() {
                    /* class com.mapbox.mapboxsdk.offline.OfflineManager.AnonymousClass1.AnonymousClass1 */

                    public void run() {
                        OfflineManager.this.fileSource.deactivate();
                        callback.onList(offlineRegions);
                    }
                });
            }

            public void onError(final String error) {
                OfflineManager.this.handler.post(new Runnable() {
                    /* class com.mapbox.mapboxsdk.offline.OfflineManager.AnonymousClass1.AnonymousClass2 */

                    public void run() {
                        OfflineManager.this.fileSource.deactivate();
                        callback.onError(error);
                    }
                });
            }
        });
    }

    public void mergeOfflineRegions(@NonNull String path, @NonNull final MergeOfflineRegionsCallback callback) {
        final File src = new File(path);
        new Thread(new Runnable() {
            /* class com.mapbox.mapboxsdk.offline.OfflineManager.AnonymousClass2 */

            public void run() {
                String errorMessage = null;
                if (src.canWrite()) {
                    OfflineManager.this.handler.post(new Runnable() {
                        /* class com.mapbox.mapboxsdk.offline.OfflineManager.AnonymousClass2.AnonymousClass1 */

                        public void run() {
                            OfflineManager.this.mergeOfflineDatabaseFiles(src, callback, false);
                        }
                    });
                } else if (src.canRead()) {
                    final File dst = new File(FileSource.getInternalCachePath(OfflineManager.this.context), src.getName());
                    try {
                        OfflineManager.copyTempDatabaseFile(src, dst);
                        OfflineManager.this.handler.post(new Runnable() {
                            /* class com.mapbox.mapboxsdk.offline.OfflineManager.AnonymousClass2.AnonymousClass2 */

                            public void run() {
                                OfflineManager.this.mergeOfflineDatabaseFiles(dst, callback, true);
                            }
                        });
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        errorMessage = ex.getMessage();
                    }
                } else {
                    errorMessage = "Secondary database needs to be located in a readable path.";
                }
                if (errorMessage != null) {
                    final String finalErrorMessage = errorMessage;
                    OfflineManager.this.handler.post(new Runnable() {
                        /* class com.mapbox.mapboxsdk.offline.OfflineManager.AnonymousClass2.AnonymousClass3 */

                        public void run() {
                            callback.onError(finalErrorMessage);
                        }
                    });
                }
            }
        }).start();
    }

    public void resetDatabase(@Nullable final FileSourceCallback callback) {
        this.fileSource.activate();
        nativeResetDatabase(new FileSourceCallback() {
            /* class com.mapbox.mapboxsdk.offline.OfflineManager.AnonymousClass3 */

            public void onSuccess() {
                OfflineManager.this.handler.post(new Runnable() {
                    /* class com.mapbox.mapboxsdk.offline.OfflineManager.AnonymousClass3.AnonymousClass1 */

                    public void run() {
                        OfflineManager.this.fileSource.deactivate();
                        if (callback != null) {
                            callback.onSuccess();
                        }
                    }
                });
            }

            public void onError(@NonNull final String message) {
                OfflineManager.this.handler.post(new Runnable() {
                    /* class com.mapbox.mapboxsdk.offline.OfflineManager.AnonymousClass3.AnonymousClass2 */

                    public void run() {
                        OfflineManager.this.fileSource.deactivate();
                        if (callback != null) {
                            callback.onError(message);
                        }
                    }
                });
            }
        });
    }

    public void invalidateAmbientCache(@Nullable final FileSourceCallback callback) {
        this.fileSource.activate();
        nativeInvalidateAmbientCache(new FileSourceCallback() {
            /* class com.mapbox.mapboxsdk.offline.OfflineManager.AnonymousClass4 */

            public void onSuccess() {
                OfflineManager.this.handler.post(new Runnable() {
                    /* class com.mapbox.mapboxsdk.offline.OfflineManager.AnonymousClass4.AnonymousClass1 */

                    public void run() {
                        OfflineManager.this.fileSource.deactivate();
                        if (callback != null) {
                            callback.onSuccess();
                        }
                    }
                });
            }

            public void onError(@NonNull final String message) {
                OfflineManager.this.handler.post(new Runnable() {
                    /* class com.mapbox.mapboxsdk.offline.OfflineManager.AnonymousClass4.AnonymousClass2 */

                    public void run() {
                        OfflineManager.this.fileSource.deactivate();
                        if (callback != null) {
                            callback.onError(message);
                        }
                    }
                });
            }
        });
    }

    public void clearAmbientCache(@Nullable final FileSourceCallback callback) {
        this.fileSource.activate();
        nativeClearAmbientCache(new FileSourceCallback() {
            /* class com.mapbox.mapboxsdk.offline.OfflineManager.AnonymousClass5 */

            public void onSuccess() {
                OfflineManager.this.handler.post(new Runnable() {
                    /* class com.mapbox.mapboxsdk.offline.OfflineManager.AnonymousClass5.AnonymousClass1 */

                    public void run() {
                        OfflineManager.this.fileSource.deactivate();
                        if (callback != null) {
                            callback.onSuccess();
                        }
                    }
                });
            }

            public void onError(@NonNull final String message) {
                OfflineManager.this.handler.post(new Runnable() {
                    /* class com.mapbox.mapboxsdk.offline.OfflineManager.AnonymousClass5.AnonymousClass2 */

                    public void run() {
                        OfflineManager.this.fileSource.deactivate();
                        if (callback != null) {
                            callback.onError(message);
                        }
                    }
                });
            }
        });
    }

    public void setMaximumAmbientCacheSize(long size, @Nullable final FileSourceCallback callback) {
        this.fileSource.activate();
        nativeSetMaximumAmbientCacheSize(size, new FileSourceCallback() {
            /* class com.mapbox.mapboxsdk.offline.OfflineManager.AnonymousClass6 */

            public void onSuccess() {
                OfflineManager.this.handler.post(new Runnable() {
                    /* class com.mapbox.mapboxsdk.offline.OfflineManager.AnonymousClass6.AnonymousClass1 */

                    public void run() {
                        OfflineManager.this.fileSource.deactivate();
                        if (callback != null) {
                            callback.onSuccess();
                        }
                    }
                });
            }

            public void onError(@NonNull final String message) {
                OfflineManager.this.fileSource.activate();
                OfflineManager.this.handler.post(new Runnable() {
                    /* class com.mapbox.mapboxsdk.offline.OfflineManager.AnonymousClass6.AnonymousClass2 */

                    public void run() {
                        OfflineManager.this.fileSource.deactivate();
                        if (callback != null) {
                            callback.onError(message);
                        }
                    }
                });
            }
        });
    }

    /* access modifiers changed from: private */
    public static void copyTempDatabaseFile(@NonNull File sourceFile, File destFile) throws IOException {
        if (destFile.exists() || destFile.createNewFile()) {
            FileChannel source = null;
            FileChannel destination = null;
            try {
                source = new FileInputStream(sourceFile).getChannel();
                destination = new FileOutputStream(destFile).getChannel();
                destination.transferFrom(source, 0, source.size());
                if (source != null) {
                    source.close();
                }
                if (destination != null) {
                    destination.close();
                }
            } catch (IOException ex) {
                throw new IOException(String.format("Unable to copy database file for merge. %s", ex.getMessage()));
            } catch (Throwable th) {
                if (source != null) {
                    source.close();
                }
                if (destination != null) {
                    destination.close();
                }
                throw th;
            }
        } else {
            throw new IOException("Unable to copy database file for merge.");
        }
    }

    /* access modifiers changed from: private */
    public void mergeOfflineDatabaseFiles(@NonNull final File file, @NonNull final MergeOfflineRegionsCallback callback, final boolean isTemporaryFile) {
        this.fileSource.activate();
        mergeOfflineRegions(this.fileSource, file.getAbsolutePath(), new MergeOfflineRegionsCallback() {
            /* class com.mapbox.mapboxsdk.offline.OfflineManager.AnonymousClass7 */

            public void onMerge(final OfflineRegion[] offlineRegions) {
                if (isTemporaryFile) {
                    file.delete();
                }
                OfflineManager.this.handler.post(new Runnable() {
                    /* class com.mapbox.mapboxsdk.offline.OfflineManager.AnonymousClass7.AnonymousClass1 */

                    public void run() {
                        OfflineManager.this.fileSource.deactivate();
                        callback.onMerge(offlineRegions);
                    }
                });
            }

            public void onError(final String error) {
                if (isTemporaryFile) {
                    file.delete();
                }
                OfflineManager.this.handler.post(new Runnable() {
                    /* class com.mapbox.mapboxsdk.offline.OfflineManager.AnonymousClass7.AnonymousClass2 */

                    public void run() {
                        OfflineManager.this.fileSource.deactivate();
                        callback.onError(error);
                    }
                });
            }
        });
    }

    public void createOfflineRegion(@NonNull OfflineRegionDefinition definition, @NonNull byte[] metadata, @NonNull final CreateOfflineRegionCallback callback) {
        if (!isValidOfflineRegionDefinition(definition)) {
            callback.onError(String.format(this.context.getString(R.string.mapbox_offline_error_region_definition_invalid), definition.getBounds()));
            return;
        }
        ConnectivityReceiver.instance(this.context).activate();
        FileSource.getInstance(this.context).activate();
        createOfflineRegion(this.fileSource, definition, metadata, new CreateOfflineRegionCallback() {
            /* class com.mapbox.mapboxsdk.offline.OfflineManager.AnonymousClass8 */

            public void onCreate(final OfflineRegion offlineRegion) {
                OfflineManager.this.handler.post(new Runnable() {
                    /* class com.mapbox.mapboxsdk.offline.OfflineManager.AnonymousClass8.AnonymousClass1 */

                    public void run() {
                        ConnectivityReceiver.instance(OfflineManager.this.context).deactivate();
                        FileSource.getInstance(OfflineManager.this.context).deactivate();
                        callback.onCreate(offlineRegion);
                    }
                });
            }

            public void onError(final String error) {
                OfflineManager.this.handler.post(new Runnable() {
                    /* class com.mapbox.mapboxsdk.offline.OfflineManager.AnonymousClass8.AnonymousClass2 */

                    public void run() {
                        ConnectivityReceiver.instance(OfflineManager.this.context).deactivate();
                        FileSource.getInstance(OfflineManager.this.context).deactivate();
                        callback.onError(error);
                    }
                });
            }
        });
        TelemetryDefinition telemetry = Mapbox.getTelemetry();
        if (telemetry != null) {
            LatLngBounds bounds = definition.getBounds();
            telemetry.onCreateOfflineRegion(definition);
        }
    }

    private boolean isValidOfflineRegionDefinition(OfflineRegionDefinition definition) {
        return LatLngBounds.world().contains(definition.getBounds());
    }
}
