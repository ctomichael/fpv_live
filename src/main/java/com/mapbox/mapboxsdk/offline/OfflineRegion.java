package com.mapbox.mapboxsdk.offline;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mapbox.mapboxsdk.LibraryLoader;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.net.ConnectivityReceiver;
import com.mapbox.mapboxsdk.storage.FileSource;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class OfflineRegion {
    public static final int STATE_ACTIVE = 1;
    public static final int STATE_INACTIVE = 0;
    private final Context context = Mapbox.getApplicationContext();
    private OfflineRegionDefinition definition;
    private boolean deliverInactiveMessages = false;
    /* access modifiers changed from: private */
    public FileSource fileSource;
    /* access modifiers changed from: private */
    public final Handler handler = new Handler(Looper.getMainLooper());
    private long id;
    /* access modifiers changed from: private */
    public boolean isDeleted;
    /* access modifiers changed from: private */
    public byte[] metadata;
    @Keep
    private long nativePtr;
    private int state = 0;

    @Retention(RetentionPolicy.SOURCE)
    public @interface DownloadState {
    }

    @Keep
    public interface OfflineRegionDeleteCallback {
        void onDelete();

        void onError(String str);
    }

    @Keep
    public interface OfflineRegionInvalidateCallback {
        void onError(String str);

        void onInvalidate();
    }

    @Keep
    public interface OfflineRegionObserver {
        void mapboxTileCountLimitExceeded(long j);

        void onError(OfflineRegionError offlineRegionError);

        void onStatusChanged(OfflineRegionStatus offlineRegionStatus);
    }

    @Keep
    public interface OfflineRegionStatusCallback {
        void onError(String str);

        void onStatus(OfflineRegionStatus offlineRegionStatus);
    }

    @Keep
    public interface OfflineRegionUpdateMetadataCallback {
        void onError(String str);

        void onUpdate(byte[] bArr);
    }

    @Keep
    private native void deleteOfflineRegion(OfflineRegionDeleteCallback offlineRegionDeleteCallback);

    @Keep
    private native void getOfflineRegionStatus(OfflineRegionStatusCallback offlineRegionStatusCallback);

    @Keep
    private native void initialize(long j, FileSource fileSource2);

    @Keep
    private native void invalidateOfflineRegion(OfflineRegionInvalidateCallback offlineRegionInvalidateCallback);

    @Keep
    private native void setOfflineRegionDownloadState(int i);

    @Keep
    private native void setOfflineRegionObserver(OfflineRegionObserver offlineRegionObserver);

    @Keep
    private native void updateOfflineRegionMetadata(byte[] bArr, OfflineRegionUpdateMetadataCallback offlineRegionUpdateMetadataCallback);

    /* access modifiers changed from: protected */
    @Keep
    public native void finalize();

    static {
        LibraryLoader.load();
    }

    public boolean isDeliveringInactiveMessages() {
        return this.deliverInactiveMessages;
    }

    public void setDeliverInactiveMessages(boolean deliverInactiveMessages2) {
        this.deliverInactiveMessages = deliverInactiveMessages2;
    }

    /* access modifiers changed from: private */
    public boolean deliverMessages() {
        if (this.state == 1) {
            return true;
        }
        return isDeliveringInactiveMessages();
    }

    @Keep
    private OfflineRegion(long offlineRegionPtr, FileSource fileSource2, long id2, OfflineRegionDefinition definition2, byte[] metadata2) {
        this.fileSource = fileSource2;
        this.id = id2;
        this.definition = definition2;
        this.metadata = metadata2;
        initialize(offlineRegionPtr, fileSource2);
    }

    public long getID() {
        return this.id;
    }

    public OfflineRegionDefinition getDefinition() {
        return this.definition;
    }

    public byte[] getMetadata() {
        return this.metadata;
    }

    public void setObserver(@Nullable final OfflineRegionObserver observer) {
        setOfflineRegionObserver(new OfflineRegionObserver() {
            /* class com.mapbox.mapboxsdk.offline.OfflineRegion.AnonymousClass1 */

            public void onStatusChanged(final OfflineRegionStatus status) {
                if (OfflineRegion.this.deliverMessages()) {
                    OfflineRegion.this.handler.post(new Runnable() {
                        /* class com.mapbox.mapboxsdk.offline.OfflineRegion.AnonymousClass1.AnonymousClass1 */

                        public void run() {
                            if (observer != null) {
                                observer.onStatusChanged(status);
                            }
                        }
                    });
                }
            }

            public void onError(final OfflineRegionError error) {
                if (OfflineRegion.this.deliverMessages()) {
                    OfflineRegion.this.handler.post(new Runnable() {
                        /* class com.mapbox.mapboxsdk.offline.OfflineRegion.AnonymousClass1.AnonymousClass2 */

                        public void run() {
                            if (observer != null) {
                                observer.onError(error);
                            }
                        }
                    });
                }
            }

            public void mapboxTileCountLimitExceeded(final long limit) {
                if (OfflineRegion.this.deliverMessages()) {
                    OfflineRegion.this.handler.post(new Runnable() {
                        /* class com.mapbox.mapboxsdk.offline.OfflineRegion.AnonymousClass1.AnonymousClass3 */

                        public void run() {
                            if (observer != null) {
                                observer.mapboxTileCountLimitExceeded(limit);
                            }
                        }
                    });
                }
            }
        });
    }

    public void setDownloadState(int state2) {
        if (this.state != state2) {
            if (state2 == 1) {
                ConnectivityReceiver.instance(this.context).activate();
                this.fileSource.activate();
            } else {
                this.fileSource.deactivate();
                ConnectivityReceiver.instance(this.context).deactivate();
            }
            this.state = state2;
            setOfflineRegionDownloadState(state2);
        }
    }

    public void getStatus(@NonNull final OfflineRegionStatusCallback callback) {
        this.fileSource.activate();
        getOfflineRegionStatus(new OfflineRegionStatusCallback() {
            /* class com.mapbox.mapboxsdk.offline.OfflineRegion.AnonymousClass2 */

            public void onStatus(final OfflineRegionStatus status) {
                OfflineRegion.this.handler.post(new Runnable() {
                    /* class com.mapbox.mapboxsdk.offline.OfflineRegion.AnonymousClass2.AnonymousClass1 */

                    public void run() {
                        OfflineRegion.this.fileSource.deactivate();
                        callback.onStatus(status);
                    }
                });
            }

            public void onError(final String error) {
                OfflineRegion.this.handler.post(new Runnable() {
                    /* class com.mapbox.mapboxsdk.offline.OfflineRegion.AnonymousClass2.AnonymousClass2 */

                    public void run() {
                        OfflineRegion.this.fileSource.deactivate();
                        callback.onError(error);
                    }
                });
            }
        });
    }

    public void delete(@NonNull final OfflineRegionDeleteCallback callback) {
        if (!this.isDeleted) {
            this.isDeleted = true;
            this.fileSource.activate();
            deleteOfflineRegion(new OfflineRegionDeleteCallback() {
                /* class com.mapbox.mapboxsdk.offline.OfflineRegion.AnonymousClass3 */

                public void onDelete() {
                    OfflineRegion.this.handler.post(new Runnable() {
                        /* class com.mapbox.mapboxsdk.offline.OfflineRegion.AnonymousClass3.AnonymousClass1 */

                        public void run() {
                            OfflineRegion.this.fileSource.deactivate();
                            callback.onDelete();
                            OfflineRegion.this.finalize();
                        }
                    });
                }

                public void onError(final String error) {
                    OfflineRegion.this.handler.post(new Runnable() {
                        /* class com.mapbox.mapboxsdk.offline.OfflineRegion.AnonymousClass3.AnonymousClass2 */

                        public void run() {
                            boolean unused = OfflineRegion.this.isDeleted = false;
                            OfflineRegion.this.fileSource.deactivate();
                            callback.onError(error);
                        }
                    });
                }
            });
        }
    }

    public void invalidate(@Nullable final OfflineRegionInvalidateCallback callback) {
        this.fileSource.activate();
        invalidateOfflineRegion(new OfflineRegionInvalidateCallback() {
            /* class com.mapbox.mapboxsdk.offline.OfflineRegion.AnonymousClass4 */

            public void onInvalidate() {
                OfflineRegion.this.handler.post(new Runnable() {
                    /* class com.mapbox.mapboxsdk.offline.OfflineRegion.AnonymousClass4.AnonymousClass1 */

                    public void run() {
                        OfflineRegion.this.fileSource.deactivate();
                        if (callback != null) {
                            callback.onInvalidate();
                        }
                    }
                });
            }

            public void onError(@NonNull final String message) {
                OfflineRegion.this.handler.post(new Runnable() {
                    /* class com.mapbox.mapboxsdk.offline.OfflineRegion.AnonymousClass4.AnonymousClass2 */

                    public void run() {
                        OfflineRegion.this.fileSource.deactivate();
                        if (callback != null) {
                            callback.onError(message);
                        }
                    }
                });
            }
        });
    }

    public void updateMetadata(@NonNull byte[] bytes, @NonNull final OfflineRegionUpdateMetadataCallback callback) {
        updateOfflineRegionMetadata(bytes, new OfflineRegionUpdateMetadataCallback() {
            /* class com.mapbox.mapboxsdk.offline.OfflineRegion.AnonymousClass5 */

            public void onUpdate(final byte[] metadata) {
                OfflineRegion.this.handler.post(new Runnable() {
                    /* class com.mapbox.mapboxsdk.offline.OfflineRegion.AnonymousClass5.AnonymousClass1 */

                    public void run() {
                        byte[] unused = OfflineRegion.this.metadata = metadata;
                        callback.onUpdate(metadata);
                    }
                });
            }

            public void onError(final String error) {
                OfflineRegion.this.handler.post(new Runnable() {
                    /* class com.mapbox.mapboxsdk.offline.OfflineRegion.AnonymousClass5.AnonymousClass2 */

                    public void run() {
                        callback.onError(error);
                    }
                });
            }
        });
    }
}
