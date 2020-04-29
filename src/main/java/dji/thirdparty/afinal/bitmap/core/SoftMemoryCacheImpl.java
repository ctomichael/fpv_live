package dji.thirdparty.afinal.bitmap.core;

import android.graphics.Bitmap;
import java.lang.ref.SoftReference;
import java.util.HashMap;

public class SoftMemoryCacheImpl implements IMemoryCache {
    private final HashMap<String, SoftReference<Bitmap>> mMemoryCache = new HashMap<>();

    public SoftMemoryCacheImpl(int size) {
    }

    public void put(String key, Bitmap bitmap) {
        this.mMemoryCache.put(key, new SoftReference(bitmap));
    }

    public Bitmap get(String key) {
        SoftReference<Bitmap> memBitmap = this.mMemoryCache.get(key);
        if (memBitmap != null) {
            return (Bitmap) memBitmap.get();
        }
        return null;
    }

    public void evictAll() {
        this.mMemoryCache.clear();
    }

    public void remove(String key) {
        this.mMemoryCache.remove(key);
    }
}
