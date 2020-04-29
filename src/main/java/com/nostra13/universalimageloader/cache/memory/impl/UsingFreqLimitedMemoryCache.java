package com.nostra13.universalimageloader.cache.memory.impl;

import android.graphics.Bitmap;
import com.nostra13.universalimageloader.cache.memory.LimitedMemoryCache;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UsingFreqLimitedMemoryCache extends LimitedMemoryCache {
    private final Map<Bitmap, Integer> usingCounts = Collections.synchronizedMap(new HashMap());

    public UsingFreqLimitedMemoryCache(int sizeLimit) {
        super(sizeLimit);
    }

    public boolean put(String key, Bitmap value) {
        if (!super.put(key, value)) {
            return false;
        }
        this.usingCounts.put(value, 0);
        return true;
    }

    public Bitmap get(String key) {
        Integer usageCount;
        Bitmap value = super.get(key);
        if (!(value == null || (usageCount = this.usingCounts.get(value)) == null)) {
            this.usingCounts.put(value, Integer.valueOf(usageCount.intValue() + 1));
        }
        return value;
    }

    public Bitmap remove(String key) {
        Bitmap value = super.get(key);
        if (value != null) {
            this.usingCounts.remove(value);
        }
        return super.remove(key);
    }

    public void clear() {
        this.usingCounts.clear();
        super.clear();
    }

    /* access modifiers changed from: protected */
    public int getSize(Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v5, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: android.graphics.Bitmap} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v6, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v2, resolved type: android.graphics.Bitmap} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v7, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v4, resolved type: java.lang.Integer} */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.Bitmap removeNext() {
        /*
            r10 = this;
            r6 = 0
            r5 = 0
            java.util.Map<android.graphics.Bitmap, java.lang.Integer> r7 = r10.usingCounts
            java.util.Set r1 = r7.entrySet()
            java.util.Map<android.graphics.Bitmap, java.lang.Integer> r8 = r10.usingCounts
            monitor-enter(r8)
            java.util.Iterator r3 = r1.iterator()     // Catch:{ all -> 0x004f }
        L_0x000f:
            boolean r7 = r3.hasNext()     // Catch:{ all -> 0x004f }
            if (r7 == 0) goto L_0x0048
            java.lang.Object r2 = r3.next()     // Catch:{ all -> 0x004f }
            java.util.Map$Entry r2 = (java.util.Map.Entry) r2     // Catch:{ all -> 0x004f }
            if (r5 != 0) goto L_0x002e
            java.lang.Object r7 = r2.getKey()     // Catch:{ all -> 0x004f }
            r0 = r7
            android.graphics.Bitmap r0 = (android.graphics.Bitmap) r0     // Catch:{ all -> 0x004f }
            r5 = r0
            java.lang.Object r7 = r2.getValue()     // Catch:{ all -> 0x004f }
            r0 = r7
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ all -> 0x004f }
            r6 = r0
            goto L_0x000f
        L_0x002e:
            java.lang.Object r4 = r2.getValue()     // Catch:{ all -> 0x004f }
            java.lang.Integer r4 = (java.lang.Integer) r4     // Catch:{ all -> 0x004f }
            int r7 = r4.intValue()     // Catch:{ all -> 0x004f }
            int r9 = r6.intValue()     // Catch:{ all -> 0x004f }
            if (r7 >= r9) goto L_0x000f
            r6 = r4
            java.lang.Object r7 = r2.getKey()     // Catch:{ all -> 0x004f }
            r0 = r7
            android.graphics.Bitmap r0 = (android.graphics.Bitmap) r0     // Catch:{ all -> 0x004f }
            r5 = r0
            goto L_0x000f
        L_0x0048:
            monitor-exit(r8)     // Catch:{ all -> 0x004f }
            java.util.Map<android.graphics.Bitmap, java.lang.Integer> r7 = r10.usingCounts
            r7.remove(r5)
            return r5
        L_0x004f:
            r7 = move-exception
            monitor-exit(r8)     // Catch:{ all -> 0x004f }
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache.removeNext():android.graphics.Bitmap");
    }

    /* access modifiers changed from: protected */
    public Reference<Bitmap> createReference(Bitmap value) {
        return new WeakReference(value);
    }
}
