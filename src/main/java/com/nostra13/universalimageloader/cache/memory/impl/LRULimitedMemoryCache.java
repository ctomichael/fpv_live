package com.nostra13.universalimageloader.cache.memory.impl;

import android.graphics.Bitmap;
import com.nostra13.universalimageloader.cache.memory.LimitedMemoryCache;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class LRULimitedMemoryCache extends LimitedMemoryCache {
    private static final int INITIAL_CAPACITY = 10;
    private static final float LOAD_FACTOR = 1.1f;
    private final Map<String, Bitmap> lruCache = Collections.synchronizedMap(new LinkedHashMap(10, LOAD_FACTOR, true));

    public LRULimitedMemoryCache(int maxSize) {
        super(maxSize);
    }

    public boolean put(String key, Bitmap value) {
        if (!super.put(key, value)) {
            return false;
        }
        this.lruCache.put(key, value);
        return true;
    }

    public Bitmap get(String key) {
        this.lruCache.get(key);
        return super.get(key);
    }

    public Bitmap remove(String key) {
        this.lruCache.remove(key);
        return super.remove(key);
    }

    public void clear() {
        this.lruCache.clear();
        super.clear();
    }

    /* access modifiers changed from: protected */
    public int getSize(Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v4, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: android.graphics.Bitmap} */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.Bitmap removeNext() {
        /*
            r6 = this;
            r3 = 0
            java.util.Map<java.lang.String, android.graphics.Bitmap> r5 = r6.lruCache
            monitor-enter(r5)
            java.util.Map<java.lang.String, android.graphics.Bitmap> r4 = r6.lruCache     // Catch:{ all -> 0x0027 }
            java.util.Set r4 = r4.entrySet()     // Catch:{ all -> 0x0027 }
            java.util.Iterator r2 = r4.iterator()     // Catch:{ all -> 0x0027 }
            boolean r4 = r2.hasNext()     // Catch:{ all -> 0x0027 }
            if (r4 == 0) goto L_0x0025
            java.lang.Object r1 = r2.next()     // Catch:{ all -> 0x0027 }
            java.util.Map$Entry r1 = (java.util.Map.Entry) r1     // Catch:{ all -> 0x0027 }
            java.lang.Object r4 = r1.getValue()     // Catch:{ all -> 0x0027 }
            r0 = r4
            android.graphics.Bitmap r0 = (android.graphics.Bitmap) r0     // Catch:{ all -> 0x0027 }
            r3 = r0
            r2.remove()     // Catch:{ all -> 0x0027 }
        L_0x0025:
            monitor-exit(r5)     // Catch:{ all -> 0x0027 }
            return r3
        L_0x0027:
            r4 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x0027 }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache.removeNext():android.graphics.Bitmap");
    }

    /* access modifiers changed from: protected */
    public Reference<Bitmap> createReference(Bitmap value) {
        return new WeakReference(value);
    }
}
