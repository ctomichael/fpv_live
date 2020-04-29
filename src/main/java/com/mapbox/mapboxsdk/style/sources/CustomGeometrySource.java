package com.mapbox.mapboxsdk.style.sources;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CustomGeometrySource extends Source {
    public static final int THREAD_POOL_LIMIT = 4;
    public static final String THREAD_PREFIX = "CustomGeom";
    /* access modifiers changed from: private */
    public static final AtomicInteger poolCount = new AtomicInteger();
    private final Map<TileID, GeometryTileRequest> awaitingTasksMap;
    /* access modifiers changed from: private */
    public ThreadPoolExecutor executor;
    private final Lock executorLock;
    private final Map<TileID, AtomicBoolean> inProgressTasksMap;
    private GeometryTileProvider provider;

    @Keep
    private native void nativeInvalidateBounds(LatLngBounds latLngBounds);

    @Keep
    private native void nativeInvalidateTile(int i, int i2, int i3);

    @Keep
    private native void nativeSetTileData(int i, int i2, int i3, FeatureCollection featureCollection);

    @Keep
    @NonNull
    private native Feature[] querySourceFeatures(Object[] objArr);

    /* access modifiers changed from: protected */
    @Keep
    public native void finalize() throws Throwable;

    /* access modifiers changed from: protected */
    @Keep
    public native void initialize(String str, Object obj);

    @UiThread
    public CustomGeometrySource(String id, GeometryTileProvider provider2) {
        this(id, new CustomGeometrySourceOptions(), provider2);
    }

    @UiThread
    public CustomGeometrySource(String id, CustomGeometrySourceOptions options, GeometryTileProvider provider2) {
        this.executorLock = new ReentrantLock();
        this.awaitingTasksMap = new HashMap();
        this.inProgressTasksMap = new HashMap();
        this.provider = provider2;
        initialize(id, options);
    }

    public void invalidateRegion(LatLngBounds bounds) {
        nativeInvalidateBounds(bounds);
    }

    public void invalidateTile(int zoomLevel, int x, int y) {
        nativeInvalidateTile(zoomLevel, x, y);
    }

    public void setTileData(int zoomLevel, int x, int y, FeatureCollection data) {
        nativeSetTileData(zoomLevel, x, y, data);
    }

    @NonNull
    public List<Feature> querySourceFeatures(@Nullable Expression filter) {
        checkThread();
        Feature[] features = querySourceFeatures(filter != null ? filter.toArray() : null);
        return features != null ? Arrays.asList(features) : new ArrayList();
    }

    /* access modifiers changed from: private */
    public void setTileData(TileID tileId, FeatureCollection data) {
        nativeSetTileData(tileId.z, tileId.x, tileId.y, data);
    }

    @Keep
    @WorkerThread
    private void fetchTile(int z, int x, int y) {
        AtomicBoolean cancelFlag = new AtomicBoolean(false);
        TileID tileID = new TileID(z, x, y);
        GeometryTileRequest request = new GeometryTileRequest(tileID, this.provider, this.awaitingTasksMap, this.inProgressTasksMap, this, cancelFlag);
        synchronized (this.awaitingTasksMap) {
            synchronized (this.inProgressTasksMap) {
                if (this.executor.getQueue().contains(request)) {
                    this.executor.remove(request);
                    executeRequest(request);
                } else if (this.inProgressTasksMap.containsKey(tileID)) {
                    this.awaitingTasksMap.put(tileID, request);
                } else {
                    executeRequest(request);
                }
            }
        }
    }

    private void executeRequest(@NonNull GeometryTileRequest request) {
        this.executorLock.lock();
        try {
            if (this.executor != null && !this.executor.isShutdown()) {
                this.executor.execute(request);
            }
        } finally {
            this.executorLock.unlock();
        }
    }

    @Keep
    @WorkerThread
    private void cancelTile(int z, int x, int y) {
        TileID tileID = new TileID(z, x, y);
        synchronized (this.awaitingTasksMap) {
            synchronized (this.inProgressTasksMap) {
                AtomicBoolean cancelFlag = this.inProgressTasksMap.get(tileID);
                if (cancelFlag == null || !cancelFlag.compareAndSet(false, true)) {
                    if (!this.executor.getQueue().remove(new GeometryTileRequest(tileID, null, null, null, null, null))) {
                        this.awaitingTasksMap.remove(tileID);
                    }
                }
            }
        }
    }

    @Keep
    private void startThreads() {
        this.executorLock.lock();
        try {
            if (this.executor != null && !this.executor.isShutdown()) {
                this.executor.shutdownNow();
            }
            this.executor = new ThreadPoolExecutor(4, 4, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), new ThreadFactory() {
                /* class com.mapbox.mapboxsdk.style.sources.CustomGeometrySource.AnonymousClass1 */
                final int poolId = CustomGeometrySource.poolCount.getAndIncrement();
                final AtomicInteger threadCount = new AtomicInteger();

                @NonNull
                public Thread newThread(@NonNull Runnable runnable) {
                    return new Thread(runnable, String.format(Locale.US, "%s-%d-%d", CustomGeometrySource.THREAD_PREFIX, Integer.valueOf(this.poolId), Integer.valueOf(this.threadCount.getAndIncrement())));
                }
            });
        } finally {
            this.executorLock.unlock();
        }
    }

    @Keep
    private void releaseThreads() {
        this.executorLock.lock();
        try {
            this.executor.shutdownNow();
        } finally {
            this.executorLock.unlock();
        }
    }

    @Keep
    private boolean isCancelled(int z, int x, int y) {
        return this.inProgressTasksMap.get(new TileID(z, x, y)).get();
    }

    static class TileID {
        public int x;
        public int y;
        public int z;

        TileID(int _z, int _x, int _y) {
            this.z = _z;
            this.x = _x;
            this.y = _y;
        }

        public int hashCode() {
            return Arrays.hashCode(new int[]{this.z, this.x, this.y});
        }

        public boolean equals(@Nullable Object object) {
            if (object == this) {
                return true;
            }
            if (object == null || getClass() != object.getClass()) {
                return false;
            }
            if (!(object instanceof TileID)) {
                return false;
            }
            TileID other = (TileID) object;
            if (this.z == other.z && this.x == other.x && this.y == other.y) {
                return true;
            }
            return false;
        }
    }

    static class GeometryTileRequest implements Runnable {
        private final Map<TileID, GeometryTileRequest> awaiting;
        private final AtomicBoolean cancelled;
        private final TileID id;
        private final Map<TileID, AtomicBoolean> inProgress;
        private final GeometryTileProvider provider;
        @NonNull
        private final WeakReference<CustomGeometrySource> sourceRef;

        GeometryTileRequest(TileID _id, GeometryTileProvider p, Map<TileID, GeometryTileRequest> awaiting2, Map<TileID, AtomicBoolean> m, CustomGeometrySource _source, AtomicBoolean _cancelled) {
            this.id = _id;
            this.provider = p;
            this.awaiting = awaiting2;
            this.inProgress = m;
            this.sourceRef = new WeakReference<>(_source);
            this.cancelled = _cancelled;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0037, code lost:
            if (isCancelled().booleanValue() != false) goto L_0x006e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0039, code lost:
            r0 = r8.provider.getFeaturesForBounds(com.mapbox.mapboxsdk.geometry.LatLngBounds.from(r8.id.z, r8.id.x, r8.id.y), r8.id.z);
            r2 = r8.sourceRef.get();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0063, code lost:
            if (isCancelled().booleanValue() != false) goto L_0x006e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0065, code lost:
            if (r2 == null) goto L_0x006e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x0067, code lost:
            if (r0 == null) goto L_0x006e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0069, code lost:
            com.mapbox.mapboxsdk.style.sources.CustomGeometrySource.access$100(r2, r8.id, r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x006e, code lost:
            r4 = r8.awaiting;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x0070, code lost:
            monitor-enter(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
            r5 = r8.inProgress;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0073, code lost:
            monitor-enter(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
            r8.inProgress.remove(r8.id);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x0083, code lost:
            if (r8.awaiting.containsKey(r8.id) == false) goto L_0x00a9;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:0x0085, code lost:
            r1 = r8.awaiting.get(r8.id);
            r2 = r8.sourceRef.get();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x0097, code lost:
            if (r2 == null) goto L_0x00a2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x0099, code lost:
            if (r1 == null) goto L_0x00a2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x009b, code lost:
            com.mapbox.mapboxsdk.style.sources.CustomGeometrySource.access$200(r2).execute(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x00a2, code lost:
            r8.awaiting.remove(r8.id);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x00a9, code lost:
            monitor-exit(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
            monitor-exit(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:64:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:65:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r8 = this;
                java.util.Map<com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$TileID, com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$GeometryTileRequest> r4 = r8.awaiting
                monitor-enter(r4)
                java.util.Map<com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$TileID, java.util.concurrent.atomic.AtomicBoolean> r5 = r8.inProgress     // Catch:{ all -> 0x00b3 }
                monitor-enter(r5)     // Catch:{ all -> 0x00b3 }
                java.util.Map<com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$TileID, java.util.concurrent.atomic.AtomicBoolean> r3 = r8.inProgress     // Catch:{ all -> 0x00b0 }
                com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$TileID r6 = r8.id     // Catch:{ all -> 0x00b0 }
                boolean r3 = r3.containsKey(r6)     // Catch:{ all -> 0x00b0 }
                if (r3 == 0) goto L_0x0024
                java.util.Map<com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$TileID, com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$GeometryTileRequest> r3 = r8.awaiting     // Catch:{ all -> 0x00b0 }
                com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$TileID r6 = r8.id     // Catch:{ all -> 0x00b0 }
                boolean r3 = r3.containsKey(r6)     // Catch:{ all -> 0x00b0 }
                if (r3 != 0) goto L_0x0021
                java.util.Map<com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$TileID, com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$GeometryTileRequest> r3 = r8.awaiting     // Catch:{ all -> 0x00b0 }
                com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$TileID r6 = r8.id     // Catch:{ all -> 0x00b0 }
                r3.put(r6, r8)     // Catch:{ all -> 0x00b0 }
            L_0x0021:
                monitor-exit(r5)     // Catch:{ all -> 0x00b0 }
                monitor-exit(r4)     // Catch:{ all -> 0x00b3 }
            L_0x0023:
                return
            L_0x0024:
                java.util.Map<com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$TileID, java.util.concurrent.atomic.AtomicBoolean> r3 = r8.inProgress     // Catch:{ all -> 0x00b0 }
                com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$TileID r6 = r8.id     // Catch:{ all -> 0x00b0 }
                java.util.concurrent.atomic.AtomicBoolean r7 = r8.cancelled     // Catch:{ all -> 0x00b0 }
                r3.put(r6, r7)     // Catch:{ all -> 0x00b0 }
                monitor-exit(r5)     // Catch:{ all -> 0x00b0 }
                monitor-exit(r4)     // Catch:{ all -> 0x00b3 }
                java.lang.Boolean r3 = r8.isCancelled()
                boolean r3 = r3.booleanValue()
                if (r3 != 0) goto L_0x006e
                com.mapbox.mapboxsdk.style.sources.GeometryTileProvider r3 = r8.provider
                com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$TileID r4 = r8.id
                int r4 = r4.z
                com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$TileID r5 = r8.id
                int r5 = r5.x
                com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$TileID r6 = r8.id
                int r6 = r6.y
                com.mapbox.mapboxsdk.geometry.LatLngBounds r4 = com.mapbox.mapboxsdk.geometry.LatLngBounds.from(r4, r5, r6)
                com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$TileID r5 = r8.id
                int r5 = r5.z
                com.mapbox.geojson.FeatureCollection r0 = r3.getFeaturesForBounds(r4, r5)
                java.lang.ref.WeakReference<com.mapbox.mapboxsdk.style.sources.CustomGeometrySource> r3 = r8.sourceRef
                java.lang.Object r2 = r3.get()
                com.mapbox.mapboxsdk.style.sources.CustomGeometrySource r2 = (com.mapbox.mapboxsdk.style.sources.CustomGeometrySource) r2
                java.lang.Boolean r3 = r8.isCancelled()
                boolean r3 = r3.booleanValue()
                if (r3 != 0) goto L_0x006e
                if (r2 == 0) goto L_0x006e
                if (r0 == 0) goto L_0x006e
                com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$TileID r3 = r8.id
                r2.setTileData(r3, r0)
            L_0x006e:
                java.util.Map<com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$TileID, com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$GeometryTileRequest> r4 = r8.awaiting
                monitor-enter(r4)
                java.util.Map<com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$TileID, java.util.concurrent.atomic.AtomicBoolean> r5 = r8.inProgress     // Catch:{ all -> 0x00ad }
                monitor-enter(r5)     // Catch:{ all -> 0x00ad }
                java.util.Map<com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$TileID, java.util.concurrent.atomic.AtomicBoolean> r3 = r8.inProgress     // Catch:{ all -> 0x00b6 }
                com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$TileID r6 = r8.id     // Catch:{ all -> 0x00b6 }
                r3.remove(r6)     // Catch:{ all -> 0x00b6 }
                java.util.Map<com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$TileID, com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$GeometryTileRequest> r3 = r8.awaiting     // Catch:{ all -> 0x00b6 }
                com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$TileID r6 = r8.id     // Catch:{ all -> 0x00b6 }
                boolean r3 = r3.containsKey(r6)     // Catch:{ all -> 0x00b6 }
                if (r3 == 0) goto L_0x00a9
                java.util.Map<com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$TileID, com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$GeometryTileRequest> r3 = r8.awaiting     // Catch:{ all -> 0x00b6 }
                com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$TileID r6 = r8.id     // Catch:{ all -> 0x00b6 }
                java.lang.Object r1 = r3.get(r6)     // Catch:{ all -> 0x00b6 }
                com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$GeometryTileRequest r1 = (com.mapbox.mapboxsdk.style.sources.CustomGeometrySource.GeometryTileRequest) r1     // Catch:{ all -> 0x00b6 }
                java.lang.ref.WeakReference<com.mapbox.mapboxsdk.style.sources.CustomGeometrySource> r3 = r8.sourceRef     // Catch:{ all -> 0x00b6 }
                java.lang.Object r2 = r3.get()     // Catch:{ all -> 0x00b6 }
                com.mapbox.mapboxsdk.style.sources.CustomGeometrySource r2 = (com.mapbox.mapboxsdk.style.sources.CustomGeometrySource) r2     // Catch:{ all -> 0x00b6 }
                if (r2 == 0) goto L_0x00a2
                if (r1 == 0) goto L_0x00a2
                java.util.concurrent.ThreadPoolExecutor r3 = r2.executor     // Catch:{ all -> 0x00b6 }
                r3.execute(r1)     // Catch:{ all -> 0x00b6 }
            L_0x00a2:
                java.util.Map<com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$TileID, com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$GeometryTileRequest> r3 = r8.awaiting     // Catch:{ all -> 0x00b6 }
                com.mapbox.mapboxsdk.style.sources.CustomGeometrySource$TileID r6 = r8.id     // Catch:{ all -> 0x00b6 }
                r3.remove(r6)     // Catch:{ all -> 0x00b6 }
            L_0x00a9:
                monitor-exit(r5)     // Catch:{ all -> 0x00b6 }
                monitor-exit(r4)     // Catch:{ all -> 0x00ad }
                goto L_0x0023
            L_0x00ad:
                r3 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x00ad }
                throw r3
            L_0x00b0:
                r3 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x00b0 }
                throw r3     // Catch:{ all -> 0x00b3 }
            L_0x00b3:
                r3 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x00b3 }
                throw r3
            L_0x00b6:
                r3 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x00b6 }
                throw r3     // Catch:{ all -> 0x00ad }
            */
            throw new UnsupportedOperationException("Method not decompiled: com.mapbox.mapboxsdk.style.sources.CustomGeometrySource.GeometryTileRequest.run():void");
        }

        private Boolean isCancelled() {
            return Boolean.valueOf(this.cancelled.get());
        }

        public boolean equals(@Nullable Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            return this.id.equals(((GeometryTileRequest) o).id);
        }
    }
}
