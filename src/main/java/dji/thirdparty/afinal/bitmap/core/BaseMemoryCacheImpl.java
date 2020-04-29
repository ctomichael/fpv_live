package dji.thirdparty.afinal.bitmap.core;

import android.graphics.Bitmap;
import dji.thirdparty.afinal.utils.Utils;

public class BaseMemoryCacheImpl implements IMemoryCache {
    private final LruMemoryCache<String, Bitmap> mMemoryCache;

    public BaseMemoryCacheImpl(int size) {
        this.mMemoryCache = new LruMemoryCache<String, Bitmap>(size) {
            /* class dji.thirdparty.afinal.bitmap.core.BaseMemoryCacheImpl.AnonymousClass1 */

            /* access modifiers changed from: protected */
            public int sizeOf(String key, Bitmap bitmap) {
                return Utils.getBitmapSize(bitmap);
            }
        };
    }

    public void put(String key, Bitmap bitmap) {
        this.mMemoryCache.put(key, bitmap);
    }

    public Bitmap get(String key) {
        return this.mMemoryCache.get(key);
    }

    public void evictAll() {
        this.mMemoryCache.evictAll();
    }

    public void remove(String key) {
        this.mMemoryCache.remove(key);
    }
}
