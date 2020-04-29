package com.nostra13.universalimageloader.cache.memory.impl;

import android.graphics.Bitmap;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class LruMemoryCache implements MemoryCache {
    private final LinkedHashMap<String, Bitmap> map;
    private final int maxSize;
    private int size;

    public LruMemoryCache(int maxSize2) {
        if (maxSize2 <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.maxSize = maxSize2;
        this.map = new LinkedHashMap<>(0, 0.75f, true);
    }

    public final Bitmap get(String key) {
        Bitmap bitmap;
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
            bitmap = this.map.get(key);
        }
        return bitmap;
    }

    public final boolean put(String key, Bitmap value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }
        synchronized (this) {
            this.size += sizeOf(key, value);
            Bitmap previous = this.map.put(key, value);
            if (previous != null) {
                this.size -= sizeOf(key, previous);
            }
        }
        trimToSize(this.maxSize);
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0032, code lost:
        throw new java.lang.IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results!");
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void trimToSize(int r7) {
        /*
            r6 = this;
        L_0x0000:
            monitor-enter(r6)
            int r3 = r6.size     // Catch:{ all -> 0x0033 }
            if (r3 < 0) goto L_0x0011
            java.util.LinkedHashMap<java.lang.String, android.graphics.Bitmap> r3 = r6.map     // Catch:{ all -> 0x0033 }
            boolean r3 = r3.isEmpty()     // Catch:{ all -> 0x0033 }
            if (r3 == 0) goto L_0x0036
            int r3 = r6.size     // Catch:{ all -> 0x0033 }
            if (r3 == 0) goto L_0x0036
        L_0x0011:
            java.lang.IllegalStateException r3 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0033 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0033 }
            r4.<init>()     // Catch:{ all -> 0x0033 }
            java.lang.Class r5 = r6.getClass()     // Catch:{ all -> 0x0033 }
            java.lang.String r5 = r5.getName()     // Catch:{ all -> 0x0033 }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x0033 }
            java.lang.String r5 = ".sizeOf() is reporting inconsistent results!"
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x0033 }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x0033 }
            r3.<init>(r4)     // Catch:{ all -> 0x0033 }
            throw r3     // Catch:{ all -> 0x0033 }
        L_0x0033:
            r3 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x0033 }
            throw r3
        L_0x0036:
            int r3 = r6.size     // Catch:{ all -> 0x0033 }
            if (r3 <= r7) goto L_0x0042
            java.util.LinkedHashMap<java.lang.String, android.graphics.Bitmap> r3 = r6.map     // Catch:{ all -> 0x0033 }
            boolean r3 = r3.isEmpty()     // Catch:{ all -> 0x0033 }
            if (r3 == 0) goto L_0x0044
        L_0x0042:
            monitor-exit(r6)     // Catch:{ all -> 0x0033 }
        L_0x0043:
            return
        L_0x0044:
            java.util.LinkedHashMap<java.lang.String, android.graphics.Bitmap> r3 = r6.map     // Catch:{ all -> 0x0033 }
            java.util.Set r3 = r3.entrySet()     // Catch:{ all -> 0x0033 }
            java.util.Iterator r3 = r3.iterator()     // Catch:{ all -> 0x0033 }
            java.lang.Object r1 = r3.next()     // Catch:{ all -> 0x0033 }
            java.util.Map$Entry r1 = (java.util.Map.Entry) r1     // Catch:{ all -> 0x0033 }
            if (r1 != 0) goto L_0x0058
            monitor-exit(r6)     // Catch:{ all -> 0x0033 }
            goto L_0x0043
        L_0x0058:
            java.lang.Object r0 = r1.getKey()     // Catch:{ all -> 0x0033 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ all -> 0x0033 }
            java.lang.Object r2 = r1.getValue()     // Catch:{ all -> 0x0033 }
            android.graphics.Bitmap r2 = (android.graphics.Bitmap) r2     // Catch:{ all -> 0x0033 }
            java.util.LinkedHashMap<java.lang.String, android.graphics.Bitmap> r3 = r6.map     // Catch:{ all -> 0x0033 }
            r3.remove(r0)     // Catch:{ all -> 0x0033 }
            int r3 = r6.size     // Catch:{ all -> 0x0033 }
            int r4 = r6.sizeOf(r0, r2)     // Catch:{ all -> 0x0033 }
            int r3 = r3 - r4
            r6.size = r3     // Catch:{ all -> 0x0033 }
            monitor-exit(r6)     // Catch:{ all -> 0x0033 }
            goto L_0x0000
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache.trimToSize(int):void");
    }

    public final Bitmap remove(String key) {
        Bitmap previous;
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
            previous = this.map.remove(key);
            if (previous != null) {
                this.size -= sizeOf(key, previous);
            }
        }
        return previous;
    }

    public Collection<String> keys() {
        HashSet hashSet;
        synchronized (this) {
            hashSet = new HashSet(this.map.keySet());
        }
        return hashSet;
    }

    public void clear() {
        trimToSize(-1);
    }

    private int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    public final synchronized String toString() {
        return String.format("LruCache[maxSize=%d]", Integer.valueOf(this.maxSize));
    }
}
