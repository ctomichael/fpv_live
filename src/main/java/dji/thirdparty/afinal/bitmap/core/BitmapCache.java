package dji.thirdparty.afinal.bitmap.core;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import dji.thirdparty.afinal.utils.Utils;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class BitmapCache {
    private static final int DEFAULT_DISK_CACHE_COUNT = 10000;
    private static final boolean DEFAULT_DISK_CACHE_ENABLED = true;
    private static final int DEFAULT_DISK_CACHE_SIZE = 52428800;
    private static final boolean DEFAULT_MEM_CACHE_ENABLED = true;
    private static final int DEFAULT_MEM_CACHE_SIZE = 8388608;
    private ImageCacheParams mCacheParams;
    private DiskCache mDiskCache;
    private IMemoryCache mMemoryCache;

    public BitmapCache(ImageCacheParams cacheParams) {
        init(cacheParams);
    }

    private void init(ImageCacheParams cacheParams) {
        this.mCacheParams = cacheParams;
        if (this.mCacheParams.memoryCacheEnabled) {
            if (this.mCacheParams.recycleImmediately) {
                this.mMemoryCache = new SoftMemoryCacheImpl(this.mCacheParams.memCacheSize);
            } else {
                this.mMemoryCache = new BaseMemoryCacheImpl(this.mCacheParams.memCacheSize);
            }
        }
        if (cacheParams.diskCacheEnabled) {
            try {
                this.mDiskCache = new DiskCache(this.mCacheParams.diskCacheDir.getAbsolutePath(), this.mCacheParams.diskCacheCount, this.mCacheParams.diskCacheSize, false);
            } catch (IOException e) {
            }
        }
    }

    public void addToMemoryCache(String url, Bitmap bitmap) {
        if (url != null && bitmap != null) {
            this.mMemoryCache.put(url, bitmap);
        }
    }

    public void addToDiskCache(String url, byte[] data) {
        if (this.mDiskCache != null && url != null && data != null) {
            byte[] key = Utils.makeKey(url);
            long cacheKey = Utils.crc64Long(key);
            ByteBuffer buffer = ByteBuffer.allocate(key.length + data.length);
            buffer.put(key);
            buffer.put(data);
            synchronized (this.mDiskCache) {
                try {
                    this.mDiskCache.insert(cacheKey, buffer.array());
                } catch (IOException ex) {
                    Log.i("", ex.getMessage());
                }
            }
            return;
        }
        return;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0032, code lost:
        if (dji.thirdparty.afinal.utils.Utils.isSameKey(r2, r3.buffer) == false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0034, code lost:
        r9.data = r3.buffer;
        r9.offset = r2.length;
        r9.length = r3.length - r9.offset;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0042, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean getImageData(java.lang.String r8, dji.thirdparty.afinal.bitmap.core.BytesBufferPool.BytesBuffer r9) {
        /*
            r7 = this;
            r4 = 0
            dji.thirdparty.afinal.bitmap.core.DiskCache r5 = r7.mDiskCache
            if (r5 != 0) goto L_0x0006
        L_0x0005:
            return r4
        L_0x0006:
            byte[] r2 = dji.thirdparty.afinal.utils.Utils.makeKey(r8)
            long r0 = dji.thirdparty.afinal.utils.Utils.crc64Long(r2)
            dji.thirdparty.afinal.bitmap.core.DiskCache$LookupRequest r3 = new dji.thirdparty.afinal.bitmap.core.DiskCache$LookupRequest     // Catch:{ IOException -> 0x0029 }
            r3.<init>()     // Catch:{ IOException -> 0x0029 }
            r3.key = r0     // Catch:{ IOException -> 0x0029 }
            byte[] r5 = r9.data     // Catch:{ IOException -> 0x0029 }
            r3.buffer = r5     // Catch:{ IOException -> 0x0029 }
            dji.thirdparty.afinal.bitmap.core.DiskCache r6 = r7.mDiskCache     // Catch:{ IOException -> 0x0029 }
            monitor-enter(r6)     // Catch:{ IOException -> 0x0029 }
            dji.thirdparty.afinal.bitmap.core.DiskCache r5 = r7.mDiskCache     // Catch:{ all -> 0x0026 }
            boolean r5 = r5.lookup(r3)     // Catch:{ all -> 0x0026 }
            if (r5 != 0) goto L_0x002b
            monitor-exit(r6)     // Catch:{ all -> 0x0026 }
            goto L_0x0005
        L_0x0026:
            r5 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x0026 }
            throw r5     // Catch:{ IOException -> 0x0029 }
        L_0x0029:
            r5 = move-exception
            goto L_0x0005
        L_0x002b:
            monitor-exit(r6)     // Catch:{ all -> 0x0026 }
            byte[] r5 = r3.buffer     // Catch:{ IOException -> 0x0029 }
            boolean r5 = dji.thirdparty.afinal.utils.Utils.isSameKey(r2, r5)     // Catch:{ IOException -> 0x0029 }
            if (r5 == 0) goto L_0x0005
            byte[] r5 = r3.buffer     // Catch:{ IOException -> 0x0029 }
            r9.data = r5     // Catch:{ IOException -> 0x0029 }
            int r5 = r2.length     // Catch:{ IOException -> 0x0029 }
            r9.offset = r5     // Catch:{ IOException -> 0x0029 }
            int r5 = r3.length     // Catch:{ IOException -> 0x0029 }
            int r6 = r9.offset     // Catch:{ IOException -> 0x0029 }
            int r5 = r5 - r6
            r9.length = r5     // Catch:{ IOException -> 0x0029 }
            r4 = 1
            goto L_0x0005
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.afinal.bitmap.core.BitmapCache.getImageData(java.lang.String, dji.thirdparty.afinal.bitmap.core.BytesBufferPool$BytesBuffer):boolean");
    }

    public Bitmap getBitmapFromMemoryCache(String data) {
        if (this.mMemoryCache != null) {
            return this.mMemoryCache.get(data);
        }
        return null;
    }

    public void clearCache() {
        clearMemoryCache();
        clearDiskCache();
    }

    public void clearDiskCache() {
        if (this.mDiskCache != null) {
            this.mDiskCache.delete();
        }
    }

    public void clearMemoryCache() {
        if (this.mMemoryCache != null) {
            this.mMemoryCache.evictAll();
        }
    }

    public void clearCache(String key) {
        clearMemoryCache(key);
        clearDiskCache(key);
    }

    public void clearDiskCache(String url) {
        addToDiskCache(url, new byte[0]);
    }

    public void clearMemoryCache(String key) {
        if (this.mMemoryCache != null) {
            this.mMemoryCache.remove(key);
        }
    }

    public void close() {
        if (this.mDiskCache != null) {
            this.mDiskCache.close();
        }
    }

    public static class ImageCacheParams {
        public int diskCacheCount = 10000;
        public File diskCacheDir;
        public boolean diskCacheEnabled = true;
        public int diskCacheSize = BitmapCache.DEFAULT_DISK_CACHE_SIZE;
        public int memCacheSize = 8388608;
        public boolean memoryCacheEnabled = true;
        public boolean recycleImmediately = true;

        public ImageCacheParams(File diskCacheDir2) {
            this.diskCacheDir = diskCacheDir2;
        }

        public ImageCacheParams(String diskCacheDir2) {
            this.diskCacheDir = new File(diskCacheDir2);
        }

        public void setMemCacheSizePercent(Context context, float percent) {
            if (percent < 0.05f || percent > 0.8f) {
                throw new IllegalArgumentException("setMemCacheSizePercent - percent must be between 0.05 and 0.8 (inclusive)");
            }
            this.memCacheSize = Math.round(((float) getMemoryClass(context)) * percent * 1024.0f * 1024.0f);
        }

        public void setMemCacheSize(int memCacheSize2) {
            this.memCacheSize = memCacheSize2;
        }

        public void setDiskCacheSize(int diskCacheSize2) {
            this.diskCacheSize = diskCacheSize2;
        }

        private static int getMemoryClass(Context context) {
            return ((ActivityManager) context.getSystemService("activity")).getMemoryClass();
        }

        public void setDiskCacheCount(int diskCacheCount2) {
            this.diskCacheCount = diskCacheCount2;
        }

        public void setRecycleImmediately(boolean recycleImmediately2) {
            this.recycleImmediately = recycleImmediately2;
        }
    }
}
